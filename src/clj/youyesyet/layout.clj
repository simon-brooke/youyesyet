(ns^{:doc "Render web pages using Selmer templating markup."
     :author "Simon Brooke"}
          youyesyet.layout
          (:require [adl-support.tags :as tags]
                    [clojure.string :refer [lower-case]]
                    [clojure.tools.logging :as log]
                    [markdown.core :refer [md-to-html-string]]
                    [ring.util.http-response :refer [content-type ok]]
                    [ring.util.anti-forgery :refer [anti-forgery-field]]
                    [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
                    [selmer.parser :as parser]
                    [selmer.filters :as filters]
                    [youyesyet.config :refer [env]]
                    [youyesyet.db.core :as db]
                    ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.layout: lay out Selmer-templated web pages.
;;;;
;;;; This program is free software; you can redistribute it and/or
;;;; modify it under the terms of the GNU General Public License
;;;; as published by the Free Software Foundation; either version 2
;;;; of the License, or (at your option) any later version.
;;;;
;;;; This program is distributed in the hope that it will be useful,
;;;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;;; GNU General Public License for more details.
;;;;
;;;; You should have received a copy of the GNU General Public License
;;;; along with this program; if not, write to the Free Software
;;;; Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
;;;; USA.
;;;;
;;;; Copyright (C) 2016 Simon Brooke for Radical Independence Campaign
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(declare ^:dynamic *app-context*)
(def ^:dynamic *user* nil)

(parser/set-resource-path!  (clojure.java.io/resource "templates"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))
(filters/add-filter! :markdown (fn [content] [:safe (md-to-html-string content)]))
(tags/add-tags)

;; role assignments change only rarely.
(def get-user-roles
  "Return, as a set, the names of the roles of which this `user` is a member."
  (memoize
   (fn [user]
     (if
       user
       (do
         (log/debug (str "seeking roles for user " user))
         (let [roles
               (set (map #(lower-case (:name %)) (db/list-roles-by-canvasser db/*db* user)))]
           (log/debug (str "found roles " roles " for user " user))
           roles))))))


(defn render-with-session
  "renders the HTML `template` located relative to resources/templates in
  the context of this session and with these parameters."
  ;; TODO: I'm passing `session` through into render. The default luminus
  ;; setup doesn't do this, and Dmitri is an awful lot better at this stuff
  ;; than me so there's almost certainly a reason it doesn't.
  [template session & [params]]
  (let [user (:user session)]
    (log/debug (str "layout/render-with-session: template: '" template "'"))
    (content-type
      (ok
        (parser/render-file
          template
          (merge params
            {:page template
            :csrf-token *anti-forgery-token*
            :user user
            :user-roles (get-user-roles user)
            :site-title (:site-title env)
            :version (System/getProperty "youyesyet.version")})))
      "text/html; charset=utf-8")))


(defn render
  "renders the HTML `template` located relative to resources/templates in
  the context of this session and with these parameters."
  [template & [params]]
    (log/debug (str "layout/render: template: '" template "'"))
    (content-type
      (ok
        (parser/render-file
          template
          (merge params
            {:page template
            :csrf-token *anti-forgery-token*
            :user *user*
            :user-roles (get-user-roles *user*)
            :site-title (:site-title env)
            :version (System/getProperty "youyesyet.version")})))
      "text/html; charset=utf-8"))



(defn error-page
  "error-details should be a map containing the following keys:
  :status - error status
  :title - error title (optional)
  :message - detailed error message (optional)
  returns a response map with the error page as the body
  and the status specified by the status key"
  [error-details]
  {:status  (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (render "error.html" {} error-details)})

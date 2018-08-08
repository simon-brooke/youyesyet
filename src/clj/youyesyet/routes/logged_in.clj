(ns ^{:doc "Routes/pages available to authenticated users."
      :author "Simon Brooke"} youyesyet.routes.logged-in
  (:require  [adl-support.core :as support]
             [adl-support.utils :refer [safe-name]]
             [clojure.java.io :as io]
             [clojure.string :as s]
             [clojure.tools.logging :as log]
             [clojure.walk :refer [keywordize-keys]]
             [markdown.core :refer [md-to-html-string]]
             [noir.util.route :as route]
             [ring.util.http-response :as response]
             [youyesyet.config :refer [env]]
             [youyesyet.db.core :as db]
             [youyesyet.layout :as layout]
             [compojure.core :refer [defroutes GET POST]]
             ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.routes.logged-in: routes and pages for authenticated users.
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


(defn app-page [request]
  (layout/render "app.html"
                 {:title "Canvasser app"}))


(defn profile-page [request]
  "Show the canvassers form for the current user, only."
  (let [record (-> request :session :user)]
  (layout/render
    "auto/form-canvassers-Canvasser.html"
    {:title (str "Profile for " (-> request :session :user :fullname))
     :record record
     :elector_id
     (flatten
       (remove
         nil?
         (list
           (support/do-or-log-error
             (db/get-elector db/*db* {:id (:elector_id record)})
             :message
             "Error while fetching elector record {:id (:elector_id record)}")
           (support/do-or-log-error
             (db/list-electors db/*db*)
             :message
             "Error while fetching elector list")))),
     :address_id
     (flatten
       (remove
         nil?
         (list
           (support/do-or-log-error
             (db/get-address db/*db* {:id (:address_id record)})
             :message
             "Error while fetching address record {:id (:address_id record)}")
           (support/do-or-log-error
             (db/list-addresses db/*db*)
             :message
             "Error while fetching address list")))),
     :authority_id
     (flatten
       (remove
         nil?
         (list
           (support/do-or-log-error
             (db/get-authority db/*db* {:id (:authority_id record)})
             :message
             "Error while fetching authority record {:id (:authority_id record)}")
           (support/do-or-log-error
             (db/list-authorities db/*db*)
             :message
             "Error while fetching authority list")))),
     :roles
     (flatten
       (remove
         nil?
         (list
           nil
           (support/do-or-log-error
             (db/list-roles db/*db*)
             :message
             "Error while fetching role list")))),
     :expertise
     (flatten
       (remove
         nil?
         (list
           nil
           (support/do-or-log-error
             (db/list-issues db/*db*)
             :message
             "Error while fetching issue list"))))})))


(defn handle-logout
  [request]
  (let [r (response/found (str (:servlet-context request) "/home"))]
    (assoc r :session (dissoc (:session r) :user))))



(defroutes logged-in-routes
  (GET "/logout" request (handle-logout request))
  (GET "/profile" request (route/restricted (profile-page request)))
  (GET "/app" [request] (route/restricted (app-page request))))

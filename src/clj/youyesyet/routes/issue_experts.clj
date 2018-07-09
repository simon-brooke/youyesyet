(ns ^{:doc "Routes/pages available to issue experts."
      :author "Simon Brooke"} youyesyet.routes.issue-experts
  (:require [adl-support.utils :refer [safe-name]]
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
            [youyesyet.oauth :as oauth]
            [compojure.core :refer [defroutes GET POST]]
            ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.routes.home: routes and pages for issue experts.
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

(defn list-page [request]
  (layout/render
    "auto/list-followuprequests-Followuprequests.html"
    (:session request)
    (let [user (:user (:session request))]
    {:title "Open requests"
     :user user
     :records (db/list-open-requests db/*db* {:expert (:id user)})})))


(defn followup-request-page [request]
  (layout/render
    "issue-expert/request.html"
    (:session request)
    {:title "Open requests"
             :user (:user (:session request))
             :request (db/get-followuprequest
                        db/*db*
                        {:id (:id (keywordize-keys (:params request)))})}))


(defroutes issue-expert-routes
  (GET "/issue-expert/list" request
       (route/restricted (list-page request)))
  (GET "/issue-expert/followup-request" request
       (route/restricted (followup-request-page request)))
  (POST "/issue-expert/followup-request" request
       (route/restricted (followup-request-page request))))

(ns ^{:doc "Routes/pages available to issue experts."
      :author "Simon Brooke"} youyesyet.routes.issue-experts
  (:require [adl-support.core :as support]
            [adl-support.utils :refer [safe-name]]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [java-time :as jt]
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

;;; TODO: Must lock record to expert when taking it from the list, unlock after
;;; thirty minutes or when the action is posted, whichever is sooner. If
;;; an expert opens a locked record, it must show up as locked. Also, expert should
;;; not be able to open a closed request.

(defn list-page [request]
  (layout/render
    "issue-expert/list.html"
    (let [user (:user (:session request))]
    {:title "Open requests"
     :user user
     :records (db/list-open-requests db/*db* {:expert (:id user)})})))


(defn get-followup-request-page [request]
  (let
    [params (support/massage-params request)
     id (:id params)
     record (db/get-followuprequest db/*db* {:id id})
     elector (if
               record
               (first
                 (db/search-strings-electors
                   db/*db* {:id (:elector_id record)})))
     visit (if
             record
             (first
               (db/search-strings-visits
                 db/*db* {:id (:visit_id record)})))]
    (layout/render
      "issue-expert/request.html"
      {:actions (map
                  ;; HTML-ise the notes in each action record
                  #(merge % {:notes (md-to-html-string (:notes %))})
                  (db/list-followupactions-by-followuprequest
                    db/*db* {:id id}))
       :elector elector
       :issue (let
                [raw-issue (if
                             record
                             (db/get-issue db/*db* {:id (:issue_id record)}))]
                (if raw-issue
                  (merge
                    raw-issue
                    {:brief (md-to-html-string (:brief raw-issue))})))
       :options (db/list-options db/*db* params)
       :record record
       :title (str "Request from " (:name elector) " at " (:date visit))
       :user (:user (:session request))
       :visit visit})))


(defn post-followup-action
  "From this `request`, create a `followupaction` record, and, if an
  `option_id` is present in the params, an `intention` record; show
  the request list on success, to the request form on failure."
  [request]
  (support/do-or-log-error
    (let
      [params (support/massage-params request)
       locality (:locality (db/get-locality-for-visit db/*db* {:id (:visit_id params)}))]
      (log/debug "post-followup-request-page with request " request)
      (db/create-followupaction!
        db/*db*
        (assoc
          params
          :actor (:id (:user (:session request)))
          :date (jt/to-sql-timestamp (jt/local-date-time))
          :closed (= (:closed params) "on")))
      (if-not
        (zero? (count (:option_id params)))
        (if
          (zero? (count (:signature (db/get-elector db/*db* {:id (:elector_id params)}))))
          ;; the elector has NOT recorded GDPR consent: explicitly bind elector_id to nil
          (db/create-intention! db/*db* (assoc params :locality locality :elector_id nil))
          ;; else the elector HAS recorded GDPR consent
          (db/create-intention! db/*db* (assoc params :locality locality))))
      (list-page request))
    :error-return
    (get-followup-request-page request)))


(defroutes issue-expert-routes
  (GET "/issue-expert/list" request
       (route/restricted (list-page request)))
  (GET "/issue-expert/followup-request" request
       (route/restricted (get-followup-request-page request)))
  (POST "/issue-expert/followup-action" request
       (route/restricted (post-followup-action request))))

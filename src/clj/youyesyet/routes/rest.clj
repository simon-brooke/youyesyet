(ns ^{:doc "Manually maintained routes which handle data transfer to/from the canvasser app."
      :author "Simon Brooke"} youyesyet.routes.rest
  (:require [adl-support.rest-support :refer :all]
            [adl-support.core :refer [massage-params do-or-log-error
                                      do-or-return-reason]]
            [clojure.core.memoize :as memo]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :refer [defroutes GET POST]]
            [java-time :as jt]
            [mount.core :as mount]
            [noir.response :as nresponse]
            [noir.util.route :as route]
            [ring.util.http-response :as response]
            [youyesyet.locality :as l]
            [youyesyet.db.core :as db]
            [youyesyet.utils :refer :all]
            ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.routes.rest: Routes which handle data transfer to/from the
;;;; canvasser app.
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
;;;; Copyright (C) 2017 Simon Brooke for Radical Independence Campaign
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;; See also src/clj/youyesyet/routes/auto-json.clj

(def in-get-local-data
  "Local data is volatile, because hopefully canvassers are updating it as they
  work. So cache for only 90 seconds."
  (memo/ttl
    (fn [here]
      (log/debug "Fetching local data for " here)
      (let [neighbourhood (l/neighbouring-localities here)
            addresses  (flatten
                         (map
                           #(db/list-addresses-by-locality db/*db* {:locality %})
                           neighbourhood))]
        (map
          (fn [a]
            (assoc a
              :dwellings
              (map
                (fn [d]
                  (assoc d
                    :electors
                    (map
                      (fn [e]
                        (assoc e
                          :intentions
                          (db/list-intentions-by-elector db/*db* {:id (:id e)})))
                      (db/list-electors-by-dwelling db/*db* {:id (:id d)}))))
                (db/list-dwellings-by-address db/*db* {:id (:id a)}))))
          addresses)))
    :ttl/threshold
    90000))


(defn get-local-data
  "Get data local to the user of the canvasser app. Expects arguments `latitude` and
  `longitude`, or `locality`. Returns a block of data for that locality"
  [request]
  (let
    [m (massage-params request)
     here (or (:locality m) (l/locality (:latitude m) (:longitude m)))]
    (valid-user-or-forbid
     (in-get-local-data here)
     request)))


(defn last-visit-by-current-user
  "Return the most recent visit by the currently logged in user"
  [request]
  (db/get-last-visit-by-canvasser
    db/*db*
    (-> request :session :user)))


(defn current-visit-id
  "Return the id of the current visit by the current user, creating it if necessary."
  [request]
  (let [last-visit (last-visit-by-current-user request)
        params (massage-params request)]
    (log/debug "rest/current-visit-id: type of address_id is: " (type (:address_id params)))
    (let
      [result (if
                (=
                  (:address_id params)
                  (:address_id last-visit))
                (:id last-visit)
                (:id
                  (db/create-visit!
                    db/*db*
                    {:address_id (:address_id params)
                     :canvasser_id (-> request :session :user :id)
                     :date (jt/to-sql-timestamp (jt/local-date-time))})))]
      (log/debug "rest/current-visit-id returning " result "; type is " (type result))
      result)))


(defn create-intention-and-visit!
  "Doing visit creation logic server side; request params are expected to
  include an `option_id`, an `elector_id` and an `address_id`, or an `option` and
  a `location`. If no `address_id` is provided, we simply create an
  `intention` record from the `option_id` and the `locality`; if an `address_id`
  is provided, we need to check whether the last `visit` by the current `user`
  was to the same address, if so use that as the `visit_id`, if not create
  a new `visit` record."
  [request]
  (let [params (massage-params request)]
    (log/debug "Creating intention with params: " params)
    (valid-user-or-forbid
      (if
        (and
          (or (:locality params)
              (and (:elector_id params)
                   (:address_id params)))
          (:option_id params))
        (do-or-server-fail
          (db/create-intention!
            db/*db*
            (assoc
              (merge {:actions nil, :issue_detail nil} params) :visit_id (current-visit-id request)))
          201)
        {:status 400
         :body (json/write-str
                 "create-intention requires params: `option_id`
                               and either `locality` or both `address_id` and `elector_id`.")})
      request)))


(defn create-request-and-visit!
  "Doing visit creation logic server side; request params are expected to
  include an `issue`, an `elector_id` and an `address_id` (and also a
  `method_id` and `method_detail`). Ye cannae reasonably create a request
  without having recorded the visit, so let's not muck about."
  [request]
  (let [params (merge
                 {:actions nil, :issue_detail ""}
                 (assoc
                 (massage-params request)
                 :visit_id (current-visit-id request)))]
    (log/debug "create-request-and-visit! params are " params)
    (valid-user-or-forbid
      (with-params-or-error
        (do-or-server-fail
          (db/create-followuprequest!
            db/*db* params)
          201)
        params
        #{:elector_id :visit_id :issue_id :method_id
          :method_detail})
      request)))


(defn update-elector-signature!
  "Set the `signature` in the params of this `request` as the signature for
  the elector whose `id` is in the params of this `request`."
  [request]
  (let [params (massage-params request)]
    (log/debug "Update elector signature with params: " params)
        (valid-user-or-forbid
      (with-params-or-error
        (do-or-server-fail
          (db/update-elector! db/*db* (merge {:email nil, :phone nil, :gender nil} params))
          201)
        params
        #{:id :signature})
      request)))


(defroutes rest-routes
  (GET "/rest/get-local-data"
       request (route/restricted (get-local-data request)))
  (GET "/rest/create-intention"
       request (route/restricted (create-intention-and-visit! request)))
  (GET "/rest/create-request"
       request (route/restricted (create-request-and-visit! request)))
  (GET "/rest/update-elector-signature"
       request (route/restricted (update-elector-signature! request))))

(ns ^{:doc "Manually maintained routes which handle data transfer to/from the canvasser app."
      :author "Simon Brooke"} youyesyet.routes.rest
  (:require [adl-support.core :refer [massage-params do-or-log-error]]
            [clojure.core.memoize :as memo]
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
    [{latitude :latitude longitude :longitude locality :locality}
     (keywordize-keys (:params request))
     here (if locality
            (coerce-to-number locality)
            (l/locality
                       (coerce-to-number latitude)
                       (coerce-to-number longitude)))]
    (in-get-local-data here)))


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
    (if
      (=
        (:address_id params)
        (:address_id last-visit))
      (:id last-visit)
      (db/create-visit!
        db/*db*
        (assoc
          params
          :canvasser_id (-> request :session :user :id)
          :date (jt/to-sql-timestamp (jt/local-date-time)))))))


;; (current-visit-id {:session {:user {:email "simon@journeyman.cc",
;;                                               :phone "07768 130255",
;;                                               :roles #{"analysts" "canvassers" "admin" "teamorganisers" "issueexperts" "issueeditors"},
;;                                               :username "simon_brooke",
;;                                               :fullname "Simon Brooke",
;;                                               :bio "Sinister pagan",
;;                                               :elector_id 2, :id 4, :address_id 2,
;;                                               :authority_id "GitHub",
;;                                               :authorised true}}
;;                              :params {:address_id 79, :elector_id 238, :locality 5494393, :option_id "Yes"}})
;; (current-visit-id {:session {:user {:email "simon@journeyman.cc",
;;                                               :phone "07768 130255",
;;                                               :roles #{"analysts" "canvassers" "admin" "teamorganisers" "issueexperts" "issueeditors"},
;;                                               :username "simon_brooke",
;;                                               :fullname "Simon Brooke",
;;                                               :bio "Sinister pagan",
;;                                               :elector_id 2, :id 4, :address_id 2,
;;                                               :authority_id "GitHub",
;;                                               :authorised true}}
;;                              :params {:address_id 80, :elector_id 239, :locality 5494393, :option_id "Yes"}})


(defmacro do-or-return-reason
  "Clojure stacktraces are unreadable. We have to do better; evaluate
  this `form` in a try-catch block; return a map. If the evaluation
  succeeds, the map will have a key `:result` whose value is the result;
  otherwise it will have a key `:error` which will be bound to the most
  sensible error message we can construct."
  ;; TODO: candidate for moving to adl-support.core
  [form]
  `(try
     {:result ~form}
     (catch Exception any#
       (clojure.tools.logging/error
         (str (.getName (.getClass any#))
              ": "
              (.getMessage any#)
              (with-out-str
                (-> any# .printStackTrace))))
       {:error
        s/join "\n\tcaused by: "
        (reverse
          (loop [ex# any# result# ()]
            (if-not (nil? ex#)
              (recur
                (.getCause ex#)
                (str (.getName (.getClass ex#)) ": " (.getMessage ex#)))
              result#)))})))


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
    (if (-> request :session :user)
      (if
        (and
          (or (:locality params)
              (and (:elector_id params)
                   (:address_id params)))
          (:option_id params))
        (let [r (do-or-return-reason
                  (db/create-intention!
                    db/*db*
                    (assoc
                      params :visit_id (current-visit-id request))))]
          (if
            (:result r)
            {:status 201
             :body (with-out-str
                     (print
                       (hash-map
                         :id (:id (:result r))
                         )))}
            {:status 500
             :body (:error r)}))
        {:status 400
         :body "create-intention requires params: `option_id` and either `locality` or both `address_id` and `elector_id`."})
      {:status 403
       :body "You must be logged in to do that"})))


(defn create-request-and-visit!
  "Doing visit creation logic server side; request params are expected to
  include an `issue`, an `elector_id` and an `address_id` (and also a
  `method_id` and `method_detail`). Ye cannae reasonably create a request
  without having recorded the visit, so let's not muck about."
  [request]
  (let [params (massage-params request)]
    (db/create-followuprequest!
      db/*db*
      (assoc
        params
        :visit-id (current-visit-id request)))))


(defroutes rest-routes
  (GET "/rest/get-local-data" request (route/restricted (get-local-data request)))
  (GET "/rest/create-intention" request (route/restricted (create-intention-and-visit! request)))
  (GET "/rest/create-request" request (route/restricted (create-request-and-visit! request)))
  ;;   (GET "/rest/get-issues" request (route/restricted (get-issues request)))
  ;;   (GET "/rest/set-intention" request (route/restricted (set-intention request)))
  ;;   (GET "/rest/request-followup" request (route/restricted (request-followup request))))
  )

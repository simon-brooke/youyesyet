(ns ^{:doc "Routes which handle data transfer to/from the canvasser app."
      :author "Simon Brooke"} youyesyet.routes.rest
  (:require [clojure.core.memoize :as memo]
            [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :refer [defroutes GET POST]]
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


(def in-get-local-data
  "Local data is volatile, because hopefully canvassers are updating it as they
  work. So cache for only 90 seconds."
  (memo/ttl
    (fn [here]
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



(defn get-issues
  "Get current issues. No arguments expected."
  [request])

(defroutes rest-routes
   (GET "/rest/get-local-data" request (get-local-data request))
;;   (GET "/rest/get-issues" request (route/restricted (get-issues request)))
;;   (GET "/rest/set-intention" request (route/restricted (set-intention request)))
;;   (GET "/rest/request-followup" request (route/restricted (request-followup request))))
)

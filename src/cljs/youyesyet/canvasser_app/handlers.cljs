(ns ^{:doc "Canvasser app event handlers."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.handlers
  (:require [ajax.core :refer [GET]]
            [ajax.json :refer [json-request-format json-response-format]]
            [cemerick.url :refer (url url-encode)]
            [cljs.reader :refer [read-string]]
            [clojure.walk :refer [keywordize-keys]]
            [day8.re-frame.http-fx]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx subscribe]]
            [youyesyet.canvasser-app.gis :refer [refresh-map-pins get-current-location]]
            [youyesyet.canvasser-app.state :as db]
            [youyesyet.locality :refer [locality]]
            ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.handlers: event handlers.
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

(defn clear-messages
  "Return a state like this state except with the error and feedback messages
  set nil"
  [state]
  (merge state {:error '() :feedback '()}))


(def source-host (assoc
                   (url js/window.location)
                   :path "/"
                   :query nil
                   :anchor nil))


(def feedback-messages
  {:fetch-locality "Fetching local data."
   :send-request "Request has been queued."
   })


(defn add-to-feedback
  "Add the value of `k` in `feedback-messages` to the feedback in this `db`."
  [db k]
  (assoc db :feedback (cons k (:feedback db))))


(defn remove-from-feedback
  "Remove the value of `k` in `feedback-messages` to the feedback in this `db`."
  [db k]
  (assoc db
    :feedback
    (remove
      #(= % k)
      (:feedback db))))


(defn coerce-to-number [v]
  "If it is possible to do so, coerce `v` to a number.
  NOTE: I tried to do this in *cljc*, but it did not work. Leave it alone."
  (if (number? v) v
    (try
      (read-string (str v))
      (catch js/Object any
        (js/console.log (str "Could not coerce '" v "' to number: " any))
        v))))


(defn get-elector
  "Return the elector at this address (or the current address if not specified)
  with this id."
  ([elector-id state]
   (get-elector elector-id state (:address state)))
  ([elector-id state address]
   (try
     (first
       (remove
         nil?
         (map
           #(if (= (coerce-to-number elector-id) (:id %)) %)
           (:electors state))))
     (catch js/Object _
       (str
         "Failed to find id '"
         elector-id
         "' among '"
         (:electors state) "'")))))


(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))


;; (reg-event-fx
;;   :feedback
;;   (fn [x y]
;;     (js/console.log (str "Feedback event called with x = " x "; y = " y))
;;     (:db x)))


;; (reg-event-fx
;;   :issues
;;   (fn [x y]
;;     (js/console.log (str "Issues event called with x = " x "; y = " y))
;;     (:db x)))


;; (reg-event-fx
;;   :options
;;   (fn [x y]
;;     (js/console.log (str "Options event called with x = " x "; y = " y))
;;     (:db x)))


;; (reg-event-fx
;;   :event
;;   (fn [x y]
;;     (js/console.log (str "Event event called with x = " x "; y = " y))
;;     (:db x)))


(reg-event-fx
 :fetch-locality
 (fn [{db :db} _]
   (let [locality (locality (:latitude db) (:longitude db))
          uri (str source-host
                  "rest/get-local-data?locality="
                  locality)]
     (js/console.log
      (str
       "Fetching locality data: " uri))
     ;; we return a map of (side) effects
     {:http-xhrio {:method          :get
                   :uri             uri
                   :format          (json-request-format)
                   :response-format (json-response-format {:keywords? true})
                   :on-success      [:process-locality]
                   :on-failure      [:bad-locality]}
      :db  (add-to-feedback db :fetch-locality)})))


(reg-event-db
  :get-current-location
  (fn [db _]
    (js/console.log "Updating current location")
    (assoc db :froboz (get-current-location))))


(reg-event-db
  :process-locality
  (fn
    [db [_ response]]
    (js/console.log (str "Updating locality data: " (count response) " addresses"))
    ;; loop to do it again
    (dispatch [:dispatch-later [{:ms 5000 :dispatch [:fetch-locality]}
                                {:ms 1000 :dispatch [:get-current-location]}]])
    (refresh-map-pins)
    (assoc
      (remove-from-feedback db :fetch-locality)
      :addresses (js->clj response))))


(reg-event-db
  :bad-locality
  (fn [db _]
    ;; TODO: signal something has failed? It doesn't matter very much, unless it keeps failing.
    (js/console.log "Failed to fetch locality data")
    ;; loop to do it again
    (dispatch [:dispatch-later [{:ms 60000 :dispatch [:fetch-locality]}
                                {:ms 1000 :dispatch [:get-current-location]}]])
    (assoc
      (remove-from-feedback db :fetch-locality)
      :error (cons :fetch-locality (:error db)))))


(reg-event-fx
  :fetch-options
  (fn [{db :db} _]
    (js/console.log "Fetching options")
    ;; we return a map of (side) effects
    {:http-xhrio {:method          :get
                  :uri             (str source-host "json/auto/list-options")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:process-options]
                  :on-failure      [:bad-options]}
     :db  (add-to-feedback db :fetch-options)}))


(reg-event-db
  :process-options
  (fn
    [db [_ response]]
    (let [options (js->clj response)]
      (js/console.log (str "Updating options: " options))
      (assoc
        (remove-from-feedback db :fetch-options)
        :options options))))


(reg-event-db
  :bad-options
  (fn [db _]
    (js/console.log "Failed to fetch options")
    (assoc
      (remove-from-feedback db :fetch-options)
      :error (cons :fetch-options (:error db)))))


(reg-event-fx
  :fetch-issues
  (fn [{db :db} _]
    (js/console.log "Fetching issues")
    ;; we return a map of (side) effects
    {:http-xhrio {:method          :get
                  :uri             (str source-host "json/auto/list-issues")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:process-issues]
                  :on-failure      [:bad-issues]}
     :db (add-to-feedback db :fetch-issues)}))


(reg-event-db
  :process-issues
  (fn
    [db [_ response]]
    (let [issues (reduce
                   merge {}
                   (map
                     #(hash-map (keyword (:id %)) %)
                     (js->clj response)))]
      (js/console.log (str "Updating issues: " issues))
      (assoc
        (remove-from-feedback db :fetch-issues)
        :issues issues))))


(reg-event-db
  :bad-issues
  (fn [db _]
    (js/console.log "Failed to fetch issues")
    (assoc
      (remove-from-feedback db :fetch-issues)
      :error (cons :fetch-issues (:error db)))))


(reg-event-db
  :send-intention
  (fn [db [_ args]]
    (let [intention (:intention args)
          elector-id (:elector-id args)
          old-elector (first
                        (remove nil?
                                (map
                                  #(if (= elector-id (:id %)) %)
                                  (:electors (:dwelling db)))))
          new-elector (assoc old-elector :intention intention)
          old-dwelling (:dwelling db)
          new-dwelling (assoc
                         old-dwelling
                         :electors
                         (cons
                           new-elector
                           (remove
                             #(= % old-elector)
                             (:electors old-dwelling))))
          old-address (:address db)
          new-address (assoc
                        old-address
                        :dwellings
                        (cons
                          new-dwelling
                          (remove
                            #(= % old-dwelling)
                            (:dwellings old-address))))]
      (cond
        (nil? old-elector)
        (assoc db :error (cons "No elector found; not setting intention" (:error db))
          (= intention (:intention old-elector))
          (do
            (js/console.log "Elector's intention hasn't changed; not setting intention")
            db))
        true
        (do
          (js/console.log (str "Setting intention of elector " old-elector " to " intention))
          (merge
            (clear-messages db)
            {:addresses
             (cons
               new-address
               (remove #(= % old-address) (:addresses db)))
             :address new-address
             :dwelling new-dwelling
             :elector new-elector
             :outqueue (cons
                         (assoc args :action :set-intention)
                         (:outqueue db))}))))))


 (reg-event-db
  :send-request
  (fn [db [_ _]]
    (if (and (:elector db) (:issue db) (:telephone db))
      (do
        (js/console.log "Sending request")
        (assoc (add-to-feedback db :send-request)
          :outqueue (cons
                     {:elector-id (:id (:elector db))
                      :issue (:issue db)
                      :action :add-request} (:outqueue db))))
      (assoc db :error "Please supply a telephone number to call"))))


(reg-event-db
 :set-active-page
 (fn [db [_ k]]
   (if k
     (assoc (clear-messages db) :page k)
     db)))


(reg-event-db
 :set-address
 (fn [db [_ address-id]]
   (let [id (coerce-to-number  address-id)
         address (first (remove nil? (map #(if (= id (:id %)) %) (:addresses db))))]
     (if
       (= (count (:dwellings address)) 1)
       (assoc (clear-messages db)
         :address address
         :dwelling (first (:dwellings address))
         :electors (:electors (first (:dwellings address)))
         :page :dwelling)
       (assoc (clear-messages db)
         :address address
         :dwelling nil
         :electors nil
         :page :building)))))


(reg-event-db
  :set-consent-and-page
  (fn [db [_ args]]
    (let [page (:page args)
          consent (:consent args)
          elector-id (coerce-to-number (:elector-id args))
          elector (get-elector elector-id db)]
      (js/console.log (str "Setting page to " page ", consent to " consent " for " (:name elector)))
      (assoc (clear-messages db) :elector (assoc elector :consent true) :page page))))


(reg-event-db
  :set-dwelling
  (fn [db [_ dwelling-id]]
    (let [id (coerce-to-number dwelling-id)
          dwelling (first
                     (remove
                       nil?
                       (map
                         #(if (= id (:id %)) %)
                         (mapcat :dwellings (:addresses db)))))]
      (if dwelling
        (assoc
          (clear-messages db)
          :dwelling dwelling
          :electors (:electors dwelling)
          :page :dwelling)))))


(reg-event-db
 :set-and-go-to-issue
 (fn [db [_ issue]]
   (js/console.log (str "Setting page to :issue, issue to " issue ", issues are " (:issues db)))
   (assoc (assoc (clear-messages db) :issue (keyword issue)) :page :issue)))


(reg-event-db
  :set-elector-and-page
  (fn [db [_ args]]
    (let [page (:page args)
          elector-id (:elector-id args)
          elector (get-elector elector-id db)]
      (js/console.log (str "Setting page to " page ", elector to " elector))
      (assoc (clear-messages db) :elector elector :page page))))


(reg-event-db
 :set-elector
 (fn [db [_ elector-id]]
   (let [elector (get-elector (coerce-to-number elector-id) db)]
     (js/console.log (str "Setting elector to " elector))
     (assoc (clear-messages db) :elector elector))))


(reg-event-db
 :set-issue
 (fn [db [_ issue]]
   (js/console.log (str "Setting issue to " issue))
   (assoc (clear-messages db) :issue (keyword issue))))


(reg-event-db
 :set-latitude
 (fn [db [_ v]]
   (assoc db :latitude (coerce-to-number v))))


(reg-event-db
 :set-longitude
 (fn [db [_ v]]
   (assoc db :longitude (coerce-to-number v))))


(reg-event-db
 :set-telephone
 (fn [db [_ telephone]]
   (js/console.log (str "Setting telephone to " telephone))
   (assoc (clear-messages db) :telephone telephone)))


(reg-event-db
  :set-view
  (fn [db [_ view]]
    (assoc db :view view)))


(reg-event-db
  :set-zoom
  (fn [db [_ zoom]]
    (if (integer? zoom)
      (assoc db :zoom zoom)
      db)))

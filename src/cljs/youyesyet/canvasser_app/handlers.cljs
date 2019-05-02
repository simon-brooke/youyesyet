(ns ^{:doc "Canvasser app event handlers."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.handlers
  (:require [ajax.core :refer [GET]]
            [ajax.json :refer [json-request-format json-response-format]]
            [cemerick.url :refer (url url-encode)]
            [cljs.reader :refer [read-string]]
            [clojure.walk :refer [keywordize-keys]]
            [re-frame.fx]
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


(def source-host
  "The base URL of the host from which the app was loaded."
  (assoc
    (url js/window.location)
    :path "/"
    :query nil
    :anchor nil))


(defn handle-forbidden
  "If response has status 403 (forbidden) redirect to the login page."
  [response & forms]
  (if
     (= (str (:status response)) "403")
     (do
       (js/console.log "Forbidden! redirecting")
       (set! (.-location js/document) "/login"))
     (apply 'do forms)))

(defn compose-packet
  [item]
  "Convert this `item` into a URI which can be sent as a GET call"
  (assoc
    (url js/window.location)
    :path (str "/rest/" (name (:action item)))
    :query (dissoc item :action)
    :fragment nil))


(def feedback-messages
  {:fetch-locality "Fetching local data."
   :send-request "Request has been queued."
   :send-intention-and-visit "Voting intention has been sent"
   })


(defn add-to-key
  "Return a copy of db with `x` added to the front of the list of items held
  against the key `k`"
  [db k x]
  (assoc db k (cons x (db k))))


(defn add-to-outqueue
  "Add the supplied `message` to the output queue in this `db`."
  [db message]
  (dispatch [:process-queue])
  (add-to-key db :outqueue message))


(defn add-to-feedback
  "Add `x` to the feedback in this `db`."
  [db x]
  (add-to-key db :feedback x))


(defn remove-from-key
  "Remove `x` from the values of key `k` in map `db`."
  [db k x]
  (assoc db k (remove #(= x %) (db k))))


(defn remove-from-feedback
  "Remove `x` from the feedback in this `db`."
  [db x]
  (remove-from-key db :feedback x))


(defn remove-from-outqueue
  "Remove `x` from the output queue in this `db`."
  [db x]
  (remove-from-key db :outqueue x))


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
          :db  (assoc
                 (add-to-feedback db :fetch-locality)
                 :locality locality)})))


(reg-event-db
 :get-current-location
 (fn [db _]
   (let [locality (get-current-location)]
     (js/console.log "Updating current location")
     (if
       (and (> locality 0) (not (= locality (:locality db))))
       (do
         (dispatch :fetch-locality) ;; if the locality has changed, fetch it immediately
         (assoc db :locality locality))
       db))))


(reg-event-fx
  :process-locality
  ;; TODO: why is this an `-fx`? Does it need to be?
  (fn
    [{db :db} [_ response]]
    (js/console.log (str ":process-locality: " response))
    (js/console.log (str "Updating locality data: " (count response) " addresses " ))
    (refresh-map-pins)
    {:db (assoc
           (remove-from-feedback db :fetch-locality)
           :addresses (js->clj response))}))


(reg-event-fx
  :bad-locality
  ;; TODO: why is this an `-fx`? Does it need to be?
  (fn
    [{db :db} [_ response]]
    ;; TODO: signal something has failed? It doesn't matter very much, unless it keeps failing.
    (js/console.log (str "Failed to fetch locality data" response))
    ;; loop to do it again
    (handle-forbidden
      response
      (dispatch [:dispatch-later [{:ms 60000 :dispatch [:fetch-locality]}]])
      {:db (assoc
             (remove-from-feedback db :fetch-locality)
             :error (cons :fetch-locality (:error db)))})))


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
  (fn [db [_ response]]
    (js/console.log "Failed to fetch options")
    (dispatch [:dispatch-later [{:ms 60000 :dispatch [:fetch-options]}]])
    (assoc
      db
      :error (:response response))))


(reg-event-fx
  :fetch-followupmethods
  (fn [{db :db} _]
    (js/console.log "Fetching options")
    ;; we return a map of (side) effects
    {:http-xhrio {:method          :get
                  :uri             (str source-host "json/auto/list-followupmethods")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:process-followupmethods]
                  :on-failure      [:bad-followupmethods]}
     :db  (add-to-feedback db :fetch-followupmethods)}))


(reg-event-db
  :process-followupmethods
  (fn
    [db [_ response]]
    (let [followupmethods (js->clj response)]
      (js/console.log (str "Updating followupmethods: " followupmethods))
      (assoc
        (remove-from-feedback db :fetch-followupmethods)
        :followupmethods followupmethods))))


(reg-event-db
  :bad-followupmethods
  (fn [db [_ response]]
    (js/console.log "Failed to fetch followupmethods")
    (dispatch [:dispatch-later [{:ms 60000 :dispatch [:fetch-followupmethods]}]])
    (assoc
      db
      :error (:response response))))


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
  (fn [db [_ response]]
    (js/console.log "Failed to fetch issues")
    (dispatch [:dispatch-later [{:ms 60000 :dispatch [:fetch-issues]}]])
    (assoc
      db
      :error (:response response))))


(reg-event-db
  :send-intention
  (fn [db [_ args]]
    (let [intention (:intention args)]
      (if
        (nil? (-> db :elector))
        (assoc db :error (cons "No elector found; not setting intention" (:error db)))
        (do
          (js/console.log (str "Setting intention of elector " (-> db :elector :id) " to " intention))
          (assoc
            (add-to-outqueue
              (clear-messages db)
              {:address_id (-> db :address :id)
               :locality (-> db :address :locality)
               :elector_id (-> db :elector :id)
               :option_id (:intention args)
               :action :create-intention})
            :elector (assoc (:elector db) :intention intention)
            :page :elector))))))


(reg-event-db
  :send-request
  (fn [db [_ _]]
    (if (and (:elector db) (:issue db) (:method_detail db))
      (do
        (js/console.log "Sending request")
        (add-to-feedback
          (add-to-outqueue
            db
            {:address_id (-> db :address :id)
             :elector_id (-> db :elector :id)
             :issue_id (name (-> db :issue))
             :issue_detail (-> db :issue-detail)
             :method_id (-> db :followupmethod)
             :method_detail (-> db :method_detail)
             :action :create-request})
          :send-request))
      (assoc db :error "Please supply a telephone number/email address for elector"))))


(reg-event-db
  :set-active-page
  (fn [db [_ k]]
    (js/console.log (str "Setting page to " k))
    (if k
      (assoc (clear-messages db) :page k)
      db)))


(reg-event-db
  :set-address
  (fn [db [_ address-id]]
    (let [id (coerce-to-number  address-id)
          address (first (remove nil? (map #(if (= id (:id %)) %) (:addresses db))))]
      (js/console.log (str "Set address to " address " "))
      (clear-messages
        (if
          (= (count (:dwellings address)) 1)
          (assoc db
            :address address
            :dwelling (first (:dwellings address))
            :electors (:electors (first (:dwellings address)))
            :page :dwelling)
          (assoc db
            :address address
            :dwelling nil
            :electors nil
            :page :building))))))


(defn do-update-elector
  [db elector]
  (if-not
    ;; if the signature has changed
    (= (:signature elector) (:signature (:elector db)))
    (assoc
      (add-to-outqueue
        (clear-messages db)
        (assoc elector
          :action :update-elector-signature))
      :elector elector)
    (assoc db
      :elector elector)))


(reg-event-db
  :update-elector
  (fn [db [_ elector]]
    (js/console.log (str "Elector is " elector))
    (do-update-elector db elector)))


(reg-event-db
  :set-consent-and-page
  (fn [db [_ args]]
    (assoc
      (do-update-elector db (:elector args))
      :page (:page args))))


(reg-event-db
  :set-dwelling
  (fn [db [_ dwelling-id]]
    (js/console.log (str "Setting dwelling to " dwelling-id " "))
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
  :set-followupmethod
  (fn [db [_ method-id]]
     (js/console.log (str "Setting followupmethod to " method-id))
    (assoc db :followupmethod method-id)))


(reg-event-db
 :set-issue
 (fn [db [_ issue]]
   (js/console.log (str "Setting issue to " issue))
   (assoc (clear-messages db) :issue (keyword issue))))

(reg-event-db
 :set-issue-detail
 (fn [db [_ issue-detail]]
   (js/console.log (str "Setting issue-detail to " issue-detail))
   (assoc (clear-messages db) :issue-detail issue-detail)))


(reg-event-db
 :set-latitude
 (fn [db [_ v]]
   (assoc db :latitude (coerce-to-number v))))


(reg-event-db
 :set-longitude
 (fn [db [_ v]]
   (assoc db :longitude (coerce-to-number v))))


(reg-event-db
 :set-method-detail
 (fn [db [_ detail]]
   (assoc (clear-messages db) :method_detail detail)))


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


(reg-event-fx
  :process-queue
  (fn [{db :db} _]
    (if-let [item (first (:outqueue db))]
      ;; if there's something in the queue, transmit it...
      (let [uri (compose-packet item)]
        (js/console.log (str "Transmitting item: " uri))
        {:http-xhrio {:method          :get
                      :uri             uri
                      :format          (json-request-format)
                      :response-format (json-response-format {:keywords? true})
                      :on-success      [:tx-success]
                      :on-failure      [:tx-failure]}
         :db (assoc
               (add-to-feedback db :process-queue)
               :tx-item item
               :outqueue (remove #(= % item) (:outqueue db)))})
      ;; else try again in a minute
      (do
        (js/console.log "Nothing to send to server")
        (dispatch [:dispatch-later [{:ms 60000 :dispatch [:process-queue]}]])
        {:db db}))))


(reg-event-db
  :tx-success
  (fn
    [db [_ response]]
    (let [r (js->clj response)]
      (js/console.log (str "Transmission success: " r))
      ;; while we've got comms working, get as many items through as we can.
      (dispatch [:process-queue])
      db)))


(reg-event-db
  :tx-failure
  (fn [db [_ response]]
    (case
      (:status response)
      (400 403 404 500)
      (let [error (str
                   "Transmission failed "
                   (:status response)
                   " - "
                   (:response response)
                   "; not requeueing")]
        (js/console.log error)
        (assoc (clear-messages db) :error error))
      ;; default
      (do
        (js/console.log (str "Transmission failed (" response "), requeueing" (:tx-item db)))
        (assoc
          (add-to-outqueue db (:tx-item db))
          :error "Transmission failed, requeueing")))))

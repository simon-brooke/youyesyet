(ns ^{:doc "Canvasser app event handlers."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.handlers
  (:require [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch reg-event-db]]
            [youyesyet.canvasser-app.state :as db]
            ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.handlers: handlers for events.
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
  (merge state {:error nil :feedback nil}))


(defn get-elector
  "Return the elector at this address (or the current address if not specified)
  with this id."
  ([elector-id state]
   (get-elector elector-id state (:address state)))
  ([elector-id state address]
   (first
    (remove
     nil?
     (map
      #(if (= elector-id (:id %)) %)
      (:electors address))))))


(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))


(reg-event-db
 :send-intention
 (fn [db [_ args]]
   (let [intention (:intention args)
         elector-id (:elector-id args)
         elector
         (first
          (remove nil?
                  (map
                   #(if (= elector-id (:id %)) %)
                   (:electors (:address db)))))
         old-address (:address db)
         new-address (assoc old-address :electors (cons (assoc elector :intention intention) (remove #(= % elector) (:electors old-address))))]
     (cond
      (nil? elector)
      (assoc db :error "No elector found; not setting intention")
      (= intention (:intention elector)) (do (js/console.log "Elector's intention hasn't changed; not setting intention") db)
      true
      (do
        (js/console.log (str "Setting intention of elector " elector " to " intention))
        (merge
         (clear-messages db)
         {:addresses
          (cons new-address (remove old-address (:addresses db)))
          :address new-address
          :elector elector
          :outqueue (cons (assoc args :action :set-intention) (:outqueue db))}))))))


 (reg-event-db
  :send-request
  (fn [db [_ _]]
    (if (and (:elector db) (:issue db) (:telephone db))
      (do
        (js/console.log "Sending request")
        (assoc db
          :feedback "Request has been queued"
          :outqueue (cons
                     {:elector-id (:id (:elector db))
                      :issue (:issue db)
                      :action :add-request} (:outqueue db))))
      (assoc db :error "Please supply a telephone number to call"))))


(reg-event-db
 :set-active-page
 (fn [db [_ page]]
   (if page
     (assoc (clear-messages db) :page page))))


(reg-event-db
 :set-address
 (fn [db [_ address-id]]
   (let [id (read-string address-id)
         address (first (remove nil? (map #(if (= id (:id %)) %) (:addresses db))))]
     (if
       (= (count (:dwellings address)) 1)
       (assoc (clear-messages db)
         :address address
         :dwelling (first (:dwellings address))
         :page :electors)
       (assoc (clear-messages db)
         :address address
         :dwelling nil
         :page :building)))))


(reg-event-db
  :set-dwelling
  (fn [db [_ dwelling-id]]
    (let [id (read-string dwelling-id)
          dwelling (first
                     (remove
                       nil?
                       (map
                         #(if (= id (:id %)) %)
                         (mapcat :dwellings (:addresses db)))))]
      (assoc (clear-messages db) :dwelling dwelling :page :electors))))


(reg-event-db
 :set-and-go-to-issue
 (fn [db [_ issue]]
   (js/console.log (str "Setting page to :issue, issue to " issue))
   (assoc (assoc (clear-messages db) :issue issue) :page :issue)))


 (reg-event-db
  :set-elector-and-page
  (fn [db [_ args]]
    (let [page (:page args)
          elector-id (read-string (:elector-id args))
          elector (get-elector elector-id db)]
      (js/console.log (str "Setting page to " page ", elector to " elector))
      (assoc (clear-messages db) :elector elector :page page))))


(reg-event-db
 :set-elector
 (fn [db [_ elector-id]]
   (let [elector (get-elector (read-string elector-id) db)]
     (js/console.log (str "Setting elector to " elector))
     (assoc (clear-messages db) :elector elector))))


(reg-event-db
 :set-issue
 (fn [db [_ issue]]
   (js/console.log (str "Setting issue to " issue))
   (assoc (clear-messages db) :issue issue)))


(reg-event-db
 :set-latitude
 (fn [db [_ issue]]
   (assoc db :latitude issue)))


(reg-event-db
 :set-longitude
 (fn [db [_ issue]]
   (assoc db :longitude issue)))


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

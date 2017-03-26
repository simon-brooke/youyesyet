(ns youyesyet.handlers
  (:require [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch reg-event-db]]
            [youyesyet.db :as db]
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

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))


(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))


(reg-event-db
 :set-address
 (fn [db [_ address-id]]
   (let [id (read-string address-id)
         address (first (remove nil? (map #(if (= id (:id %)) %) (:addresses db))))]
     (assoc (assoc db :address address) :page :electors))))


(reg-event-db
 :set-elector-and-page
 (fn [db [_ args]]
   (let [page (:page args)
         elector-id (read-string (:elector-id args))
         elector
         (first
          (remove nil?
                 (map
                  #(if (= elector-id (:id %)) %)
                  (:electors (:address db)))))]
     (js/console.log (str "Setting page to " page ", elector to " elector))
     (assoc (assoc db :elector elector) :page page))))


(reg-event-db
 :set-issue
 (fn [db [_ issue]]
   (js/console.log (str "Setting page to :issue, issue to " issue))
   (assoc (assoc db :issue issue) :page :issue)))



(ns ^{:doc "Canvasser app event subscriptions."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.electors: subscriptions for everything in the app state.
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

(reg-sub
  :motd
  (fn [db _]
    (:motd db)))

(reg-sub
  :address
  (fn [db _]
    (:address db)))

(reg-sub
  :addresses
  (fn [db _]
    (:addresses db)))

(reg-sub
 :changes
 (fn [db _]
   (:changes db)))

(reg-sub
  :dwelling
  (fn [db _]
    (:dwelling db)))

(reg-sub
  :elector
  (fn [db _]
    (:elector db)))

(reg-sub
 :error
  (fn [db _]
    (apply str (:error db))))

(reg-sub
 :feedback
  (fn [db _]
    (:feedback db)))

(reg-sub
  :followupmethod
  (fn [db _]
    (:followupmethod db)))

(reg-sub
  :followupmethods
  (fn [db _]
    (:followupmethods db)))

(reg-sub
  :issue
  (fn [db _]
    (:issue db)))

(reg-sub
  :issues
  (fn [db _]
    (:issues db)))

(reg-sub
  :latitude
  (fn [db _]
    (:latitude db)))

(reg-sub
  :longitude
  (fn [db _]
    (:longitude db)))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :options
  (fn [db _]
    (:options db)))

(reg-sub
 :outqueue
 (fn [db _]
   (:outqueue db)))

(reg-sub
  :view
  (fn [db _]
    (:view db)))

(reg-sub
  :zoom
  (fn [db _]
    (:zoom db)))

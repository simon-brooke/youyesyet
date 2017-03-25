(ns youyesyet.subscriptions
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
  :electors
  (fn [db _]
    (:electors db)))

(reg-sub
  :issue
  (fn [db _]
    (:issue db)))

(reg-sub
  :issues
  (fn [db _]
    (:issues db)))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :options
  (fn [db _]
    (:options db)))

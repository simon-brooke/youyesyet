(ns youyesyet.views.followup
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [youyesyet.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.followup-request: followup-request view for youyesyet.
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


;;; The pattern from the re-com demo (https://github.com/Day8/re-com) is to have
;;; one source file/namespace per view. Each namespace contains a function 'panel'
;;; whose output is an enlive-style specification of the view to be redered.
;;; I propose to follow this pattern. This file will (eventually) provide the followup-request view.

;;; See https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md#followup-request-form

(defn panel
  "Generate the followup-request panel."
  []
  (let [issue @(subscribe [:issue])
        issues @(subscribe [:issues])
        elector @(subscribe [:elector])
        address @(subscribe [:address])]
    (js/console.log (str "Issue is " issue "; elector is " elector))
    (cond
     (nil? address)
     (ui/error-panel "No address selected")
     (nil? issues)
     (ui/error-panel "No issues loaded")
     true
     [:div
      [:h1 "Followup Request"]
      [:div.container {:id "main-container"}
       [:form {}
        [:p.widget
         [:label {:for "elector"} "Elector"]
         [:select {:id "elector" :name "elector" :value (:id elector)}
          (map
           #(let []
              [:option {:value (:id %) :key (:id %)} (:name %)]) (:electors address))]]
        [:p.widget
         [:label {:for "issue"} "Issue"]
         [:select {:id "issue" :name "issue" :value issue}
          (map
           #(let []
              [:option {:key % :value %} %]) (keys issues))]]
        [:p.widget
         [:label {:for "telephone"} "Telephone number"]
         [:input {:type "text" :id "telephone" :name "telephone"}]]
        [:p.widget
         [:label {:for "submit"} "To request a call"]
         [:input {:id "submit" :name "submit" :type "submit" :value "Send this!"}]]
        ]
       (ui/back-link)]])))


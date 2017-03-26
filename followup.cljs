(ns youyesyet.views.followup
  (:require [reagent.core :as r]
            [re-frame.core :refer [reg-sub subscribe]]
;;             [re-frame-forms.core :as form]
;;             [re-frame-forms.input :as input]
;;             [re-com.core     :refer [h-box v-box box gap single-dropdown input-text checkbox label title hyperlink-href p]]
;;             [re-com.dropdown :refer [filter-choices-by-keyword single-dropdown-args-desc]]
            [youyesyet.ui-utils :as ui]
))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.followup: followup-request view for youyesyet.
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
  (js/console.log (str "Rendering follow-up form"))

  (let [issue @(subscribe [:issue])
        issues @(subscribe [:issues])
        elector @(subscribe [:elector])
        address @(subscribe [:address])
        form (form/make-form {:elector (:id elector)
                              :issue (:id issue)})]
    [:div
     [:h1 "Followup Request"]
     (let [selected-elector-id (r/atom (:id elector))
           selected-issue (r/atom (:id issue))]
       [:form {}
        [:p.widget
         [:label {:for "elector"} "Elector"]
         [single-dropdown
          :id elector
          :choices (:electors address)
          :model selected-elector-id
          :label-fn #(:name %)]]
        [:p.widget
         [:label {:for "issue"} "Issue"]
         [single-dropdown
          :id issue
          :choices (map #({:id % :label %}) (keys issues))
          :model issue]]

        ])]))

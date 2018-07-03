(ns ^{:doc "Canvasser app electors in household panel."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.views.dwelling
  (:require [reagent.core :refer [atom]]
            [re-frame.core :refer [reg-sub subscribe dispatch]]
            [youyesyet.canvasser-app.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.views.dwelling: dweling view for youyesyet.
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
;;; I propose to follow this pattern. This file will provide the electors view.

;;; See https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md#electors-view

;;; The design for this panel is one column per elector within the address.
;;; Each column contains
;;; 1. a stick figure identifying gender (for recognition);
;;; 2. the elector's name;
;;; The mechanics of how this panel is laid out don't matter.

(defn go-to-gdpr-for-elector [elector]
  (dispatch [:set-elector-and-page {:elector-id (:id elector)
                                    :page :gdpr}]))


(defn gender-cell
  [elector]
  (let [gender (:gender elector)
        image (if gender (name gender) "Unknown")]
    [:td {:key (str "gender-" (:id elector))}
      [:a {:href (str "#gdpr/" (:id elector))}
       [:img {:src (str "img/gender/" image ".png") :alt image}]]]))


(defn genders-row
  [electors]
  [:tr
   (map
     #(gender-cell %) electors)])


(defn name-cell
  [elector]
  [:td {:key (str "name-" (:id elector))
        :on-click  #(go-to-gdpr-for-elector elector)}
   (:name elector)])


(defn names-row
  [electors]
  [:tr
   (map
     #(name-cell %) electors)])


(defn panel
  "Generate the electors panel."
  []
  (let [dwelling @(subscribe [:dwelling])
        address @(subscribe [:address])
        sub-address (:sub-address dwelling)
        electors (sort-by :id (:electors dwelling))
        options @(subscribe [:options])]
    (if address
      [:div
       [:h1 (if sub-address
              (str sub-address ", " (:address address))
              (:address address))]
       [:div.container {:id "main-container"}
        [:table
         [:tbody
          (genders-row electors)
          (names-row electors)]]
        (ui/back-link)]]
      (ui/error-panel "No address selected"))))



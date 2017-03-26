(ns youyesyet.views.electors
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [youyesyet.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.electors: electors view for youyesyet.
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
;;; I propose to follow this pattern. This file will (eventually) provide the electors view.

;;; See https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md#electors-view

;;; The design for this panel is one column per elector within the address.
;;; Each column contains
;;; 1. a stick figure identifying gender (for recognition);
;;; 2. the elector's name;
;;; 3. one icon for each option on the ballot;
;;; 4. an 'issues' icon.
;;; The mechanics of how this panel is laid out don't matter.

(defn gender-cell
  [elector]
  (let [gender (:gender elector)
        image (if gender (name gender) "unknown")]
    [:td {:key (:id elector)} (if gender [:img {:src (str "img/gender/" image ".png") :alt image}])]))

(defn genders-row
  [electors]
  [:tr
   (map
     #(gender-cell %) electors)])

(defn name-cell
  [elector]
  [:td {:key (str "name-" (:id elector))} (:name elector)])

(defn names-row
  [electors]
  [:tr
   (map
     #(name-cell %) electors)])

(defn options-row
  [electors option]
  (let [optid (:id option)
        optname (name optid)]
    [:tr {:key (str "options-" optid)}
     (map
       #(let [selected (= optid (:intention %))
              image (if selected (str "img/option/" optname "-selected.png")
                      (str "img/option/" optname "-unselected.png"))]
          [:td  {:key (str "option-" optid "-" (:id %))}
           [:a {:href (str "#/set-intention/" (:id %) "/" optid)}
            [:img {:src image :alt optname}]]])
       electors)]))

(defn issue-cell
  "Create an issue cell for a particular elector"
  [elector]
  [:td {:key (:id elector)}
   [:a {:href (str "#/issues/" (:id elector))}
    [:img {:src "/img/issues.png" :alt "Issues"}]]])

(defn issues-row
  [electors]
  [:tr
   (map
     #(issue-cell %)
     electors)])

(defn panel
  "Generate the electors panel."
  []
  (let [address @(subscribe [:address])
        electors (:electors address)
        options @(subscribe [:options])]
    (if address
      [:div
       [:h1 (:address address)]
       [:div.container {:id "main-container"}
        [:table
         [:tbody
          ;; genders row
          (genders-row electors)
          ;; names row
          (names-row electors)
          ;; options rows
          (map
           #(options-row electors %)
           options)
          ;; issues row
          (issues-row electors)]]
        (ui/back-link)]]
      (ui/error-panel "No address selected"))))



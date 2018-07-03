(ns ^{:doc "Canvasser app single elector panel."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.views.elector
  (:require [reagent.core :refer [atom]]
            [re-frame.core :refer [reg-sub subscribe dispatch]]
            [youyesyet.canvasser-app.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.views.elector: elector view for youyesyet.
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


(defn gender-row
  "Generate a row containing a cell showing the gender of this `elector`."
  [elector]
  (let [gender (:gender elector)
        image (if gender (name gender) "unknown")]
    [:tr
     [:td {:key (:id elector)}
      [:img {:src (str "img/gender/" image ".png") :alt image}]]]))


(defn name-row
  "Generate a row containing a cell showing the name of this `elector`."
  [elector]
  [:tr
     [:td {:key (:id elector)}
      (:name elector)]])


(defn option-row
  "Generate a row showing this `option` for this elector."
  [elector option]
  (let [optid (:id option)
        optname (name optid)]
    [:tr {:key (str "options-" optname)}
     (let [selected (= optid (:intention elector))
           image (if selected (str "img/option/" optname "-selected.png")
                   (str "img/option/" optname "-unselected.png"))]
       [:td  {:key (str "option-" optid "-" (:id elector))}
        [:img
         {:src image
          :alt optname
          :on-click #(dispatch
                       [:send-intention {:elector-id (:id elector)
                                         :intention optid}])}]])]))


(defn issue-row
  "Generate a row containing an issue cell for a particular elector"
  [elector]
  [:tr
   [:td {:key (:id elector)}
    [:a {:href (str "#/issues/" (:id elector))}
     [:img {:src "img/issues.png" :alt "Issues"}]]]])


(defn panel
  "Generate the elector panel."
  []
  (let [elector @(subscribe [:elector])
        options @(subscribe [:options])]
    (if elector
      [:div
       [:h1 (:name elector)]
       [:div.container {:id "main-container"}
        [:table
         [:tbody
          (map
            #(option-row elector %)
            options)
          (issue-row elector)]]
        (ui/back-link "#dwelling")]]
      (ui/error-panel "No elector selected"))))

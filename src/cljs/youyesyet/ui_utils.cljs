(ns youyesyet.ui-utils
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.ui-utils: User interface stuff common to many views.
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


(defn back-link []
  [:div.back-link-container {:id "back-link-container"}
   [:a {:href "javascript:history.back()" :id "back-link"} "Back"]])


(defn big-link [text target]
  [:div.big-link-container {:key target}
   [:a.big-link {:href target} text]])


(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))


(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:div {:id "nav"}
     [:img {:id "nav-icon"
            :src "img/threelines.png"
            :on-click #(swap! collapsed? not)}]
     [:menu.nav {:id "nav-menu" :class (if @collapsed? "hidden" "shown")}
      (nav-link "#/map" "Map" :map collapsed?)
      (nav-link "#/electors" "Electors" :electors collapsed?)
      (nav-link "#/issues" "Issues" :issues collapsed?)
      (nav-link "#/about" "About" :about collapsed?)]]))

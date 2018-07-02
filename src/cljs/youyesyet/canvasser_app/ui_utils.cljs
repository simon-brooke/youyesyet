(ns ^{:doc "Canvasser app user interface widgets."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.ui-utils
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


(defn back-link
  "Generate a back link to the preceding page, or, if `target` is specified,
  to a particular page."
  ([]
   (back-link "javascript:history.back()"))
  ([target]
   [:div.back-link-container {:id "back-link-container"}
    [:a {:href target :id "back-link"} "Back"]]))

(defn big-link
  [text & {:keys [target handler]}]
  [:div.big-link-container {:key target}
   [:a.big-link (merge
                  (if target {:href target}{})
                  (if handler {:on-click handler}))
    text]])

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))


(defn error-panel
  [message]
  [:div
   [:h1.error message]
   [:div.container {:id "main-container"}
    (back-link)]])


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

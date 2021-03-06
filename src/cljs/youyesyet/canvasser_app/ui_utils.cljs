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


(defn log-and-dispatch [event]
  "Log this `event` and dispatch it."
  (js/console.log (str "Dispatching " event))
  (rf/dispatch event))


(defn back-link
  "Generate a back link to the preceding page, or, if `target` is specified,
  to a particular page."
  ([]
   (back-link "javascript:history.back()"))
  ([target]
   [:div.back-link-container {:key (gensym "back-link")}
    [:a.back-link {:href target} "Back"]]))


(defn big-link
  "Generate a big link with this `text` which, when selected, either opens
  the url which is this `target` if supplied, or else invokes this `handler`."
  [text & {:keys [target handler]}]
  [:div.big-link-container {:key (gensym "big-link")}
   [:a.big-link (merge {}
                  (if target {:href target}{})
                  (if handler {:on-click handler}{}))
    text]])


(defn nav-link [uri title page collapsed?]
  "Generate and return a navigaton link for this `uri` with the text which is
  this `title`; the `uri` is expected to be the uri of this `page`, and if
  this `page` is the currently selected page, the lin should be highlighted to
  indicate this."
  (let [selected-page @(rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page selected-page) "active")
      :key (gensym "nav-link")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))


(defn error-panel
  "Generate and return an error panel with this `message`."
  [message]
  [:div
   [:h1.error message]
   [:div.container {:id "main-container"}
    (back-link)]])


(defn navbar
  "Generate and return a navigation bar representing the current state of the
  app."
  []
  (r/with-let [collapsed? (r/atom true)]
    [:div {:id "nav"}
     [:img {:id "nav-icon"
            :src "img/threelines.png"
            :on-click #(swap! collapsed? not)}]
     [:menu.nav {:id "nav-menu" :class (if @collapsed? "hidden" "shown")}
      (nav-link "#/map" "Map" :map collapsed?)
      (nav-link "#/dwelling" "Electors" :dwelling collapsed?)
      (nav-link "#/issues" "Issues" :issues collapsed?)
      (nav-link "#/about" "About" :about collapsed?)
      (nav-link "/logout" "Logout" :logout collapsed?)]]))

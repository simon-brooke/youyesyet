(ns youyesyet.views.about
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [markdown.core :refer [md->html]]
            [youyesyet.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.electors: about/credits view for youyesyet.
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

(defn panel
  "Generate the about panel."
  []
  (let [motd @(subscribe [:motd])]
  [:div
   [:h1 "You Yes Yet?"]
   [:div.container {:id "main-container"}
    [:h2 "Pre-alpha/proof of concept"]
    [:p.motd {:dangerouslySetInnerHTML
              {:__html (md->html motd)}}]
    [:p
     [:img {:src "img/ric-logo.png" :width "24" :height "24"}]
     " A project of the "
     [:a {:href "https://radical.scot/"} "Radical Independence Campaign"]]
    [:p
     [:img {:src "img/luminus-logo.png" :alt "Luminus" :height "24" :width "24"}]
     " Built with "
     [:a {:href "http://www.luminusweb.net/"} "Luminus Web"]]
    [:p
     [:img {:src "img/clojure-icon.gif" :alt "Clojure" :height "24" :width "24"}]
     " Powered by "
     [:a {:href "http://clojure.org"} "Clojure"]]
    [:p
     [:img {:src "img/github-logo-transparent.png" :alt "GitHub" :height "24" :width "24"}]
     " Find me/fork me on "
     [:a {:href "https://github.com/simon-brooke/youyesyet"} "GitHub"]]
    [:p
     [:img {:src "img/gnu.small.png" :alt "Free Software Foundation" :height "24" :width "24"}]
     " Licensed under the "
     [:a {:href "http://www.gnu.org/licenses/gpl-2.0.html"}
      "GNU General Public License v2.0"]]]]))

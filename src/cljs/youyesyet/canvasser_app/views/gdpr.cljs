(ns ^{:doc "Canvasser app electors in household panel."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.views.gdpr
  (:require [reagent.core :refer [atom]]
            [re-frame.core :refer [reg-sub subscribe dispatch]]
            [youyesyet.canvasser-app.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.views.gdpr: consent form.
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

;; OK, the idea here is a GDPR consent form to be signed by the elector


(defn gdpr-panel-render
  []
  (let [elector @(subscribe [:elector])]
    [:div
     [:h1 "GDPR Consent"]
     [:div.container {:id "main-container"}
      (ui/back-link "#electors")
      [:table
       [:tbody
        [:tr
         [:th "I," (:name elector)]]
        [:tr
         [:td
          [:p "Consent to have data about my voting intention stored by "
           [:b "Project Hope"]
           " for use in the current referendum campaign, after which
           it will be anonymised or deleted."]
          [:p [:i "If you do not consent, we will store your voting intention
               only against your electoral district, and not link it to you"]]]]
        [:tr
         [:td {:id "signature-pad"}
          [:canvas]]]]]]
     (ui/big-link "I consent" :target "#elector") ;; TODO: need to save the signature
     (ui/big-link "I DO NOT consent" :target "#elector")]))




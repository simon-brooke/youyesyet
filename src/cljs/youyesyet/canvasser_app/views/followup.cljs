(ns ^{:doc "Canvasser followup request form panel."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.views.followup
  (:require [re-frame.core :refer [reg-sub subscribe dispatch]]
            [youyesyet.canvasser-app.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.views.followup-request: followup-request view for youyesyet.
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
        dwelling @(subscribe [:dwelling])
        method @(subscribe [:followupmethod])]
    (js/console.log (str "followup/panel; Issue is " issue "; elector is " elector "; method is " method " (" (type method) ")"))
    (dispatch [:set-followupmethod "Phone"])
    (dispatch [:set-method-detail nil])
    (dispatch [:set-issue-detail nil])
    (cond
     (nil? dwelling)
     (ui/error-panel "No dwelling selected")
     (nil? issues)
     (ui/error-panel "No issues loaded")
     true
     [:div
      [:h1 "Followup Request"]
      [:div.container {:id "main-container"}
       [:div {}
        [:p.widget
         [:label {:for "elector"} "Elector"]
         [:select {:id "elector" :name "elector" :defaultValue (:id elector)
                   :on-change #(dispatch [:set-elector (.-value (.-target %))])}
          (map
           #(let []
              [:option {:value (:id %) :key (:id %)} (:name %)]) (:electors dwelling))]]
        [:p.widget
         [:label {:for "issue"} "Issue"]
         ;; #(reset! val (-> % .-target .-value))
         [:select {:id "issue" :name "issue" :defaultValue issue
                   :on-change #(dispatch [:set-issue (.-value (.-target %))])}
          (map
           #(let []
              [:option {:key % :value %} %]) (keys issues))]]
        (if (= issue :Other)
          [:p.widget
           [:label {:for "issue_detail"} "Issue detail"]
           [:input {:type "text" :id "issue_detail" :name "issue_detail"
                    :on-change #(dispatch [:set-issue-detail (.-value (.-target %))])}]])
        [:p.widget
         [:label {:for "method"} "Method"]
         [:select {:id "method" :name "method" :defaultValue "Phone"
                   :on-change #(dispatch [:set-followupmethod (.-value (.-target %))])}
          (map
           #(let []
              [:option {:value (:id %) :key (:id %)} (:id %)]) @(subscribe [:followupmethods]))]]
        [:p.widget
         [:label {:for "method_detail"} (if (= (str @(subscribe [:followupmethod])) "Phone") "Telephone number" "EMail Address")]
         [:input {:type "text" :id "method_detail" :name "method_detail"
                  :on-change #(dispatch [:set-method-detail (.-value (.-target %))])}]]
        [:p.widget
         [:label {:for "send"} "To request a call"]
         [:button {:id "send" :on-click #(dispatch [:send-request])} "Send this!"]]]
       (ui/back-link)]])))


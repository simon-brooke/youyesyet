(ns ^{:doc "Canvasser app current issues list panel."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.views.issues
  (:require [re-frame.core :refer [reg-sub subscribe]]
            [youyesyet.canvasser-app.ui-utils :as ui]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.views.issues: issues view for youyesyet.
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
;;; I propose to follow this pattern. This file will (eventually) provide the issues view.

;;; See https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md#issues-view

;;; Simple list of the issues of the day.
(defn panel
  "Generate the issues panel."
  []
  (let [issues @(subscribe [:issues])]
    (if issues
      [:div
       [:h1 "Issues"]
       [:div.container {:id "main-container"}
        (ui/back-link)
        [:div {:id "issue-list"}
         (map (fn [k] (ui/big-link k (str "#/issue/" k))) (keys issues))]]]
      (ui/error-panel "No issues loaded"))))

(ns ^{:doc "Canvasser app navigation and routing."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.core
  (:require cljsjs.react-leaflet
            [ajax.core :refer [GET POST]]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [youyesyet.canvasser-app.ajax :refer [load-interceptors!]]
            [youyesyet.canvasser-app.handlers]
            [youyesyet.canvasser-app.subscriptions]
            [youyesyet.canvasser-app.ui-utils :as ui]
            [youyesyet.canvasser-app.views.about :as about]
            [youyesyet.canvasser-app.views.building :as building]
            [youyesyet.canvasser-app.views.elector :as elector]
            [youyesyet.canvasser-app.views.electors :as electors]
            [youyesyet.canvasser-app.views.followup :as followup]
            [youyesyet.canvasser-app.views.gdpr :as gdpr]
            [youyesyet.canvasser-app.views.issue :as issue]
            [youyesyet.canvasser-app.views.issues :as issues]
            [youyesyet.canvasser-app.views.map :as maps])
  (:import goog.History))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.core: core of the app.
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

;;; So that we can do debug logging!
(enable-console-print!)

(defn about-page []
  (about/panel))

(defn building-page []
  (building/panel))

(defn electors-page []
  (electors/panel))

(defn elector-page []
  (elector/panel))

(defn followup-page []
  (followup/panel))

(defn issues-page []
  (issues/panel))

(defn issue-page []
  (issue/panel))

(defn map-page []
  (maps/panel))

(def pages
  {:about #'about-page
   :building #'building-page
   :elector #'elector-page
   :electors #'electors-page
   :followup #'followup-page
   :issues #'issues-page
   :issue #'issue-page
   :map #'map-page
   })


(defn page
  "Render the single page of the app, taking the current panel from
  the :page key in the state map."
  []
  (let [content (pages @(rf/subscribe [:page]))
        error @(rf/subscribe [:error])
        feedback @(rf/subscribe [:feedback])
        outqueue @(rf/subscribe [:outqueue])]
    [:div
     [:header
      [ui/navbar]]
     (if content [content]
       [:div.error (str "No content in page " :page)])
     [:footer
      [:div.error {:style [:display (if error "block" "none")]} (str error)]
      [:div.feedback {:style [:display (if feedback :block :none)]} (str feedback)]
      [:div.queue (if
                    (nil? outqueue) ""
                    (str (count outqueue) " items queued to send"))]]]))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :map]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

(secretary/defroute "/electors/:dwelling" {dwelling-id :dwelling}
  (rf/dispatch [:set-dwelling dwelling-id]))

(secretary/defroute "/building/:address" {address-id :address}
  (rf/dispatch [:set-address address-id]))

(secretary/defroute "/followup" []
  (rf/dispatch [:set-active-page :followup]))

(secretary/defroute "/issues" []
  (rf/dispatch [:set-active-page :issues]))

(secretary/defroute "/issues/:elector" {elector-id :elector}
  (rf/dispatch [:set-elector-and-page {:elector-id elector-id :page :issues}]))

(secretary/defroute "/issue/:issue" {issue :issue}
  (rf/dispatch [:set-and-go-to-issue issue]))

(secretary/defroute "/map" []
  (rf/dispatch [:set-active-page :map]))

(secretary/defroute "/set-intention/:elector/:intention" {elector-id :elector intention :intention}
  (rf/dispatch [:set-intention {:elector-id elector-id :intention intention}]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))

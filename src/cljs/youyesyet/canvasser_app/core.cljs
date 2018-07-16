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
            [youyesyet.canvasser-app.gis :refer [get-current-location]]
            [youyesyet.canvasser-app.handlers :as h]
            [youyesyet.canvasser-app.subscriptions]
            [youyesyet.canvasser-app.ui-utils :as ui]
            [youyesyet.canvasser-app.views.about :as about]
            [youyesyet.canvasser-app.views.building :as building]
            [youyesyet.canvasser-app.views.dwelling :as dwelling]
            [youyesyet.canvasser-app.views.elector :as elector]
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

(defn dwelling-page []
  (dwelling/panel))

(defn elector-page []
  (elector/panel))

(defn gdpr-page []
  (gdpr/panel))

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
   :dwelling #'dwelling-page
   :elector #'elector-page
   :followup #'followup-page
   :gdpr #'gdpr-page
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
       [:div.error (str "No content in page " @(rf/subscribe [:page]))])
     [:footer
      [:div.error {:style [:display (if (empty? error) :none :block)]} (apply str error)]
      [:div.feedback
       {:style [:display (if (empty? feedback) :none :block)]}
       (apply str (map #(h/feedback-messages %) (distinct feedback)))]
      [:div.queue (if
                    (nil? outqueue) ""
                    (str (count outqueue) " items queued to send"))]]]))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(defn log-and-dispatch [arg]
  (js/console.log (str "Dispatching " arg))
  (rf/dispatch arg))

(secretary/defroute "/" []
  (log-and-dispatch [:set-active-page :map]))

(secretary/defroute "/about" []
  (log-and-dispatch [:set-active-page :about]))

(secretary/defroute "/dwelling" []
  (log-and-dispatch [:set-active-page :dwelling]))

(secretary/defroute "/dwelling/:dwelling" {dwelling-id :dwelling}
  (log-and-dispatch [:set-dwelling dwelling-id])
  (log-and-dispatch [:set-active-page :dwelling]))

(secretary/defroute "/building/:address" {address-id :address}
  (log-and-dispatch [:set-address address-id]))

(secretary/defroute "/elector" []
  (log-and-dispatch [:set-active-page :elector]))

(secretary/defroute "/elector/:elector" {elector-id :elector}
  (log-and-dispatch [:set-elector-and-page {:elector-id elector-id :page :elector}]))

(secretary/defroute "/elector/:elector/:consent" {elector-id :elector consent :consent}
  (log-and-dispatch [:set-consent-and-page {:elector-id elector-id :consent (and true consent) :page :elector}]))

(secretary/defroute "/elector" []
  (log-and-dispatch [:set-active-page :elector]))

(secretary/defroute "/followup" []
  (log-and-dispatch [:set-active-page :followup]))

(secretary/defroute "/gdpr" []
  (log-and-dispatch [:set-active-page :gdpr]))

(secretary/defroute "/gdpr/:elector" {elector-id :elector}
  (log-and-dispatch [:set-elector-and-page {:elector-id elector-id :page :gdpr}]))

(secretary/defroute "/issues" []
  (log-and-dispatch [:set-active-page :issues]))

(secretary/defroute "/issues/:elector" {elector-id :elector}
  (log-and-dispatch [:set-elector-and-page {:elector-id elector-id :page :issues}]))

(secretary/defroute "/issue/:issue" {issue :issue}
  (log-and-dispatch [:set-and-go-to-issue issue]))

(secretary/defroute "/map" []
  (log-and-dispatch [:set-active-page :map]))

(secretary/defroute "/set-intention/:elector/:intention" {elector-id :elector intention :intention}
  (log-and-dispatch [:set-intention {:elector-id elector-id :intention intention}]))

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
  (get-current-location)
  (rf/dispatch [:fetch-locality])
  (rf/dispatch [:fetch-options])
  (rf/dispatch [:fetch-issues])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))


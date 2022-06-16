(ns ^{:doc "Canvasser app navigation and routing."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.core
  (:require [ajax.core :refer [GET POST]]
            [cljsjs.leaflet]
            [devtools.core :as devtools]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frame.fx]
            [recalcitrant.core :refer [error-boundary]] ;; may not be needed here
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


;; install tools to aid debugging in Chrome/Chromium.
(devtools/install!)


;;; So that we can do debug logging!
(enable-console-print!)

(defn about-page []
    "Return the content for the 'about' page."
  (about/panel))

(defn building-page []
    "Return the content for the single building page, for the current address."
  (building/panel))

(defn dwelling-page []
    "Return the content for the single dwelling page, for the current
    dwelling."
  (dwelling/panel))

(defn elector-page []
    "Return the content for the elector page, for the current dwelling."
  (elector/panel))

(defn gdpr-page []
    "Return the content for the general data protection regulation consent
    page."
  (gdpr/panel))

(defn followup-page []
    "Return the content for the followup-request page, for the current elector
    and selected issue."
  (followup/panel))

(defn issues-page []
    "Return the content for the current issues page - list of currently
    prompted for issues."
  (issues/panel))

(defn issue-page []
    "Return the content for the current issue page: canned text prompt for the
    canvasser to say to the elector on this issue."
  (issue/panel))

(defn map-page []
    "Return the content for the main map page. Map showing current location."
  (maps/panel))

(def pages
    "Dispatcher table for pages."
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
      (if-not (empty? error)
        [:div.error
         error])
      (if-not (empty? feedback)
        [:div.feedback
         (apply str (map #(h/feedback-messages %) (distinct feedback)))])
;;       (if-not (empty? outqueue)
;;         [:div.queue (str (count outqueue) " items queued to send")])
       ]]))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (ui/log-and-dispatch [:set-active-page :about]))

(secretary/defroute "/about" []
  (ui/log-and-dispatch [:set-active-page :about]))

(secretary/defroute "/dwelling" []
  (ui/log-and-dispatch [:set-active-page :dwelling]))

(secretary/defroute "/dwelling/:dwelling" {dwelling-id :dwelling}
  (ui/log-and-dispatch [:set-dwelling dwelling-id])
  (ui/log-and-dispatch [:set-active-page :dwelling]))

(secretary/defroute "/building/:address" {address-id :address}
  (ui/log-and-dispatch [:set-address address-id]))

(secretary/defroute "/elector" []
  (ui/log-and-dispatch [:set-active-page :elector]))

(secretary/defroute "/elector/:elector" {elector-id :elector}
  (ui/log-and-dispatch [:set-elector-and-page {:elector-id elector-id :page :elector}]))

(secretary/defroute "/elector/:elector/:consent" {elector-id :elector consent :consent}
  (ui/log-and-dispatch [:set-active-page {:page :elector}]))

(secretary/defroute "/elector" []
  (ui/log-and-dispatch [:set-active-page :elector]))

(secretary/defroute "/followup" []
  (ui/log-and-dispatch [:set-active-page :followup]))

(secretary/defroute "/gdpr" []
  (ui/log-and-dispatch [:set-active-page :gdpr]))

(secretary/defroute "/gdpr/:elector" {elector-id :elector}
  (ui/log-and-dispatch [:set-elector-and-page {:elector-id elector-id :page :gdpr}]))

(secretary/defroute "/issues" []
  (ui/log-and-dispatch [:set-active-page :issues]))

(secretary/defroute "/issues/:elector" {elector-id :elector}
  (ui/log-and-dispatch [:set-elector-and-page {:elector-id elector-id :page :issues}]))

(secretary/defroute "/issue/:issue" {issue :issue}
  (ui/log-and-dispatch [:set-and-go-to-issue issue]))

(secretary/defroute "/map" []
  (ui/log-and-dispatch [:set-active-page :map]))

(secretary/defroute "/set-intention/:elector/:intention" {elector-id :elector intention :intention}
  (ui/log-and-dispatch [:set-intention {:elector-id elector-id :intention intention}]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation!
  "Interceptor for the browser back button."
  []
  (try
    (js/console.log "Entering `hook-browser-navigation!`")
    (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [event]
       (js/console.log "Entering anonymous history update handler")
       (try 
         (secretary/dispatch! (.-token event))
         (catch js/Error e (js/console.warn e)))
       (js/console.log "Exiting anonymous history update handler")))
    (.setEnabled true))
    (catch js/Error e (js/console.warn e)))
  (js/console.log "Exiting `hook-browser-navigation!`"))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init!
    "Initialise the app."
    []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [:get-current-location])
  (rf/dispatch [:fetch-locality])
  (rf/dispatch [:fetch-options])
  (rf/dispatch [:fetch-issues])
  (rf/dispatch [:fetch-followupmethods])
;;  (rf/dispatch [:dispatch-later [{:ms 60000 :dispatch [:process-queue]}]])
  (load-interceptors!)
  ;; TEMP: the browser navigation hook is breaking the loading of the building page, and I don't at this moment understand why.
  (hook-browser-navigation!)
  (mount-components))


(ns youyesyet.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [youyesyet.ajax :refer [load-interceptors!]]
            [youyesyet.handlers]
            [youyesyet.subscriptions]
            [youyesyet.ui-utils :as ui]
            [youyesyet.views.about :as about]
            [youyesyet.views.electors :as electors]
            [youyesyet.views.issue :as issue]
            [youyesyet.views.issues :as issues]
            [youyesyet.views.map :as maps]
            [youyesyet.views.followup-request :as request])
  (:import goog.History))


(defn about-page []
  (about/panel))

(defn issues-page []
  (issues/panel))

(defn issue-page []
  (issue/panel))

(defn map-page []
  (maps/panel))

(defn request-page []
  (request/panel))

(def pages
  {:about #'about-page
   :issues #'issues-page
   :issue #'issue-page
   :map #'map-page
   })

(defn page []
  [:div
  [:header
   [ui/navbar]]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :map]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

(secretary/defroute "/issues" []
  (rf/dispatch [:set-active-page :issues]))

(secretary/defroute "/issue/:issue" {issue :issue}
  (rf/dispatch [:set-issue issue]))

(secretary/defroute "/map" []
  (rf/dispatch [:set-active-page :map]))

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

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
            [youyesyet.views.map :as maps])
  (:import goog.History))


(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))


(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:div {:id "nav"}
     [:img {:id "nav-icon"
            :src "img/threelines.png"
            :on-click #(swap! collapsed? not)}]
     [:menu.nav {:id "nav-menu" :class (if @collapsed? "hidden" "shown")}
      (nav-link "#/" "Home" :home collapsed?)
      (nav-link "#/library" "Library" :library collapsed?)
      (nav-link "#/register" "Register" :register collapsed?)
      (nav-link "#/login" "Login" :login collapsed?)
      (nav-link "#/about" "About" :about collapsed?)]]))


(defn back-link []
  [:div.back-link-container {:id "back-link-container"}
   [:a {:href "javascript:history.back()" :id "back-link"} "Back"]])


(defn big-link [text target]
  [:div.big-link-container
   [:a.big-link {:href target} text]])


(defn about-page []
  [:div.container {:id "main-container"}
   (back-link)
   [:div.row
    [:div.col-md-12
     "this is the story of youyesyet... work in progress"]]])


(defn home-page []
  [:div.container {:id "main-container"}
   (big-link "About" "#/about")
   (big-link "Map" "#/map")
   [:div.jumbotron
    [:h1 "Welcome to youyesyet"]
    [:p "Time to start building your site!"]
    [:p [:a.btn.btn-primary.btn-lg {:href "http://luminusweb.net"} "Learn more »"]]]])
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row
      [:div.col-md-12
       [:div {:dangerouslySetInnerHTML
              {:__html (md->html docs)}}]]])

(defn map-page []
  (maps/map-div))

(def pages
  {:home #'home-page
   :map #'map-page
   :about #'about-page})

(defn page []
  [:div
  [:header
   [navbar]
    [:h1 "You yes yet?"]]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))


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
(defn fetch-docs! []
  (GET (str js/context "/docs")
       {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))

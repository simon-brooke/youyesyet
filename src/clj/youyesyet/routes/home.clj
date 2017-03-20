(ns youyesyet.routes.home
  (:require [youyesyet.layout :as layout]
            [youyesyet.db.core :as db-core]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn app-page []
  (layout/render "app.html"))

(defn call-me-page [request]
  (if
    request
    ;; do something to store it in the database
    (layout/render "call-me-accepted.html" (:params request))
    (layout/render "call-me.html"
                   {:title "Please call me!"
                    ;; TODO: Concerns need to be fetched from the database
                    :concerns nil})))

(defn home-page []
  (layout/render "home.html" {:title "You Yes Yet?"}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/app" [] (app-page))
  (GET "/call-me" [] (call-me-page nil))
  (POST "/call-me" request (call-me-page request))
  (GET "/notyet" [] (layout/render "notyet.html"
                                      {:title "Can we persuade you?"}))
  (GET "/supporter" [] (layout/render "supporter.html"
                                      {:title "Have you signed up as a canvasser yet?"})))

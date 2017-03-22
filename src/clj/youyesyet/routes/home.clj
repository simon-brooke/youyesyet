(ns youyesyet.routes.home
  (:require [noir.util.route :as route]
            [youyesyet.layout :as layout]
            [youyesyet.db.core :as db-core]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn app-page []
  (layout/render "app.html"))

(defn call-me-page [request]
  (if
    request
    (do
      ;; do something to store it in the database
      (layout/render "call-me-accepted.html" (:params request)))
    (layout/render "call-me.html"
                   {:title "Please call me!"
                    ;; TODO: Issues need to be fetched from the database
                    :concerns nil})))

(defn home-page []
  (layout/render "home.html" {:title "You Yes Yet?"}))

(defn login-page
  "This is very temporary. We're going to do authentication by oauth."
  [request]
  (let [params (keywordize-keys (:form-params request))
        username (:username params)
        password (:password params)
        redirect-to (or (:redirect-to params) "app")]
    (if
     (and (= username "test" (= password "test"))
     (do
       (session/put! :user username)
       (response/redirect redirect-to))
     (layout/render "login.html" {:title "Please log in" :redirect-to redirect-to})))))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/app" [] (route/restricted (app-page)))
  (GET "/call-me" [] (call-me-page nil))
  (POST "/call-me" request (call-me-page request))
  (GET "/auth" request (login-page request))
  (POST "/auth" request (login-page request))
  (GET "/notyet" [] (layout/render "notyet.html"
                                      {:title "Can we persuade you?"}))
  (GET "/supporter" [] (layout/render "supporter.html"
                                      {:title "Have you signed up as a canvasser yet?"})))

(ns youyesyet.routes.home
  (:require [youyesyet.layout :as layout]
            [youyesyet.db.core :as db-core]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn app-page []
  (layout/render "app.html"))

(defn home-page []
  (layout/render "home.html" {:title "You Yes Yet?"}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/app" [] (app-page))
  (GET "/docs" [] (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                      (response/header "Content-Type" "text/plain; charset=utf-8"))))

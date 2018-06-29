(ns ^{:doc "Routes/pages available to unauthenticated users."
      :author "Simon Brooke"} youyesyet.routes.home
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [noir.response :as nresponse]
            [noir.util.route :as route]
            [ring.util.http-response :refer [content-type ok]]
            [youyesyet.layout :as layout]
            [youyesyet.db.core :as db-core]
            [youyesyet.oauth :as oauth]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.routes.home: routes and pages for unauthenticated users.
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

(defn app-page []
  (layout/render "app.html" {}))


(defn about-page []
  (layout/render "about.html" {}))


(defn call-me-page [request]
  (if
    request
    (do
      ;; do something to store it in the database
      (layout/render "call-me-accepted.html" (:session request) (:params request)))
    (layout/render "call-me.html" (:session request)
                   {:title "Please call me!"
                    ;; TODO: Issues need to be fetched from the database
                    :concerns nil})))


(defn roles-page [request]
  (let
    [session (:session request)
     user (:user session)
     roles (if user (db-core/list-roles-by-canvasser db-core/*db* {:id (:id user)}))]
    (cond
      roles (layout/render "roles.html"
                           (:session request)
                           {:title (str "Welcome " (:fullname user) ", what do you want to do?")
                            :user user
                            :roles roles})
      (empty? roles)(response/found "/app")
      true (assoc (response/found "/login") :session (dissoc session :user)))))


(defn home-page []
  (layout/render "home.html" {} {:title "You Yes Yet?"}))


(defn login-page
  "This is very temporary. We're going to do authentication by oauth."
  [request]
  (let [params (keywordize-keys (:params request))
        session (:session request)
        username (:username params)
        user (if username (db-core/get-canvasser-by-username db-core/*db* {:username username}))
        password (:password params)
        redirect-to (or (:redirect-to params) "roles")]
    (cond
      (:authority params)
      (let [auth (oauth/authority! (:authority params))]
        (if auth
          (do
            (log/info "Attempting to authorise with authority " (:authority params))
            (oauth/fetch-request-token
              (assoc request :session (assoc session :authority auth))
              auth))
          (throw (Exception. (str "No such authority: " (:authority params))))))
      ;; this is obviously, ABSURDLY, insecure. I don't want to put just-about-good-enough,
      ;; it-will-do-for-now security in place; instead, I want this to be test code only
      ;; until we have o-auth properly working.
      (and user (= username password))
      (assoc
        (response/found redirect-to)
        :session (assoc session :user user :roles (layout/get-user-roles user)))
      username
      (layout/render
        "login.html"
        session
        {:title (str "User " username " is unknown") :redirect-to redirect-to})
      true
      (layout/render
        "login.html"
        session
        {:title "Please log in"
         :redirect-to redirect-to
         :authorities (db-core/list-authorities db-core/*db*)}))))


(defroutes home-routes
  (GET "/" [] (home-page))
;;   (GET "/js/:file" [file]
;;        (-> (io/input-stream (str "resources/public/js/" file))
;;            ok
;;            (content-type "text/javascript;charset=UTF-8")))
  (GET "/home" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/roles" request (route/restricted (roles-page request)))
  (GET "/canvassers" [] (route/restricted (app-page)))
  (GET "/app" [] (route/restricted (app-page)))
  (GET "/call-me" [] (call-me-page nil))
  (POST "/call-me" request (call-me-page request))
  (GET "/auth" request (login-page request))
  (POST "/auth" request (login-page request))
  (GET "/notyet" [] (layout/render "notyet.html" {}
                                   {:title "Can we persuade you?"}))
  (GET "/supporter" [] (layout/render "supporter.html" {}
                                      {:title "Have you signed up as a canvasser yet?"})))

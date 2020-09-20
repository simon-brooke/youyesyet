(ns ^{:doc "Routes/pages available to unauthenticated users."
      :author "Simon Brooke"} youyesyet.routes.home
  (:require [adl-support.utils :refer [safe-name]]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [markdown.core :refer [md-to-html-string]]
            [noir.util.route :as route]
            [ring.util.http-response :as response]
            [youyesyet.config :refer [env]]
            [youyesyet.db.core :as db-core]
            [youyesyet.layout :as layout]
            [youyesyet.oauth :as oauth]
            [compojure.core :refer [defroutes GET POST]]
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


(defn motd
  []
  "Message of the day data is currently being loaded from a file in resources.
  That probably isn't the final solution, but I don't currently have a final
  solution"
  (let [motd (io/as-file (io/resource (env :motd)))]
    (if (.exists motd) (slurp motd) "")))


(defn about-page []
  (layout/render "md.html" {:title
                                  (str "About " (:site-title env))
                                  :content (md-to-html-string (slurp (io/resource "about.md")))}))


(defn call-me-page [request]
  (if
    request
    (do
      ;; do something to store it in the database
      (layout/render "call-me-accepted.html" (:session request) (:params request)))
    (layout/render "call-me.html" (:session request)
                   {:title "Please call me!"
                    ;; TODO: Issues need to be fetched from the database
                    :concerns (db-core/list-issues db-core/*db* {})})))


(defn home-page []
    (layout/render "home.html" {:title "You yes yet?"
                                   :motd (md-to-html-string (motd))}))


(defn login-page
  "This is very temporary. We're going to do authentication by oauth."
  [request]
  (let [params (keywordize-keys (:params request))
        session (:session request)
        username (:username params)
        user (if username (db-core/get-canvasser-by-username db-core/*db* {:username username}))
        password (:password params)
        redirect-to (or
                     (:redirect-to params)
                     (str (:servlet-context request) "/roles"))]
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
     (let
       [roles (layout/get-user-roles user)]
       (log/info (str "Logged in user '" username "' with roles " roles))
       (assoc
         (response/found redirect-to)
         :session
         (assoc session :user (assoc user :roles roles))))
     ;; if we've got a username but either no user object or else
     ;; the password doesn't match
     username
      (layout/render
       "login.html"
       {:title (str "User " username " is unknown")
        :redirect-to redirect-to
        :warnings ["Your user name was not recognised or your password did not match"]})
     ;; if we've no username, just invite the user to log in
     :else
      (layout/render
       "login.html"
       {:title "Please log in"
        :redirect-to redirect-to
        :authorities (db-core/list-authorities db-core/*db*)}))))


(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/home" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/call-me" [] (call-me-page nil))
  (POST "/call-me" request (call-me-page request))
  (GET "/login" request (login-page request))
  (POST "/login" request (login-page request))
  (GET "/notyet" [] (layout/render "notyet.html"
                                   {:title "Can we persuade you?"}))
  (GET "/supporter" [] (layout/render "supporter.html"
                                      {:title "Have you signed up as a canvasser yet?"})))

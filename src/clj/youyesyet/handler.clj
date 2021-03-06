(ns ^{:doc "Handlers for starting and stopping the webapp."
      :author "Simon Brooke"}
  youyesyet.handler
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer [routes wrap-routes]]
            [compojure.route :as route]
            [mount.core :as mount]
            [noir.session :as session]
            [youyesyet.config :refer [env]]
            [youyesyet.layout :refer [error-page]]
            [youyesyet.middleware :as middleware]
            [youyesyet.routes.home :refer [home-routes]]
            [youyesyet.routes.auto :refer [auto-selmer-routes]]
            [youyesyet.routes.auto-json :refer [auto-rest-routes]]
            [youyesyet.routes.issue-experts :refer [issue-expert-routes]]
            [youyesyet.routes.logged-in :refer [logged-in-routes]]
            [youyesyet.routes.oauth :refer [oauth-routes]]
            [youyesyet.routes.rest :refer [rest-routes]]
            [youyesyet.routes.roles :refer [roles-routes]]
            [youyesyet.routes.services :refer [service-routes]]
            [youyesyet.env :refer [defaults]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.handler: handlers for starting and stopping the webapp.
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

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (doseq [component (:started (mount/start))]
    (log/info component "started")))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents)
  (log/info "youyesyet has shut down!"))


(def app-routes
  "All routes served as part of the `youyesyet` (server-side) web-app (not
  to be confused with the client-side `canvasser-app`, q.v.)."
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'logged-in-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'roles-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'issue-expert-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'auto-rest-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'auto-selmer-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'auto-rest-routes
        (wrap-routes middleware/wrap-formats))
    (-> #'rest-routes
        (wrap-routes middleware/wrap-formats))
    (-> #'service-routes
        (wrap-routes middleware/wrap-formats)) ;; TODO: and authentication, but let's not sweat the small stuff.
    'oauth-routes
    (route/resources "/")
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "Page not found"
                     :message "The page you requested has not yet been implemented"})))))


(def app
  "The `youyesyet` server-side web-app (not to be confused with the client-
  side `canvasser-app`, q.v.)"
  (middleware/wrap-base #'app-routes))

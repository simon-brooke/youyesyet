(ns youyesyet.routes.authenticated
  (:require [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :refer [defroutes GET POST]]
            [noir.response :as nresponse]
            [noir.util.route :as route]
            [ring.util.http-response :as response]
            [youyesyet.layout :as layout]
            [youyesyet.db.core :as db]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.routes.authenticated: routes and pages for authenticated users.
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

;;; This code adapted from http://www.luminusweb.net/docs#accessing_the_database

(defn canvasser-page
  [request]
  (if
    (:params request)
    (let [params (:params request)]
      (if (:id params)
        (db/update-canvasser! params)
        (db/create-canvasser! params))
      )))

(defn routing-page
  "Render the routing page, which offers routes according to the user's roles"
  []
  (layout/render "routing.html"))

(defroutes authenticated-routes
  (GET "/edit-canvasser" request (canvasser-page request))
  (POST "/edit-canvasser" request (canvasser-page request))
  (GET "/routing" [] (routing-page)))

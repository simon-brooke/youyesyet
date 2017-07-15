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

(defn post?
  "Return true if the argument is a ring request which is a post request"
  [request]
  true)

(defn canvasser-page
  "Process this canvasser request, and render the canvasser page"
  [request]
  (let [canvasser (if
    (:params request)
    (let [params (:params request)]
      (if (:id params)
        (if (post? request)
          (db/update-canvasser! params)
          (db/create-canvasser! params))
        (db/get-canvasser (:id params)))
      ))]
    (layout/render
      "canvasser.html"
      {:title (if canvasser
                (str
                  "Edit canvasser "
                  (:fullname canvasser)
                  " "
                  (:email canvasser))
                "Add new canvasser")
       :canvasser canvasser
       :address (if (:address_id canvasser) (db/get-address (:address_id canvasser)))})))

(defn routing-page
  "Render the routing page, which offers routes according to the user's roles"
  []
  (layout/render "routing.html"))

(defroutes authenticated-routes
  (GET "/edit-canvasser" request (canvasser-page request))
  (POST "/edit-canvasser" request (canvasser-page request))
  (GET "/routing" [] (routing-page)))

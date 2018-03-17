(ns ^{:doc "Routes which handle data transfer to/from the canvasser app."
      :author "Simon Brooke"} youyesyet.routes.rest
  (:require [clojure.walk :refer [keywordize-keys]]
            [noir.response :as nresponse]
            [noir.util.route :as route]
            [youyesyet.db.core :as db-core]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.routes.rest: Routes which handle data transfer to/from the
;;;; canvasser app.
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
;;;; Copyright (C) 2017 Simon Brooke for Radical Independence Campaign
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-local-data
  "Get data local to the user of the canvasser app. Expects arguments `lat` and
  `long`. Returns a block of data for that locality"
  [request]
  )

(defn get-issues
  "Get current issues. No arguments expected."
  [request])

(defroutes rest-routes
  (GET "/rest/get-local-data" request (route/restricted (get-local-data request)))
  (GET "/rest/get-issues" request (route/restricted (get-issues request)))
  (GET "/rest/set-intention" request (route/restricted (set-intention request)))
  (GET "/rest/request-followup" request (route/restricted (request-followup request))))

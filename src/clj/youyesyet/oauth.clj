(ns ^{:doc "Handle oauth with multiple authenticating authorities."
      :author "Simon Brooke"} youyesyet.oauth
  (:require [youyesyet.config :refer [env]]
            [youyesyet.db.core :as db]
            [oauth.client :as oauth]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]))

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


(defn get-authorities
  "Fetch the authorities from the database and return a map of them."
  [_]
  (reduce
    merge
    {}
    (map
      (fn [authority]
        (hash-map
          (:id authority)
          (oauth/make-consumer
            (:consumer_key authority)
            (:consumer_secret authority)
            (:request_token_uri authority)
            (:access_token_uri authority)
            (:authorize_uri authority)
            :hmac-sha1)))
      (db/list-authorities db/*db* {}))))


(def authority!
  ;; Closure to allow authorities to be created once when the function is first
  ;; called. The argument `id` should be a string, the id of some authority
  ;; known to the database. As side-effect, the key `:authority` is bound in the
  ;; session to the selected authority.
  (let [authorities (atom nil)]
    (fn [id]
      (if
        (nil? @authorities)
        (do
          (log/debug "Initialising authorities map")
          (swap!
            authorities
            get-authorities)))
      (let
        [authority (@authorities id)]
        (if authority
          (do
            (log/debug (str "Selected authority " id))
            (session/put! :authority authority)))
        authority))))

(defn oauth-callback-uri
  "Generates the oauth request callback URI."
  [{:keys [headers]}]
  (str (headers "x-forwarded-proto") "://" (headers "host") "/oauth/oauth-callback"))

(defn fetch-request-token
  "Fetches a request token from the authority implied by this `request`."
  [request]
  (let [callback-uri (oauth-callback-uri request)
        auth-id (:authority (:params request))
        auth (authority! auth-id)]
    (log/info "Attempting to authorise with authority " auth-id)
    (if
      auth
      (do
        (log/info "Fetching request token using callback-uri" callback-uri)
        (oauth/request-token auth (oauth-callback-uri request)))
      (throw (Exception. (str "No such authority: " auth-id))))))

(defn fetch-access-token
  [request_token]
  (oauth/access-token (session/get :authority) request_token (:oauth_verifier request_token)))

(defn auth-redirect-uri
  "Gets the URI the user should be redirected to when authenticating."
  [request-token]
  (str (oauth/user-approval-uri (session/get :authority) request-token)))

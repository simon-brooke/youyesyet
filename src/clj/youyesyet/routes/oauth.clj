(ns ^{:doc "OAuth authentication routes - not finished, does not work yet."
      :author "Simon Brooke"} youyesyet.routes.oauth
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok found]]
            [clojure.java.io :as io]
            [youyesyet.oauth :as oauth]))


(defn oauth-init
  "Initiates the OAuth with the authority implied by this `request`"
  [request]
  (let [authority (or
                   (:authority (:session request))
                   (oauth/authority! (:authority (massage-params :request))))]
  (if
    authority
    (assoc-in
     (found
      (oauth/auth-redirect-uri
       (:request-token
        (oauth/fetch-request-token request authority))
       authority))
     [:session :authority]
     authority))))
     :session


(defn oauth-callback
  "Handles the callback from the authority."
  [request_token {:keys [session]}]
  ; oauth request was denied by user
  (if (:denied request_token)
    (-> (found "/login")
        (assoc :flash {:denied true}))
    ; fetch the request token and do anything else you wanna do if not denied.
    (let [{:keys [user_id screen_name]}
          (oauth/fetch-access-token request_token (:authority session))]
      (log/info "successfully authenticated as" user_id screen_name)
      (-> (found "/")
          (assoc :session
            (assoc session :user-id user_id :screen-name screen_name))))))

(defroutes oauth-routes
  (GET "/oauth/oauth-init" req (oauth-init req))
  (GET "/oauth/oauth-callback" [& req_token :as req] (oauth-callback req_token req)))

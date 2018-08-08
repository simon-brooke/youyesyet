(ns ^{:doc "OAuth authentication routes - not finished, does not work yet."
      :author "Simon Brooke"} youyesyet.routes.oauth
  (:require [adl-support.core :refer :all]
            [clojure.tools.logging :as log]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok found]]
            [clojure.java.io :as io]
            [youyesyet.authentication :as auth]
            [youyesyet.layout :refer [error-page]]))


(defn oauth-init
  "Initiates the OAuth with the authority implied by this `request`"
  [request]
  (let [authority-name (:authority (massage-params request))
        authority (or
                    (:authority (:session request))
                    (auth/authority! authority-name))]
    (log/debug "Authenticating with oauth request: " request "; authority: " authority)
    (if
      authority
      (do-or-log-error
        (assoc-in
          (auth/auth-redirect-uri
            (:request-token
              (auth/fetch-request-token request authority))
            authority)
          [:session :authority]
          authority)
        :error-return
        {:status 500
         :title "Error while seeking authentication"
         :message "See server log for more detail"})
      (:body
        (error-page {:status 404
                     :title (str "No such authority: " authority-name)
                     :message "The authority you requested is unknown to this system."})))))


(defn oauth-callback
  "Handles the callback from the authority."
  [request_token {:keys [session]}]
  ; oauth request was denied by user
  (if (:denied request_token)
    (-> (found "/login")
        (assoc :flash {:denied true}))
    ; fetch the request token and do anything else you wanna do if not denied.
    (let [{:keys [user_id screen_name]}
          (auth/fetch-access-token request_token (:authority session))]
      (log/info "successfully authenticated as" user_id screen_name)
      (-> (found "/")
          (assoc :session
            (assoc session :user-id user_id :screen-name screen_name))))))


(defroutes oauth-routes
  (GET "/oauth/oauth-init" req (oauth-init req))
  (GET "/oauth/oauth-callback" [& req_token :as req] (oauth-callback req_token req)))

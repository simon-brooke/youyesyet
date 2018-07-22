(ns ^{:doc "Plumbing, mainly boilerplate from Luminus."}
  youyesyet.middleware
  (:require [clojure.tools.logging :as log]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [ring-ttl-session.core :refer [ttl-memory-store]]
            [youyesyet.env :refer [defaults]]
            [youyesyet.config :refer [env]]
            [youyesyet.layout :refer [*app-context* *user* error-page]])
  (:import [javax.servlet ServletContext]))


(defn wrap-context [handler]
  (fn [request]
    (binding [*app-context*
              (if-let [context (:servlet-context request)]
                ;; If we're not inside a servlet environment
                ;; (for example when using mock requests), then
                ;; .getContextPath might not exist
                (try (.getContextPath ^ServletContext context)
                  (catch IllegalArgumentException err
                    (log/warn "Failed to initialise *app-context*: " (.getMessage err))
                    context))
                ;; if the context is not specified in the request
                ;; we check if one has been specified in the environment
                ;; instead
                (do
                  (log/info "Taking '" (:app-context env) "' as *app-context* from env")
                  (:app-context env)))]
      (log/debug "Using '" *app-context* "' as *app-context*")
      (handler (assoc request :servlet-context *app-context*)))))


(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t)
        (error-page {:status 500
                     :title "Something very bad has happened!"
                     :message "We've dispatched a team of highly trained gnomes to take care of the problem."})))))


(defn wrap-csrf [handler]
  (wrap-anti-forgery
    handler
    {:error-response
     (error-page
       {:status 403
        :title "Invalid anti-forgery token"})}))


(defn wrap-formats [handler]
  (let [wrapped (wrap-restful-format
                  handler
                  {:formats [:json-kw :transit-json :transit-msgpack]})]
    (fn [request]
      ;; disable wrap-formats for websockets
      ;; since they're not compatible with this middleware
      ((if (:websocket? request) handler wrapped) request))))


(defn wrap-user
  "Dynamically bind *user* to the user in the session, if any, so that it
  is available in layout/render, q.v."
  [handler]
  (fn [request]
    (if-let [user (-> request :session :user)]
      (binding [*user* user]
        (log/debug "*user* bound as: " *user*)
        (handler request))
      (do
        (log/debug "No user found in session")
        (handler request)))))


(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-user
      wrap-webjars
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (assoc-in  [:session :store] (ttl-memory-store (* 60 30)))))
      wrap-context
      wrap-internal-error))


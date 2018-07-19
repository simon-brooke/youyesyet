(ns ^{:doc "Production launcher, entirely boilerplate from Luminus."}
  youyesyet.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [luminus.http-server :as http]
            [luminus-migrations.core :as migrations]
            [mount.core :as mount]
            [youyesyet.config :refer [env]]
            [youyesyet.handler :as handler])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.core: run as a standalone application. Entirely luminus
;;;; boilerplate.
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])


(mount/defstate ^{:on-reload :noop}
                http-server
                :start
                (http/start
                  (-> env
                      (assoc :handler handler/app)
                      (update :port #(or (-> env :options :port) %))))
                :stop
                (http/stop http-server))


(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))


(defn init-jndi []
  (System/setProperty "java.naming.factory.initial"
                      "org.apache.naming.java.javaURLContextFactory")
  (System/setProperty "java.naming.factory.url.pkgs"
                      "org.apache.naming"))


(defn start-app [args]
  (init-jndi)
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info component "started"))
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. handler/destroy)))


(defn -main [& args]
  (cond
    (some #{"migrate" "rollback"} args)
    (do
      (mount/start #'youyesyet.config/env)
      (migrations/migrate args (select-keys env [:database-url]))
      (System/exit 0))
    :else
    (start-app args)))

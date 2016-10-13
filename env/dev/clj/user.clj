(ns user
  (:require [mount.core :as mount]
            [youyesyet.figwheel :refer [start-fw stop-fw cljs]]
            youyesyet.core))

(defn start []
  (mount/start-without #'youyesyet.core/repl-server))

(defn stop []
  (mount/stop-except #'youyesyet.core/repl-server))

(defn restart []
  (stop)
  (start))



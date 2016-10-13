(ns youyesyet.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[youyesyet started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[youyesyet has shut down successfully]=-"))
   :middleware identity})

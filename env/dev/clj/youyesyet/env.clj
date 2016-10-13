(ns youyesyet.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [youyesyet.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[youyesyet started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[youyesyet has shut down successfully]=-"))
   :middleware wrap-dev})

(ns ^{:doc "Read configuration; largely unaltered from Luminus default."
      :author "Simon Brooke"}
  youyesyet.config
  (:require [cprop.core :refer [load-config]]
            [cprop.source :as source]
            [mount.core :refer [args defstate]]))

(defstate env
  "Configuration, loaded at startup time from properties. **Note** that
  this conficuration is used only when not running in a Servlet context."
  :start (load-config
                       :merge
                       [(args)
                        (source/from-system-props)
                        (source/from-env)]))

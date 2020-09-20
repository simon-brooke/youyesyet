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

;; Roughly working under Tomcat.
;; Database setup using JNDI: see http://www.luminusweb.net/docs/deployment.md#deploying_to_tomcat
;; Note that this duplicates configuration in profiles.clj; one of these is wrong (and neither
;; should be in the Git repository but this is for now!)
(System/setProperty "java.naming.factory.initial"
                    "org.apache.naming.java.javaURLContextFactory")
(System/setProperty "java.naming.factory.url.pkgs"
                    "org.apache.naming")

(doto (new javax.naming.InitialContext)
  (.createSubcontext "java:")
  (.createSubcontext "java:comp")
  (.createSubcontext "java:comp/env")
  (.createSubcontext "java:comp/env/jdbc")
  (.bind "java:comp/env/jdbc/testdb"
         (doto (org.postgresql.ds.PGSimpleDataSource.)
           (.setServerName "localhost")
           (.setDatabaseName "loriner") ;; "youyesyet_dev")
           (.setUser "youyesyet")
           (.setPassword "thisisnotsecure"))))

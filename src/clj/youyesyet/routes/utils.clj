(ns youyesyet.routes.utils)

(defn with-servlet-context
  "Returns a map like `m` into which the servlet context from `request`, 
   expected to be a request map, has been merged. When an app is served from
   Tomcat, there will be a `:servlet-context` key in the map, and it matters;
   when served from Jetty or other lightweight servlet containers, there won't
   be. TODO: should probably be moved (or copied) to adl-support."
  [m request]
  (if (:servlet-context m) 
    (assoc m :servlet-context (:servlet-context request))
    m))
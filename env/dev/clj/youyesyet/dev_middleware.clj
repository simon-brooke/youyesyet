(ns youyesyet.dev-middleware
  (:require
;;    [ring.middleware.reload :refer [wrap-reload]]  ;; this fails with a self referential dependency, which I haven't tracked down.
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
  ))

(defn wrap-dev [handler]
   (-> handler
;;       wrap-reload
       wrap-error-page
       wrap-exceptions
       ))

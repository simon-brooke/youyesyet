(ns  ^{:doc "Routes/pages available to administrators, only."
      :author "Simon Brooke"}
  youyesyet.routes.administrator
  (:require [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :refer [defroutes GET POST]]
            [noir.response :as nresponse]
            [noir.util.route :as route]
            [ring.util.http-response :as response]
            [youyesyet.layout :as layout]
            [youyesyet.db.core :as db]))

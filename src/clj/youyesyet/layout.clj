(ns^{:doc "Render web pages using Selmer templating markup."
     :author "Simon Brooke"}
          youyesyet.layout
          (:require [adl-support.tags :as tags]
                    [clojure.string :refer [lower-case]]
                    [clojure.tools.logging :as log]
                    [markdown.core :refer [md-to-html-string]]
                    [ring.util.http-response :refer [content-type ok]]
                    [ring.util.anti-forgery :refer [anti-forgery-field]]
                    [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
                    [selmer.parser :as parser]
                    [selmer.filters :as filters]
                    [youyesyet.db.core :as db]
                    ))



(declare ^:dynamic *app-context*)
(parser/set-resource-path!  (clojure.java.io/resource "templates"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))
(filters/add-filter! :markdown (fn [content] [:safe (md-to-html-string content)]))
(tags/add-tags)

(defn raw-get-user-roles [user]
  "Return, as a set, the names of the roles of which this user is a member."
  (if
    user
    (do
      (log/debug (str "seeking roles for user " user))
      (set (map #(lower-case (:name %)) (db/list-roles-by-canvasser db/*db* user))))))


;; role assignments change only rarely.
(def get-user-roles (memoize raw-get-user-roles))


(defn render
  "renders the HTML template located relative to resources/templates"
  [template session & [params]]
  (let [user (:user session)]
    (content-type
      (ok
        (parser/render-file
          template
          (assoc params
            :page template
            :csrf-token *anti-forgery-token*
            :user user
            :user-roles (get-user-roles user)
            :version (System/getProperty "youyesyet.version"))))
      "text/html; charset=utf-8")))


(defn error-page
  "error-details should be a map containing the following keys:
  :status - error status
  :title - error title (optional)
  :message - detailed error message (optional)
  returns a response map with the error page as the body
  and the status specified by the status key"
  [error-details]
  {:status  (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (parser/render-file "error.html" error-details)})

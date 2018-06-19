  (ns^{:doc "Render web pages using Selmer templating markup."
        :author "Simon Brooke"}
     youyesyet.layout
    (:require [selmer.parser :as parser]
              [selmer.filters :as filters]
              [markdown.core :refer [md-to-html-string]]
              [noir.session :as session]
              [ring.util.http-response :refer [content-type ok]]
              [ring.util.anti-forgery :refer [anti-forgery-field]]
              [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))



  (declare ^:dynamic *app-context*)
  (parser/set-resource-path!  (clojure.java.io/resource "templates"))
  (parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))
  (filters/add-filter! :markdown (fn [content] [:safe (md-to-html-string content)]))


  (parser/add-tag! :ifmemberof
                   (fn [args context content]
                     (let [permitted (if args (some (:user-groups context) args) false)]
                       (if permitted
                       (get-in content [:ifreadable :content]))))
                   :else
                   (fn [args context content]
                     (let [permitted (if args (some (:user-groups context) args) false)]
                       (if (not permitted)
                       (get-in content [:else :content]))))
                   :ifmemberof)


  (defn raw-get-user-roles [_]
    #{"admin" "canvassers"})

  (def get-user-roles (memoize raw-get-user-roles))


  (defn render
    "renders the HTML template located relative to resources/templates"
    [template & [params]]
    (let [user (try session/get :user)]
      (content-type
       (ok
        (parser/render-file
         template
         (assoc params
           :page template
           :csrf-token *anti-forgery-token*
           :user user
           :user-roles [get-user-roles user]
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

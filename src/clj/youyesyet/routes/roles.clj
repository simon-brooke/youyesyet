(ns ^{:doc "Routes/pages available to authenticated users in specific roles."
      :author "Simon Brooke"} youyesyet.routes.roles
  (:require [adl-support.core :as support]
            [adl-support.utils :refer [safe-name]]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :refer [defroutes GET POST]]
            [noir.util.route :as route]
            [ring.util.http-response :as response]
            [youyesyet.config :refer [env]]
            [youyesyet.db.core :as db-core]
            [youyesyet.routes.issue-experts :as expert]
            [youyesyet.layout :as layout]
            [youyesyet.oauth :as oauth]
            [youyesyet.routes.auto :as auto]))


(defn roles-page [request]
  "Render the routing page for the roles the currently logged in user is member of."
  (let
    [session (:session request)
     user (-> request :session :user)
     roles (if
             user
             (db-core/list-roles-by-canvasser db-core/*db* user))]
    (log/info (str "Roles routing page; user is " user "; roles are " roles))
    (cond
      roles (layout/render "roles.html"
                           {:title (str "Welcome " (:fullname user) ", what do you want to do?")
                            :user user
                            :roles (map #(assoc % :link (safe-name (:name %) :sql)) roles)})
      (empty? roles)(response/found "/app")
      true (assoc (response/found "/login") :session (dissoc session :user)))))


(defn admins-page
  [request]
  (layout/render
    (support/resolve-template "application-index.html")
    {:title "Administrative menu"}))


(defn analysts-page
  "My expectation is that analysts will do a lot of their work through QGIS or
  some other geographical information system; so there isn't a need to put
  anything sophisticated here."
  [request]
  (layout/render
    (support/resolve-template "application-index.html")
    {:title "Administrative menu"}))


(defn canvassers-page
  [request]
  (layout/render
   "roles/canvasser.html"
   {}))


(defn team-organisers-page
  [request]
  (layout/render
   "roles/team-orgenisers.html"
   {}))


(defroutes roles-routes
  (GET "/roles/admin" request (route/restricted (admins-page request)))
  (GET "/roles/analysts" request (route/restricted (analysts-page request)))
  (GET "/roles/canvassers" request (route/restricted (canvassers-page request)))
  (GET "/roles/issueeditors" request (route/restricted (auto/get-list-issues-Issues request)))
  (GET "/roles/issueexperts" request (route/restricted (expert/list-page request)))
  (GET "/roles/team_organisers" request (route/restricted (auto/get-list-teams-Teams request)))
  (GET "/roles" request (route/restricted (roles-page request))))


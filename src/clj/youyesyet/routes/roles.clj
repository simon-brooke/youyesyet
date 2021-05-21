(ns ^{:doc "Routes/pages available to authenticated users in specific roles."
      :author "Simon Brooke"} youyesyet.routes.roles
  (:require [adl-support.core :as support]
            [adl-support.utils :refer [safe-name]]
            [clojure.tools.logging :as log]
            [compojure.core :refer [defroutes GET POST]]
            [noir.util.route :as route]
            [ring.util.http-response :as response]
            [youyesyet.db.core :as db-core]
            [youyesyet.routes.issue-experts :as expert]
            [youyesyet.layout :as layout]
            [youyesyet.oauth :as oauth]
            [youyesyet.routes.auto :as auto]
            [youyesyet.routes.utils :refer [with-servlet-context]]))


(defn roles-page
  "Render the routing page for the roles the currently logged in user is member of."
  [request]
  (let
   [session (:session request)
    user (-> request :session :user)
    roles (if
           user
            (db-core/list-roles-by-canvasser db-core/*db* {:id (:id user)}))]
    (log/info (str "Roles routing page; user is " user "; roles are " roles))
    (if
     roles
      (layout/render
       "roles.html"
       {:title (str "Welcome " (:fullname user) ", what do you want to do?")
        :servlet-context (:servlet-context request)
        :user user
        :roles (map #(assoc % :link (safe-name (:name %) :sql)) roles)})
      (assoc (response/found "/login") :session (dissoc session :user)))))


(defn admins-page
  [request]
  (layout/render
   (support/resolve-template "application-index.html")
   (with-servlet-context {:title "Administrative menu"} request)))


(defn analysts-page
  "My expectation is that analysts will do a lot of their work through QGIS or
  some other geographical information system; so there isn't a need to put
  anything sophisticated here."
  [request]
  (layout/render
   (support/resolve-template "application-index.html")
   (with-servlet-context {:title "Administrative menu"} request)))


(defn canvassers-page
  [request]
  (layout/render
   "roles/canvasser.html"
   (with-servlet-context {} request)))


(defn team-organisers-page
  [request]
  (layout/render
   "roles/team-orgenisers.html"
   (with-servlet-context {} request)))


(defroutes roles-routes
  (GET "/roles/admin" request (route/restricted (admins-page request)))
  (GET "/roles/analysts" request (route/restricted (analysts-page request)))
  (GET "/roles/canvassers" request (route/restricted (canvassers-page request)))
  (GET "/roles/issueeditors" request (route/restricted (auto/get-list-issues-Issues request)))
  (GET "/roles/issueexperts" request (route/restricted (expert/list-page request)))
  (GET "/roles/team_organisers" request (route/restricted (auto/get-list-teams-Teams request)))
  (GET "/roles" request (route/restricted (roles-page request))))


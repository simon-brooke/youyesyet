(ns
 youyesyet.routes.auto-json-routes
 (require
  [noir.response :as nresponse]
  [noir.util.route :as route]
  [compojure.core :refer [defroutes GET POST]]
  [ring.util.http-response :as response]
  [clojure.java.io :as io]
  [hugsql.core :as hugsql]
  [youyesyet.db.core :as db]))


(declare
 create-address
 create-authority
 create-canvasser
 create-district
 create-elector
 create-followupaction
 create-followupmethod
 create-followuprequest
 create-intention
 create-issue
 create-issueexpertise
 create-option
 create-role
 create-rolemembership
 create-schema-migration
 create-team
 create-teammembership
 create-teamorganisership
 create-visit
 delete-address
 delete-authority
 delete-canvasser
 delete-district
 delete-elector
 delete-followupaction
 delete-followupmethod
 delete-followuprequest
 delete-issue
 delete-option
 delete-visit
 get-address
 get-authority
 get-canvasser
 get-district
 get-elector
 get-followupaction
 get-followupmethod
 get-followuprequest
 get-issue
 get-option
 get-visit
 list-addresses
 list-addresses-by-district
 list-authorities
 list-canvassers
 list-canvassers-by-address
 list-canvassers-by-authoritie
 list-canvassers-by-elector
 list-districts
 list-electors
 list-electors-by-address
 list-followupactions
 list-followupactions-by-canvasser
 list-followupactions-by-followuprequest
 list-followupmethods
 list-followuprequests
 list-followuprequests-by-elector
 list-followuprequests-by-followupmethod
 list-followuprequests-by-issue
 list-followuprequests-by-visit
 list-intentions-electors-by-option
 list-intentions-electors-by-visit
 list-intentions-options-by-elector
 list-intentions-options-by-visit
 list-intentions-visits-by-elector
 list-intentions-visits-by-option
 list-issueexpertise-canvassers-by-followupmethod
 list-issueexpertise-canvassers-by-issue
 list-issueexpertise-followupmethods-by-canvasser
 list-issueexpertise-followupmethods-by-issue
 list-issueexpertise-issues-by-canvasser
 list-issueexpertise-issues-by-followupmethod
 list-issues
 list-options
 list-rolememberships-canvassers-by-role
 list-rolememberships-roles-by-canvasser
 list-roles
 list-schemamigrations
 list-teammemberships-canvassers-by-team
 list-teammemberships-teams-by-canvasser
 list-teamorganiserships-canvassers-by-team
 list-teamorganiserships-teams-by-canvasser
 list-teams
 list-teams-by-district
 list-visits
 list-visits-by-address
 list-visits-by-canvasser
 update-address
 update-canvasser
 update-district
 update-elector
 update-followupaction
 update-followuprequest
 update-issue
 update-visit)


(defroutes
 auto-rest-routes
 (POST "/json/auto/create-address" request (create-address request))
 (POST
  "/json/auto/create-authority"
  request
  (create-authority request))
 (POST
  "/json/auto/create-canvasser"
  request
  (create-canvasser request))
 (POST "/json/auto/create-district" request (create-district request))
 (POST "/json/auto/create-elector" request (create-elector request))
 (POST
  "/json/auto/create-followupaction"
  request
  (create-followupaction request))
 (POST
  "/json/auto/create-followupmethod"
  request
  (create-followupmethod request))
 (POST
  "/json/auto/create-followuprequest"
  request
  (create-followuprequest request))
 (POST
  "/json/auto/create-intention"
  request
  (create-intention request))
 (POST "/json/auto/create-issue" request (create-issue request))
 (POST
  "/json/auto/create-issueexpertise"
  request
  (create-issueexpertise request))
 (POST "/json/auto/create-option" request (create-option request))
 (POST "/json/auto/create-role" request (create-role request))
 (POST
  "/json/auto/create-rolemembership"
  request
  (create-rolemembership request))
 (POST
  "/json/auto/create-schema-migration"
  request
  (create-schema-migration request))
 (POST "/json/auto/create-team" request (create-team request))
 (POST
  "/json/auto/create-teammembership"
  request
  (create-teammembership request))
 (POST
  "/json/auto/create-teamorganisership"
  request
  (create-teamorganisership request))
 (POST "/json/auto/create-visit" request (create-visit request))
 (POST "/json/auto/delete-address" request (delete-address request))
 (POST
  "/json/auto/delete-authority"
  request
  (delete-authority request))
 (POST
  "/json/auto/delete-canvasser"
  request
  (delete-canvasser request))
 (POST "/json/auto/delete-district" request (delete-district request))
 (POST "/json/auto/delete-elector" request (delete-elector request))
 (POST
  "/json/auto/delete-followupaction"
  request
  (delete-followupaction request))
 (POST
  "/json/auto/delete-followupmethod"
  request
  (delete-followupmethod request))
 (POST
  "/json/auto/delete-followuprequest"
  request
  (delete-followuprequest request))
 (POST "/json/auto/delete-issue" request (delete-issue request))
 (POST "/json/auto/delete-option" request (delete-option request))
 (POST "/json/auto/delete-visit" request (delete-visit request))
 (POST "/json/auto/get-address" request (get-address request))
 (POST "/json/auto/get-authority" request (get-authority request))
 (POST "/json/auto/get-canvasser" request (get-canvasser request))
 (POST "/json/auto/get-district" request (get-district request))
 (POST "/json/auto/get-elector" request (get-elector request))
 (POST
  "/json/auto/get-followupaction"
  request
  (get-followupaction request))
 (POST
  "/json/auto/get-followupmethod"
  request
  (get-followupmethod request))
 (POST
  "/json/auto/get-followuprequest"
  request
  (get-followuprequest request))
 (POST "/json/auto/get-issue" request (get-issue request))
 (POST "/json/auto/get-option" request (get-option request))
 (POST "/json/auto/get-visit" request (get-visit request))
 (GET "/json/auto/list-addresses" request (list-addresses request))
 (GET
  "/json/auto/list-addresses-by-district"
  request
  (list-addresses-by-district request))
 (GET "/json/auto/list-authorities" request (list-authorities request))
 (GET "/json/auto/list-canvassers" request (list-canvassers request))
 (GET
  "/json/auto/list-canvassers-by-address"
  request
  (list-canvassers-by-address request))
 (GET
  "/json/auto/list-canvassers-by-authoritie"
  request
  (list-canvassers-by-authoritie request))
 (GET
  "/json/auto/list-canvassers-by-elector"
  request
  (list-canvassers-by-elector request))
 (GET "/json/auto/list-districts" request (list-districts request))
 (GET "/json/auto/list-electors" request (list-electors request))
 (GET
  "/json/auto/list-electors-by-address"
  request
  (list-electors-by-address request))
 (GET
  "/json/auto/list-followupactions"
  request
  (list-followupactions request))
 (GET
  "/json/auto/list-followupactions-by-canvasser"
  request
  (list-followupactions-by-canvasser request))
 (GET
  "/json/auto/list-followupactions-by-followuprequest"
  request
  (list-followupactions-by-followuprequest request))
 (GET
  "/json/auto/list-followupmethods"
  request
  (list-followupmethods request))
 (GET
  "/json/auto/list-followuprequests"
  request
  (list-followuprequests request))
 (GET
  "/json/auto/list-followuprequests-by-elector"
  request
  (list-followuprequests-by-elector request))
 (GET
  "/json/auto/list-followuprequests-by-followupmethod"
  request
  (list-followuprequests-by-followupmethod request))
 (GET
  "/json/auto/list-followuprequests-by-issue"
  request
  (list-followuprequests-by-issue request))
 (GET
  "/json/auto/list-followuprequests-by-visit"
  request
  (list-followuprequests-by-visit request))
 (GET
  "/json/auto/list-intentions-electors-by-option"
  request
  (list-intentions-electors-by-option request))
 (GET
  "/json/auto/list-intentions-electors-by-visit"
  request
  (list-intentions-electors-by-visit request))
 (GET
  "/json/auto/list-intentions-options-by-elector"
  request
  (list-intentions-options-by-elector request))
 (GET
  "/json/auto/list-intentions-options-by-visit"
  request
  (list-intentions-options-by-visit request))
 (GET
  "/json/auto/list-intentions-visits-by-elector"
  request
  (list-intentions-visits-by-elector request))
 (GET
  "/json/auto/list-intentions-visits-by-option"
  request
  (list-intentions-visits-by-option request))
 (GET
  "/json/auto/list-issueexpertise-canvassers-by-followupmethod"
  request
  (list-issueexpertise-canvassers-by-followupmethod request))
 (GET
  "/json/auto/list-issueexpertise-canvassers-by-issue"
  request
  (list-issueexpertise-canvassers-by-issue request))
 (GET
  "/json/auto/list-issueexpertise-followupmethods-by-canvasser"
  request
  (list-issueexpertise-followupmethods-by-canvasser request))
 (GET
  "/json/auto/list-issueexpertise-followupmethods-by-issue"
  request
  (list-issueexpertise-followupmethods-by-issue request))
 (GET
  "/json/auto/list-issueexpertise-issues-by-canvasser"
  request
  (list-issueexpertise-issues-by-canvasser request))
 (GET
  "/json/auto/list-issueexpertise-issues-by-followupmethod"
  request
  (list-issueexpertise-issues-by-followupmethod request))
 (GET "/json/auto/list-issues" request (list-issues request))
 (GET "/json/auto/list-options" request (list-options request))
 (GET
  "/json/auto/list-rolememberships-canvassers-by-role"
  request
  (list-rolememberships-canvassers-by-role request))
 (GET
  "/json/auto/list-rolememberships-roles-by-canvasser"
  request
  (list-rolememberships-roles-by-canvasser request))
 (GET "/json/auto/list-roles" request (list-roles request))
 (GET
  "/json/auto/list-schemamigrations"
  request
  (list-schemamigrations request))
 (GET
  "/json/auto/list-teammemberships-canvassers-by-team"
  request
  (list-teammemberships-canvassers-by-team request))
 (GET
  "/json/auto/list-teammemberships-teams-by-canvasser"
  request
  (list-teammemberships-teams-by-canvasser request))
 (GET
  "/json/auto/list-teamorganiserships-canvassers-by-team"
  request
  (list-teamorganiserships-canvassers-by-team request))
 (GET
  "/json/auto/list-teamorganiserships-teams-by-canvasser"
  request
  (list-teamorganiserships-teams-by-canvasser request))
 (GET "/json/auto/list-teams" request (list-teams request))
 (GET
  "/json/auto/list-teams-by-district"
  request
  (list-teams-by-district request))
 (GET "/json/auto/list-visits" request (list-visits request))
 (GET
  "/json/auto/list-visits-by-address"
  request
  (list-visits-by-address request))
 (GET
  "/json/auto/list-visits-by-canvasser"
  request
  (list-visits-by-canvasser request))
 (POST "/json/auto/update-address" request (update-address request))
 (POST
  "/json/auto/update-canvasser"
  request
  (update-canvasser request))
 (POST "/json/auto/update-district" request (update-district request))
 (POST "/json/auto/update-elector" request (update-elector request))
 (POST
  "/json/auto/update-followupaction"
  request
  (update-followupaction request))
 (POST
  "/json/auto/update-followuprequest"
  request
  (update-followuprequest request))
 (POST "/json/auto/update-issue" request (update-issue request))
 (POST "/json/auto/update-visit" request (update-visit request)))


(defn
 create-address
 "Auto-generated method to insert one record to the addresses table. Expects the following key(s) to be present in `params`: (:id :address :postcode :phone :district_id :latitude :longitude). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-address! params)))


(defn
 create-authority
 "Auto-generated method to insert one record to the authorities table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-authority! params)))


(defn
 create-canvasser
 "Auto-generated method to insert one record to the canvassers table. Expects the following key(s) to be present in `params`: (:id :username :fullname :elector_id :address_id :phone :email :authority_id :authorised). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-canvasser! params)))


(defn
 create-district
 "Auto-generated method to insert one record to the districts table. Expects the following key(s) to be present in `params`: (:id :name). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-district! params)))


(defn
 create-elector
 "Auto-generated method to insert one record to the electors table. Expects the following key(s) to be present in `params`: (:id :name :address_id :phone :email). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-elector! params)))


(defn
 create-followupaction
 "Auto-generated method to insert one record to the followupactions table. Expects the following key(s) to be present in `params`: (:id :request_id :actor :date :notes :closed). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-followupaction! params)))


(defn
 create-followupmethod
 "Auto-generated method to insert one record to the followupmethods table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-followupmethod! params)))


(defn
 create-followuprequest
 "Auto-generated method to insert one record to the followuprequests table. Expects the following key(s) to be present in `params`: (:id :elector_id :visit_id :issue_id :method_id). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-followuprequest! params)))


(defn
 create-intention
 "Auto-generated method to insert one record to the intentions table. Expects the following key(s) to be present in `params`: (:visit_id :elector_id :option_id). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-intention! params)))


(defn
 create-issue
 "Auto-generated method to insert one record to the issues table. Expects the following key(s) to be present in `params`: (:id :url). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-issue! params)))


(defn
 create-issueexpertise
 "Auto-generated method to insert one record to the issueexpertise table. Expects the following key(s) to be present in `params`: (:canvasser_id :issue_id :method_id). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-issueexpertise! params)))


(defn
 create-option
 "Auto-generated method to insert one record to the options table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-option! params)))


(defn
 create-role
 "Auto-generated method to insert one record to the roles table. Expects the following key(s) to be present in `params`: (:id :name). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-role! params)))


(defn
 create-rolemembership
 "Auto-generated method to insert one record to the rolememberships table. Expects the following key(s) to be present in `params`: (:role_id :canvasser_id). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-rolemembership! params)))


(defn
 create-schema-migration
 "Auto-generated method to insert one record to the schema_migrations table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-schema-migration! params)))


(defn
 create-team
 "Auto-generated method to insert one record to the teams table. Expects the following key(s) to be present in `params`: (:id :name :district_id :latitude :longitude). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-team! params)))


(defn
 create-teammembership
 "Auto-generated method to insert one record to the teammemberships table. Expects the following key(s) to be present in `params`: (:team_id :canvasser_id). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-teammembership! params)))


(defn
 create-teamorganisership
 "Auto-generated method to insert one record to the teamorganiserships table. Expects the following key(s) to be present in `params`: (:team_id :canvasser_id). Returns a map containing the keys nil identifying the record created."
 [{:keys [params]}]
 (do (db/create-teamorganisership! params)))


(defn
 create-visit
 "Auto-generated method to insert one record to the visits table. Expects the following key(s) to be present in `params`: (:id :address_id :canvasser_id :date). Returns a map containing the keys (:id) identifying the record created."
 [{:keys [params]}]
 (do (db/create-visit! params)))


(defn
 delete-address
 "Auto-generated method to delete one record from the addresses table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-address! params))
 (response/found "/"))


(defn
 delete-authority
 "Auto-generated method to delete one record from the authorities table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-authority! params))
 (response/found "/"))


(defn
 delete-canvasser
 "Auto-generated method to delete one record from the canvassers table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-canvasser! params))
 (response/found "/"))


(defn
 delete-district
 "Auto-generated method to delete one record from the districts table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-district! params))
 (response/found "/"))


(defn
 delete-elector
 "Auto-generated method to delete one record from the electors table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-elector! params))
 (response/found "/"))


(defn
 delete-followupaction
 "Auto-generated method to delete one record from the followupactions table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-followupaction! params))
 (response/found "/"))


(defn
 delete-followupmethod
 "Auto-generated method to delete one record from the followupmethods table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-followupmethod! params))
 (response/found "/"))


(defn
 delete-followuprequest
 "Auto-generated method to delete one record from the followuprequests table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-followuprequest! params))
 (response/found "/"))


(defn
 delete-issue
 "Auto-generated method to delete one record from the issues table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-issue! params))
 (response/found "/"))


(defn
 delete-option
 "Auto-generated method to delete one record from the options table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-option! params))
 (response/found "/"))


(defn
 delete-visit
 "Auto-generated method to delete one record from the visits table. Expects the following key(s) to be present in `params`: (:id)."
 [{:keys [params]}]
 (do (db/delete-visit! params))
 (response/found "/"))


(defn
 get-address
 "Auto-generated method to select one record from the addresses table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:address :district_id :id :latitude :longitude :phone :postcode)."
 [{:keys [params]}]
 (do (db/get-address params)))


(defn
 get-authority
 "Auto-generated method to select one record from the authorities table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/get-authority params)))


(defn
 get-canvasser
 "Auto-generated method to select one record from the canvassers table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:address_id :authorised :authority_id :elector_id :email :fullname :id :phone :username)."
 [{:keys [params]}]
 (do (db/get-canvasser params)))


(defn
 get-district
 "Auto-generated method to select one record from the districts table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:id :name)."
 [{:keys [params]}]
 (do (db/get-district params)))


(defn
 get-elector
 "Auto-generated method to select one record from the electors table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:address_id :email :id :name :phone)."
 [{:keys [params]}]
 (do (db/get-elector params)))


(defn
 get-followupaction
 "Auto-generated method to select one record from the followupactions table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:actor :closed :date :id :notes :request_id)."
 [{:keys [params]}]
 (do (db/get-followupaction params)))


(defn
 get-followupmethod
 "Auto-generated method to select one record from the followupmethods table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/get-followupmethod params)))


(defn
 get-followuprequest
 "Auto-generated method to select one record from the followuprequests table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:elector_id :id :issue_id :method_id :visit_id)."
 [{:keys [params]}]
 (do (db/get-followuprequest params)))


(defn
 get-issue
 "Auto-generated method to select one record from the issues table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:id :url)."
 [{:keys [params]}]
 (do (db/get-issue params)))


(defn
 get-option
 "Auto-generated method to select one record from the options table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/get-option params)))


(defn
 get-visit
 "Auto-generated method to select one record from the visits table. Expects the following key(s) to be present in `params`: (:id). Returns a map containing the following keys: (:address_id :canvasser_id :date :id)."
 [{:keys [params]}]
 (do (db/get-visit params)))


(defn
 list-addresses
 "Auto-generated method to select all records from the addresses table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:address :district_id :id :latitude :longitude :phone :postcode)."
 [{:keys [params]}]
 (do (db/list-addresses params)))


(defn
 list-addresses-by-district
 [{:keys [params]}]
 (do (db/list-addresses-by-district params)))


(defn
 list-authorities
 "Auto-generated method to select all records from the authorities table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/list-authorities params)))


(defn
 list-canvassers
 "Auto-generated method to select all records from the canvassers table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:address_id :authorised :authority_id :elector_id :email :fullname :id :phone :username)."
 [{:keys [params]}]
 (do (db/list-canvassers params)))


(defn
 list-canvassers-by-address
 [{:keys [params]}]
 (do (db/list-canvassers-by-address params)))


(defn
 list-canvassers-by-authoritie
 [{:keys [params]}]
 (do (db/list-canvassers-by-authoritie params)))


(defn
 list-canvassers-by-elector
 [{:keys [params]}]
 (do (db/list-canvassers-by-elector params)))


(defn
 list-districts
 "Auto-generated method to select all records from the districts table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id :name)."
 [{:keys [params]}]
 (do (db/list-districts params)))


(defn
 list-electors
 "Auto-generated method to select all records from the electors table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:address_id :email :id :name :phone)."
 [{:keys [params]}]
 (do (db/list-electors params)))


(defn
 list-electors-by-address
 [{:keys [params]}]
 (do (db/list-electors-by-address params)))


(defn
 list-followupactions
 "Auto-generated method to select all records from the followupactions table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:actor :closed :date :id :notes :request_id)."
 [{:keys [params]}]
 (do (db/list-followupactions params)))


(defn
 list-followupactions-by-canvasser
 [{:keys [params]}]
 (do (db/list-followupactions-by-canvasser params)))


(defn
 list-followupactions-by-followuprequest
 [{:keys [params]}]
 (do (db/list-followupactions-by-followuprequest params)))


(defn
 list-followupmethods
 "Auto-generated method to select all records from the followupmethods table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/list-followupmethods params)))


(defn
 list-followuprequests
 "Auto-generated method to select all records from the followuprequests table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:elector_id :id :issue_id :method_id :visit_id)."
 [{:keys [params]}]
 (do (db/list-followuprequests params)))


(defn
 list-followuprequests-by-elector
 [{:keys [params]}]
 (do (db/list-followuprequests-by-elector params)))


(defn
 list-followuprequests-by-followupmethod
 [{:keys [params]}]
 (do (db/list-followuprequests-by-followupmethod params)))


(defn
 list-followuprequests-by-issue
 [{:keys [params]}]
 (do (db/list-followuprequests-by-issue params)))


(defn
 list-followuprequests-by-visit
 [{:keys [params]}]
 (do (db/list-followuprequests-by-visit params)))


(defn
 list-intentions-electors-by-option
 [{:keys [params]}]
 (do (db/list-intentions-electors-by-option params)))


(defn
 list-intentions-electors-by-visit
 [{:keys [params]}]
 (do (db/list-intentions-electors-by-visit params)))


(defn
 list-intentions-options-by-elector
 [{:keys [params]}]
 (do (db/list-intentions-options-by-elector params)))


(defn
 list-intentions-options-by-visit
 [{:keys [params]}]
 (do (db/list-intentions-options-by-visit params)))


(defn
 list-intentions-visits-by-elector
 [{:keys [params]}]
 (do (db/list-intentions-visits-by-elector params)))


(defn
 list-intentions-visits-by-option
 [{:keys [params]}]
 (do (db/list-intentions-visits-by-option params)))


(defn
 list-issueexpertise-canvassers-by-followupmethod
 [{:keys [params]}]
 (do (db/list-issueexpertise-canvassers-by-followupmethod params)))


(defn
 list-issueexpertise-canvassers-by-issue
 [{:keys [params]}]
 (do (db/list-issueexpertise-canvassers-by-issue params)))


(defn
 list-issueexpertise-followupmethods-by-canvasser
 [{:keys [params]}]
 (do (db/list-issueexpertise-followupmethods-by-canvasser params)))


(defn
 list-issueexpertise-followupmethods-by-issue
 [{:keys [params]}]
 (do (db/list-issueexpertise-followupmethods-by-issue params)))


(defn
 list-issueexpertise-issues-by-canvasser
 [{:keys [params]}]
 (do (db/list-issueexpertise-issues-by-canvasser params)))


(defn
 list-issueexpertise-issues-by-followupmethod
 [{:keys [params]}]
 (do (db/list-issueexpertise-issues-by-followupmethod params)))


(defn
 list-issues
 "Auto-generated method to select all records from the issues table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id :url)."
 [{:keys [params]}]
 (do (db/list-issues params)))


(defn
 list-options
 "Auto-generated method to select all records from the options table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/list-options params)))


(defn
 list-rolememberships-canvassers-by-role
 [{:keys [params]}]
 (do (db/list-rolememberships-canvassers-by-role params)))


(defn
 list-rolememberships-roles-by-canvasser
 [{:keys [params]}]
 (do (db/list-rolememberships-roles-by-canvasser params)))


(defn
 list-roles
 "Auto-generated method to select all records from the roles table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id :name)."
 [{:keys [params]}]
 (do (db/list-roles params)))


(defn
 list-schemamigrations
 "Auto-generated method to select all records from the schema_migrations table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:id)."
 [{:keys [params]}]
 (do (db/list-schema_migrations params)))


(defn
 list-teammemberships-canvassers-by-team
 [{:keys [params]}]
 (do (db/list-teammemberships-canvassers-by-team params)))


(defn
 list-teammemberships-teams-by-canvasser
 [{:keys [params]}]
 (do (db/list-teammemberships-teams-by-canvasser params)))


(defn
 list-teamorganiserships-canvassers-by-team
 [{:keys [params]}]
 (do (db/list-teamorganiserships-canvassers-by-team params)))


(defn
 list-teamorganiserships-teams-by-canvasser
 [{:keys [params]}]
 (do (db/list-teamorganiserships-teams-by-canvasser params)))


(defn
 list-teams
 "Auto-generated method to select all records from the teams table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:district_id :id :latitude :longitude :name)."
 [{:keys [params]}]
 (do (db/list-teams params)))


(defn
 list-teams-by-district
 [{:keys [params]}]
 (do (db/list-teams-by-district params)))


(defn
 list-visits
 "Auto-generated method to select all records from the visits table. If the keys (:limit :offset) are present in the request then they will be used to page through the data. Returns a sequence of maps each containing the following keys: (:address_id :canvasser_id :date :id)."
 [{:keys [params]}]
 (do (db/list-visits params)))


(defn
 list-visits-by-address
 [{:keys [params]}]
 (do (db/list-visits-by-address params)))


(defn
 list-visits-by-canvasser
 [{:keys [params]}]
 (do (db/list-visits-by-canvasser params)))


(defn
 update-address
 "Auto-generated method to update one record in the addresses table. Expects the following key(s) to be present in `params`: (:address :district_id :id :latitude :longitude :phone :postcode)."
 [{:keys [params]}]
 (do (db/update-address! params))
 (response/found "/"))


(defn
 update-canvasser
 "Auto-generated method to update one record in the canvassers table. Expects the following key(s) to be present in `params`: (:address_id :authorised :authority_id :elector_id :email :fullname :id :phone :username)."
 [{:keys [params]}]
 (do (db/update-canvasser! params))
 (response/found "/"))


(defn
 update-district
 "Auto-generated method to update one record in the districts table. Expects the following key(s) to be present in `params`: (:id :name)."
 [{:keys [params]}]
 (do (db/update-district! params))
 (response/found "/"))


(defn
 update-elector
 "Auto-generated method to update one record in the electors table. Expects the following key(s) to be present in `params`: (:address_id :email :id :name :phone)."
 [{:keys [params]}]
 (do (db/update-elector! params))
 (response/found "/"))


(defn
 update-followupaction
 "Auto-generated method to update one record in the followupactions table. Expects the following key(s) to be present in `params`: (:actor :closed :date :id :notes :request_id)."
 [{:keys [params]}]
 (do (db/update-followupaction! params))
 (response/found "/"))


(defn
 update-followuprequest
 "Auto-generated method to update one record in the followuprequests table. Expects the following key(s) to be present in `params`: (:elector_id :id :issue_id :method_id :visit_id)."
 [{:keys [params]}]
 (do (db/update-followuprequest! params))
 (response/found "/"))


(defn
 update-issue
 "Auto-generated method to update one record in the issues table. Expects the following key(s) to be present in `params`: (:id :url)."
 [{:keys [params]}]
 (do (db/update-issue! params))
 (response/found "/"))


(defn
 update-visit
 "Auto-generated method to update one record in the visits table. Expects the following key(s) to be present in `params`: (:address_id :canvasser_id :date :id)."
 [{:keys [params]}]
 (do (db/update-visit! params))
 (response/found "/"))



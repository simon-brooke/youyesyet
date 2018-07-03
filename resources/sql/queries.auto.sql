------------------------------------------------------------------------
--	File queries.sql
--
--	autogenerated by adl.to-hugsql-queries at 2018-07-01T22:15:28.111Z
--
--	See [Application Description
--	Language](https://github.com/simon-brooke/adl).
------------------------------------------------------------------------

-- :name create-address! :! :n
-- :doc creates a new address record
INSERT INTO addresses (address,
	postcode,
	phone,
	district_id,
	latitude,
	longitude,
	locality)
VALUES (:address,
	:postcode,
	:phone,
	:district_id,
	:latitude,
	:longitude,
	:locality)
returning id

-- :name create-authority! :! :n
-- :doc creates a new authority record
INSERT INTO authorities (request_token_uri,
	access_token_uri,
	authorize_uri,
	consumer_key,
	consumer_secret,
	id)
VALUES (:request_token_uri,
	:access_token_uri,
	:authorize_uri,
	:consumer_key,
	:consumer_secret,
	:id)
returning id

-- :name create-canvasser! :! :n
-- :doc creates a new canvasser record
INSERT INTO canvassers (username,
	fullname,
	elector_id,
	address_id,
	phone,
	email,
	authority_id,
	authorised)
VALUES (:username,
	:fullname,
	:elector_id,
	:address_id,
	:phone,
	:email,
	:authority_id,
	:authorised)
returning id

-- :name create-district! :! :n
-- :doc creates a new district record
INSERT INTO districts (name)
VALUES (:name)
returning id

-- :name create-dwelling! :! :n
-- :doc creates a new dwelling record
INSERT INTO dwellings (address_id,
	sub_address)
VALUES (:address_id,
	:sub_address)
returning id

-- :name create-elector! :! :n
-- :doc creates a new elector record
INSERT INTO electors (name,
	dwelling_id,
	phone,
	email,
	gender)
VALUES (:name,
	:dwelling_id,
	:phone,
	:email,
	:gender)
returning id

-- :name create-followupaction! :! :n
-- :doc creates a new followupaction record
INSERT INTO followupactions (request_id,
	actor,
	date,
	notes,
	closed)
VALUES (:request_id,
	:actor,
	:date,
	:notes,
	:closed)
returning id

-- :name create-followupmethod! :! :n
-- :doc creates a new followupmethod record
INSERT INTO followupmethods (id)
VALUES (:id)
returning id

-- :name create-followuprequest! :! :n
-- :doc creates a new followuprequest record
INSERT INTO followuprequests (elector_id,
	visit_id,
	issue_id,
	method_id)
VALUES (:elector_id,
	:visit_id,
	:issue_id,
	:method_id)
returning id

-- :name create-gender! :! :n
-- :doc creates a new gender record
INSERT INTO genders (id)
VALUES (:id)
returning id

-- :name create-intention! :! :n
-- :doc creates a new intention record
INSERT INTO intentions (visit_id,
	elector_id,
	option_id,
	locality)
VALUES (:visit_id,
	:elector_id,
	:option_id,
	:locality)
returning Id

-- :name create-issue! :! :n
-- :doc creates a new issue record
INSERT INTO issues (url,
	current,
	id)
VALUES (:url,
	:current,
	:id)
returning id

-- :name create-option! :! :n
-- :doc creates a new option record
INSERT INTO options (id)
VALUES (:id)
returning id

-- :name create-role! :! :n
-- :doc creates a new role record
INSERT INTO roles (name)
VALUES (:name)
returning id

-- :name create-team! :! :n
-- :doc creates a new team record
INSERT INTO teams (name,
	district_id,
	latitude,
	longitude)
VALUES (:name,
	:district_id,
	:latitude,
	:longitude)
returning id

-- :name create-visit! :! :n
-- :doc creates a new visit record
INSERT INTO visits (address_id,
	canvasser_id,
	date)
VALUES (:address_id,
	:canvasser_id,
	:date)
returning id

-- :name delete-address! :! :n
-- :doc updates an existing address record
DELETE FROM addresses
WHERE addresses.id = :id

-- :name delete-authority! :! :n
-- :doc updates an existing authority record
DELETE FROM authorities
WHERE authorities.id = :id

-- :name delete-canvasser! :! :n
-- :doc updates an existing canvasser record
DELETE FROM canvassers
WHERE canvassers.id = :id

-- :name delete-district! :! :n
-- :doc updates an existing district record
DELETE FROM districts
WHERE districts.id = :id

-- :name delete-dwelling! :! :n
-- :doc updates an existing dwelling record
DELETE FROM dwellings
WHERE dwellings.id = :id

-- :name delete-elector! :! :n
-- :doc updates an existing elector record
DELETE FROM electors
WHERE electors.id = :id

-- :name delete-followupaction! :! :n
-- :doc updates an existing followupaction record
DELETE FROM followupactions
WHERE followupactions.id = :id

-- :name delete-followupmethod! :! :n
-- :doc updates an existing followupmethod record
DELETE FROM followupmethods
WHERE followupmethods.id = :id

-- :name delete-followuprequest! :! :n
-- :doc updates an existing followuprequest record
DELETE FROM followuprequests
WHERE followuprequests.id = :id

-- :name delete-gender! :! :n
-- :doc updates an existing gender record
DELETE FROM genders
WHERE genders.id = :id

-- :name delete-intention! :! :n
-- :doc updates an existing intention record
DELETE FROM intentions
WHERE intentions.Id = :Id

-- :name delete-issue! :! :n
-- :doc updates an existing issue record
DELETE FROM issues
WHERE issues.id = :id

-- :name delete-option! :! :n
-- :doc updates an existing option record
DELETE FROM options
WHERE options.id = :id

-- :name delete-role! :! :n
-- :doc updates an existing role record
DELETE FROM roles
WHERE roles.id = :id

-- :name delete-team! :! :n
-- :doc updates an existing team record
DELETE FROM teams
WHERE teams.id = :id

-- :name delete-visit! :! :n
-- :doc updates an existing visit record
DELETE FROM visits
WHERE visits.id = :id

-- :name get-address :? :1
-- :doc selects an existing address record
SELECT * FROM addresses
WHERE addresses.id = :id
ORDER BY addresses.address,
	addresses.postcode,
	addresses.id

-- :name get-authority :? :1
-- :doc selects an existing authority record
SELECT * FROM authorities
WHERE authorities.id = :id

-- :name get-canvasser :? :1
-- :doc selects an existing canvasser record
SELECT * FROM canvassers
WHERE canvassers.id = :id
ORDER BY canvassers.username,
	canvassers.fullname,
	canvassers.address_id,
	canvassers.phone,
	canvassers.email,
	canvassers.id

-- :name get-canvasser-by-username :? :1
-- :doc selects an existing canvasser record
SELECT * FROM canvassers
WHERE canvassers.username = :username
ORDER BY canvassers.username,
	canvassers.fullname,
	canvassers.address_id,
	canvassers.phone,
	canvassers.email,
	canvassers.id

-- :name get-district :? :1
-- :doc selects an existing district record
SELECT * FROM districts
WHERE districts.id = :id
ORDER BY districts.name,
	districts.id

-- :name get-dwelling :? :1
-- :doc selects an existing dwelling record
SELECT * FROM dwellings
WHERE dwellings.id = :id
ORDER BY dwellings.address_id,
	dwellings.sub_address,
	dwellings.id

-- :name get-elector :? :1
-- :doc selects an existing elector record
SELECT * FROM electors
WHERE electors.id = :id
ORDER BY electors.name,
	electors.phone,
	electors.email,
	electors.gender,
	electors.id

-- :name get-followupaction :? :1
-- :doc selects an existing followupaction record
SELECT * FROM followupactions
WHERE followupactions.id = :id
ORDER BY followupactions.date,
	followupactions.notes,
	followupactions.id

-- :name get-followupmethod :? :1
-- :doc selects an existing followupmethod record
SELECT * FROM followupmethods
WHERE followupmethods.id = :id

-- :name get-followuprequest :? :1
-- :doc selects an existing followuprequest record
SELECT * FROM followuprequests
WHERE followuprequests.id = :id
ORDER BY followuprequests.elector_id,
	followuprequests.visit_id,
	followuprequests.issue_id,
	followuprequests.id

-- :name get-gender :? :1
-- :doc selects an existing gender record
SELECT * FROM genders
WHERE genders.id = :id

-- :name get-intention :? :1
-- :doc selects an existing intention record
SELECT * FROM intentions
WHERE intentions.Id = :Id

-- :name get-issue :? :1
-- :doc selects an existing issue record
SELECT * FROM issues
WHERE issues.id = :id

-- :name get-option :? :1
-- :doc selects an existing option record
SELECT * FROM options
WHERE options.id = :id

-- :name get-role :? :1
-- :doc selects an existing role record
SELECT * FROM roles
WHERE roles.id = :id
ORDER BY roles.name,
	roles.id

-- :name get-role-by-name :? :1
-- :doc selects an existing role record
SELECT * FROM roles
WHERE roles.name = :name
ORDER BY roles.name,
	roles.id

-- :name get-team :? :1
-- :doc selects an existing team record
SELECT * FROM teams
WHERE teams.id = :id
ORDER BY teams.name,
	teams.id

-- :name get-visit :? :1
-- :doc selects an existing visit record
SELECT * FROM visits
WHERE visits.id = :id
ORDER BY visits.address_id,
	visits.date,
	visits.id

-- :name list-addresses :? :*
-- :doc lists all existing address records
SELECT DISTINCT * FROM lv_addresses
ORDER BY lv_addresses.address,
	lv_addresses.postcode,
	lv_addresses.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-addresses-by-district :? :*
-- :doc lists all existing address records related to a given district
SELECT *
FROM lv_addresses, addresses
WHERE lv_addresses.id = addresses.id
	AND addresses.district_id = :id
ORDER BY lv_addresses.address,
	lv_addresses.postcode,
	lv_addresses.id

-- :name list-authorities :? :*
-- :doc lists all existing authority records
SELECT DISTINCT * FROM lv_authorities
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-canvassers :? :*
-- :doc lists all existing canvasser records
SELECT DISTINCT * FROM lv_canvassers
ORDER BY lv_canvassers.username,
	lv_canvassers.fullname,
	lv_canvassers.address_id,
	lv_canvassers.phone,
	lv_canvassers.email,
	lv_canvassers.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-canvassers-by-address :? :*
-- :doc lists all existing canvasser records related to a given address
SELECT *
FROM lv_canvassers, canvassers
WHERE lv_canvassers.id = canvassers.id
	AND canvassers.address_id = :id
ORDER BY lv_canvassers.username,
	lv_canvassers.fullname,
	lv_canvassers.address_id,
	lv_canvassers.phone,
	lv_canvassers.email,
	lv_canvassers.id

-- :name list-canvassers-by-authority :? :*
-- :doc lists all existing canvasser records related to a given authority
SELECT *
FROM lv_canvassers, canvassers
WHERE lv_canvassers.id = canvassers.id
	AND canvassers.authority_id = :id
ORDER BY lv_canvassers.username,
	lv_canvassers.fullname,
	lv_canvassers.address_id,
	lv_canvassers.phone,
	lv_canvassers.email,
	lv_canvassers.id

-- :name list-canvassers-by-elector :? :*
-- :doc lists all existing canvasser records related to a given elector
SELECT *
FROM lv_canvassers, canvassers
WHERE lv_canvassers.id = canvassers.id
	AND canvassers.elector_id = :id
ORDER BY lv_canvassers.username,
	lv_canvassers.fullname,
	lv_canvassers.address_id,
	lv_canvassers.phone,
	lv_canvassers.email,
	lv_canvassers.id

-- :name list-canvassers-by-role :? :*
-- :doc links all existing canvasser records related to a given role
SELECT *
FROM canvassers, ln_canvassers_roles
WHERE canvassers.id = ln_canvassers_roles.canvasser_id
	AND ln_canvassers_roles.role_id = :id
ORDER BY canvassers.username,
	canvassers.fullname,
	canvassers.address_id,
	canvassers.phone,
	canvassers.email,
	canvassers.id

-- :name list-districts :? :*
-- :doc lists all existing district records
SELECT DISTINCT * FROM lv_districts
ORDER BY lv_districts.name,
	lv_districts.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-dwellings :? :*
-- :doc lists all existing dwelling records
SELECT DISTINCT * FROM lv_dwellings
ORDER BY lv_dwellings.address_id,
	lv_dwellings.sub_address,
	lv_dwellings.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-dwellings-by-address :? :*
-- :doc lists all existing dwelling records related to a given address
SELECT *
FROM lv_dwellings, dwellings
WHERE lv_dwellings.id = dwellings.id
	AND dwellings.address_id = :id
ORDER BY lv_dwellings.address_id,
	lv_dwellings.sub_address,
	lv_dwellings.id

-- :name list-electors :? :*
-- :doc lists all existing elector records
SELECT DISTINCT * FROM lv_electors
ORDER BY lv_electors.name,
	lv_electors.phone,
	lv_electors.email,
	lv_electors.gender,
	lv_electors.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-electors-by-dwelling :? :*
-- :doc lists all existing elector records related to a given dwelling
SELECT *
FROM lv_electors, electors
WHERE lv_electors.id = electors.id
	AND electors.dwelling_id = :id
ORDER BY lv_electors.name,
	lv_electors.phone,
	lv_electors.email,
	lv_electors.gender,
	lv_electors.id

-- :name list-electors-by-gender :? :*
-- :doc lists all existing elector records related to a given gender
SELECT *
FROM lv_electors, electors
WHERE lv_electors.id = electors.id
	AND electors.gender = :id
ORDER BY lv_electors.name,
	lv_electors.phone,
	lv_electors.email,
	lv_electors.gender,
	lv_electors.id

-- :name list-followupactions :? :*
-- :doc lists all existing followupaction records
SELECT DISTINCT * FROM lv_followupactions
ORDER BY lv_followupactions.date,
	lv_followupactions.notes,
	lv_followupactions.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-followupactions-by-canvasser :? :*
-- :doc lists all existing followupaction records related to a given canvasser
SELECT *
FROM lv_followupactions, followupactions
WHERE lv_followupactions.id = followupactions.id
	AND followupactions.actor = :id
ORDER BY lv_followupactions.date,
	lv_followupactions.notes,
	lv_followupactions.id

-- :name list-followupactions-by-followuprequest :? :*
-- :doc lists all existing followupaction records related to a given followuprequest
SELECT *
FROM lv_followupactions, followupactions
WHERE lv_followupactions.id = followupactions.id
	AND followupactions.request_id = :id
ORDER BY lv_followupactions.date,
	lv_followupactions.notes,
	lv_followupactions.id

-- :name list-followupmethods :? :*
-- :doc lists all existing followupmethod records
SELECT DISTINCT * FROM lv_followupmethods
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-followuprequests :? :*
-- :doc lists all existing followuprequest records
SELECT DISTINCT * FROM lv_followuprequests
ORDER BY lv_followuprequests.elector_id,
	lv_followuprequests.visit_id,
	lv_followuprequests.issue_id,
	lv_followuprequests.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-followuprequests-by-elector :? :*
-- :doc lists all existing followuprequest records related to a given elector
SELECT *
FROM lv_followuprequests, followuprequests
WHERE lv_followuprequests.id = followuprequests.id
	AND followuprequests.elector_id = :id
ORDER BY lv_followuprequests.elector_id,
	lv_followuprequests.visit_id,
	lv_followuprequests.issue_id,
	lv_followuprequests.id

-- :name list-followuprequests-by-followupmethod :? :*
-- :doc lists all existing followuprequest records related to a given followupmethod
SELECT *
FROM lv_followuprequests, followuprequests
WHERE lv_followuprequests.id = followuprequests.id
	AND followuprequests.method_id = :id
ORDER BY lv_followuprequests.elector_id,
	lv_followuprequests.visit_id,
	lv_followuprequests.issue_id,
	lv_followuprequests.id

-- :name list-followuprequests-by-issue :? :*
-- :doc lists all existing followuprequest records related to a given issue
SELECT *
FROM lv_followuprequests, followuprequests
WHERE lv_followuprequests.id = followuprequests.id
	AND followuprequests.issue_id = :id
ORDER BY lv_followuprequests.elector_id,
	lv_followuprequests.visit_id,
	lv_followuprequests.issue_id,
	lv_followuprequests.id

-- :name list-followuprequests-by-visit :? :*
-- :doc lists all existing followuprequest records related to a given visit
SELECT *
FROM lv_followuprequests, followuprequests
WHERE lv_followuprequests.id = followuprequests.id
	AND followuprequests.visit_id = :id
ORDER BY lv_followuprequests.elector_id,
	lv_followuprequests.visit_id,
	lv_followuprequests.issue_id,
	lv_followuprequests.id

-- :name list-genders :? :*
-- :doc lists all existing gender records
SELECT DISTINCT * FROM lv_genders
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-intentions :? :*
-- :doc lists all existing intention records
SELECT DISTINCT * FROM lv_intentions
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-intentions-by-elector :? :*
-- :doc lists all existing intention records related to a given elector
SELECT *
FROM lv_intentions, intentions
WHERE lv_intentions.Id = intentions.Id
	AND intentions.elector_id = :id

-- :name list-intentions-by-option :? :*
-- :doc lists all existing intention records related to a given option
SELECT *
FROM lv_intentions, intentions
WHERE lv_intentions.Id = intentions.Id
	AND intentions.option_id = :id

-- :name list-intentions-by-visit :? :*
-- :doc lists all existing intention records related to a given visit
SELECT *
FROM lv_intentions, intentions
WHERE lv_intentions.Id = intentions.Id
	AND intentions.visit_id = :id

-- :name list-issues :? :*
-- :doc lists all existing issue records
SELECT DISTINCT * FROM lv_issues
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-options :? :*
-- :doc lists all existing option records
SELECT DISTINCT * FROM lv_options
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-roles :? :*
-- :doc lists all existing role records
SELECT DISTINCT * FROM lv_roles
ORDER BY lv_roles.name,
	lv_roles.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-roles-by-canvasser :? :*
-- :doc links all existing role records related to a given canvasser
SELECT *
FROM roles, ln_canvassers_roles
WHERE roles.id = ln_canvassers_roles.role_id
	AND ln_canvassers_roles.canvasser_id = :id
ORDER BY roles.name,
	roles.id

-- :name list-teams :? :*
-- :doc lists all existing team records
SELECT DISTINCT * FROM lv_teams
ORDER BY lv_teams.name,
	lv_teams.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-teams-by-canvasser :? :*
-- :doc links all existing team records related to a given canvasser
SELECT *
FROM teams, ln_canvassers_teams
WHERE teams.id = ln_canvassers_teams.team_id
	AND ln_canvassers_teams.canvasser_id = :id
ORDER BY teams.name,
	teams.id

-- :name list-teams-by-district :? :*
-- :doc lists all existing team records related to a given district
SELECT *
FROM lv_teams, teams
WHERE lv_teams.id = teams.id
	AND teams.district_id = :id
ORDER BY lv_teams.name,
	lv_teams.id

-- :name list-visits :? :*
-- :doc lists all existing visit records
SELECT DISTINCT * FROM lv_visits
ORDER BY lv_visits.address_id,
	lv_visits.date,
	lv_visits.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-visits-by-address :? :*
-- :doc lists all existing visit records related to a given address
SELECT *
FROM lv_visits, visits
WHERE lv_visits.id = visits.id
	AND visits.address_id = :id
ORDER BY lv_visits.address_id,
	lv_visits.date,
	lv_visits.id

-- :name list-visits-by-canvasser :? :*
-- :doc lists all existing visit records related to a given canvasser
SELECT *
FROM lv_visits, visits
WHERE lv_visits.id = visits.id
	AND visits.canvasser_id = :id
ORDER BY lv_visits.address_id,
	lv_visits.date,
	lv_visits.id

-- :name search-strings-address :? :1
-- :doc selects existing address records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_addresses
WHERE false
	--~ (if (:address params) "OR address LIKE '%:address%'")
	--~ (if (:postcode params) "OR postcode LIKE '%:postcode%'")
	--~ (if (:phone params) "OR phone LIKE '%:phone%'")
	--~ (if (:district_id params) "OR district_id = :district_id")
	--~ (if (:latitude params) "OR latitude = :latitude")
	--~ (if (:longitude params) "OR longitude = :longitude")
	--~ (if (:locality params) "OR locality = :locality")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_addresses.address,
	lv_addresses.postcode,
	lv_addresses.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-authority :? :1
-- :doc selects existing authority records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_authorities
WHERE false
	--~ (if (:request-token-uri params) "OR request_token_uri LIKE '%:request-token-uri%'")
	--~ (if (:access-token-uri params) "OR access_token_uri LIKE '%:access-token-uri%'")
	--~ (if (:authorize-uri params) "OR authorize_uri LIKE '%:authorize-uri%'")
	--~ (if (:consumer-key params) "OR consumer_key LIKE '%:consumer-key%'")
	--~ (if (:consumer-secret params) "OR consumer_secret LIKE '%:consumer-secret%'")
	--~ (if (:id params) "OR id LIKE '%:id%'")
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-canvasser :? :1
-- :doc selects existing canvasser records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_canvassers
WHERE false
	--~ (if (:username params) "OR username LIKE '%:username%'")
	--~ (if (:fullname params) "OR fullname LIKE '%:fullname%'")
	--~ (if (:elector_id params) "OR elector_id = :elector_id")
	--~ (if (:address_id params) "OR address_id = :address_id")
	--~ (if (:phone params) "OR phone LIKE '%:phone%'")
	--~ (if (:email params) "OR email LIKE '%:email%'")
	--~ (if (:authority_id params) "OR authority_id LIKE '%:authority_id%'")
	--~ (if (:authorised params) "OR authorised = :authorised")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_canvassers.username,
	lv_canvassers.fullname,
	lv_canvassers.address_id,
	lv_canvassers.phone,
	lv_canvassers.email,
	lv_canvassers.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-district :? :1
-- :doc selects existing district records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_districts
WHERE false
	--~ (if (:name params) "OR name LIKE '%:name%'")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_districts.name,
	lv_districts.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-dwelling :? :1
-- :doc selects existing dwelling records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_dwellings
WHERE false
	--~ (if (:address_id params) "OR address_id = :address_id")
	--~ (if (:sub-address params) "OR sub_address LIKE '%:sub-address%'")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_dwellings.address_id,
	lv_dwellings.sub_address,
	lv_dwellings.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-elector :? :1
-- :doc selects existing elector records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_electors
WHERE false
	--~ (if (:name params) "OR name LIKE '%:name%'")
	--~ (if (:dwelling_id params) "OR dwelling_id = :dwelling_id")
	--~ (if (:phone params) "OR phone LIKE '%:phone%'")
	--~ (if (:email params) "OR email LIKE '%:email%'")
	--~ (if (:gender params) "OR gender LIKE '%:gender%'")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_electors.name,
	lv_electors.phone,
	lv_electors.email,
	lv_electors.gender,
	lv_electors.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-followupaction :? :1
-- :doc selects existing followupaction records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_followupactions
WHERE false
	--~ (if (:request_id params) "OR request_id = :request_id")
	--~ (if (:actor params) "OR actor = :actor")
	--~ (if (:date params) "OR date = ':date'")
	--~ (if (:notes params) "OR notes LIKE '%:notes%'")
	--~ (if (:closed params) "OR closed = :closed")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_followupactions.date,
	lv_followupactions.notes,
	lv_followupactions.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-followupmethod :? :1
-- :doc selects existing followupmethod records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_followupmethods
WHERE false
	--~ (if (:id params) "OR id LIKE '%:id%'")
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-followuprequest :? :1
-- :doc selects existing followuprequest records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_followuprequests
WHERE false
	--~ (if (:elector_id params) "OR elector_id = :elector_id")
	--~ (if (:visit_id params) "OR visit_id = :visit_id")
	--~ (if (:issue_id params) "OR issue_id LIKE '%:issue_id%'")
	--~ (if (:method_id params) "OR method_id LIKE '%:method_id%'")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_followuprequests.elector_id,
	lv_followuprequests.visit_id,
	lv_followuprequests.issue_id,
	lv_followuprequests.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-gender :? :1
-- :doc selects existing gender records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_genders
WHERE false
	--~ (if (:id params) "OR id LIKE '%:id%'")
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-intention :? :1
-- :doc selects existing intention records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_intentions
WHERE false
	--~ (if (:visit_id params) "OR visit_id = :visit_id")
	--~ (if (:elector_id params) "OR elector_id = :elector_id")
	--~ (if (:option_id params) "OR option_id LIKE '%:option_id%'")
	--~ (if (:locality params) "OR locality = :locality")
	--~ (if (:Id params) "OR Id = :Id")
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-issue :? :1
-- :doc selects existing issue records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_issues
WHERE false
	--~ (if (:url params) "OR url LIKE '%:url%'")
	--~ (if (:current params) "OR current = :current")
	--~ (if (:id params) "OR id LIKE '%:id%'")
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-option :? :1
-- :doc selects existing option records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_options
WHERE false
	--~ (if (:id params) "OR id LIKE '%:id%'")
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-role :? :1
-- :doc selects existing role records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_roles
WHERE false
	--~ (if (:name params) "OR name LIKE '%:name%'")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_roles.name,
	lv_roles.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-team :? :1
-- :doc selects existing team records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_teams
WHERE false
	--~ (if (:name params) "OR name LIKE '%:name%'")
	--~ (if (:district_id params) "OR district_id = :district_id")
	--~ (if (:latitude params) "OR latitude = :latitude")
	--~ (if (:longitude params) "OR longitude = :longitude")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_teams.name,
	lv_teams.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name search-strings-visit :? :1
-- :doc selects existing visit records having any string field matching the parameter of the same name by substring match
SELECT DISTINCT * FROM lv_visits
WHERE false
	--~ (if (:address_id params) "OR address_id = :address_id")
	--~ (if (:canvasser_id params) "OR canvasser_id = :canvasser_id")
	--~ (if (:date params) "OR date = ':date'")
	--~ (if (:id params) "OR id = :id")
ORDER BY lv_visits.address_id,
	lv_visits.date,
	lv_visits.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name update-address! :! :n
-- :doc updates an existing address record
UPDATE addresses
SET address = :address,
	postcode = :postcode,
	phone = :phone,
	district_id = :district_id,
	latitude = :latitude,
	longitude = :longitude,
	locality = :locality
WHERE addresses.id = :id

-- :name update-authority! :! :n
-- :doc updates an existing authority record
UPDATE authorities
SET request_token_uri = :request-token-uri,
	access_token_uri = :access-token-uri,
	authorize_uri = :authorize-uri,
	consumer_key = :consumer-key,
	consumer_secret = :consumer-secret,
	id = :id
WHERE authorities.id = :id

-- :name update-canvasser! :! :n
-- :doc updates an existing canvasser record
UPDATE canvassers
SET username = :username,
	fullname = :fullname,
	elector_id = :elector_id,
	address_id = :address_id,
	phone = :phone,
	email = :email,
	authority_id = :authority_id,
	authorised = :authorised
WHERE canvassers.id = :id

-- :name update-district! :! :n
-- :doc updates an existing district record
UPDATE districts
SET name = :name
WHERE districts.id = :id

-- :name update-dwelling! :! :n
-- :doc updates an existing dwelling record
UPDATE dwellings
SET address_id = :address_id,
	sub_address = :sub-address
WHERE dwellings.id = :id

-- :name update-elector! :! :n
-- :doc updates an existing elector record
UPDATE electors
SET name = :name,
	dwelling_id = :dwelling_id,
	phone = :phone,
	email = :email,
	gender = :gender
WHERE electors.id = :id

-- :name update-followupaction! :! :n
-- :doc updates an existing followupaction record
UPDATE followupactions
SET request_id = :request_id,
	actor = :actor,
	date = :date,
	notes = :notes,
	closed = :closed
WHERE followupactions.id = :id

-- :name update-followuprequest! :! :n
-- :doc updates an existing followuprequest record
UPDATE followuprequests
SET elector_id = :elector_id,
	visit_id = :visit_id,
	issue_id = :issue_id,
	method_id = :method_id
WHERE followuprequests.id = :id

-- :name update-intention! :! :n
-- :doc updates an existing intention record
UPDATE intentions
SET visit_id = :visit_id,
	elector_id = :elector_id,
	option_id = :option_id,
	locality = :locality
WHERE intentions.Id = :Id

-- :name update-issue! :! :n
-- :doc updates an existing issue record
UPDATE issues
SET url = :url,
	current = :current,
	id = :id
WHERE issues.id = :id

-- :name update-role! :! :n
-- :doc updates an existing role record
UPDATE roles
SET name = :name
WHERE roles.id = :id

-- :name update-team! :! :n
-- :doc updates an existing team record
UPDATE teams
SET name = :name,
	district_id = :district_id,
	latitude = :latitude,
	longitude = :longitude
WHERE teams.id = :id

-- :name update-visit! :! :n
-- :doc updates an existing visit record
UPDATE visits
SET address_id = :address_id,
	canvasser_id = :canvasser_id,
	date = :date
WHERE visits.id = :id
-- :name delete-elector! :! :n
-- :doc updates an existing elector record
DELETE FROM electors
WHERE electors.id = :id

-- :name get-district :? :1
-- :doc selects an existing district record
SELECT * FROM districts
WHERE districts.id = :id
ORDER BY districts.id

-- :name update-addresse! :! :n
-- :doc updates an existing addresse record
UPDATE addresses
SET id = :id,
	address = :address,
	postcode = :postcode,
	phone = :phone,
	district_id = :district_id,
	latitude = :latitude,
	longitude = :longitude
WHERE addresses.id = :id

-- :name list-teamorganiserships-teams-by-canvasser :? :*
-- :doc lists all existing teams records related through teamorganiserships to a given canvasser
SELECT teams.*
FROM teams, teamorganiserships
WHERE teams. = teamorganiserships.team_id
	AND teamorganiserships.canvasser_id = :id
ORDER BY teams.

-- :name create-authority!  :! :n
-- :doc creates a new authority record
INSERT INTO authorities (id)
VALUES (:id)
returning id

-- :name list-canvassers :? :*
-- :doc lists all existing canvasser records
SELECT * FROM canvassers
ORDER BY canvassers.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name delete-canvasser! :! :n
-- :doc updates an existing canvasser record
DELETE FROM canvassers
WHERE canvassers.id = :id

-- :name get-followupmethod :? :1
-- :doc selects an existing followupmethod record
SELECT * FROM followupmethods
WHERE followupmethods.id = :id
ORDER BY followupmethods.id

-- :name get-canvasser :? :1
-- :doc selects an existing canvasser record
SELECT * FROM canvassers
WHERE canvassers.id = :id
ORDER BY canvassers.id

-- :name create-role!  :! :n
-- :doc creates a new role record
INSERT INTO roles (id,
	name)
VALUES (:id,
	:name)

-- :name list-issueexpertise-issues-by-canvasser :? :*
-- :doc lists all existing issues records related through issueexpertise to a given canvasser
SELECT issues.*
FROM issues, issueexpertise
WHERE issues.id = issueexpertise.issue_id
	AND issueexpertise.canvasser_id = :id
ORDER BY issues.id

-- :name update-followupaction! :! :n
-- :doc updates an existing followupaction record
UPDATE followupactions
SET id = :id,
	request_id = :request_id,
	actor = :actor,
	date = :date,
	notes = :notes,
	closed = :closed
WHERE followupactions.id = :id

-- :name list-teammemberships-teams-by-canvasser :? :*
-- :doc lists all existing teams records related through teammemberships to a given canvasser
SELECT teams.*
FROM teams, teammemberships
WHERE teams. = teammemberships.team_id
	AND teammemberships.canvasser_id = :id
ORDER BY teams.

-- :name list-authorities :? :*
-- :doc lists all existing authority records
SELECT * FROM authorities
ORDER BY authorities.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name create-intention!  :! :n
-- :doc creates a new intention record
INSERT INTO intentions (visit_id,
	elector_id,
	option_id)
VALUES (:visit_id,
	:elector_id,
	:option_id)

-- :name delete-issue! :! :n
-- :doc updates an existing issue record
DELETE FROM issues
WHERE issues.id = :id

-- :name list-followuprequests-by-visit :? :*
-- :doc lists all existing followuprequest records related to a given visit
SELECT *
FROM followuprequests
WHERE followuprequests.visit_id = :id
ORDER BY followuprequests.id

-- :name list-canvassers-by-addresse :? :*
-- :doc lists all existing canvasser records related to a given addresse
SELECT *
FROM canvassers
WHERE canvassers.address_id = :id
ORDER BY canvassers.id

-- :name list-intentions-options-by-elector :? :*
-- :doc lists all existing options records related through intentions to a given elector
SELECT options.*
FROM options, intentions
WHERE options.id = intentions.option_id
	AND intentions.elector_id = :id
ORDER BY options.id

-- :name create-followupmethod!  :! :n
-- :doc creates a new followupmethod record
INSERT INTO followupmethods (id)
VALUES (:id)
returning id

-- :name list-canvassers-by-elector :? :*
-- :doc lists all existing canvasser records related to a given elector
SELECT *
FROM canvassers
WHERE canvassers.elector_id = :id
ORDER BY canvassers.id

-- :name create-district!  :! :n
-- :doc creates a new district record
INSERT INTO districts (id,
	name)
VALUES (:id,
	:name)
returning id

-- :name delete-followupaction! :! :n
-- :doc updates an existing followupaction record
DELETE FROM followupactions
WHERE followupactions.id = :id

-- :name create-followupaction!  :! :n
-- :doc creates a new followupaction record
INSERT INTO followupactions (id,
	request_id,
	actor,
	date,
	notes,
	closed)
VALUES (:id,
	:request_id,
	:actor,
	:date,
	:notes,
	:closed)
returning id

-- :name list-canvassers-by-authoritie :? :*
-- :doc lists all existing canvasser records related to a given authoritie
SELECT *
FROM canvassers
WHERE canvassers.authority_id = :id
ORDER BY canvassers.id

-- :name list-followuprequests :? :*
-- :doc lists all existing followuprequest records
SELECT * FROM followuprequests
ORDER BY followuprequests.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-followupactions :? :*
-- :doc lists all existing followupaction records
SELECT * FROM followupactions
ORDER BY followupactions.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name create-followuprequest!  :! :n
-- :doc creates a new followuprequest record
INSERT INTO followuprequests (id,
	elector_id,
	visit_id,
	issue_id,
	method_id)
VALUES (:id,
	:elector_id,
	:visit_id,
	:issue_id,
	:method_id)
returning id

-- :name update-issue! :! :n
-- :doc updates an existing issue record
UPDATE issues
SET id = :id,
	url = :url
WHERE issues.id = :id

-- :name get-option :? :1
-- :doc selects an existing option record
SELECT * FROM options
WHERE options.id = :id
ORDER BY options.id

-- :name list-issueexpertise-followupmethods-by-issue :? :*
-- :doc lists all existing followupmethods records related through issueexpertise to a given issue
SELECT followupmethods.*
FROM followupmethods, issueexpertise
WHERE followupmethods.id = issueexpertise.method_id
	AND issueexpertise.issue_id = :id
ORDER BY followupmethods.id

-- :name list-intentions-visits-by-option :? :*
-- :doc lists all existing visits records related through intentions to a given option
SELECT visits.*
FROM visits, intentions
WHERE visits.id = intentions.visit_id
	AND intentions.option_id = :id
ORDER BY visits.id

-- :name list-teams :? :*
-- :doc lists all existing team records
SELECT * FROM teams
ORDER BY teams.
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-schema_migrations :? :*
-- :doc lists all existing schema-migration records
SELECT * FROM schema_migrations
ORDER BY schema_migrations.
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name create-elector!  :! :n
-- :doc creates a new elector record
INSERT INTO electors (id,
	name,
	address_id,
	phone,
	email)
VALUES (:id,
	:name,
	:address_id,
	:phone,
	:email)
returning id

-- :name delete-addresse! :! :n
-- :doc updates an existing addresse record
DELETE FROM addresses
WHERE addresses.id = :id

-- :name delete-followuprequest! :! :n
-- :doc updates an existing followuprequest record
DELETE FROM followuprequests
WHERE followuprequests.id = :id

-- :name list-options :? :*
-- :doc lists all existing option records
SELECT * FROM options
ORDER BY options.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name get-followupaction :? :1
-- :doc selects an existing followupaction record
SELECT * FROM followupactions
WHERE followupactions.id = :id
ORDER BY followupactions.id

-- :name list-followupactions-by-canvasser :? :*
-- :doc lists all existing followupaction records related to a given canvasser
SELECT *
FROM followupactions
WHERE followupactions.actor = :id
ORDER BY followupactions.id

-- :name get-issue :? :1
-- :doc selects an existing issue record
SELECT * FROM issues
WHERE issues.id = :id
ORDER BY issues.id

-- :name create-teamorganisership!  :! :n
-- :doc creates a new teamorganisership record
INSERT INTO teamorganiserships (team_id,
	canvasser_id)
VALUES (:team_id,
	:canvasser_id)

-- :name get-visit :? :1
-- :doc selects an existing visit record
SELECT * FROM visits
WHERE visits.id = :id
ORDER BY visits.id

-- :name list-addresses :? :*
-- :doc lists all existing addresse records
SELECT * FROM addresses
ORDER BY addresses.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name create-team!  :! :n
-- :doc creates a new team record
INSERT INTO teams (id,
	name,
	district_id,
	latitude,
	longitude)
VALUES (:id,
	:name,
	:district_id,
	:latitude,
	:longitude)

-- :name list-addresses-by-district :? :*
-- :doc lists all existing addresse records related to a given district
SELECT *
FROM addresses
WHERE addresses.district_id = :id
ORDER BY addresses.id

-- :name create-issue!  :! :n
-- :doc creates a new issue record
INSERT INTO issues (id,
	url)
VALUES (:id,
	:url)
returning id

-- :name delete-authority! :! :n
-- :doc updates an existing authority record
DELETE FROM authorities
WHERE authorities.id = :id

-- :name create-canvasser!  :! :n
-- :doc creates a new canvasser record
INSERT INTO canvassers (id,
	username,
	fullname,
	elector_id,
	address_id,
	phone,
	email,
	authority_id,
	authorised)
VALUES (:id,
	:username,
	:fullname,
	:elector_id,
	:address_id,
	:phone,
	:email,
	:authority_id,
	:authorised)
returning id

-- :name list-visits-by-addresse :? :*
-- :doc lists all existing visit records related to a given addresse
SELECT *
FROM visits
WHERE visits.address_id = :id
ORDER BY visits.id

-- :name delete-district! :! :n
-- :doc updates an existing district record
DELETE FROM districts
WHERE districts.id = :id

-- :name get-addresse :? :1
-- :doc selects an existing addresse record
SELECT * FROM addresses
WHERE addresses.id = :id
ORDER BY addresses.id

-- :name create-addresse!  :! :n
-- :doc creates a new addresse record
INSERT INTO addresses (id,
	address,
	postcode,
	phone,
	district_id,
	latitude,
	longitude)
VALUES (:id,
	:address,
	:postcode,
	:phone,
	:district_id,
	:latitude,
	:longitude)
returning id

-- :name create-rolemembership!  :! :n
-- :doc creates a new rolemembership record
INSERT INTO rolememberships (role_id,
	canvasser_id)
VALUES (:role_id,
	:canvasser_id)

-- :name list-issueexpertise-followupmethods-by-canvasser :? :*
-- :doc lists all existing followupmethods records related through issueexpertise to a given canvasser
SELECT followupmethods.*
FROM followupmethods, issueexpertise
WHERE followupmethods.id = issueexpertise.method_id
	AND issueexpertise.canvasser_id = :id
ORDER BY followupmethods.id

-- :name get-followuprequest :? :1
-- :doc selects an existing followuprequest record
SELECT * FROM followuprequests
WHERE followuprequests.id = :id
ORDER BY followuprequests.id

-- :name create-teammembership!  :! :n
-- :doc creates a new teammembership record
INSERT INTO teammemberships (team_id,
	canvasser_id)
VALUES (:team_id,
	:canvasser_id)

-- :name delete-option! :! :n
-- :doc updates an existing option record
DELETE FROM options
WHERE options.id = :id

-- :name list-teammemberships-canvassers-by-team :? :*
-- :doc lists all existing canvassers records related through teammemberships to a given team
SELECT canvassers.*
FROM canvassers, teammemberships
WHERE canvassers.id = teammemberships.canvasser_id
	AND teammemberships.team_id = :id
ORDER BY canvassers.id

-- :name list-issueexpertise-canvassers-by-issue :? :*
-- :doc lists all existing canvassers records related through issueexpertise to a given issue
SELECT canvassers.*
FROM canvassers, issueexpertise
WHERE canvassers.id = issueexpertise.canvasser_id
	AND issueexpertise.issue_id = :id
ORDER BY canvassers.id

-- :name delete-followupmethod! :! :n
-- :doc updates an existing followupmethod record
DELETE FROM followupmethods
WHERE followupmethods.id = :id

-- :name list-districts :? :*
-- :doc lists all existing district records
SELECT * FROM districts
ORDER BY districts.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-rolememberships-canvassers-by-role :? :*
-- :doc lists all existing canvassers records related through rolememberships to a given role
SELECT canvassers.*
FROM canvassers, rolememberships
WHERE canvassers.id = rolememberships.canvasser_id
	AND rolememberships.role_id = :id
ORDER BY canvassers.id

-- :name get-authority :? :1
-- :doc selects an existing authority record
SELECT * FROM authorities
WHERE authorities.id = :id
ORDER BY authorities.id

-- :name create-option!  :! :n
-- :doc creates a new option record
INSERT INTO options (id)
VALUES (:id)
returning id

-- :name list-visits :? :*
-- :doc lists all existing visit records
SELECT * FROM visits
ORDER BY visits.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-teamorganiserships-canvassers-by-team :? :*
-- :doc lists all existing canvassers records related through teamorganiserships to a given team
SELECT canvassers.*
FROM canvassers, teamorganiserships
WHERE canvassers.id = teamorganiserships.canvasser_id
	AND teamorganiserships.team_id = :id
ORDER BY canvassers.id

-- :name get-elector :? :1
-- :doc selects an existing elector record
SELECT * FROM electors
WHERE electors.id = :id
ORDER BY electors.id

-- :name create-visit!  :! :n
-- :doc creates a new visit record
INSERT INTO visits (id,
	address_id,
	canvasser_id,
	date)
VALUES (:id,
	:address_id,
	:canvasser_id,
	:date)
returning id

-- :name list-roles :? :*
-- :doc lists all existing role records
SELECT * FROM roles
ORDER BY roles.
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name update-visit! :! :n
-- :doc updates an existing visit record
UPDATE visits
SET id = :id,
	address_id = :address_id,
	canvasser_id = :canvasser_id,
	date = :date
WHERE visits.id = :id

-- :name update-district! :! :n
-- :doc updates an existing district record
UPDATE districts
SET id = :id,
	name = :name
WHERE districts.id = :id

-- :name list-followupactions-by-followuprequest :? :*
-- :doc lists all existing followupaction records related to a given followuprequest
SELECT *
FROM followupactions
WHERE followupactions.request_id = :id
ORDER BY followupactions.id

-- :name create-issueexpertise!  :! :n
-- :doc creates a new issueexpertise record
INSERT INTO issueexpertise (canvasser_id,
	issue_id,
	method_id)
VALUES (:canvasser_id,
	:issue_id,
	:method_id)

-- :name list-issueexpertise-canvassers-by-followupmethod :? :*
-- :doc lists all existing canvassers records related through issueexpertise to a given followupmethod
SELECT canvassers.*
FROM canvassers, issueexpertise
WHERE canvassers.id = issueexpertise.canvasser_id
	AND issueexpertise.method_id = :id
ORDER BY canvassers.id

-- :name update-elector! :! :n
-- :doc updates an existing elector record
UPDATE electors
SET id = :id,
	name = :name,
	address_id = :address_id,
	phone = :phone,
	email = :email
WHERE electors.id = :id

-- :name list-followupmethods :? :*
-- :doc lists all existing followupmethod records
SELECT * FROM followupmethods
ORDER BY followupmethods.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name delete-visit! :! :n
-- :doc updates an existing visit record
DELETE FROM visits
WHERE visits.id = :id

-- :name list-intentions-electors-by-option :? :*
-- :doc lists all existing electors records related through intentions to a given option
SELECT electors.*
FROM electors, intentions
WHERE electors.id = intentions.elector_id
	AND intentions.option_id = :id
ORDER BY electors.id

-- :name create-schema-migration!  :! :n
-- :doc creates a new schema-migration record
INSERT INTO schema_migrations (id)
VALUES (:id)

-- :name update-canvasser! :! :n
-- :doc updates an existing canvasser record
UPDATE canvassers
SET id = :id,
	username = :username,
	fullname = :fullname,
	elector_id = :elector_id,
	address_id = :address_id,
	phone = :phone,
	email = :email,
	authority_id = :authority_id,
	authorised = :authorised
WHERE canvassers.id = :id

-- :name list-intentions-options-by-visit :? :*
-- :doc lists all existing options records related through intentions to a given visit
SELECT options.*
FROM options, intentions
WHERE options.id = intentions.option_id
	AND intentions.visit_id = :id
ORDER BY options.id

-- :name list-followuprequests-by-issue :? :*
-- :doc lists all existing followuprequest records related to a given issue
SELECT *
FROM followuprequests
WHERE followuprequests.issue_id = :id
ORDER BY followuprequests.id

-- :name list-followuprequests-by-followupmethod :? :*
-- :doc lists all existing followuprequest records related to a given followupmethod
SELECT *
FROM followuprequests
WHERE followuprequests.method_id = :id
ORDER BY followuprequests.id

-- :name list-intentions-visits-by-elector :? :*
-- :doc lists all existing visits records related through intentions to a given elector
SELECT visits.*
FROM visits, intentions
WHERE visits.id = intentions.visit_id
	AND intentions.elector_id = :id
ORDER BY visits.id

-- :name list-rolememberships-roles-by-canvasser :? :*
-- :doc lists all existing roles records related through rolememberships to a given canvasser
SELECT roles.*
FROM roles, rolememberships
WHERE roles. = rolememberships.role_id
	AND rolememberships.canvasser_id = :id
ORDER BY roles.

-- :name list-intentions-electors-by-visit :? :*
-- :doc lists all existing electors records related through intentions to a given visit
SELECT electors.*
FROM electors, intentions
WHERE electors.id = intentions.elector_id
	AND intentions.visit_id = :id
ORDER BY electors.id

-- :name list-issueexpertise-issues-by-followupmethod :? :*
-- :doc lists all existing issues records related through issueexpertise to a given followupmethod
SELECT issues.*
FROM issues, issueexpertise
WHERE issues.id = issueexpertise.issue_id
	AND issueexpertise.method_id = :id
ORDER BY issues.id

-- :name list-teams-by-district :? :*
-- :doc lists all existing team records related to a given district
SELECT *
FROM teams
WHERE teams.district_id = :id
ORDER BY teams.

-- :name list-issues :? :*
-- :doc lists all existing issue records
SELECT * FROM issues
ORDER BY issues.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-electors-by-addresse :? :*
-- :doc lists all existing elector records related to a given addresse
SELECT *
FROM electors
WHERE electors.address_id = :id
ORDER BY electors.id

-- :name list-visits-by-canvasser :? :*
-- :doc lists all existing visit records related to a given canvasser
SELECT *
FROM visits
WHERE visits.canvasser_id = :id
ORDER BY visits.id

-- :name list-electors :? :*
-- :doc lists all existing elector records
SELECT * FROM electors
ORDER BY electors.id
--~ (if (:offset params) "OFFSET :offset ")
--~ (if (:limit params) "LIMIT :limit" "LIMIT 100")

-- :name list-followuprequests-by-elector :? :*
-- :doc lists all existing followuprequest records related to a given elector
SELECT *
FROM followuprequests
WHERE followuprequests.elector_id = :id
ORDER BY followuprequests.id

-- :name update-followuprequest! :! :n
-- :doc updates an existing followuprequest record
UPDATE followuprequests
SET id = :id,
	elector_id = :elector_id,
	visit_id = :visit_id,
	issue_id = :issue_id,
	method_id = :method_id
WHERE followuprequests.id = :id


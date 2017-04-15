------------------------------------------------------------------------------;
----
---- youyesyet.routes.authenticated: routes and pages for authenticated users.
----
---- This program is free software; you can redistribute it and/or
---- modify it under the terms of the GNU General Public License
---- as published by the Free Software Foundation; either version 2
---- of the License, or (at your option) any later version.
----
---- This program is distributed in the hope that it will be useful,
---- but WITHOUT ANY WARRANTY; without even the implied warranty of
---- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
---- GNU General Public License for more details.
----
---- You should have received a copy of the GNU General Public License
---- along with this program; if not, write to the Free Software
---- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
---- USA.
----
---- Copyright (C) 2016 Simon Brooke for Radical Independence Campaign
----
------------------------------------------------------------------------------;

-- This file gets slurped in and converted into simple functions by the line
-- in youyesyet.db.core.clj:
--     (conman/bind-connection *db* "sql/queries.sql")
-- the functions then appeare in the youyesyet.db.core namespace.

-- :name create-address! :! :n
-- :doc creates a new address record
INSERT INTO addresses
(address, postcode, district_id, latitude, longitude)
VALUES (:address, :postcode, :district, :latitude, :longitude)

-- :name update-address! :! :n
-- :doc update an existing address record
UPDATE addresses
SET address = :address, postcode = :postcode, latitude = :latitude, longitude = :longitude
WHERE id = :id

-- :name get-address :? :1
-- :doc retrieve a address given the id.
SELECT * FROM addresses
WHERE id = :id

-- :name delete-address! :! :n
-- :doc delete a address given the id
DELETE FROM addresses
WHERE id = :id


-- :name create-authority! :! :n
-- :doc creates a new authority record
INSERT INTO authorities
(id)
VALUES (:id)

-- :name update-authority! :! :n
-- :doc update an existing authority record
UPDATE authorities
SET id = :id
WHERE id = :id

-- :name get-authority :? :1
-- :doc retrieve a authority given the id.
SELECT * FROM authorities
WHERE id = :id

-- :name delete-authority! :! :n
-- :doc delete a authority given the id
DELETE FROM authorities
WHERE id = :id


-- :name create-canvasser! :! :n
-- :doc creates a new canvasser record
INSERT INTO canvassers
(username, fullname, elector_id, address_id, phone, email, authority_id, authorised)
VALUES (:username, :fullname, :elector_id, :address_id, :phone, :email, :authority_id, :authorised)

-- :name update-canvasser! :! :n
-- :doc update an existing canvasser record
UPDATE canvassers
SET username = :username, fullname = :fullname, elector_id = :elector_id, address_id = :address_id, phone = :phone, email = :email, authority_id = :authority_id, authorised = :authorised
WHERE id = :id

-- :name get-canvasser :? :1
-- :doc retrieve a canvasser given the id.
SELECT * FROM canvassers
WHERE id = :id

-- :name delete-canvasser! :! :n
-- :doc delete a canvasser given the id
DELETE FROM canvassers
WHERE id = :id


-- :name create-district! :! :n
-- :doc creates a new district record
INSERT INTO districts
(id, name)
VALUES (:id, :name)

-- :name update-district! :! :n
-- :doc update an existing district record
UPDATE districts
SET name = :name
WHERE id = :id

-- :name get-district :? :1
-- :doc retrieve a district given the id.
SELECT * FROM districts
WHERE id = :id

-- :name delete-district! :! :n
-- :doc delete a district given the id
DELETE FROM districts
WHERE id = :id


-- :name create-elector! :! :n
-- :doc creates a new elector record
INSERT INTO electors
(name, address_id, phone, email)
VALUES (:name, :address_id, :phone, :email)

-- :name update-elector! :! :n
-- :doc update an existing elector record
UPDATE electors
SET name = :name, address_id = :address_id, phone = :phone, email = :email
WHERE id = :id

-- :name get-elector :? :1
-- :doc retrieve a elector given the id.
SELECT * FROM electors
WHERE id = :id

-- :name delete-elector! :! :n
-- :doc delete a elector given the id
DELETE FROM electors
WHERE id = :id


-- :name create-followupaction! :! :n
-- :doc creates a new followupaction record
INSERT INTO followupactions
(request_id, actor, date, notes, closed)
VALUES (:request_id, :actor, :date, :notes, :closed)

-- We don't update followup actions. They're permanent record.

-- :name get-followupaction :? :1
-- :doc retrieve a followupaction given the id.
SELECT * FROM followupactions
WHERE id = :id

-- We don't delete followup actions. They're permanent record.


-- followup methods are reference data, do not need to be programmatically maintained.


-- :name create-followuprequest! :! :n
-- :doc creates a new followupaction record
INSERT INTO followuprequests
(elector_id, visit_id, issue_id, method_id)
VALUES (:elector_id, :visit_id, :issue_id, :method_id)

-- We don't update followup requests. They're permanent record.

-- :name get-followuprequest :? :1
-- :doc retrieve a followupaction given the id.
SELECT * FROM followuprequests
WHERE id = :id

-- We don't delete followup requests. They're permanent record.


-- :name create-issueexpertise! :! :n
-- :doc creates a new issueexpertise record
INSERT INTO issueexpertise
(canvasser_id, issue_id, method_id)
VALUES (:canvasser_id, :issue_id, :method_id)

-- :name update-issueexpertise! :! :n
-- :doc update an existing issueexpertise record
UPDATE issueexpertise
SET canvasser_id = :canvasser_id, issue_id = :issue_id, method_id = :method_id
WHERE id = :id

-- :name get-issueexpertise :? :1
-- :doc retrieve a issueexpertise given the canvasser_id -
-- getting it by its own id is unlikely to be interesting or useful.
SELECT * FROM issueexpertise
WHERE canvasser_id = :canvasser_id

-- :name delete-issueexpertise! :! :n
-- :doc delete a issueexpertise given the id
DELETE FROM issueexpertise
WHERE id = :id


-- :name create-issue! :! :n
-- :doc creates a new issue record
INSERT INTO issues
(id, url, content, current)
VALUES (:id, :url, :content, :current)

-- :name update-issue! :! :n
-- :doc update an existing issue record
UPDATE issues
SET url = :url, content = :content, current = :current
WHERE id = :id

-- :name get-issue :? :1
-- :doc retrieve a issue given the id -
SELECT * FROM issues
WHERE id = :id

-- :name delete-issue! :! :n
-- :doc delete a issue given the id
DELETE FROM issues
WHERE id = :id


-- options is virtually reference data; it's not urgent to create a programmatic means of editing

-- :name create-visit! :! :n
-- :doc creates a new visit record
INSERT INTO visits
(address_id, canvasser_id)
VALUES (:address_id, :canvasser_id)

-- visits is audit data; we don't update it.

-- :name get-visit :? :1
-- :doc retrieve a visit given the id.
SELECT * FROM visits
WHERE id = :id

-- visits is audit data; we don't delete it.


-- views are select only

-- :name get-roles-by-canvasser :? :*
-- :doc Get the role names for the canvasser with the specified id
select name from roles_by_canvasser
  where canvasser = :canvasser

-- :name get-teams-by-canvasser :? :*
-- :doc Get details of the teams which the canvasser with the specified id is member of.
select * from teams_by_canvasser
  where canvasser = :canvasser_id

-- :name get-canvassers-by-team :? :*
-- :doc Get details of all canvassers who are members of the team with the specified id
select * from canvassers_by_team
  where team = :team_id

-- :name get-canvassers-by-team :? :*
-- :doc Get details of all authorised canvassers who are members of this team.
select * from canvassers_by_introducer
  where introducer = :introducer_id

-- :name get-teams_by_organiser :? :*
-- :doc Get details of all the teams organised by the canvasser with the specified id
select * from teams_by_organiser
  where organiser = :organiser_id

-- :name get-organisers-by-team :? :*
-- :doc Get details of all organisers of the team with the specified id
select * from organisers_by_team
  where team = :team_id


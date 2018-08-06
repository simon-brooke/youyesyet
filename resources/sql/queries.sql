------------------------------------------------------------------------------;
----
---- queries.sql: manually maintained queries.
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
--     (conman/bind-connection *db* "sql/queries-auto.sql" "sql/queries.sql")
-- the functions then appeare in the youyesyet.db.core namespace.
-- Note that queries generated by ADL are in the file
-- resources/sql/queries-auto.sql; they do not have to be (and should not be)
-- redefined here.

-- :name list-addresses-by-locality :? :*
-- :doc lists all existing address records in a given locality
SELECT *
FROM addresses
WHERE locality = :locality


-- :name list-open-requests :? :*
-- :doc lists all existing followuprequest records which have not been closed and which the :expert has expertise to answer.
SELECT DISTINCT request.*,
  electors.name ||', '|| electors.gender AS elector_id_expanded,
	addresses.address ||', '|| addresses.postcode ||', '|| visits.date AS visit_id_expanded,
  request.issue_id as issue_id_expanded,
	request.method_id AS method_id_expanded,
  visits.date
FROM followuprequests as request,
  ln_experts_issues_canvassers as expertise,
  canvassers as experts,
  electors,
  addresses,
  visits
where not exists (select * from followupactions as action
                  where action.request_id = request.id
                  and action.closed = true)
and request.elector_id = electors.id
and request.visit_id = visits.id
and visits.address_id = addresses.id
and request.issue_id = expertise.issue_id
and expertise.canvasser_id = :expert
ORDER BY visits.date desc

--:name get-last-visit-by-canvasser :? :1
--:doc returns the most recent visit record of the canvasser with the specified `:id`
SELECT * FROM visits
WHERE canvasser_id = :id
ORDER BY date desc
LIMIT 1

-- I don't know why this next one isn't autogenerating, but it isn't and it's critical.

-- :name list-roles-by-canvasser :? :*
-- :doc links all existing canvasser records related to a given role
SELECT roles.*
FROM roles, ln_canvassers_roles
WHERE roles.id = ln_canvassers_roles.role_id
	AND ln_canvassers_roles.canvasser_id = :id
ORDER BY roles.name,
	roles.id

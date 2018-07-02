------------------------------------------------------------------------
--	Database definition for application 
--	
--	youyesyet version 0.1.1 
--	
--	auto-generated by [Application Description Language framework] 
--	
--	(https://github.com/simon-brooke/adl) at 20180701T221532.672Z 
--	
--	
--	    A web-app intended to be used by canvassers campaigning for a 
--	'Yes' vote in the second independence referendum.
--	
--	    The web-app will be delivered to canvassers out knocking doors 
--	primarily through an HTML5/React single-page app designed to work on a 
--	mobile phone; it's possible that someone else may do an Android of 
--	iPhone native app to address the same back end but at present I have 
--	no plans for this.
--	
--	    There must also be an administrative interface through which 
--	privileged users can set the system up and authorise canvassers, and a 
--	'followup' interface through which issue-expert specialist canvassers 
--	can address particular electors' queries.
--	 
------------------------------------------------------------------------

------------------------------------------------------------------------
--	security group admin 
------------------------------------------------------------------------

CREATE GROUP admin;

------------------------------------------------------------------------
--	security group analysts 
------------------------------------------------------------------------

CREATE GROUP analysts;

------------------------------------------------------------------------
--	security group canvassers 
------------------------------------------------------------------------

CREATE GROUP canvassers;

------------------------------------------------------------------------
--	security group issueeditors 
------------------------------------------------------------------------

CREATE GROUP issueeditors;

------------------------------------------------------------------------
--	security group issueexperts 
------------------------------------------------------------------------

CREATE GROUP issueexperts;

------------------------------------------------------------------------
--	security group public 
------------------------------------------------------------------------

CREATE GROUP public;

------------------------------------------------------------------------
--	security group teamorganisers 
------------------------------------------------------------------------

CREATE GROUP teamorganisers;

------------------------------------------------------------------------
--	primary table addresses for entity addresses 
--	
--	Addresses of all buildings which contain dwellings. 
------------------------------------------------------------------------
CREATE TABLE addresses
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 address VARCHAR(256) NOT NULL,
	 postcode VARCHAR(16) CONSTRAINT pattern_14 CHECK (postcode ~* '^([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([AZa-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))[0-9][A-Za-z]{2})$'),
	 phone VARCHAR(16),
	 district_id INTEGER,
	 latitude DOUBLE PRECISION,
	 longitude DOUBLE PRECISION,
	 locality INTEGER
);
GRANT SELECT ON addresses TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON addresses TO admin ;
GRANT UPDATE ON addresses TO admin ;
GRANT DELETE ON addresses TO admin ;

------------------------------------------------------------------------
--	primary table authorities for entity authorities 
--	
--	Authorities which may authenticate canvassers to the system. 
------------------------------------------------------------------------
CREATE TABLE authorities
(
	 id VARCHAR(32) NOT NULL PRIMARY KEY,
	 request_token_uri VARCHAR(256) NOT NULL,
	 access_token_uri VARCHAR(256) NOT NULL,
	 authorize_uri VARCHAR(256) NOT NULL,
	 consumer_key VARCHAR(32) DEFAULT 'youyesyet' NOT NULL,
	 consumer_secret VARCHAR(256) NOT NULL
);
GRANT SELECT ON authorities TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON authorities TO admin ;
GRANT UPDATE ON authorities TO admin ;
GRANT DELETE ON authorities TO admin ;

------------------------------------------------------------------------
--	primary table canvassers for entity canvassers 
--	
--	Primary users of the system: those actually interviewing electors. 
------------------------------------------------------------------------
CREATE TABLE canvassers
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 username VARCHAR(32) NOT NULL,
	 fullname VARCHAR(64) NOT NULL,
	 elector_id INTEGER,
	 address_id INTEGER NOT NULL,
	 phone VARCHAR(16),
	 email VARCHAR(128),
	 authority_id VARCHAR(32) NOT NULL,
	 authorised BOOLEAN
);
GRANT SELECT ON canvassers TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON canvassers TO admin,
	canvassers,
	teamorganisers ;
GRANT UPDATE ON canvassers TO admin,
	canvassers,
	teamorganisers ;
GRANT DELETE ON canvassers TO admin ;

------------------------------------------------------------------------
--	primary table districts for entity districts 
--	
--	Electoral districts: TODO: Shape (polygon) information will need to be 
--	added, for use in maps. 
------------------------------------------------------------------------
CREATE TABLE districts
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 name VARCHAR(64) NOT NULL
);
GRANT SELECT ON districts TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	public,
	teamorganisers ;
GRANT INSERT ON districts TO admin ;
GRANT UPDATE ON districts TO admin ;
GRANT DELETE ON districts TO admin ;

------------------------------------------------------------------------
--	primary table dwellings for entity dwellings 
--	
--	All dwellings within addresses in the system; a dwelling is a
--	      house, flat or appartment in which electors live. Every address 
--	should have
--	      at least one dwelling; essentially, an address maps onto a 
--	street door and
--	      dwellings map onto what's behind that door. So a tenement or a 
--	block of flats
--	      would be one address with many dwellings.
--	 
------------------------------------------------------------------------
CREATE TABLE dwellings
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 address_id INTEGER NOT NULL,
	 sub_address VARCHAR(32)
);
GRANT SELECT ON dwellings TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON dwellings TO admin ;
GRANT UPDATE ON dwellings TO admin ;
GRANT DELETE ON dwellings TO admin ;

------------------------------------------------------------------------
--	primary table electors for entity electors 
--	
--	All electors known to the system; electors are people believed to be 
--	entitled to vote in the current campaign. 
------------------------------------------------------------------------
CREATE TABLE electors
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 name VARCHAR(64) NOT NULL,
	 dwelling_id INTEGER NOT NULL,
	 phone VARCHAR(16),
	 email VARCHAR(128),
	 gender VARCHAR(32) DEFAULT 'Unknown'
);
GRANT SELECT ON electors TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON electors TO admin ;
GRANT UPDATE ON electors TO admin ;
GRANT DELETE ON electors TO admin ;

------------------------------------------------------------------------
--	primary table followupactions for entity followupactions 
--	
--	Actions taken on followup requests. 
------------------------------------------------------------------------
CREATE TABLE followupactions
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 request_id INTEGER NOT NULL,
	 actor INTEGER NOT NULL,
	 date TIMESTAMP DEFAULT 'now()' NOT NULL,
	 notes TEXT,
	 closed BOOLEAN DEFAULT false
);
GRANT SELECT ON followupactions TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts ;
GRANT INSERT ON followupactions TO admin,
	issueexperts ;
GRANT UPDATE ON followupactions TO admin ;
GRANT DELETE ON followupactions TO admin ;

------------------------------------------------------------------------
--	primary table followupmethods for entity followupmethods 
------------------------------------------------------------------------
CREATE TABLE followupmethods
(
	 id VARCHAR(32) NOT NULL PRIMARY KEY
);
GRANT SELECT ON followupmethods TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON followupmethods TO admin ;
GRANT UPDATE ON followupmethods TO admin ;
GRANT DELETE ON followupmethods TO admin ;

------------------------------------------------------------------------
--	primary table followuprequests for entity followuprequests 
--	
--	Requests for a followup with an issue expert 
------------------------------------------------------------------------
CREATE TABLE followuprequests
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 elector_id INTEGER NOT NULL,
	 visit_id INTEGER NOT NULL,
	 issue_id VARCHAR(32) NOT NULL,
	 method_id VARCHAR(32) NOT NULL
);
GRANT SELECT ON followuprequests TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON followuprequests TO admin,
	canvassers ;
GRANT UPDATE ON followuprequests TO admin ;
GRANT DELETE ON followuprequests TO admin ;

------------------------------------------------------------------------
--	primary table genders for entity genders 
--	
--	All genders which may be assigned to electors. 
------------------------------------------------------------------------
CREATE TABLE genders
(
	 id VARCHAR(32) NOT NULL PRIMARY KEY
);
GRANT SELECT ON genders TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON genders TO admin ;
GRANT UPDATE ON genders TO admin ;
GRANT DELETE ON genders TO admin ;

------------------------------------------------------------------------
--	primary table intentions for entity intentions 
--	
--	Intentions of electors to vote for options elicited in visits. 
------------------------------------------------------------------------
CREATE TABLE intentions
(
	 Id SERIAL NOT NULL PRIMARY KEY,
	 visit_id INTEGER NOT NULL,
	 elector_id INTEGER,
	 option_id VARCHAR(32) NOT NULL,
	 locality INTEGER NOT NULL
);
GRANT SELECT ON intentions TO admin,
	analysts,
	canvassers ;
GRANT INSERT ON intentions TO admin,
	canvassers ;
GRANT UPDATE ON intentions TO admin ;
GRANT DELETE ON intentions TO admin ;

------------------------------------------------------------------------
--	primary table issues for entity issues 
--	
--	Issues believed to be of interest to electors, about which they may 
--	have questions. 
------------------------------------------------------------------------
CREATE TABLE issues
(
	 id VARCHAR(32) NOT NULL PRIMARY KEY,
	 url VARCHAR(256),
	 current BOOLEAN DEFAULT true
);
GRANT SELECT ON issues TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON issues TO admin,
	issueeditors ;
GRANT UPDATE ON issues TO admin,
	issueeditors ;
GRANT DELETE ON issues TO admin ;

------------------------------------------------------------------------
--	primary table options for entity options 
--	
--	Options in the election or referendum being canvassed on 
------------------------------------------------------------------------
CREATE TABLE options
(
	 id VARCHAR(32) NOT NULL PRIMARY KEY
);
GRANT SELECT ON options TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON options TO admin ;
GRANT UPDATE ON options TO admin ;
GRANT DELETE ON options TO admin ;

------------------------------------------------------------------------
--	primary table roles for entity roles 
--	
--	A role (essentially, the same as a group, but application layer rather 
--	than database layer) of which a user may be a member. 
------------------------------------------------------------------------
CREATE TABLE roles
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 name VARCHAR(64) NOT NULL
);
GRANT SELECT ON roles TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON roles TO admin ;
GRANT UPDATE ON roles TO admin ;
GRANT DELETE ON roles TO admin ;

------------------------------------------------------------------------
--	primary table teams for entity teams 
------------------------------------------------------------------------
CREATE TABLE teams
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 name VARCHAR(64) NOT NULL,
	 district_id INTEGER NOT NULL,
	 latitude DOUBLE PRECISION,
	 longitude DOUBLE PRECISION
);
GRANT SELECT ON teams TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON teams TO admin,
	teamorganisers ;
GRANT UPDATE ON teams TO admin,
	teamorganisers ;
GRANT DELETE ON teams TO admin ;

------------------------------------------------------------------------
--	primary table visits for entity visits 
--	
--	All visits made by canvassers to dwellings in which opinions were 
--	recorded. 
------------------------------------------------------------------------
CREATE TABLE visits
(
	 id SERIAL NOT NULL PRIMARY KEY,
	 address_id INTEGER NOT NULL,
	 canvasser_id INTEGER NOT NULL,
	 date TIMESTAMP DEFAULT 'now()' NOT NULL
);
GRANT SELECT ON visits TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON visits TO admin,
	canvassers,
	teamorganisers ;
GRANT UPDATE ON visits TO admin ;
GRANT DELETE ON visits TO admin ;

------------------------------------------------------------------------
--	convenience view lv_addresses of entity addresses for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_addresses AS
SELECT addresses.address,
	addresses.postcode,
	addresses.phone,
	districts.name AS district_id_expanded,
	addresses.district_id,
	addresses.latitude,
	addresses.longitude,
	addresses.locality,
	addresses.id
FROM addresses, districts
WHERE addresses.district_id = districts.id
;
GRANT SELECT ON lv_addresses TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_authorities of entity authorities for lists, et 
--	cetera 
------------------------------------------------------------------------
CREATE VIEW lv_authorities AS
SELECT authorities.request_token_uri,
	authorities.access_token_uri,
	authorities.authorize_uri,
	authorities.consumer_key,
	authorities.consumer_secret,
	authorities.id
FROM authorities
;
GRANT SELECT ON lv_authorities TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_canvassers of entity canvassers for lists, et 
--	cetera 
------------------------------------------------------------------------
CREATE VIEW lv_canvassers AS
SELECT canvassers.username,
	canvassers.fullname,
	electors.name ||', '|| electors.phone ||', '|| electors.email ||', '|| genders.id AS elector_id_expanded,
	canvassers.elector_id,
	addresses.address ||', '|| addresses.postcode AS address_id_expanded,
	canvassers.address_id,
	canvassers.phone,
	canvassers.email,
	authorities.id AS authority_id_expanded,
	canvassers.authority_id,
	canvassers.authorised,
	canvassers.id
FROM canvassers, authorities, addresses, genders, electors
WHERE canvassers.elector_id = electors.id
	AND canvassers.address_id = addresses.id
	AND canvassers.authority_id = authorities.id
;
GRANT SELECT ON lv_canvassers TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_districts of entity districts for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_districts AS
SELECT districts.name,
	districts.id
FROM districts
;
GRANT SELECT ON lv_districts TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	public,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_dwellings of entity dwellings for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_dwellings AS
SELECT addresses.address ||', '|| addresses.postcode AS address_id_expanded,
	dwellings.address_id,
	dwellings.sub_address,
	dwellings.id
FROM dwellings, addresses
WHERE dwellings.address_id = addresses.id
;
GRANT SELECT ON lv_dwellings TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_electors of entity electors for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_electors AS
SELECT electors.name,
	addresses.address ||', '|| addresses.postcode ||', '|| dwellings.sub_address AS dwelling_id_expanded,
	electors.dwelling_id,
	electors.phone,
	electors.email,
	genders.id AS gender_expanded,
	electors.gender,
	electors.id
FROM dwellings, addresses, genders, electors
WHERE electors.dwelling_id = dwellings.id
	AND electors.gender = genders.id
;
GRANT SELECT ON lv_electors TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_followupactions of entity followupactions for 
--	lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_followupactions AS
SELECT electors.name ||', '|| electors.phone ||', '|| electors.email ||', '|| genders.id ||', '|| addresses.address ||', '|| addresses.postcode ||', '|| visits.date ||', '|| issues.id AS request_id_expanded,
	followupactions.request_id,
	canvassers.username ||', '|| canvassers.fullname ||', '|| addresses.address ||', '|| addresses.postcode ||', '|| canvassers.phone ||', '|| canvassers.email AS actor_expanded,
	followupactions.actor,
	followupactions.date,
	followupactions.notes,
	followupactions.closed,
	followupactions.id
FROM followuprequests, visits, canvassers, addresses, followupactions, genders, issues, electors
WHERE followupactions.request_id = followuprequests.id
	AND followupactions.actor = canvassers.id
;
GRANT SELECT ON lv_followupactions TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts ;

------------------------------------------------------------------------
--	convenience view lv_followupmethods of entity followupmethods for 
--	lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_followupmethods AS
SELECT followupmethods.id
FROM followupmethods
;
GRANT SELECT ON lv_followupmethods TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_followuprequests of entity followuprequests for 
--	lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_followuprequests AS
SELECT electors.name ||', '|| electors.phone ||', '|| electors.email ||', '|| genders.id AS elector_id_expanded,
	followuprequests.elector_id,
	addresses.address ||', '|| addresses.postcode ||', '|| visits.date AS visit_id_expanded,
	followuprequests.visit_id,
	issues.id AS issue_id_expanded,
	followuprequests.issue_id,
	followupmethods.id AS method_id_expanded,
	followuprequests.method_id,
	followuprequests.id
FROM followuprequests, visits, addresses, genders, issues, electors, followupmethods
WHERE followuprequests.elector_id = electors.id
	AND followuprequests.visit_id = visits.id
	AND followuprequests.issue_id = issues.id
	AND followuprequests.method_id = followupmethods.id
;
GRANT SELECT ON lv_followuprequests TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_genders of entity genders for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_genders AS
SELECT genders.id
FROM genders
;
GRANT SELECT ON lv_genders TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_intentions of entity intentions for lists, et 
--	cetera 
------------------------------------------------------------------------
CREATE VIEW lv_intentions AS
SELECT addresses.address ||', '|| addresses.postcode ||', '|| visits.date AS visit_id_expanded,
	intentions.visit_id,
	electors.name ||', '|| electors.phone ||', '|| electors.email ||', '|| genders.id AS elector_id_expanded,
	intentions.elector_id,
	options.id AS option_id_expanded,
	intentions.option_id,
	intentions.locality,
	intentions.Id
FROM visits, intentions, addresses, genders, electors, options
WHERE intentions.visit_id = visits.id
	AND intentions.elector_id = electors.id
	AND intentions.option_id = options.id
;
GRANT SELECT ON lv_intentions TO admin,
	analysts,
	canvassers ;

------------------------------------------------------------------------
--	convenience view lv_issues of entity issues for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_issues AS
SELECT issues.url,
	issues.current,
	issues.id
FROM issues
;
GRANT SELECT ON lv_issues TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_options of entity options for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_options AS
SELECT options.id
FROM options
;
GRANT SELECT ON lv_options TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_roles of entity roles for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_roles AS
SELECT roles.name,
	roles.id
FROM roles
;
GRANT SELECT ON lv_roles TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_teams of entity teams for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_teams AS
SELECT teams.name,
	districts.name AS district_id_expanded,
	teams.district_id,
	teams.latitude,
	teams.longitude,
	teams.id
FROM teams, districts
WHERE teams.district_id = districts.id
;
GRANT SELECT ON lv_teams TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	convenience view lv_visits of entity visits for lists, et cetera 
------------------------------------------------------------------------
CREATE VIEW lv_visits AS
SELECT addresses.address ||', '|| addresses.postcode AS address_id_expanded,
	visits.address_id,
	canvassers.username ||', '|| canvassers.fullname ||', '|| addresses.address ||', '|| addresses.postcode ||', '|| canvassers.phone ||', '|| canvassers.email AS canvasser_id_expanded,
	visits.canvasser_id,
	visits.date,
	visits.id
FROM visits, canvassers, addresses
WHERE visits.address_id = addresses.id
	AND visits.canvasser_id = canvassers.id
;
GRANT SELECT ON lv_visits TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;

------------------------------------------------------------------------
--	referential integrity links for primary tables 
------------------------------------------------------------------------

ALTER TABLE addresses ADD CONSTRAINT ri_addresses_districts_district_id 
	FOREIGN KEY( district_id ) 
	REFERENCES districts(id) 
	ON DELETE NO ACTION ;

ALTER TABLE canvassers ADD CONSTRAINT ri_canvassers_addresses_address_id 
	FOREIGN KEY( address_id ) 
	REFERENCES addresses(id) 
	ON DELETE NO ACTION ;

ALTER TABLE canvassers ADD CONSTRAINT ri_canvassers_authorities_authority_id 
	FOREIGN KEY( authority_id ) 
	REFERENCES authorities(id) 
	ON DELETE NO ACTION ;

ALTER TABLE canvassers ADD CONSTRAINT ri_canvassers_electors_elector_id 
	FOREIGN KEY( elector_id ) 
	REFERENCES electors(id) 
	ON DELETE NO ACTION ;

ALTER TABLE dwellings ADD CONSTRAINT ri_dwellings_addresses_address_id 
	FOREIGN KEY( address_id ) 
	REFERENCES addresses(id) 
	ON DELETE NO ACTION ;

ALTER TABLE electors ADD CONSTRAINT ri_electors_dwellings_dwelling_id 
	FOREIGN KEY( dwelling_id ) 
	REFERENCES dwellings(id) 
	ON DELETE NO ACTION ;

ALTER TABLE electors ADD CONSTRAINT ri_electors_genders_gender 
	FOREIGN KEY( gender ) 
	REFERENCES genders(id) 
	ON DELETE NO ACTION ;

ALTER TABLE followupactions ADD CONSTRAINT ri_followupactions_canvassers_actor 
	FOREIGN KEY( actor ) 
	REFERENCES canvassers(id) 
	ON DELETE NO ACTION ;

ALTER TABLE followupactions ADD CONSTRAINT ri_followupactions_followuprequests_request_id 
	FOREIGN KEY( request_id ) 
	REFERENCES followuprequests(id) 
	ON DELETE NO ACTION ;

ALTER TABLE followuprequests ADD CONSTRAINT ri_followuprequests_electors_elector_id 
	FOREIGN KEY( elector_id ) 
	REFERENCES electors(id) 
	ON DELETE NO ACTION ;

ALTER TABLE followuprequests ADD CONSTRAINT ri_followuprequests_issues_issue_id 
	FOREIGN KEY( issue_id ) 
	REFERENCES issues(id) 
	ON DELETE NO ACTION ;

ALTER TABLE followuprequests ADD CONSTRAINT ri_followuprequests_followupmethods_method_id 
	FOREIGN KEY( method_id ) 
	REFERENCES followupmethods(id) 
	ON DELETE NO ACTION ;

ALTER TABLE followuprequests ADD CONSTRAINT ri_followuprequests_visits_visit_id 
	FOREIGN KEY( visit_id ) 
	REFERENCES visits(id) 
	ON DELETE NO ACTION ;

ALTER TABLE intentions ADD CONSTRAINT ri_intentions_electors_elector_id 
	FOREIGN KEY( elector_id ) 
	REFERENCES electors(id) 
	ON DELETE NO ACTION ;

ALTER TABLE intentions ADD CONSTRAINT ri_intentions_options_option_id 
	FOREIGN KEY( option_id ) 
	REFERENCES options(id) 
	ON DELETE NO ACTION ;

ALTER TABLE intentions ADD CONSTRAINT ri_intentions_visits_visit_id 
	FOREIGN KEY( visit_id ) 
	REFERENCES visits(id) 
	ON DELETE NO ACTION ;

ALTER TABLE teams ADD CONSTRAINT ri_teams_districts_district_id 
	FOREIGN KEY( district_id ) 
	REFERENCES districts(id) 
	ON DELETE NO ACTION ;

ALTER TABLE visits ADD CONSTRAINT ri_visits_addresses_address_id 
	FOREIGN KEY( address_id ) 
	REFERENCES addresses(id) 
	ON DELETE NO ACTION ;

ALTER TABLE visits ADD CONSTRAINT ri_visits_canvassers_canvasser_id 
	FOREIGN KEY( canvasser_id ) 
	REFERENCES canvassers(id) 
	ON DELETE NO ACTION ;

------------------------------------------------------------------------
--	link table joining canvassers with roles 
------------------------------------------------------------------------
CREATE TABLE ln_canvassers_roles
(
	 canvasser_id INTEGER,
	 role_id INTEGER
);
GRANT SELECT ON ln_canvassers_roles TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON ln_canvassers_roles TO admin,
	canvassers,
	teamorganisers ;
GRANT UPDATE ON ln_canvassers_roles TO admin,
	canvassers,
	teamorganisers ;
GRANT DELETE ON ln_canvassers_roles TO admin ;

ALTER TABLE ln_canvassers_roles ADD CONSTRAINT ri_ln_canvassers_roles_canvassers_canvasser_id 
	FOREIGN KEY( canvasser_id ) 
	REFERENCES canvassers(id) 
	ON DELETE NO ACTION ;

ALTER TABLE ln_canvassers_roles ADD CONSTRAINT ri_ln_canvassers_roles_roles_role_id 
	FOREIGN KEY( role_id ) 
	REFERENCES roles(id) 
	ON DELETE NO ACTION ;



------------------------------------------------------------------------
--	link table joining teams with canvassers 
------------------------------------------------------------------------
CREATE TABLE ln_canvassers_teams
(
	 team_id INTEGER,
	 canvasser_id INTEGER
);
GRANT SELECT ON ln_canvassers_teams TO admin,
	analysts,
	canvassers,
	issueeditors,
	issueexperts,
	teamorganisers ;
GRANT INSERT ON ln_canvassers_teams TO admin,
	teamorganisers ;
GRANT UPDATE ON ln_canvassers_teams TO admin,
	teamorganisers ;
GRANT DELETE ON ln_canvassers_teams TO admin ;

ALTER TABLE ln_canvassers_teams ADD CONSTRAINT ri_ln_canvassers_teams_canvassers_canvasser_id 
	FOREIGN KEY( canvasser_id ) 
	REFERENCES canvassers(id) 
	ON DELETE NO ACTION ;

ALTER TABLE ln_canvassers_teams ADD CONSTRAINT ri_ln_canvassers_teams_teams_team_id 
	FOREIGN KEY( team_id ) 
	REFERENCES teams(id) 
	ON DELETE NO ACTION ;


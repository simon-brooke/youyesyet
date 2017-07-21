--------------------------------------------------------------------------------
----
---- 20170721084900.up.sql: add dwellings table, to deal with flatted addresses.
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
---- Copyright (C) 2017 Simon Brooke for Radical Independence Campaign
----
--------------------------------------------------------------------------------
----
---- NOTE
---- This file is essentially a Postgres schema dump of a database schema which was
---- created with the function initdb! in the file src/clj/youyesyet/db/schema.clj.
---- This file has then been mildly massaged to work with Migratus.
---- Either this file or src/clj/youyesyet/db/schema.clj is redundant; schema.clj
---- represents the older, Korma, way of doing things but does not readily allow
---- for migrations; this file represents the newer Migratus/HugSQL way. I'm not
---- certain which of these paths I'm going to go down.
----
--------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS dwellings (
    id serial NOT NULL primary key,
    address_id integer NOT NULL references addresses(id),
    sub_address varchar(16)
);
--;;

ALTER TABLE public.dwellings OWNER TO youyesyet;
--;;

INSERT INTO dwellings (address_id, sub_address)
  SELECT DISTINCT id, '' FROM addresses;
--;;

alter table canvassers add column dwelling_id integer references dwellings(id);
--;;
alter table electors add column dwelling_id integer references dwellings(id);
--;;
alter table visits add column dwelling_id integer references dwellings(id);
--;;

update canvassers set dwelling_id =
  (select id from dwellings where address_id = canvassers.address_id);
--;;

update electors set dwelling_id =
  (select id from dwellings where address_id = electors.address_id);
--;;

update visits set dwelling_id =
  (select id from dwellings where address_id = visits.address_id);
--;;

alter table canvassers alter column dwelling_id set not null;
--;;
alter table electors alter column dwelling_id set not null;
--;;
alter table visits alter column dwelling_id set not null;
--;;

alter table canvassers drop constraint canvassers_address_id_fkey;
--;;
alter table electors drop constraint electors_address_id_fkey;
--;;
alter table visits drop constraint visits_address_id_fkey;
--;;

alter table canvassers drop column address_id;
--;;
alter table electors drop column address_id;
--;;
alter table visits drop column address_id;
--;;

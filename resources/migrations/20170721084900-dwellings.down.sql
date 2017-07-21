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

alter table canvassers add column address_id integer references addresses(id);
--;;
alter table electors add column address_id integer references addresses(id);
--;;
alter table visits add column address_id integer references addresses(id);
--;;

update canvassers set address_id =
  (select address_id from dwellings where id = canvassers.dwelling_id);
--;;

update electors set address_id =
  (select address_id from dwellings where id = electors.dwelling_id);
--;;

update visits set address_id =
  (select address_id from dwellings where id = visits.dwelling_id);
--;;

alter table canvassers alter column address_id set not null;
--;;
alter table electors alter column address_id set not null;
--;;
alter table visits alter column address_id set not null;
--;;

alter table canvassers drop column dwelling_id;
--;;
alter table electors drop column dwelling_id;
--;;
alter table visits drop column dwelling_id;
--;;

drop table if exists dwellings;
--;;

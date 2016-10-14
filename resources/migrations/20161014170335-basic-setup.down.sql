--------------------------------------------------------------------------------
----
---- 20161014170335-basic-setup.down.sql: database schema for youyesyet.
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
--------------------------------------------------------------------------------

-- intended to reverse out the database changes made in
-- 20161014170335-basic-setup.up.sql

drop table addresses cascade;
--;;
drop table authorities cascade;
--;;
drop table canvassers cascade;
--;;
drop table districts cascade;
--;;
drop table electors cascade;
--;;
drop table followupactions cascade;
--;;
drop table followupmethods cascade;
--;;
drop table followuprequests cascade;
--;;
drop table issueexpertise cascade;
--;;
drop table issues cascade;
--;;
drop table options cascade;
--;;
drop table visits cascade;
--;;

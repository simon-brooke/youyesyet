--------------------------------------------------------------------------------
----
---- 20180316110100intentions-and-options.up.sql: add intentions and options
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

CREATE TABLE IF NOT EXISTS intentions (
  visit_id int not null references visits(id) on delete no action,
  elector_id int not null references electors(id) on delete no action,
  option_id varchar(32) not null references options(id) on delete no action
  );

ALTER TABLE intentions owner to youyesyet;

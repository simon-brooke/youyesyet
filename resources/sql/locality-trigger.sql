------------------------------------------------------------------------------;
----
---- locality-trigger.sql: compute localities for addresses
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

---- See also: src/cljc/locality.cljc

CREATE FUNCTION compute_locality() RETURNS trigger AS $compute_locality$
    BEGIN
        NEW.locality = (10000 * floor (NEW.latitude * 1000)) -
                        (NEW.longitude * 1000);
        RETURN NEW;
    END;
$compute_locality$ LANGUAGE plpgsql;

CREATE TRIGGER compute_locality BEFORE INSERT OR UPDATE ON addresses
    FOR EACH ROW EXECUTE PROCEDURE compute_locality();

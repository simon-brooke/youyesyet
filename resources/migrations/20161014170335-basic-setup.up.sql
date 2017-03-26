--------------------------------------------------------------------------------
----
---- 20161014170335-basic-setup.up.sql: database schema for youyesyet.
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

SET statement_timeout = 0;
--;;
SET lock_timeout = 0;
--;;
SET client_encoding = 'UTF8';
--;;
SET standard_conforming_strings = on;
--;;
SET check_function_bodies = false;
--;;
SET client_min_messages = warning;
--;;
--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
--;;

--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
--;;

SET search_path = public, pg_catalog;
--;;
SET default_tablespace = '';
--;;
SET default_with_oids = false;
--;;
--
-- Name: addresses; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS addresses (
    id integer NOT NULL,
    address character varying(256) NOT NULL,
    postcode character varying(16),
    phone character varying(16),
    district_id integer,
    latitude real,
    longitude real
);
--;;

ALTER TABLE public.addresses OWNER TO youyesyet;
--;;
--
-- Name: addresses_id_seq; Type: SEQUENCE; Schema: public; Owner: youyesyet
--

CREATE SEQUENCE addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--;;

ALTER TABLE public.addresses_id_seq OWNER TO youyesyet;
--;;
--
-- Name: addresses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: youyesyet
--

ALTER SEQUENCE addresses_id_seq OWNED BY addresses.id;
--;;

--
-- Name: authorities; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS authorities (
    id character varying(32) NOT NULL
);
--;;

ALTER TABLE public.authorities OWNER TO youyesyet;
--;;
--
-- Name: canvassers; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS canvassers (
    id serial,
    username character varying(32) NOT NULL,
    fullname character varying(64) NOT NULL,
    elector_id integer,
    address_id integer NOT NULL,
    phone character varying(16),
    email character varying(128),
    authority_id character varying(32) NOT NULL,
    authorised boolean
);
--;;

ALTER TABLE public.canvassers OWNER TO youyesyet;
--;;
--
-- Name: districts; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS districts (
    id integer NOT NULL,
    name character varying(64) NOT NULL
);
--;;

ALTER TABLE public.districts OWNER TO youyesyet;
--;;
--
-- Name: electors; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS electors (
    id integer NOT NULL,
    name character varying(64) NOT NULL,
    address_id integer NOT NULL,
    phone character varying(16),
    email character varying(128)
);
--;;

ALTER TABLE public.electors OWNER TO youyesyet;
--;;
--
-- Name: followupactions; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS followupactions (
    id integer NOT NULL,
    request_id integer NOT NULL,
    actor integer NOT NULL,
    date timestamp with time zone DEFAULT now() NOT NULL,
    notes text,
    closed boolean
);
--;;

ALTER TABLE public.followupactions OWNER TO youyesyet;

--
-- Name: followupactions_id_seq; Type: SEQUENCE; Schema: public; Owner: youyesyet
--

CREATE SEQUENCE followupactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--;;

ALTER TABLE public.followupactions_id_seq OWNER TO youyesyet;
--;;
--
-- Name: followupactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: youyesyet
--

ALTER SEQUENCE followupactions_id_seq OWNED BY followupactions.id;
--;;

--
-- Name: followupmethods; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS followupmethods (
    id character varying(32) NOT NULL
);
--;;

ALTER TABLE public.followupmethods OWNER TO youyesyet;
--;;
--
-- Name: followuprequests; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

insert into followupmethods values ('Telephone');
--;;
insert into followupmethods values ('eMail');
--;;
insert into followupmethods values ('Post');
--;;


CREATE TABLE IF NOT EXISTS followuprequests (
    id integer NOT NULL,
    elector_id integer NOT NULL,
    visit_id integer NOT NULL,
    issue_id character varying(32) NOT NULL,
    method_id character varying(32) NOT NULL
);
--;;

ALTER TABLE public.followuprequests OWNER TO youyesyet;
--;;

--
-- Name: followuprequests_id_seq; Type: SEQUENCE; Schema: public; Owner: youyesyet
--

CREATE SEQUENCE followuprequests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--;;


ALTER TABLE public.followuprequests_id_seq OWNER TO youyesyet;
--;;

--
-- Name: followuprequests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: youyesyet
--

ALTER SEQUENCE followuprequests_id_seq OWNED BY followuprequests.id;
--;;


--
-- Name: issueexpertise; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS issueexpertise (
    canvasser_id integer NOT NULL,
    issue_id character varying(32) NOT NULL,
    method_id character varying(32) NOT NULL
);
--;;


ALTER TABLE public.issueexpertise OWNER TO youyesyet;
--;;

--
-- Name: issues; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS issues (
    id character varying(32) NOT NULL,
    url character varying(256)
);
--;;


ALTER TABLE public.issues OWNER TO youyesyet;
--;;

--
-- Name: options; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS options (
    id character varying(32) NOT NULL
);
--;;


ALTER TABLE public.options OWNER TO youyesyet;
--;;

--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS schema_migrations (
    id bigint NOT NULL
);
--;;


ALTER TABLE public.schema_migrations OWNER TO youyesyet;
--;;

--
-- Name: visits; Type: TABLE; Schema: public; Owner: youyesyet; Tablespace:
--

CREATE TABLE IF NOT EXISTS visits (
    id integer NOT NULL,
    address_id integer NOT NULL,
    canvasser_id integer NOT NULL,
    date timestamp with time zone DEFAULT now() NOT NULL
);
--;;


ALTER TABLE public.visits OWNER TO youyesyet;
--;;

--
-- Name: visits_id_seq; Type: SEQUENCE; Schema: public; Owner: youyesyet
--

CREATE SEQUENCE visits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--;;


ALTER TABLE public.visits_id_seq OWNER TO youyesyet;
--;;

--
-- Name: visits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: youyesyet
--

ALTER SEQUENCE visits_id_seq OWNED BY visits.id;
--;;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY addresses ALTER COLUMN id SET DEFAULT nextval('addresses_id_seq'::regclass);
--;;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followupactions ALTER COLUMN id SET DEFAULT nextval('followupactions_id_seq'::regclass);
--;;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followuprequests ALTER COLUMN id SET DEFAULT nextval('followuprequests_id_seq'::regclass);
--;;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY visits ALTER COLUMN id SET DEFAULT nextval('visits_id_seq'::regclass);
--;;


--
-- Name: addresses_address_key; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY addresses
    ADD CONSTRAINT addresses_address_key UNIQUE (address);
--;;


--
-- Name: addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);
--;;


--
-- Name: authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY authorities
    ADD CONSTRAINT authorities_pkey PRIMARY KEY (id);
--;;


--
-- Name: canvassers_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY canvassers
    ADD CONSTRAINT canvassers_pkey PRIMARY KEY (id);
--;;


--
-- Name: districts_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY districts
    ADD CONSTRAINT districts_pkey PRIMARY KEY (id);
--;;


--
-- Name: electors_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY electors
    ADD CONSTRAINT electors_pkey PRIMARY KEY (id);
--;;


--
-- Name: followupactions_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY followupactions
    ADD CONSTRAINT followupactions_pkey PRIMARY KEY (id);
--;;


--
-- Name: followupmethods_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY followupmethods
    ADD CONSTRAINT followupmethods_pkey PRIMARY KEY (id);
--;;


--
-- Name: followuprequests_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY followuprequests
    ADD CONSTRAINT followuprequests_pkey PRIMARY KEY (id);
--;;


--
-- Name: issues_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY issues
    ADD CONSTRAINT issues_pkey PRIMARY KEY (id);
--;;


--
-- Name: options_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY options
    ADD CONSTRAINT options_pkey PRIMARY KEY (id);
--;;


--
-- Name: schema_migrations_id_key; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

--ALTER TABLE ONLY schema_migrations
--    ADD CONSTRAINT schema_migrations_id_key UNIQUE (id);
--;;


--
-- Name: visits_pkey; Type: CONSTRAINT; Schema: public; Owner: youyesyet; Tablespace:
--

ALTER TABLE ONLY visits
    ADD CONSTRAINT visits_pkey PRIMARY KEY (id);
--;;


--
-- Name: addresses_district_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY addresses
    ADD CONSTRAINT addresses_district_id_fkey FOREIGN KEY (district_id) REFERENCES districts(id);
--;;


--
-- Name: canvassers_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY canvassers
    ADD CONSTRAINT canvassers_address_id_fkey FOREIGN KEY (address_id) REFERENCES addresses(id);
--;;


--
-- Name: canvassers_authority_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY canvassers
    ADD CONSTRAINT canvassers_authority_id_fkey FOREIGN KEY (authority_id) REFERENCES authorities(id);
--;;


--
-- Name: canvassers_elector_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY canvassers
    ADD CONSTRAINT canvassers_elector_id_fkey FOREIGN KEY (elector_id) REFERENCES electors(id);
--;;


--
-- Name: electors_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY electors
    ADD CONSTRAINT electors_address_id_fkey FOREIGN KEY (address_id) REFERENCES addresses(id);
--;;


--
-- Name: followupactions_actor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followupactions
    ADD CONSTRAINT followupactions_actor_fkey FOREIGN KEY (actor) REFERENCES canvassers(id);
--;;


--
-- Name: followupactions_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followupactions
    ADD CONSTRAINT followupactions_request_id_fkey FOREIGN KEY (request_id) REFERENCES followuprequests(id);
--;;


--
-- Name: followuprequests_elector_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followuprequests
    ADD CONSTRAINT followuprequests_elector_id_fkey FOREIGN KEY (elector_id) REFERENCES electors(id);
--;;


--
-- Name: followuprequests_issue_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followuprequests
    ADD CONSTRAINT followuprequests_issue_id_fkey FOREIGN KEY (issue_id) REFERENCES issues(id);
--;;


--
-- Name: followuprequests_method_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followuprequests
    ADD CONSTRAINT followuprequests_method_id_fkey FOREIGN KEY (method_id) REFERENCES followupmethods(id);
--;;


--
-- Name: followuprequests_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY followuprequests
    ADD CONSTRAINT followuprequests_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES visits(id);
--;;


--
-- Name: issueexpertise_canvasser_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY issueexpertise
    ADD CONSTRAINT issueexpertise_canvasser_id_fkey FOREIGN KEY (canvasser_id) REFERENCES canvassers(id);
--;;


--
-- Name: issueexpertise_issue_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY issueexpertise
    ADD CONSTRAINT issueexpertise_issue_id_fkey FOREIGN KEY (issue_id) REFERENCES issues(id);
--;;


--
-- Name: issueexpertise_method_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY issueexpertise
    ADD CONSTRAINT issueexpertise_method_id_fkey FOREIGN KEY (method_id) REFERENCES followupmethods(id);
--;;


--
-- Name: visits_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY visits
    ADD CONSTRAINT visits_address_id_fkey FOREIGN KEY (address_id) REFERENCES addresses(id);
--;;


--
-- Name: visits_canvasser_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: youyesyet
--

ALTER TABLE ONLY visits
    ADD CONSTRAINT visits_canvasser_id_fkey FOREIGN KEY (canvasser_id) REFERENCES canvassers(id);
--;;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
--;;

REVOKE ALL ON SCHEMA public FROM postgres;
--;;

GRANT ALL ON SCHEMA public TO postgres;
--;;

GRANT ALL ON SCHEMA public TO PUBLIC;
--;;


--
-- PostgreSQL database dump complete
--


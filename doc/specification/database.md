# Database Specification

Note that this is a work in progress. Read it in concert with the Entity-Relationship Diagram.

Tables are listed in alphabetical order.

## Address

The postal address of a dwelling at which electors are registered.

    CREATE TABLE IF NOT EXISTS addresses (
        id integer NOT NULL,
        address character varying(256) NOT NULL,
        postcode character varying(16),
        phone character varying(16),
        district_id integer,
        latitude real,
        longitude real
    );

## Authority

An *oauth* authority which authenticates canvassers. *Note that* there will need to be substantially more in this table but I don't yet know what.

    CREATE TABLE IF NOT EXISTS authorities (
        id character varying(32) NOT NULL
    );


## Canvasser

A user of the system.

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


## District

An electoral district.

    CREATE TABLE IF NOT EXISTS districts (
        id integer NOT NULL,
        name character varying(64) NOT NULL
    );


## Elector

Someone entitled to cast a vote in the referendum.

    CREATE TABLE IF NOT EXISTS electors (
        id integer NOT NULL,
        name character varying(64) NOT NULL,
        address_id integer NOT NULL,
        phone character varying(16),
        email character varying(128)
    );

## Followup Action

An action performed by an issue expert in response to a followup request.

    CREATE TABLE IF NOT EXISTS followupactions (
        id integer NOT NULL,
        request_id integer NOT NULL,
        actor integer NOT NULL,
        date timestamp with time zone DEFAULT now() NOT NULL,
        notes text,
        closed boolean
    );

## Followup Method

A method for responding to a followup request; reference data.

    CREATE TABLE IF NOT EXISTS followupmethods (
        id character varying(32) NOT NULL
    );

    insert into followupmethods values ('Telephone');
    insert into followupmethods values ('eMail');
    insert into followupmethods values ('Post');

## Followup Request

A request recorded by a canvasser for an issue expert to contact an elector with regard to a particular issue.

    CREATE TABLE IF NOT EXISTS followuprequests (
        id integer NOT NULL,
        elector_id integer NOT NULL,
        visit_id integer NOT NULL,
        issue_id character varying(32) NOT NULL,
        method_id character varying(32) NOT NULL
    );

## Intention

An intention, by an elector, to vote for an option; captured by a canvasser during a visit.

    CREATE TABLE IF NOT EXISTS intentions (
        id serial not null,
        elector integer not null references elector(id),
        option varchar(32) not null references option(id),
        visit integer not null references visit(id),
        date timestamp with time zone DEFAULT now() NOT NULL
    );


## Issue

An issue which might affect electors' decisions regarding their intention.

    CREATE TABLE IF NOT EXISTS issues (
        id character varying(32) NOT NULL,
        url character varying(256),
        content varchar(1024),
        current default false
    );


## Issue expertise

Expertise of a canvasser able to use a method, in an issue.

    CREATE TABLE IF NOT EXISTS issueexpertise (
        canvasser_id integer NOT NULL,
        issue_id character varying(32) NOT NULL,
        method_id character varying(32) NOT NULL
    );


## Option

An option for which an elector may have an intention to vote.

    CREATE TABLE IF NOT EXISTS options (
        id character varying(32) NOT NULL
    );


## Role

A role (other than basic *Canvasser*) that a user may have in the system. Reference data.

    create table if not exists roles (
      id serial primary key,
      name varchar(64) not null
    );


## Role Member

Membership of a user (*Canvasser*) of an additional role; link table.

    create table if not exists rolememberships (
      role_id integer not null references roles(id),
      canvasser_id integer not null references canvassers(id)
    );


## Team

A team of canvassers in a locality who are known to one another and frequently
canvas together.

    create table if not exists teams (
      id serial primary key,
      name varchar(64) not null,
      district_id integer not null references districts(id),
      latitude real,
      longitude real
    );


## Team Member

Membership of a user (*Canvasser*) of a particular team. Canvassers may join multiple teams. Link table.

    create table if not exists teammemberships (
      team_id integer not null references teams(id),
      canvasser_id integer not null references canvassers(id)
    );


## Team Organiser

A relationship which defines a user (*Canvasser*) as an organiser of a team. A team may
have more than one organiser. An organiser (if they also have the role 'Recruiter', which
they often will have) may recruit additional Canvassers as members of their team, or
accept applications by canvassers to join their team. An organiser may promote a member of
the team to organiser of the team, and may also exclude a member from the team.

    create table if not exists teamorganiserships (
      team_id integer not null references teams(id),
      canvasser_id integer not null references canvassers(id)
    );


## Visit

A visit by a canvasser to an address on a date to solicit intentions from electors.

    CREATE TABLE IF NOT EXISTS visits (
        id integer NOT NULL,
        address_id integer NOT NULL,
        canvasser_id integer NOT NULL,
        date timestamp with time zone DEFAULT now() NOT NULL
    );

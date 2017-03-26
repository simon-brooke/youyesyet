create table if not exists roles (
  id serial primary key,
  name varchar(64) not null
);

create unique index ix_roles_name on roles(name);

create table if not exists rolememberships (
  role_id integer not null references roles(id),
  canvasser_id integer not null references canvassers(id)
);

create table if not exists teams (
  id serial primary key,
  name varchar(64) not null,
  district_id integer not null references districts(id),
  latitude real,
  longitude real
);

create unique index ix_teams_name on teams(name);

create table if not exists teammemberships (
  team_id integer not null references teams(id),
  canvasser_id integer not null references canvassers(id)
);

create table if not exists teamorganiserships (
  team_id integer not null references teams(id),
  canvasser_id integer not null references canvassers(id)
);

alter table roles owner to youyesyet;

alter table rolememberships owner to youyesyet;

alter table teams owner to youyesyet;

alter table teammemberships owner to youyesyet;

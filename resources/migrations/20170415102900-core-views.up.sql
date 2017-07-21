
create view roles_by_canvasser as
  select canvassers.id as canvasser, roles.name
    from roles, rolememberships, canvassers
    where roles.id = rolememberships.role_id
      and canvassers.id = rolememberships.canvasser_id
      and canvassers.authorised = true;

create view teams_by_canvasser as
  select canvassers.id as canvasser, teams.id, teams.name, teams.latitude, teams.longitude
    from teams, teammemberships, canvassers
    where teams.id = teammemberships.team_id
      and canvassers.id = teammemberships.canvasser_id;

create view canvassers_by_team as
  select teams.id as team,
        canvassers.id,
        canvassers.username,
        canvassers.fullname,
        canvassers.email,
        canvassers.phone
    from teams, teammemberships, canvassers
    where teams.id = teammemberships.team_id
      and canvassers.id = teammemberships.canvasser_id
      and canvassers.authorised = true;

create view canvassers_by_introducer as
  select introducers.id as introducer,
        canvassers.id as canvasser,
        canvassers.username,
        canvassers.fullname,
        canvassers.email,
        canvassers.phone,
        canvassers.authorised
    from canvassers, canvassers as introducers
    where introducers.id = canvassers.introduced_by;

create view teams_by_organiser as
  select canvassers.id as organiser,
        teams.id,
        teams.name,
        teams.latitude,
        teams.longitude
    from teams, teamorganiserships, canvassers
    where teams.id = teamorganiserships.team_id
      and canvassers.id = teamorganiserships.canvasser_id
      and canvassers.authorised = true;

create view organisers_by_team as
  select teams.id as team,
        canvassers.id,
        canvassers.username,
        canvassers.fullname,
        canvassers.email,
        canvassers.phone
    from teams, teamorganiserships, canvassers
    where teams.id = teamorganiserships.team_id
      and canvassers.id = teamorganiserships.canvasser_id
      and canvassers.authorised = true;

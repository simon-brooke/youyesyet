
insert into canvassers (username, fullname, elector_id, address_id, authority_id)
  values ('test_admin', 'Michael Thomson', 14, 4, 'Twitter');

insert into canvassers (username, fullname, elector_id, address_id, authority_id)
  values ('test_analyst', 'Jack Lang', 13, 4, 'Twitter');

insert into canvassers (username, fullname, elector_id, address_id, authority_id)
  values ('test_canvasser', 'Catriona Lang', 12, 4, 'Twitter');

insert into canvassers (username, fullname, elector_id, address_id, authority_id)
  values ('test_editor', 'Ursula Lang', 11, 4, 'Twitter');

insert into canvassers (username, fullname, elector_id, address_id, authority_id)
  values ('test_expert', 'Charlie Gourlay', 18, 5, 'Twitter');

insert into canvassers (username, fullname, elector_id, address_id, authority_id)
  values ('test_organiser', 'Jude Morrison', 15, 5, 'Twitter');

insert into ln_members_roles_canvassers
  values (1, (select id from canvassers where username='test_admin'));
insert into ln_members_roles_canvassers
  values (2, (select id from canvassers where username='test_analyst'));
insert into ln_members_roles_canvassers
  values (3, (select id from canvassers where username='test_editor'));
insert into ln_members_roles_canvassers
  values (4, (select id from canvassers where username='test_organiser'));
insert into ln_members_roles_canvassers
  values (5, (select id from canvassers where username='test_expert'));
insert into ln_members_roles_canvassers
  values (6, (select id from canvassers where username='test_canvasser'));

insert into teams (name, district_id, latitude, longitude)
values ('Yes Stewartry', 1, 54.94, -3.94);

insert into ln_members_teams_canvassers
values (
  (select id from teams where name='Yes Stewartry'),
  (select id from canvassers where username='test_admin'));

insert into ln_members_teams_canvassers
values (
  (select id from teams where name='Yes Stewartry'),
  (select id from canvassers where username='test_analyst'));

insert into ln_members_teams_canvassers
values (
  (select id from teams where name='Yes Stewartry'),
  (select id from canvassers where username='test_editor'));

insert into ln_members_teams_canvassers
values (
  (select id from teams where name='Yes Stewartry'),
  (select id from canvassers where username='test_organiser'));

insert into ln_members_teams_canvassers
values (
  (select id from teams where name='Yes Stewartry'),
  (select id from canvassers where username='test_expert'));

insert into ln_members_teams_canvassers
values (
  (select id from teams where name='Yes Stewartry'),
  (select id from canvassers where username='test_canvasser'));


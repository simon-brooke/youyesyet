-- enough data to get the system working and real logins

insert into addresses (id, address, postcode, latitude, longitude)
values (5, 'West Croft, Standingstone, Auchencairn', 'DG7 1RF', 54.822389, -3.920265);

insert into dwellings (id, address_id, sub_address)
values (5, 5, '');

insert into electors (id, name, dwelling_id, gender)
values (11, 'Simon Brooke', 5, 'Male');

insert into authorities (id) values ('GitHub');

insert into canvassers (username, fullname, elector_id, address_id, authority_id, authorised)
values ('simon_brooke', 'Simon Brooke', 11, 5, 'GitHub', true);

CREATE TABLE  dwellings
(
  id INT NOT NULL PRIMARY KEY,
  address_id INT NOT NULL references addresses on delete no action,
  sub_address VARCHAR( 32)
);

alter table electors
  add column dwelling_id int references dwellings on delete no action;

alter table electors drop column address_id;

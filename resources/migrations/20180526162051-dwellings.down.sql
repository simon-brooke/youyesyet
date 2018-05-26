alter table electors
  add column address_id references addresses on delete no action;

update electors
  set address_id =
    (select address_id
       from dwellings
       where dwellings.id electors.dwelling_id);

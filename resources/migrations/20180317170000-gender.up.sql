create table genders (
  id varchar(32) not null primary key
);

-- genders is reference data
insert into genders values ('Female');
insert into genders values ('Male');
insert into genders values ('Non-binary');
insert into genders values ('Unknown');

alter table electors add column gender varchar(32) references genders(id) default 'Unknown';

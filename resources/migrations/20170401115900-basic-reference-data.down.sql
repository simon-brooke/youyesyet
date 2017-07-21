-- this is just a teardown of everything set up in the corresponding .up.sql file

delete from roles where name = 'Expert';
delete from roles where name = 'Administrator';
delete from roles where name = 'Recruiter';
delete from roles where name = 'Organiser';
delete from roles where name = 'Editor';

alter table issues drop column content;
alter table issues drop column current;

delete from issues where id = 'Currency';
delete from issues where id = 'Monarchy';
delete from issues where id = 'Defence';

delete from options where id = 'Yes';
delete from options where id = 'No';

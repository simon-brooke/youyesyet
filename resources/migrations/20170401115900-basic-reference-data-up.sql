
-- We don't explicitly instantiate the 'Canvasser' role since every user is
-- deemed to be a canvasser.


-- an 'Expert' is someone with expertise in one or more issues, who is
-- trusted to discuss those issues in detail with electors.
insert into roles (name) values ('Expert');


-- an 'Administrator' is someone entitled to broadly alter reference data
-- throughout the system.
insert into roles (name) values ('Administrator');


-- a 'Recruiter' is someone entitled to invite other people to become users
-- ('Canvassers'). A Recruiter is entitled to lock the account of anyone they
-- have recruited, recursively.
insert into roles (name) values ('Recruiter');


-- an 'Organiser' is someone who organises one or more local teams. An Organiser
-- is entitled to exclude any Canvasser from any team they organise.
insert into roles (name) values ('Organiser');


-- an 'Editor' is someone entitled to add and edit issues.
insert into roles (name) values ('Editor');

-- issue text is local; there may still in addition be a further link to more
-- information, but the basic issue text should be part of the issue record.
-- The text should fit on a phone screen without scrolling, so is reasonably
-- short.
alter table issues add column content varchar(1024);

-- an issue may be current or not current; when not current it is not deleted
-- from the system but kept because it may become current again later. Only
-- current issues are shown in the app. Typically not fewer than three and not
-- more than about seven issues should be current at any time.
alter table issues add column current default false;

insert into issues (id, content, current) values ('Currency',
                                        'Scotland could keep the Pound, or use the Euro. But we could also set up a new currency of our own.',
                                                 true);

insert into issues (id, content, current) values ('Monarchy',
                                        'Scotland could keep the Queen. This is an issue to be decided after independence.',
                                                 true);

insert into issues (id, content, current) values ('Defence',
                                        'Scotland will not have nuclear weapons, and will probably not choose to engage in far-off wars. But we could remain members of NATO.',
                                                 true);


insert into options (id) values ('Yes');

insert into options (id) values ('No');


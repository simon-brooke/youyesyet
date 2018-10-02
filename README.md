# youyesyet

A web-app intended to be used by canvassers campaigning for a 'Yes' vote in the second independence referendum.

The web-app will be delivered to canvassers out knocking doors primarily through an HTML5/React single-page app designed to work on a mobile phone; it's possible that someone else may do an Android of iPhone native app to address the same back end but at present I have no plans for this.

There must also be an administrative interface through which privileged users can set the system up and authorise canvassers, and a 'followup' interface through which issue-expert specialist canvassers can address particular electors' queries.

generated using Luminus version "2.9.11.05"

## Status

Very early pre-alpha; user interface mostly works (enough to demonstrate), back end is hardly started.

## What is it supposed to do?

To understand what I'm aiming for, read [this essay](http://blog.journeyman.cc/2016/10/preparing-for-next-independence.html). Design documentation, such as there is of it yet, is in the *dummy* sub-directory. Also look at [src/clj/youyesyet/db/schema.clj](https://github.com/simon-brooke/youyesyet/blob/master/src/clj/youyesyet/db/schema.clj).

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed. The database required must be [Postgres][2] 9.3 or above.

[1]: https://github.com/technomancy/leiningen
[2]: https://www.postgresql.org/

You'll also need to create your own local copy of *profiles.clj*, which should contain something like:

    ;; WARNING
    ;; The profiles.clj file is used for local environment variables, such as database credentials.
    ;; This file is listed in .gitignore and will be excluded from version control by Git.

    {:profiles/dev  {:env {:database-url "jdbc:postgresql://127.0.0.1/youyesyet_dev?user=username&password=thisisnotsecure"}}
     :profiles/test {:env {:database-url "jdbc:postgresql://127.0.0.1/youyesyet_test?user=username&password=thisisnotsecure"}}}

Where *username* is the username required to access the database, and *thisisnotsecure* is the password which authenticates that username.

It will be helpful for you to have the [Zenhub](https://www.zenhub.com/) plugin in your browser, either Firefox or Chrome, as I'm using it for project planning.

## Further Reading

If you're thinking of joining in development on this I'd strongly recommend you get hold of a copy of [Dmitry Sotnikov](https://github.com/yogthos)'s [Web Development with Clojure, Second Edition](https://pragprog.com/book/dswdcloj2/web-development-with-clojure-second-edition).

You should also read the [User-Oriented Specification](doc/specification/userspec.md) and any other documentation which appears under the *doc/specification* hierarchy.


## Building this

This application is built using [Application Description Language](https://github.com/simon-brooke/adl/). The `adl` pre-processor is run as a prep task to building the `uberjar`, which in turn is preparatory to building the `uberwar`.

This will generate a large number of the source files required by YouYesYet, **including** the database initialisation scripts. These generated source files are not, as a matter of policy, held in the repository.

### What is auto-generated, and how to override it

The following files are generated from the master file `youyesyet.adl.xml`:

* `resources/sql/queries.auto.sql` - [HugSQL](https://www.hugsql.org/) queries for selection, insertion, modification and deletion of records of all entities described in the ADL file.
* `resources/sql/[application-name].postgres.sql` - [Postgres](https://www.postgresql.org/) database initialisation script including tables for all entities, convenience views for all entities, all necessary link tables and referential integrity constraints.
* `resources/templates/auto/*.html` - [Selmer](https://github.com/yogthos/Selmer) templates for each form or list list specified in the ADL file (pages are not yet handled).
* `src/clj/[application-name]/routes/auto.clj` - [Compojure]() routes for each form or list list specified in the ADL file (pages are not yet handled).
* `src/clj/[application-name]/routes/auto-json.clj` - [Compojure]() routes returning JSON responses for each query generated in `resources/sql/queries.auto.sql`.

*You are strongly advised never to edit any of these files*.

* To override any query, add that query to a file `resources/sql/queries.sql`
* To add additional material (for example reference data) to the database initialisation, add it to a separate file or a family of separate files.
* To override any template, copy the template file from `resources/templates/auto/` to `resources/templates/` and edit it there.
* To override any route, write a function of the same name in the namespace `[application-name].routes.manual`.


## Getting the database up

You'll need a file *profiles.clj*, with content similar to the following; it's not in the repository because it contains passwords.

    ;; WARNING
    ;; The profiles.clj file is used for local environment variables, such as database credentials.
    ;; This file is listed in .gitignore and will be excluded from version control by Git.

    {:profiles/dev  {:env {:database-url "jdbc:postgresql://127.0.0.1/youyesyet_dev?user=youyesyet&password=yourverysecurepassword"}}
     :profiles/test {:env {:database-url "jdbc:postgresql://127.0.0.1/youyesyet_test?user=youyesyet&password=yourverysecurepassword"}}}


Do get the database initialised, run

    createdb youyesyet_dev

I'm no longer using Migratus as I'm using [Application Description Language](https://github.com/simon-brooke/adl/)
to generate the majority of the application, and, as changes are made to the application
description, new database schemas are generated. The database initialisation script will
be found at `resources/sql/youyesyet.postgres.sql`. Manually maintained overrides are found in
`resources/sql/youyesyet.postgres.overrides.sql`. So to initialise the database, invoke

    psql youyesyet_dev < resources/sql/youyesyet.postgres.sql

followed by

    psql youyesyet_dev < resources/sql/youyesyet.postgres.overrides.sql

Reference data initialisation scripts will in due course be stored in the same directory.

Once we have a more or less finished application it may be worth going back to
[Migratus](https://github.com/yogthos/migratus); I might have a go at generating migrations from
diffs between successive versions of the application description.

## Running in a dev environment

To run in a dev environment, checkout the *develop* branch

To download and install Javascript delendencies, run

    cd youyesyet
    lein npm install

To start a development web server for the application, run:

Then

    lein repl

Wait for the clojure `user=>` prompt to appear, and enter

    (mount/start)

This will get the application running for development; ideally, open a new terminal and invoke

    lein figwheel

which will aid in work on the ClojureScript components.

## Running in a production environment

Either

1. run `lein uberjar` and execute the resulting jar file directly; or
2. run `lein uberwar` and serve the resulting war file from a servlet container.

The [beta production server](https://www.projecthope.scot/) currently runs an uberwar build in Tomcat behind Nginx.

## Working on this project

You should get the [ZenHub](https://github.com/integrations/zenhub) plug-in for your favourite browser; I use ZenHub for project management, and you (and I) will find collaborating much easier if you do. To join in

1. Clone the project from [my repository](https://github.com/simon-brooke/youyesyet);
2. Visit the [kanban board](https://github.com/simon-brooke/youyesyet#boards?repos=70809242) and choose an issue you'd like to work on;
3. Assign that issue to yourself and move it into the 'in progress' column;
4. Use gitflow: start a new feature;
5. Work on the issue;
6. When you are satisfied with your work, commit it to your repository in your feature branch;
7. Submit a pull request and move the issue to the 'QA/Review' column on the kanban board;
8. If your work works, I'll integrate it into the develop branch, which you can then pull;
9. Rinse and repeat!

Obviously I'd appreciate it if you'd [mail me](mailto:simon@journeyman.cc) to introduce yourself, but there's no need to do so.

Finally, if you take a ticket and are not able to finish it, please

1. mail me to say so;
2. move the issue back to the 'backlog' column on the kanban board.

And *many thanks*!

## More about tooling

Note that all tools recommended in this document are free for non-commercial use; not all are open source.

### Editors/IDEs

I (Simon) use, like and recommend [LightTable](http://lighttable.com/) as my editor; I used to use Emacs, and there is excellent Clojure tooling for Emacs, but these days Emacs ways of working seem just too far from everything else to be comfortable to me. [NightCode](https://sekao.net/nightcode/) is a lighter-weight Clojure IDE which you may like. There's also [Cursive](https://cursive-ide.com/) but it isn't free and I have so far found it more annoying than helpful; or [Counterclockwise](https://github.com/ccw-ide/ccw) which I don't have recent experience of.

### Git

I'm currently using [GitKraken](https://www.gitkraken.com/), on Linux, Windows, and Mac. Like so many things it works best on Linux, but it's good on both other platforms. However, I confess that although I like the ability to easily view the whole structure of the git repository, I tend to drop back to the command line if I'm doing anything tricky with Git.

### Database

The database is [Postgres](https://www.postgresql.org/). I have no plans even to support anything else; I don't feel we have time to mess about. However, if someone wants to fork the code and implement a different database, that's up to them. I'm afraid I do all database stuff from the command line; [there are graphical tools for Postgres](https://wiki.postgresql.org/wiki/Community_Guide_to_PostgreSQL_GUI_Tools) but personally I've never found graphical tools for databases much use.

## Further Reading

If you're thinking of joining in development on this I'd strongly recommend you get hold of a copy of [Dmitry Sotnikov](https://github.com/yogthos)'s [Web Development with Clojure, Second Edition](https://pragprog.com/book/dswdcloj2/web-development-with-clojure-second-edition).

You should also read the [User-Oriented Specification](doc/specification/userspec.md) and any other documentation which appears under the *doc/specification* hierarchy.

## License

Copyright © 2016 Simon Brooke for the Radical Independence Campaign.

Licensed under the GNU General Public License, version 2.0 or (at your option) any later version.

**NOTE THAT** files which are directly created by the Luminus template do not currently have a GPL header
at the top; files which are new in this project or which have been substantially modified for this project
do have a GPL header at the top.

# youyesyet

A web-app intended to be used by canvassers campaigning for a 'Yes' vote in the second independence referendum.

The web-app will be delivered to canvassers out knocking doors primarily through an HTML5/React single-page app designed to work on a mobile phone; it's possible that someone else may do an Android of iPhone native app to address the same back end but at present I have no plans for this.

There must also be an administrative interface through which privileged users can set the system up and authorise canvassers, and a 'followup' interface through which issue-expert specialist canvassers can address particular electors' queries.

generated using Luminus version "2.9.11.05"

## Status

**Nothing works yet**. This is very early development code, a long way pre-alpha.

## What is it supposed to do?

To understand what I'm aiming for, read [this essay](http://blog.journeyman.cc/2016/10/preparing-for-next-independence.html). Design documentation, such as there is of it yet, is in the *dummy* sub-directory. Also look at [src/clj/youyesyet/db/schema.clj](https://github.com/simon-brooke/youyesyet/blob/master/src/clj/youyesyet/db/schema.clj).

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed. The database required must be [Postgres][2].

[1]: https://github.com/technomancy/leiningen
[2]: https://www.postgresql.org/

You'll also need to create your own local copy of *profiles.clj*, which should contain something like:

    ;; WARNING
    ;; The profiles.clj file is used for local environment variables, such as database credentials.
    ;; This file is listed in .gitignore and will be excluded from version control by Git.

    {:profiles/dev  {:env {:database-url "jdbc:postgresql://127.0.0.1/youyesyet_dev?user=username&password=thisisnotsecure"}}
     :profiles/test {:env {:database-url "jdbc:postgresql://127.0.0.1/youyesyet_test?user=username&password=thisisnotsecure"}}}

Where *username* is the username required to access the database, and *thisisnotsecure* is the password which authenticates that username.

## Further Reading

If you're thinking of joining in development on this I'd strongly recommend you get hold of a copy of [Dmitry Sotnikov](https://github.com/yogthos)'s [Web Development with Clojure, Second Edition](https://pragprog.com/book/dswdcloj2/web-development-with-clojure-second-edition).

You should also read the [User-Oriented Specification](doc/specification/userspec.md) and any other documentation which appears under the *doc/specification* hierarchy.

## Running

To start a web server for the application, run:

    lein run

If you're wanting to work on cljs development, you need two terminal sessions. In one run

    lein run

as above; in the other, run

    lein figwheel

## License

Copyright © 2016 Simon Brooke for the Radical Independence Campaign.

Licensed under the GNU General Public License, version 2.0 or (at your option) any later version.

**NOTE THAT** files which are directly created by the Luminus template do not currently have a GPL header
at the top; files which are new in this project or which have been substantially modified for this project
do have a GPL header at the top.

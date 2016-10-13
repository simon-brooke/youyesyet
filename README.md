# youyesyet

A web-app intended to be used by canvassers campaigning for a 'Yes' vote in the second independence referendum.

The web-app will be delivered to canvassers out knocking doors primarily through an HTML5/React single-page app; it's possible that someone else may do an Android of iPhone native app to address the same back end but at present I have no plans for this.

There must also be an administrative interface through which privileged users can set the system up and authorise canvassers, and a 'followup' interface through which issue-expert specialist canvassers can address particular electors' queries.

generated using Luminus version "2.9.11.05"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed. The database required must be [Postgres][2].

[1]: https://github.com/technomancy/leiningen
[2]: https://www.postgresql.org/

## Running

To start a web server for the application, run:

    lein run

## License

Copyright © 2016 Simon Brooke for the Radical Independence Campaign.

Licensed under the GNU General Public License, version 2.0 or (at your option) any later version.

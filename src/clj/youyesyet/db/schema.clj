(ns youyesyet.db.schema
  (:require [clojure.java.jdbc :as sql]
            [korma.core :as kc]
            [youyesyet.db.core :as yyydb]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.db.schema: database schema for youyesyet.
;;;;
;;;; This program is free software; you can redistribute it and/or
;;;; modify it under the terms of the GNU General Public License
;;;; as published by the Free Software Foundation; either version 2
;;;; of the License, or (at your option) any later version.
;;;;
;;;; This program is distributed in the hope that it will be useful,
;;;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;;; GNU General Public License for more details.
;;;;
;;;; You should have received a copy of the GNU General Public License
;;;; along with this program; if not, write to the Free Software
;;;; Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
;;;; USA.
;;;;
;;;; Copyright (C) 2016 Simon Brooke
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn create-districts-table!
  "Create a table to hold the electoral districts in which electors are registered.
  Note that, as this app is being developed for the independence referendum in which
  polling is across the whole of Scotland, this part of the design isn't fully thought
  through; if later adapted to general or local elections, some breakdown or hierarchy
  of polling districts into constituencies will be required."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :districts
      [;; it may be necessary to have a serial abstract primary key but I suspect
        ;; polling districts already have numbers assigned by the Electoral Commission and
        ;; it would be sensible to use those. TODO: check.
        [:id "integer not null primary key"]
        [:name "varchar(64) not null"]
        ;; TODO: it would make sense to hold polygon data for polling districts so we can reflect
        ;; them on the map, but I haven't thought through how to do that yet.
        ])))


(kc/defentity district
  (pk :id)
  (table :districts)
  (database yyydb/*db*)
  (entity-fields :id :name))


(defn create-addresses-table!
  "Create a table to hold the addresses at which electors are registered."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :addresses
      [[:id "serial not null primary key"]
       ;; we do NOT want to hold multiple address records for the same household. When we receive
       ;; the electoral roll data the addresses are likely to be text fields inlined in the elector
       ;; record; in digesting the roll data we need to split these out and resolve them against existing
       ;; addresses in the table, creating a new address record only if there's no match.
       [:address "varchar(256) not null unique"]
       [:postcode "varchar(16)"]
       [:phone "varchar(16)"]
       ;; the electoral district within which this address exists
       [:district "integer references districts(id)"]
       [:latitude :real]
       [:longitude :real]])))


(kc/defentity address
  (pk :id)
  (table :addresses)
  (database yyydb/*db*)
  (entity-fields :id :address :postcode :phone :district :latitude :longitude))


(defn create-authority-table!
  "Create a table to hold the oauth authorities against which we with authenticate canvassers."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :authority
      [[:id "varchar(32) not null primary key"]
       ;; more stuff here when I understand more
       ])))


(kc/defentity authority
  (pk :id)
  (table :authorities)
  (database yyydb/*db*)
  (entity-fields :id :authority))


(defn create-electors-table!
  "Create a table to hold electors data."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :electors
      [[:rollno "integer primary key"]
       [:name "varchar(64) not null"]
       [:address "integer not null references addresses(id)" ]
       [:phone "varchar(16)"]
       ;; we'll probably only capture email data on electors if they request a followup
       ;; on a particular issue by email.
       [:email "varchar(128)"]])))


(kc/defentity elector
  (pk :id)
  (table :districts)
  (database yyydb/*db*)
  (entity-fields :id :name))


(defn create-canvassers-table!
  "Create a table to hold data on canvassers (including authentication data)."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :canvassers
      [[:username "varchar(32) not null primary key"]
       [:fullname "varchar(64) not null"]
       ;; most canvassers will be electors, we should link them:
       [:elector "integer references electors(rollno) on delete no action"]
       ;; but some canvassers may not be electors, so we need contact details separately:
       [:address "integer not null references addresses(id)" ]
       [:phone "varchar(16)"]
       [:email "varchar(128)"]
       ;; with which authority do we authenticate this canvasser? I do not want to hold even
       ;; encrypted passwords locally
       [:authority "varchar(32) not null references authority(id) on delete no action"]
       ;; true if the canvasser is authorised to use the app; else false. This allows us to
       ;; block canvassers we suspect of misbehaving.
       [:authorised :boolean]])))


(defn create-visit-table!
  "Create a table to record visits by canvassers to addresses (including virtual visits by telephone)."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :visits
      [[:id "serial not null primary key"]
       [:canvasser "varchar(32) references canvassers(username) not null"]
       [:date "timestamp with timezone not null default now()"]])))


(defn create-option-table!
  "Create a table to record options in the vote. This app is being created for the Independence
   referendum, which will have just two options, 'Yes' and 'No', but it might later be adapted
   for more general political canvassing."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :options
      [[:option "varchar(32) not null primary key"
        ;; To do elections you probably need party and candidate and stuff here, but
        ;; for the referendum it's unnecessary.
        ]])))


(defn create-option-district-table!
  "Create a table to link options to the districts in which they are relevant. This is extremely
   simple for the referendum: both options are relevant to all districts. This table is essentially
   'for later expansion'."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :optionsdistricts
      [[:option "varchar(32) not null references options(option)"]
       [:district "integer not null references districts(id)"]])))


(defn create-opinion-table!
  "Create a table to record the opinion of an elector as solicited by a canvasser during a visit.
  TODO: decide whether to insert a record in this table for 'don't knows'."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :opinions
      [[:id "serial primary key"]
       ;; the elector who gave this opinion
       [:elector "integer not null references electors(rollno)"]
       ;; the option the elector said they were planning to vote for
       [:option "varchar(32) not null references options(option)"]
       [:visit "integer not null references visits(id)"]])))


(defn create-issues-table!
  "A table for issues we predict electors may raise on the doorstep, for which we may be
  able to provide extra information or arrange for issue-specialists to phone and talk
  to the elector."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :issues
      [;; short name of this issue, e.g. 'currency', 'defence', 'pensions'
        [:issue "varchar(32) not null primary key"]
        ;; URL of some brief material the canvasser can use on the doorstap
        [:url "varchar(256)"]])))


(defn create-followup-method-table!
  "Create a table to hold reference data on followup methods."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :followupmethod
      [[:method "varchar(32) not null primary key"]])))


(defn create-issue-expertise-table!
  "A table to record which canvassers have expertise in which issues, so that followup
  requests can be directed to the right canvassers."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :issueexpertise
      [[:expert "varchar(32) not null references canvasser(username)"]
       [:issue "varchar(32) not null references issues(issue)"]
       ;; the method by which this expert can respond to electors on this issue
       [:method "varchar 32 not null references followupmethod(method)"]])))


(defn create-followup-request-table!
  "Create a table to record requests for followup contacts on particular issues."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :followuprequest
      [[:id "serial primary key"]
       [:elector "integer not null references electors(rollno)"]
       [:visit "integer not null references visits(id)"]
       [:issue "varchar(32) not null references issues(issue)"]
       ;; We probably need a followupmethod (telephone, email, postal) and, for telephone,
       ;; convenient times but I haven't thought through how to represent this or how
       ;; the user interface will work.
       [:method "varchar(32) not null references followupmethod(method)"]])))


(defn create-followup-action-table!
  "Create a table to record actions on followup requests. Record in this table are almost
  certainly created through a desktop-style interface rather than through te app, so it's
  reasonable that there should be narrative fields."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :followupaction
      [[:id "serial primary key"]
       [:request "integer not null references followuprequest(id)"]
       [:actor "varchar(32) not null references canvassers(username)"]
       [:date "timestamp with timezone not null default now()"]
       [:notes "text"]
       ;; true if this action closes the request
       [:closed :boolean]])))


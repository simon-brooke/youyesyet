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


;;; Note that this is the (old) Korma way of doing things;
;;;  it may not play well with migrations, nor with the HugSQL way of doing things recommended
;;;  in Web Development with Clojure, Second Ed. So this may be temporary 'get us started' code,
;;;  which later gets thrown away.


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
      ;; it may be necessary to have a serial abstract primary key but I suspect
      ;; polling districts already have numbers assigned by the Electoral Commission and
      ;; it would be sensible to use those. TODO: check.
      [:id "integer not null primary key"]
      [:name "varchar(64) not null"]
      ;; TODO: it would make sense to hold polygon data for polling districts so we can reflect
      ;; them on the map, but I haven't thought through how to do that yet.
      )))


(kc/defentity district
  (kc/pk :id)
  (kc/table :districts)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :name))


(defn create-addresses-table!
  "Create a table to hold the addresses at which electors are registered."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :addresses
      [:id "serial not null primary key"]
      ;; we do NOT want to hold multiple address records for the same household. When we receive
      ;; the electoral roll data the addresses are likely to be text fields inlined in the elector
      ;; record; in digesting the roll data we need to split these out and resolve them against existing
      ;; addresses in the table, creating a new address record only if there's no match.
      [:address "varchar(256) not null unique"]
      [:postcode "varchar(16)"]
      [:phone "varchar(16)"]
      ;; the electoral district within which this address exists
      [:district_id "integer references districts(id)"]
      [:latitude :real]
      [:longitude :real])))


(kc/defentity address
  (kc/pk :id)
  (kc/table :addresses)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :address :postcode :phone :latitude :longitude)
  (kc/has-one district))


(defn create-authorities-table!
  "Create a table to hold the oauth authorities against which we with authenticate canvassers."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :authorities
      [:id "varchar(32) not null primary key"]
      ;; more stuff here when I understand more
      )))


(kc/defentity authority
  (kc/pk :id)
  (kc/table :authorities)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id))


(defn create-electors-table!
  "Create a table to hold electors data."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :electors
      ;; id should be the roll number on the electoral roll, I think, but only if this is unique
      ;; across Scotland. Otherwise we need a separate id field. TODO: check.
      [:id "integer primary key"]
      [:name "varchar(64) not null"]
      [:address_id "integer not null references addresses(id)" ]
      [:phone "varchar(16)"]
      ;; we'll probably only capture email data on electors if they request a followup
      ;; on a particular issue by email.
      [:email "varchar(128)"])))


(kc/defentity elector
  (kc/pk :id)
  (kc/table :electors)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :name :phone :email)
  (kc/has-one address))


;;; Lifecycle of the canvasser record goes like this, I think:
;;; A canvasser record is created when an existing canvasser issues an invitation to a friend.
;;; The invitation takes the form of an automatically generated email with a magic token in it.
;;; At this point the record has only an email address, the introduced_by and the magic token,
;;; which is itself probably a hash of the email address. Therefore, having the username as the
;;; primary key won't work.
;;;
;;; The invited person clicks on the link in the email and completes the sign-up form, adding
;;; their full name, and their phone number. If the username they have chosen is unique, they
;;; are then sent a second email with a new magic token, possibly a hash of email address +
;;; full name. When they click on the link in this second email, their 'authorised' flag is
;;; set to 'true'.
;;;
;;; Administrators can also create canvasser records directly.aw
;;; TODO: Do we actually need a username at all? Wouldn't the email address do?

(defn create-canvassers-table!
  "Create a table to hold data on canvassers (including authentication data)."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :canvassers
      ;; id is the username the canvasser logs in as.
      [:id "serial primary key"]
      [:username "varchar(32) unique"]
      [:fullname "varchar(64) not null"]
      ;; most canvassers will be electors, we should link them:
      [:elector_id "integer references electors(id) on delete no action"]
      ;; but some canvassers may not be electors, so we need contact details separately:
      [:address_id "integer not null references addresses(id)" ]
      [:phone "varchar(16)"]
      [:email "varchar(128)"]
      ;; with which authority do we authenticate this canvasser? I do not want to hold even
      ;; encrypted passwords locally
      [:authority_id "varchar(32) not null references authorities(id) on delete no action"]
      [:introduced_by "integer references canvassers(id)"]
      [:is_admin :boolean]
      ;; true if the canvasser is authorised to use the app; else false. This allows us to
      ;; block canvassers we suspect of misbehaving.
      [:authorised :boolean])))


(kc/defentity canvasser
  (kc/pk :id)
  (kc/table :canvassers)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :fullname :phone :email :is_admin :authorised)
  (kc/has-one elector)
  (kc/has-one address)
  (kc/has-one canvasser {:fk :introduced_by})
  (kc/has-one authority))


(defn create-visits-table!
  "Create a table to record visits by canvassers to addresses (including virtual visits by telephone)."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :visits
      [:id "serial not null primary key"]
      [:address_id "integer not null references addresses(id)"]
      [:canvasser_id "integer not null references canvassers(id)"]
      [:date "timestamp with time zone not null default now()"])))


(kc/defentity visit
  (kc/pk :id)
  (kc/table :visits)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :date)
  (kc/has-one address)
  (kc/has-one canvasser))


(defn create-options-table!
  "Create a table to record options in the vote. This app is being created for the Independence
  referendum, which will have just two options, 'Yes' and 'No', but it might later be adapted
  for more general political canvassing."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :options
      ;; id is also the text of the option; e.g. 'Yes', 'No'.
      [:id "varchar(32) not null primary key"]
      ;; To do elections you probably need party and candidate and stuff here, but
      ;; for the referendum it's unnecessary.
      )))


(kc/defentity option
  (kc/pk :id)
  (kc/table :options)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id))


(defn create-option-district-table!
  "Create a table to link options to the districts in which they are relevant. This is extremely
  simple for the referendum: both options are relevant to all districts. This table is essentially
  'for later expansion'."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :optionsdistricts
      [:option_id"varchar(32) not null references options(option)"]
      [:district_id "integer not null references districts(id)"])))


;; I think we don't need an entity for optionsdistricts, because it's just a link table.


(defn create-intention-table!
  "Create a table to record the intention of an elector as solicited by a canvasser during a visit.
  TODO: decide whether to insert a record in this table for 'don't knows'."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :intentions
      [:id "serial primary key"]
      ;; the elector who gave this intention
      [:elector_id "integer not null references electors(id)"]
      ;; the option the elector said they were planning to vote for
      [:option_id "varchar(32) not null references options(option)"]
      [:visit_id "integer not null references visits(id)"])))


(kc/defentity intention
  (kc/pk :id)
  (kc/table :intentions)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id)
  (kc/has-one elector)
  (kc/has-one option)
  (kc/has-one visit))


(defn create-issues-table!
  "A table for issues we predict electors may raise on the doorstep, for which we may be
  able to provide extra information or arrange for issue-specialists to phone and talk
  to the elector."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :issues
      ;; short name of this issue, e.g. 'currency', 'defence', 'pensions'
      [:id "varchar(32) not null primary key"]
      ;; URL of some brief material the canvasser can use on the doorstap
      [:url "varchar(256)"])))


(kc/defentity issue
  (kc/pk :id)
  (kc/table :issues)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :url))


(defn create-followup-methods-table!
  "Create a table to hold reference data on followup methods."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :followupmethods
      [;; the method, e.g. 'telephone', 'email', 'post'
        :id "varchar(32) not null primary key"])))


(kc/defentity followup-method
  (kc/pk :id)
  (kc/table :followupmethods)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id))


(defn create-issue-expertise-table!
  "A table to record which canvassers have expertise in which issues, so that followup
  requests can be directed to the right canvassers."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :issueexpertise
      ;; the expert canvasser
      [:canvasser_id "integer not null references canvassers(id)"]
      ;; the issue they have expertise in
      [:issue_id "varchar(32) not null references issues(id)"]
      ;; the method by which this expert can respond to electors on this issue
      [:method_id "varchar(32) not null references followupmethods(id)"])))


(kc/defentity issue-expertise
  (kc/table :issueexpertise)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id)
  (kc/has-one canvasser)
  (kc/has-one issue)
  (kc/has-one followup-method))


(defn create-followup-requests-table!
  "Create a table to record requests for followup contacts on particular issues."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :followuprequests
      [:id "serial primary key"]
      [:elector_id "integer not null references electors(id)"]
      [:visit_id "integer not null references visits(id)"]
      [:issue_id "varchar(32) not null references issues(id)"]
      ;; We probably need a followupmethod (telephone, email, postal) and, for telephone,
      ;; convenient times but I haven't thought through how to represent this or how
      ;; the user interface will work.
      [:method_id "varchar(32) not null references followupmethods(id)"])))


(kc/defentity followup-request
  (kc/table :followuprequests)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id)
  (kc/has-one elector)
  (kc/has-one visit)
  (kc/has-one issue)
  (kc/has-one followup-method))


(defn create-followup-actions-table!
  "Create a table to record actions on followup requests. Record in this table are almost
  certainly created through a desktop-style interface rather than through te app, so it's
  reasonable that there should be narrative fields."
  []
  (sql/db-do-commands
    yyydb/*db*
    (sql/create-table-ddl
      :followupactions
      [:id "serial primary key"]
      [:request_id "integer not null references followuprequests(id)"]
      [:actor "integer not null references canvassers(id)"]
      [:date "timestamp with time zone not null default now()"]
      [:notes "text"]
      ;; true if this action closes the request
      [:closed :boolean])))


(kc/defentity followup-action
  (kc/table :followupactions)
  (kc/database yyydb/*db*)
  (kc/entity-fields :id :notes :date :closed)
  (kc/has-one followup-request)
  (kc/has-one canvasser {:fk :actor}))


(defn init-db! []
  "Initialised the whole database."
  (create-districts-table!)
  (create-addresses-table!)
  (create-authorities-table!)
  (create-electors-table!)
  (create-canvassers-table!)
  (create-visits-table!)
  (create-options-table!)
  (create-issues-table!)
  (create-followup-methods-table!)
  (create-issue-expertise-table!)
  (create-followup-requests-table!)
  (create-followup-actions-table!)
  )

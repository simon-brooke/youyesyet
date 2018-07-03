(ns ^{:doc "Canvasser app client state."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.state)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.state: the state of the app.
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
;;;; Copyright (C) 2016 Simon Brooke for Radical Independence Campaign
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; This is the constructor for the atom in which the state of the user interface is held.
;;; The atom gets updated by 'events' registered in handler.cljs, q.v.

(def default-db
  {
    :addresses
    [{:locality 548223905,
  :address
  "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF",
  :phone nil,
  :postcode "DG7 1RF",
  :longitude -3.905045374625994,
  :district_id 1,
  :dwellings
  [{:address_id_expanded
    "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF, DG7 1RF",
    :address_id 18,
    :sub_address "",
    :id 17,
    :id_2 17,
    :address_id_2 18,
    :sub_address_2 "",
    :electors
    [{:email nil,
      :dwelling_id_2 17,
      :dwelling_id_expanded
      "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF, DG7 1RF, ",
      :intentions
      [{:locality 548223905,
        :visit_id_expanded
        "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF, DG7 1RF, 2018-06-14 20:29:34.721522",
        :option_id_expanded "Yes",
        :option_id "Yes",
        :option_id_2 "Yes",
        :visit_id_2 1,
        :elector_id_2 61,
        :visit_id 1,
        :elector_id 61,
        :id 1,
        :elector_id_expanded nil,
        :id_2 1}],
      :phone nil,
      :phone_2 nil,
      :gender_expanded "Female",
      :name "Alice Sutherland",
      :dwelling_id 17,
      :id 61,
      :gender "Female",
      :gender_2 "Female",
      :name_2 "Alice Sutherland",
      :email_2 nil,
      :id_2 61}
     {:email nil,
      :dwelling_id_2 17,
      :dwelling_id_expanded
      "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF, DG7 1RF, ",
      :intentions [],
      :phone nil,
      :phone_2 nil,
      :gender_expanded "Female",
      :name "Charlie Sutherland",
      :dwelling_id 17,
      :id 62,
      :gender "Female",
      :gender_2 "Female",
      :name_2 "Charlie Sutherland",
      :email_2 nil,
      :id_2 62}
     {:email nil,
      :dwelling_id_2 17,
      :dwelling_id_expanded
      "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF, DG7 1RF, ",
      :intentions [],
      :phone nil,
      :phone_2 nil,
      :gender_expanded "Male",
      :name "Keith Sutherland",
      :dwelling_id 17,
      :id 64,
      :gender "Male",
      :gender_2 "Male",
      :name_2 "Keith Sutherland",
      :email_2 nil,
      :id_2 64}
     {:email nil,
      :dwelling_id_2 17,
      :dwelling_id_expanded
      "HAZELFIELD HOUSE, CASTLE DOUGLAS, DG7 1RF, DG7 1RF, ",
      :intentions [],
      :phone nil,
      :phone_2 nil,
      :gender_expanded "Female",
      :name "Lucy Sutherland",
      :dwelling_id 17,
      :id 63,
      :gender "Female",
      :gender_2 "Female",
      :name_2 "Lucy Sutherland",
      :email_2 nil,
      :id_2 63}]}],
  :id 18,
  :latitude 54.8222716877376}]

    ;;; the currently selected address, if any.
    :address nil
    ;;; electors at the currently selected dwelling
    :electors nil
    ;;; any error to display
    :error nil
    ;;; the issue from among the issues which is currently selected.
    ;;; any confirmation message to display
    :feedback '()
    ;;; the currently selected issue
    :issue nil
    ;;; the issues selected for the issues page on this day.
    :issues nil
    ;;; message of the day
    :motd "This is a test version only. There is no real data."
    ;;; the options from among which electors can select.
    :options nil
    ;;; the queue of items waiting to be transmitted.
    :outqueue ()
    ;;; the currently displayed page within the app.
    :page :home
    :view nil
    :latitude 54.82
    :longitude -3.90
    :zoom 12})


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
  {;;; the currently selected address, if any.
    :address {:id 1 :address "13 Imaginary Terrace, IM1 3TE" :latitude 55.8253043 :longitude -4.2569057
              :dwellings [{:id 1
                           :electors [{:id 1 :name "Alan Anderson" :gender :male :intention :no}
                                      {:id 2 :name "Ann Anderson" :gender :female}
                                      {:id 3 :name "Alex Anderson" :gender :fluid :intention :yes}
                                      {:id 4 :name "Andy Anderson" :intention :yes}]}]}
    ;;; a list of the addresses in the current location at which there
    ;;; are electors registered.
    :addresses [{:id 1 :address "13 Imaginary Terrace, IM1 3TE" :latitude 55.8253043 :longitude -4.2570944
                 :dwellings [{:id 1
                              :electors [{:id 1 :name "Alan Anderson" :gender :male :intention :no}
                                         {:id 2 :name "Ann Anderson" :gender :female}
                                         {:id 3 :name "Alex Anderson" :gender :fluid :intention :yes}
                                         {:id 4 :name "Andy Anderson" :intention :yes}]}]}
                {:id 2  :address "15 Imaginary Terrace, IM1 3TE" :latitude 55.8252354 :longitude -4.2572778
                 :dwellings [{:id 2
                              :electors [{:id 5 :name "Beryl Brown" :gender :female}
                                         {:id 6 :name "Betty Black" :gender :female}]}]}

                {:id 3 :address "17 Imaginary Terrace, IM1 3TE" :latitude 55.825166 :longitude -4.257026
                 :dwellings [{:id 3 :sub-address "Flat 1"
                              :electors [{:id 7 :name "Catriona Crathie" :gender :female :intention :yes}
                                         {:id 8 :name "Colin Caruthers" :gender :male :intention :yes}
                                         {:id 9 :name "Calum Crathie" :intention :yes}]}
                             {:id 4 :sub-address "Flat 2"
                              :electors [{:id 1 :name "David Dewar" :gender :male :intention :no}]}]}]
    ;;; electors at the currently selected dwelling
    :electors [{:id 1 :name "Alan Anderson" :gender :male :intention :no}
               {:id 2 :name "Ann Anderson" :gender :female}
               {:id 3 :name "Alex Anderson" :gender :fluid :intention :yes}
               {:id 4 :name "Andy Anderson" :intention :yes}]
    ;;; any error to display
    :error nil
    ;;; the issue from among the issues which is currently selected.
    ;;; any confirmation message to display
    :feedback nil
    ;;; the currently selected issue
    :issue "Currency"
    ;;; the issues selected for the issues page on this day.
    :issues {"Currency" "Scotland could keep the Pound, or use the Euro. But we could also set up a new currency of our own. Yada yada yada"
             "Monarchy" "Scotland could keep the Queen. This is an issue to be decided after independence. Yada yada yada"
             "Defence" "Scotland will not have nuclear weapons, and will probably not choose to engage in far-off wars. But we could remain members of NATO"}
    ;;; message of the day
    :motd "This is a test version only. There is no real data."
    ;;; the options from among which electors can select.
    :options [{:id :yes :description "Yes"} {:id :no :description "No"}]
    ;;; the queue of items waiting to be transmitted.
    :outqueue ()
    ;;; the currently displayed page within the app.
    :page :home
    :view nil
    :latitude 55.82
    :longitude -4.25
    :zoom 12})

(ns youyesyet.db)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.electors: electors view for youyesyet.
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
;;;
;;; not wonderfully happy with 'db' as a name for this namespace; will probably change to
;;; 'client-state'.

(def default-db
  ;;; the currently displayed 'page' within the app.
  {:page :home
   ;;; the currently selected address, if any.
   :address {:address "13 Imaginary Terrace, IM1 3TE"}
   ;;; a list of the addresses in the current location at which there
   ;;; are electors registered.
   :addresses []
   ;;; electors at the currently selected address
   :electors [{:name "Alan Anderson" :gender :male :intention :no}
              {:name "Ann Anderson" :gender :female}
              {:name "Alex Anderson" :gender :fluid :intention :yes}
              {:name "Andy Anderson" :intention :yes}]
   ;;; the issues selected for the issues page on this day.
   :issues {"Currency" "Scotland could keep the Pound, or use the Euro. But we could also set up a new currency of our own. Yada yada yada"
            "Monarchy" "Scotland could keep the Queen. This is an issue to be decided after independence. Yada yada yada"
            "Defence" "Scotland will not have nuclear weapons, and will probably not choose to engage in far-off wars. But we could remain members of NATO"}
   ;;; the issue from among those issues which is currently selected.
   :issue "Currency"
   })

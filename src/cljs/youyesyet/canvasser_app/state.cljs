(ns ^{:doc "Canvasser app client state."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.state
  (:require [youyesyet.canvasser-app.gis :as gis]))

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

(def default-db
  "The default configuration state of the app, when first loaded.
  This is the constructor for the atom in which the state of the user interface
  is held. The atom gets updated by 'events' registered in handler.cljs, q.v."
  { ;;; any confirmation message to display
    :feedback '("Welcome to the canvasser app!")
    ;;; message of the day
    :motd "This is a test version only. There is no real data."
    ;;; the options from among which electors can select.
    :outqueue '()
    ;;; the view of the map we display
    :view nil
    ;;; the currently displayed page within the app.
    :page :home
    ;;; initial starting coords in the centre of Scotland.
    :latitude 56
    :longitude -4
    :zoom 12})



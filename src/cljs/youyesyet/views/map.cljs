(ns youyesyet.views.map
  (:require [re-frame.core :refer [reg-sub]]
            [reagent.core :as reagent]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.views.map: map view for youyesyet.
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


;;; The pattern from the re-com demo (https://github.com/Day8/re-com) is to have
;;; one source file/namespace per view. Each namespace contains a function 'panel'
;;; whose output is an enlive-style specification of the view to be redered.
;;; I propose to follow this pattern. This file will (eventually) provide the map view.

;;; See https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md#map-view


;;; Cribbed heavily from
;;;   https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/leaflet
;;; but using OSM data because we can't afford commercial, so also cribbed from
;;;   https://switch2osm.org/using-tiles/getting-started-with-leaflet/
;;; Note that this is raw reagent stylee; it should be refactoed into re-frame stylee
;;; when I understand it better.

;; which provider to use
(def *map-provider* :osm)


(defn map-did-mount-mapbox
    "Did-mount function loading map tile data from MapBox (proprietary)."
  []
  (let [map (.setView (.map js/L "map") #js [55.86 -4.25] 13)]
    ;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18}))
            map)))


(defn map-did-mount-osm
  "Did-mount function loading map tile data from Open Street Map (open)."
  []
  (let [map (.setView (.map js/L "map") #js [55.86 -4.25] 13)]
    (.addTo (.tileLayer js/L "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors"
                                  :maxZoom 18}))
            map)))


(defn map-did-mount
  "Select the actual map provider to use."
  []
  (case *map-provider*
    :mapbox (map-did-mount-mapbox)
    :osm (map-did-mount-osm))
  ;; potentially others
  )


(defn map-render
  "Render the actual div containing the map."
  []
  [:div#map {:style {:height "360px"}}])


(defn map-div
  "A reagent class for the map object."
  []
  (reagent/create-class {:reagent-render map-render
                         :component-did-mount map-did-mount}))


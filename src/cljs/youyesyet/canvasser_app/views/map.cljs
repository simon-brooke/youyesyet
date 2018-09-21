(ns ^{:doc "Canvasser app map view panel."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.views.map
  (:require [re-frame.core :refer [reg-sub subscribe dispatch dispatch-sync]]
            [reagent.core :as reagent]
            [youyesyet.canvasser-app.gis :refer [refresh-map-pins get-current-location]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.views.map: map view for youyesyet.
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

;;; There should be one flag on the map for each address record currently in frame.
;;; Clicking the flag sets that address as the current address in the app state,
;;; and redirects to the electors view. How we handle blocks of flats needs further
;;; thought.

;; which provider to use
(def *map-provider* :osm)

(def osm-url "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png")
(def osm-attrib "Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors")

;; My gods mapbox is user-hostile!
(defn map-did-mount-mapbox
  "Did-mount function loading map tile data from MapBox (proprietary)."
  []
  (get-current-location)
  (let [view (.setView (.map js/L "map" (clj->js {:zoomControl "false"})) #js [55.82 -4.25] 40)]
    ;; NEED TO REPLACE FIXME with your mapID!
    (.addTo (.tileLayer js/L "http://{s}.tiles.mapbox.com/v3/FIXME/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18})))
    view))


(defn map-did-mount-osm
  "Did-mount function loading map tile data from Open Street Map."
  []
  (get-current-location)
  (let [view (.setView
               (.map js/L
                     "map"
                     (clj->js {:zoomControl false}))
               #js [@(subscribe [:latitude]) @(subscribe [:longitude])]
               @(subscribe [:zoom]))]
    (.addTo (.tileLayer js/L osm-url
                        (clj->js {:attribution osm-attrib
                                  :maxZoom 18}))
            view)
    (dispatch-sync [:set-view view])
    (.on view "moveend"
         (fn [_] (let [c (.getCenter view)]
                   (js/console.log (str "Moving centre to " c))
                   (dispatch-sync [:set-latitude (.-lat c)])
                   (dispatch-sync [:set-longitude (.-lng c)])
                   (dispatch [:fetch-locality]))))
    (refresh-map-pins)
    view))


(defn map-did-mount
  "Select the actual map provider to use."
  []
  (dispatch-sync [:set-view (case *map-provider*
                              :mapbox (map-did-mount-mapbox)
                              :osm (map-did-mount-osm)
                              ;; potentially others
                              )]))


(defn map-render
  "Render the actual div containing the map."
  []
  [:div#map {:style {:height "500px"}}])


(defn panel
  "A reagent class for the map object."
  []
  (get-current-location)
  (reagent/create-class {:reagent-render map-render
                         :component-did-mount map-did-mount}))

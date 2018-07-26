(ns ^{:doc "Canvasser app map stuff."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.gis
  (:require [cljs.reader :refer [read-string]]
            [cemerick.url :refer (url url-encode)]
            [day8.re-frame.http-fx]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx subscribe]]
            [ajax.core :refer [GET]]
            [ajax.json :refer [json-request-format json-response-format]]
            [youyesyet.canvasser-app.state :as db]
            [youyesyet.locality :refer [locality]]
            ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.gis: stuff to do with maps.
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

;; map stuff. If we do this in canvasser-app.views.map we get circular
;; references, so do it here.

(defn get-current-location []
  "Get the current location from the device, setting it in the database and
   returning the locality."
  (try
    (if (.-geolocation js/navigator)
      (.getCurrentPosition
        (.-geolocation js/navigator)
        (fn [position]
          (js/console.log (str "Current location is: "
                               (.-latitude (.-coords position)) ", "
                               (.-longitude (.-coords position))))
          (dispatch-sync [:set-latitude (.-latitude (.-coords position))])
          (dispatch-sync [:set-longitude (.-longitude (.-coords position))])))
      (js/console.log "Geolocation not available")
      (locality (.-latitude (.-coords position)) (.-longitude (.-coords position)))
    (catch js/Object any
      (js/console.log "Exception while trying to access location: " + any)
      0)))


(defn pin-image
  "select the name of a suitable pin image for this address"
  [address]
  (let [intentions
        (set
          (remove
            nil?
            (map
              :intention
              (mapcat :electors
                      (:dwellings address)))))]
    (case (count intentions)
      0 "unknown-pin"
      1 (str (name (first intentions)) "-pin")
      "mixed-pin")))


(defn map-pin-click-handler
  "On clicking on the pin, navigate to the electors at the address.
  This way of doing it adds an antry in the browser location history,
  so back links work."
  [id]
  (js/console.log (str "Click handler for address #" id))
  (let [view @(subscribe [:view])
        centre (.getCenter view)]
    (dispatch [:set-zoom (.getZoom view)])
    (dispatch [:set-latitude (.-lat centre)])
    (dispatch [:set-longitude (.-lng centre)]))
  (set! window.location.href (str "#building/" id)))


(defn add-map-pin
  "Add a map-pin at this address in this map view"
  [address view]
  (let [lat (:latitude address)
        lng (:longitude address)
        pin (.icon js/L
                   (clj->js
                    {:iconAnchor [16 41]
                     :iconSize [32 42]
                     :iconUrl (str "img/map-pins/" (pin-image address) ".png")
                     :riseOnHover true
                     :shadowAnchor [16 23]
                     :shadowSize [57 24]
                     :shadowUrl "img/map-pins/shadow_pin.png"}))
        marker (.marker js/L
                        (.latLng js/L lat lng)
                        (clj->js {:icon pin
                                  :title (:address address)}))]
    (.on (.addTo marker view) "click" (fn [_] (map-pin-click-handler (str (:id address)))))
    marker))


(defn map-remove-pins
  "Remove all pins from this map `view`. Side-effecty; liable to be problematic."
  [view]
  (.eachLayer view
              #(if
                 (instance? js/L.Marker %)
                 (.removeLayer view %)))
  view)


(defn refresh-map-pins
  "Refresh the map pins on this map. Side-effecty; liable to be problematic."
  []
  (let [view (map-remove-pins @(subscribe [:view]))
        addresses @(subscribe [:addresses])]
    (if
      view
      (do
        (js/console.log (str "Adding " (count addresses) " pins"))
        (doall (map #(add-map-pin % view) addresses)))
      (js/console.log "View is not yet ready"))
    view))



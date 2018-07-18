(ns youyesyet.locality)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.locality: compute localities.
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

;;;; See also resources/sql/locality-trigger.sql

(defn locality
  "Compute the locality index for this `latitude`/`longitude` pair."
  [latitude longitude]
  (+
    (* 1000            ;; left-shift the latitude component three digits
       (int
         (* latitude 100)))
    (-                  ;; invert the sign of the longitude component, since
      (int              ;; we're interested in localities West of Greenwich.
        (* longitude 100)))))

(defn neighbouring-localities
  "Return this locality with the localities immediately
  north east, north, north west, east, west, south west,
  south and south east of it."
  ;; TODO: I'm not absolutely confident of my arithmetic here!
  [locality]
  (list
    (- locality 99)
    (- locality 100)
    (- locality 101)
    (- locality 1)
    locality
    (+ locality 1)
    (+ locality 99)
    (+ locality 100)
    (+ locality 101)))

(neighbouring-localities 5482391)

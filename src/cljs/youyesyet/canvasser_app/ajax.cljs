(ns ^{:doc "Canvasser app transciever for ajax packets."
      :author "Simon Brooke"}
  youyesyet.canvasser-app.ajax
  (:require [ajax.core :as ajax]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.canvasser-app.ajax: transciever for ajax packets.
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


(defn local-uri? [{:keys [uri]}]
    "Return `true` if the supplied `uri` has no protocol part."
  (not (re-find #"^\w+?://" (str uri))))

(defn default-headers [request]
    "Copy the current uri and cross site request forgery token into the headers
    of this request."
  (if (local-uri? request)
    (-> request
        (update :uri #(str js/context %))
        (update :headers #(merge {"x-csrf-token" js/csrfToken} %)))
    request))

(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name "default headers"
                               :request default-headers})))



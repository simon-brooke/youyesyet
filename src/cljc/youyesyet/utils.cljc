(ns youyesyet.utils
  #?(:clj (:require [clojure.tools.logging :as log])
          :cljs (:require [cljs.reader :refer [read-string]])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.utils: small utility functions.
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

(defn coerce-to-number [v]
  "If it is possible to do so, coerce `v` to a number"
  ;; TODO: this doesn't work in cljs.
  (if (number? v) v
    (try
      (read-string (str v))
      #?(:clj  (catch Exception any
                 (log/error (str "Could not coerce '" v "' to number: " any)))
         :cljs (catch js/Object any
                 (js/console.log (str "Could not coerce '" v "' to number: " any)))))))

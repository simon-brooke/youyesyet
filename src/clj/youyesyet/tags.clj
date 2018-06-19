(ns^{:doc "Custom Selmer tags."
      :author "Simon Brooke"}
   youyesyet.tags
  (:require [selmer.parser :as parser]
            [selmer.filters :as filters]
            [selmer.util :refer :all]))


(defn if-writable-handler [params tag-content render rdr]
  "If the current element is writable by the current user, emit the content of
  the if clause; else emit the content of the else clause."
  (let [{if-tags :ifwritable else-tags :else} (tag-content rdr :ifwritable :else :endifwritable)]
    params))


(defn if-readable-handler [params tag-content render rdr]
  "If the current element is readable by the current user, emit the content of
  the if clause; else emit the content of the else clause."
  (let [{if-tags :ifreadable else-tags :else} (tag-content rdr :ifwritable :else :endifwritable)]
    params))

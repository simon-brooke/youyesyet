(ns ^{:doc "Queue of messages waiting to be sent to the server."
      :author "Simon Brooke"}
  youyesyet.outqueue
  (:require
  #?(:clj [clojure.core]
     :cljs [reagent.core :refer [atom]])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; youyesyet.outqueue: queue of messages waiting to be sent to the server.
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

;;; The items are (obviously) the actual items in the queue;
;;; the queue is locked if an attempt is currently being made to transmit
;;; an item.

(defn new-queue
  "Create a new queue"
  ([]
   (new-queue '()))
  ([items]
  (atom {:locked false
         :items (if
                  (seq? items)
                  (reverse items)
                  (list items))})))


(defn add!
  "Add this item to the queue."
  [q item]
  (swap! q
         (fn [a]
           (assoc a :items
             (cons item (:items a))))))


(defn queue?
  "True if x is a queue, else false."
  [x]
  (try
    (let [q (deref x)
          locked (:locked q)]
      (and
       (seq? (:items q))
       (or (true? locked) (false? locked))))
      (catch #?(:clj Exception :cljs js/Object) any
        #?(:clj (print (.getMessage any))
                :cljs (js/console.log (str any))))))


(defn peek
  "Look at the next item which could be removed from the queue."
  [q]
  (last (:items (deref q))))


(defn locked?
  [q]
  (:locked (deref q)))


(defn unlock!
  ([q ]
   (unlock! q true))
  ([q value]
   (swap! q (fn [a] (assoc a :locked (not (true? value)))))))


(defn lock!
  [q]
  (unlock! q false))


(defn count
  "Return the count of items currently in the queue."
  [q]
  (count (deref q)))


(defn take!
  "Return the first item from the queue, rebind the queue to the remaining
  items. If the queue is empty return nil."
  [q]
  (swap! q (fn [a]
             (let [items (reverse (:items a))
                   item (first items)
                   new-queue (reverse (rest items))]
               (assoc (assoc a :items new-queue) :v item))))
  (:v (deref q)))


(defn maybe-process-next
  "Apply this process, assumed to be a function of one argument, to the next
  item in the queue, if the queue is not currently locked; return the value
  returned by process."
  [q process]
  (if (and (queue? q)(not (locked? q)))
    (try
      (lock! q)
      (let [v (apply process (list (peek q)))]
        (take! q)
        v)
      (catch #?(:clj Exception :cljs js/Object) any
        #?(:clj (print (.getMessage any))
                :cljs (js/console.log (str any))))
      (finally (unlock! q)))
    ))

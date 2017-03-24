(ns youyesyet.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :issues
  (fn [db _]
    (:issues db)))

(reg-sub
  :issue
  (fn [db _]
    (:issue db)))

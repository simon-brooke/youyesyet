(ns youyesyet.canvasser-app.ui-utils-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [reagent.core :as reagent :refer [atom]]
            [youyesyet.canvasser-app.ui-utils :refer :all]))

(deftest big-link-tests
  (testing "big-link"
    (is (= [:div.big-link-container {:key (gensym "big-link")}
            [:a.big-link {}
             "Test"]]
           (big-link "Test" nil nil)))))

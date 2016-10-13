(ns youyesyet.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [youyesyet.core-test]))

(doo-tests 'youyesyet.core-test)


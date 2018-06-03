(ns youyesyet.canvasser-app.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [youyesyet.canvasser-app.core-test]))

(doo-tests 'youyesyet.canvasser-app.canvasser-app.core-test)


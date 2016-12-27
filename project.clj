(defproject youyesyet "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229" :scope "provided"]
                 [ring/ring-servlet "1.5.0"]
                 [clj-oauth "1.5.5"]
                 [ch.qos.logback/logback-classic "1.1.8"]
                 [re-frame "0.9.1"]
                 [cljs-ajax "0.5.8"]
                 [secretary "1.2.3"]
                 [reagent-utils "0.2.0"]
                 [reagent "0.6.0"]
                 [korma "0.4.3"]
                 [selmer "1.10.3"]
                 [markdown-clj "0.9.91"]
                 [ring-middleware-format "0.7.0"]
                 [metosin/ring-http-response "0.8.0"]
                 [bouncer "1.0.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [org.webjars.bower/tether "1.3.7"]
                 [org.clojure/tools.logging "0.3.1"]
                 [compojure "1.5.1"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.2.1"]
                 [luminus/ring-ttl-session "0.3.1"]
                 [mount "0.1.11"]
                 [cprop "0.1.9"]
                 [org.clojure/tools.cli "0.3.5"]
                 [migratus "0.8.32"]
                 [luminus-nrepl "0.1.4"]
                 [luminus-migrations "0.2.9"]
                 [conman "0.6.2"]
                 [org.postgresql/postgresql "9.4.1212"]
                 ]

  :min-lein-version "2.0.0"

  :license {:name "GNU General Public License v2"
            :url "http://www.gnu.org/licenses/gpl-2.0.html"}

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main youyesyet.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.1"]
            [migratus-lein "0.4.2"]
            [org.clojars.punkisdead/lein-cucumber "1.0.5"]
            [lein-cljsbuild "1.1.4"]
            [lein-uberwar "0.2.0"]
            [lein-bower "0.5.1"]
            [lein-less "1.7.5"]]

  :bower-dependencies [
                        ;; Problem with using boostrap and font-awsome from Bower: neither
                        ;; of the distributed packages compile cleanly with less :-(
                        ;; [bootstrap "2.3.1"]
                        ;; [font-awesome "3.2.1"]
                        [leaflet "0.7.3"]]

  :cucumber-feature-paths ["test/clj/features"]

  :hooks [leiningen.less]

  :uberwar
  {:handler youyesyet.handler/app
   :init youyesyet.handler/init
   :destroy youyesyet.handler/destroy
   :name "youyesyet.war"}

  :clean-targets ^{:protect false}
  [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]

  :figwheel
  {:http-server-root "public"
   :nrepl-port 7002
   :css-dirs ["resources/public/css"]
   :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :externs ["externs.js"]

  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
             :cljsbuild
             {:builds
              {:min
               {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                :compiler
                {:output-to "target/cljsbuild/public/js/app.js"
                 :externs ["react/externs/react.js" "externs.js"]
                 :optimizations :advanced
                 :pretty-print false
                 :closure-warnings
                 {:externs-validation :off :non-standard-jsdoc :off}}}}}


             :aot :all
             :uberjar-name "youyesyet.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[prone "1.1.4"]
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.5.0"]
                                 [luminus-jetty "0.1.4"]
                                 [pjstadig/humane-test-output "0.8.1"]
                                 [org.clojure/core.cache "0.6.5"]
                                 [org.apache.httpcomponents/httpcore "4.4.5"]
                                 [clj-webdriver/clj-webdriver "0.7.2"]
                                 [org.seleniumhq.selenium/selenium-server "3.0.1"]
                                 [doo "0.1.7"]
                                 [binaryage/devtools "0.8.3"]
                                 [figwheel-sidecar "0.5.8"]
                                 [com.cemerick/piggieback "0.2.2-SNAPSHOT"]
                                 [directory-naming/naming-java "0.8"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.14.0"]
                                 [lein-doo "0.1.7"]
                                 [lein-figwheel "0.5.8"]
                                 [org.clojure/clojurescript "1.9.229"]]
                  :cljsbuild
                  {:builds
                   {:app
                    {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                     :compiler
                     {:main "youyesyet.app"
                      :asset-path "/js/out"
                      :output-to "target/cljsbuild/public/js/app.js"
                      :output-dir "target/cljsbuild/public/js/out"
                      :source-map true
                      :optimizations :none
                      :pretty-print true}}}}



                  :doo {:build "test"}
                  :source-paths ["env/dev/clj" "test/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/dev/resources" "env/test/resources"]
                  :cljsbuild
                  {:builds
                   {:test
                    {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                     :compiler
                     {:output-to "target/test.js"
                      :main "youyesyet.doo-runner"
                      :optimizations :whitespace
                      :pretty-print true}}}}

                  }
   :profiles/dev {}
   :profiles/test {}})

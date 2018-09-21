(defproject youyesyet "0.2.1-SNAPSHOT"

  :description "Canvassing tool for referenda"
  :url "https://github.com/simon-brooke/youyesyet"

  :dependencies [[adl-support "0.1.4"]
                 [bouncer "1.0.1"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [clj-oauth "1.5.5"]
                 [cljsjs/react-leaflet "1.6.5-0"]
                 [cljs-ajax "0.7.4"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [clojure.java-time "0.3.2"]
                 [com.cemerick/url "0.1.1"]
                 [compojure "1.6.1"]
                 [conman "0.8.2"]
                 [cprop "0.1.11"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [korma "0.4.3"]
                 [lib-noir "0.9.9" :exclusions [org.clojure/tools.reader]]
                 [luminus/ring-ttl-session "0.3.2"]
                 [luminus-nrepl "0.1.4"]
                 [luminus-migrations "0.5.2"]
                 [luminus-immutant "0.2.4"]
                 [markdown-clj "1.0.2"]
                 [metosin/compojure-api "1.1.12"]
                 [metosin/ring-http-response "0.9.0"]
                 [migratus "1.0.8"]
                 [mount "0.1.12"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339" :scope "provided"]
                 [org.clojure/core.memoize "0.7.1"]
                 ;;[org.clojure/spec.alpha "0.2.168"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.postgresql/postgresql "42.2.4"]
                 [org.webjars/bootstrap "4.1.2"]
                 [org.webjars/font-awesome "5.1.0"]
                 [org.webjars.bower/tether "1.4.4"]
                 [postgre-types "0.0.4"]
                 [re-frame "0.10.5"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.1"]
                 [ring-middleware-format "0.7.2"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-servlet "1.6.3"]
                 [ring-webjars "0.2.0"]
                 [secretary "1.2.3"]
                 [selmer "1.11.8"]]

  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot youyesyet.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-adl "0.1.6"]
            [lein-cljsbuild "1.1.7"]
            [lein-codox "0.10.4"]
            [lein-cprop "1.0.3"]
            [lein-kibit "0.1.6"]
            [lein-less "1.7.5"]
            [lein-npm "0.6.2"]
            [lein-release "1.0.5"]
            [lein-uberwar "0.2.0"]
            [migratus-lein "0.5.9"]
            [org.clojars.punkisdead/lein-cucumber "1.0.7"]]

  :cucumber-feature-paths ["test/clj/features"]

  :codox {:metadata {:doc "FIXME: write docs"}
          :languages [:clojure :clojurescript]
          :source-paths ["src/clj" "src/cljc" "src/cljs"]}

  :npm {:dependencies [[datatables.net "1.10.19"]
                       [datatables.net-dt "1.10.19"]
                       [jquery "3.3.1"]
                       [leaflet "1.3.1"]
                       [selectize "0.12.6"]
                       [signature_pad "2.3.2"]
                       [simplemde "1.11.2"]]
        :root "resources/public/js/lib"}

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ;; ["vcs" "tag"] -- not working, problems with secret key
                  ["clean"]
                  ["uberjar"]
                  ["uberwar"]
                  ["docker" "build"]
                  ["docker" "push"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]]

  :uberwar {:handler youyesyet.handler/app
            :init youyesyet.handler/init
            :destroy youyesyet.handler/destroy
            :name "youyesyet.war"}

  :clean-targets ^{:protect false} [:target-path [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :figwheel {:http-server-root "public"
             :nrepl-port 7002
             :css-dirs ["resources/public/css"]
             :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}


  :profiles {:uberjar {:omit-source true
                       :prep-tasks ["adl"
                                    "compile"
                                    ["npm" "install"]
                                    ["cljsbuild" "once" "min"]]
                       :cljsbuild
                       {:builds
                        {:min
                         {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                          :compiler
                          {:output-to "target/cljsbuild/public/js/app.js"
                           :optimizations :advanced
                           :pretty-print false
                           :closure-warnings
                           {:externs-validation :off :non-standard-jsdoc :off}
                           :externs ["react/externs/react.js"]}}}}
                       :aot :all
                       :uberjar-name "youyesyet.jar"
                       :source-paths ["env/prod/clj"]
                       :resource-paths ["env/prod/resources"]}

             :dev           [:project/dev :profiles/dev]

             :test          [:project/dev :project/test :profiles/test]

             :project/dev  {:dependencies [[prone "1.1.4"]
                                           [ring/ring-mock "0.3.2"]
                                           [ring/ring-devel "1.6.3"]
                                           [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                                           [luminus-immutant "0.2.4"]
                                           [pjstadig/humane-test-output "0.8.3"]
                                           [binaryage/devtools "0.9.10"]
                                           [com.cemerick/piggieback "0.2.2"]
                                           [directory-naming/naming-java "0.8"]
                                           [doo "0.1.10"]
                                           [figwheel-sidecar "0.5.16"]]
                            :plugins      [[com.jakemccrary/lein-test-refresh "0.23.0"]
                                           [lein-doo "0.1.10"]
                                           [lein-figwheel "0.5.16"]
                                           [org.clojure/clojurescript "1.9.495"]]
                            :cljsbuild {:builds
                                        {:app
                                         {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                                          :compiler
                                          {:main "youyesyet.canvasser-app.app"
                                           :asset-path "/js/out"
                                           :output-to "target/cljsbuild/public/js/app.js"
                                           :output-dir "target/cljsbuild/public/js/out"
                                           :source-map true
                                           :optimizations :none
                                           :pretty-print true}}}}
                            :doo {:build "test"}
                            :source-paths ["env/dev/clj"]
                            :resource-paths ["env/dev/resources"]
                            :repl-options {:init-ns user}
                            :injections [(require 'pjstadig.humane-test-output)
                                         (pjstadig.humane-test-output/activate!)]}
             :project/test {:resource-paths ["env/test/resources"]
                            :cljsbuild
                            {:builds
                             {:test
                              {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                               :compiler
                               {:output-to "target/test.js"
                                :main "youyesyet.doo-runner"
                                :optimizations :whitespace
                                :pretty-print true}}}}}
             :profiles/dev {}
             :profiles/test {}})

(defproject youyesyet "0.1.0-SNAPSHOT"

  :description "Canvassing tool for referenda"
  :url "https://github.com/simon-brooke/youyesyet"

  :dependencies [[bouncer "1.0.1"]
                 [clj-oauth "1.5.4"]
                 [cljs-ajax "0.5.8"]
                 [compojure "1.5.2"]
                 [conman "0.6.3"]
                 [cprop "0.1.10"]
                 [funcool/struct "1.0.0"]
                 [korma "0.4.3"]
                 ;; TODO: Latest Luminus no longer includes noir, and I only
                 ;; use it in home.clj for routing. Worth looking at how Luminus
                 ;; currently does roouting, and perhaps removing this dependency.
                 [lib-noir "0.9.9" :exclusions [org.clojure/tools.reader]]
                 [luminus-migrations "0.3.0"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.1"]
                 [markdown-clj "0.9.98"]
                 [metosin/muuntaja "0.1.0"]
                 [metosin/ring-http-response "0.8.2"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.495" :scope "provided"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.postgresql/postgresql "42.0.0"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [re-frame "0.9.2"]
                 [reagent "0.6.1"]
                 [reagent-utils "0.2.1"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-core "1.6.0-RC1"]
                 [ring/ring-defaults "0.2.3"]
                 [ring/ring-servlet "1.4.0"]
                 [secretary "1.2.3"]
                 [selmer "1.10.7"]]

  :min-lein-version "2.0.0"

  :license {:name "GNU General Public License v2"
            :url "http://www.gnu.org/licenses/gpl-2.0.html"}

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot youyesyet.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.1"]
            [migratus-lein "0.4.4"]
            [lein-cljsbuild "1.1.5"]
            [lein-immutant "2.1.0"]
            [lein-kibit "0.1.2"]
            [lein-uberwar "0.2.0"]
            [lein-bower "0.5.1"]]

  :bower-dependencies [[leaflet "0.7.3"]]

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
             :prep-tasks ["compile" ["bower" "install"] ["cljsbuild" "once" "min"]]
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
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.5.1"]
                                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                                 [luminus-immutant "0.2.3"]
                                 [pjstadig/humane-test-output "0.8.1"]
                                 [binaryage/devtools "0.9.2"]
                                 [com.cemerick/piggieback "0.2.2-SNAPSHOT"]
                                 [directory-naming/naming-java "0.8"]
                                 [doo "0.1.7"]
                                 [figwheel-sidecar "0.5.9"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.18.1"]
                                 [lein-doo "0.1.7"]
                                 [lein-figwheel "0.5.9"]
                                 [org.clojure/clojurescript "1.9.495"]]
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
                      :pretty-print true}}}}

                  }
   :profiles/dev {}
   :profiles/test {}})

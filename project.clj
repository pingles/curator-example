(defproject curator-example "0.1.0-SNAPSHOT"
  :description "Example use of the curator library"
  :url "https://github.com/pingles/curator-example"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.1"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                 [org.clojure/tools.logging "0.2.6"]
                 [curator "0.0.2"]]
  :main curator.example.main
  :profiles {:dev {:dependencies [[org.slf4j/log4j-over-slf4j "1.6.4"]
                                  [org.slf4j/slf4j-simple "1.6.4"]
                                  [org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})

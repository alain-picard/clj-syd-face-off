(defproject the-great-face-off "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]


                 ;; Libs required by Steve
                 [io.pedestal/pedestal.service "0.5.3"]
                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 [io.pedestal/pedestal.jetty "0.5.3"]
                 ;; [io.pedestal/pedestal.immutant "0.5.3"]
                 ;; [io.pedestal/pedestal.tomcat "0.5.3"]
                 [ch.qos.logback/logback-classic "1.1.8" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.22"]
                 [org.slf4j/jcl-over-slf4j "1.7.22"]
                 [org.slf4j/log4j-over-slf4j "1.7.22"]

                 ;; Libs required by Alain
                 [http-kit "2.2.0"]
                 [org.clojure/core.async "0.2.391"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-mock "0.3.0"]
                 [org.clojure/data.json "0.2.6"]
                 [camel-snake-kebab   "0.1.5"]
                 [medley                    "0.8.3"]
                 [com.rpl/specter           "0.13.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [hiccup                    "1.0.5"]
                 [clj-time "0.12.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  ;:java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.5"]]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "the-great-face-off.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.3"]]}
             :uberjar {:aot [the-great-face-off.server]}}
  :main ^{:skip-aot true} the-great-face-off.server)

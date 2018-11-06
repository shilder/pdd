(defproject jum-chat "0.1.0-SNAPSHOT"
  :description "java.ural.Meetup live demo chat"
  :url "https://jum.dshilov.me"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :jvm-opts ["-Xmx512M"
             "-XX:+UseG1GC"]

  :dependencies [[org.clojure/clojure "1.9.0"]

                 ;; logging
                 [org.slf4j/slf4j-api "1.7.25"]
                 [ch.qos.logback/logback-core "1.2.3"]
                 [ch.qos.logback/logback-classic "1.2.3"]

                 ;; HTTP server
                 [aleph "0.4.6"]

                 ;; HTML generation server-side only
                 [rum "0.11.2" :exclusions [[sablono]
                                            [cljsjs/react]
                                            [cljsjs/react-dom]]]
                 ;; CSS generation
                 [garden "1.3.6"]

                 ;; JSON parsing and generation
                 [cheshire "5.6.1"]

                 ;; don't need clj-time - we will be using java.time
                 [ring/ring-core "1.7.0" :exclusions [[clj-time]]]
                 ;; Router
                 [compojure "1.6.1"]

                 ;; nREPL server
                 [org.clojure/tools.nrepl "0.2.13"]

                 ])

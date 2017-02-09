(defproject embedded-postgres-demo "0.1.0-SNAPSHOT"
  :description "Embedded Postgres demo"
  :license {:name "Public domain"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.7.0-alpha1"]]
  :profiles {:dev {:dependencies [[com.opentable.components/otj-pg-embedded "0.7.1"]]}}  )

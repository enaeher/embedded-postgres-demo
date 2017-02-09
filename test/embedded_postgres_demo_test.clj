(ns embedded-postgres-demo-test
  (:require [clojure.test :as t]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io])
  (:import [com.opentable.db.postgres.embedded EmbeddedPostgres]))

(def port 59432)

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname (str
                        "//localhost:"
                        port
                        "/postgres")
              :user "postgres"})

(defn with-postgres [f]
  (let [db (-> (EmbeddedPostgres/builder)
               (.setPort port)
               (.start))]
    (try
      (f)
      (finally
        (.close db)))))

(defn with-schema [f]
  (jdbc/with-db-connection [db db-spec]
    (jdbc/execute! db (slurp (io/resource "schema.ddl")))
    (f)))

(t/use-fixtures :once (t/join-fixtures [with-postgres with-schema]))

(t/deftest sanity-check
  (jdbc/with-db-connection [db db-spec]
    (jdbc/execute! db "insert into pgbench_branches (bid, bbalance) values (1, 100)")
    (t/is (= '({:bbalance 100})
             (jdbc/query db "select bbalance from pgbench_branches where bid = 1")))))

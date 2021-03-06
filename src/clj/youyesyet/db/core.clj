(ns ^{:doc "Database access functions, mostly from Luminus template."}
  youyesyet.db.core
  (:require
    [cheshire.core :refer [generate-string parse-string]]
    [clojure.java.jdbc :as jdbc]
    [conman.core :as conman]
    [hugsql.core :as hugsql]
    [mount.core :refer [defstate]]
   [postgre-types.json :refer [add-json-type add-jsonb-type]]
    [youyesyet.config :refer [env]])
  (:import org.postgresql.util.PGobject
           java.sql.Array
           clojure.lang.IPersistentMap
           clojure.lang.IPersistentVector
           [java.sql
            ;;         BatchUpdateException
            Date
            Timestamp
            PreparedStatement]))

(defstate ^:dynamic *db*
  "Primary connection to the main database. TODO: this does not yet enable
  sharding."
  :start (conman/connect! {:jdbc-url-env (env :database-url)
                           :jdbc-url "jdbc:postgresql://127.0.0.1/youyesyet_dev?user=youyesyet&password=thisisnotsecure"
                           :driver-class-name "org.postgresql.Driver"})
  :stop (conman/disconnect! *db*))

(add-json-type generate-string parse-string)
(add-jsonb-type generate-string parse-string)


(conman/bind-connection *db* "sql/queries.auto.sql" "sql/queries.sql")
(hugsql/def-sqlvec-fns "sql/queries.auto.sql")

(defn to-date
  "Return the SQL date `sql-date` as a Java date."
  [^java.sql.Date sql-date]
  (-> sql-date (.getTime) (java.util.Date.)))

(extend-protocol jdbc/IResultSetReadColumn
  Date
  (result-set-read-column [v _ _] (to-date v))

  Timestamp
  (result-set-read-column [v _ _] (to-date v))

  Array
  (result-set-read-column [v _ _] (vec (.getArray v)))

  PGobject
  (result-set-read-column [pgobj _metadata _index]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "json" (parse-string value true)
        "jsonb" (parse-string value true)
        "citext" (str value)
        value))))

(extend-type java.util.Date
  jdbc/ISQLParameter
  (set-parameter [v ^PreparedStatement stmt ^long idx]
    (.setTimestamp stmt idx (Timestamp. (.getTime v)))))

(defn to-pg-json
  "Render this `value` as JavaScript Object Notation."
  [value]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (generate-string value))))

(extend-type clojure.lang.IPersistentVector
  jdbc/ISQLParameter
  (set-parameter [v ^java.sql.PreparedStatement stmt ^long idx]
    (let [conn      (.getConnection stmt)
          meta      (.getParameterMetaData stmt)
          type-name (.getParameterTypeName meta idx)]
      (if-let [elem-type (when (= (first type-name) \_) (apply str (rest type-name)))]
        (.setObject stmt idx (.createArrayOf conn elem-type (to-array v)))
        (.setObject stmt idx (to-pg-json v))))))

(extend-protocol jdbc/ISQLValue
  IPersistentMap
  (sql-value [value] (to-pg-json value))
  IPersistentVector
  (sql-value [value] (to-pg-json value)))

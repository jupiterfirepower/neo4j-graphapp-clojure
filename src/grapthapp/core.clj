(ns grapthapp.core
  (:require [neo4j-clj.core :as db])
  (:import (java.net URI))
  (:gen-class))

(def local-db
  (db/connect (URI. "neo4j://localhost:7688")
              "neo4j"
              "test"))

(db/defquery create-user
    "CREATE (u:user $user)")

(db/defquery get-all-users
    "MATCH (u:user) RETURN u as user")

(db/defquery create-person "CREATE (u:person {name: $name, age: $age})")

(db/defquery persons-by-age "MATCH (p:person {age: $age}) RETURN p as person")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; Using a session
  (with-open [session (db/get-session local-db)]
    (create-user session {:user {:first-name "test" :last-name "test"}})
    (create-user session {:user {:first-name "test1" :last-name "test1"}})
    (create-user session {:user {:fname "ttf" :lname "ttl"}}))

  ;; Using a transaction
  (db/with-transaction local-db tx
    (create-person tx {:name "t3" :age 42})
    (create-person tx {:name "t4" :age 35})

    (println (persons-by-age tx {:age 42}))
    ;; print, as query result has to be consumed inside session
    (println (get-all-users tx)))

  (println "Hello, World!"))

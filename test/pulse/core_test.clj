(ns pulse.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [pulse.core :refer :all]
            [pulse.sample-domain :refer :all]
            [monger.core :as mg]
            [pulse.simple :as simple]
            [pulse.core :as pulse]))


(defn connect-with-params [db-config]
  "Connect to Mongodb from a map with parameters"
  (mg/connect! (merge {:host "localhost" :port 27017} db-config))
  (mg/authenticate (mg/get-db (:db db-config)) (:username db-config) (.toCharArray (:password db-config)))
  (mg/set-db! (mg/get-db (:db db-config))))

(defn init-db []
  (connect-with-params
    {
     :username "test"
     :password "test"
     :host     "localhost"
     :db       "test-pulse"}))


(defn init-tests []
  (init-db)
  (set-pulse! (simple/create-pulse-simple)))

(fact "initializes"
      (let [_ (init-tests)
            ]

        *pulse* => truthy))

(fact "creates entity"
      (let [_ (init-tests)
            id (pulse/new-entity :doc {:some-data :some-val})
            ]
        (class id) => java.lang.String
        (count id) => 40))



(fact "passes cmd"
      (let [_ (init-tests)
            id (pulse/new-entity :doc {:some-data :some-val})
            _ (pulse/pass-command id {:type :set :name "first"})
            ]

        (class id) => java.lang.String
        (count id) => 40
        ))


;(fact "passes cmd"
;      (let [_ (init-db)
;            _ (set-pulse! (simple/create-pulse-simple))
;            id (pulse/new-entity :doc {:some-data :some-val})
;            _ 1                                             ; (pulse/pass-command id :set {:name "first"})
;            ]


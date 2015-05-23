(ns pulse.simple
  (:require [pulse.core :as pulse]
            [monger.collection :as mc]
            [pulse.util :refer :all]
            [jota.core :as log]
            ))

;; Simple implementation

; data for this implementation
(def EVENT "event")
(def ENTITY "entity")

(defn- store [event]
  {:pre [(:type event) (:id event)]}
  (mc/save EVENT event))

(deftype simple-event-bus [^{:volatile-mutable true} clients]
  pulse/event-bus
  (publish [_ id event]
    {:pre [(:type event)]}
    (do
      (store event)
      (doseq [client clients] (.notify client id event))))
  (subscribe [_ cl]
    {:pre (instance? (class cl) pulse/client)}
    (set! clients (conj clients cl)))
  (unsubscribe [_ _] (throw (Exception. "not implemented")))
  )


(defn read-entity [id]
  (if-let [entity (mc/find-one-as-map ENTITY {:_id id})]
    (assoc entity :type (keyword (:type entity)))))


(defn read-events [id]
  (mc/find-maps EVENT {:id id}))



(deftype simple-command-bus []
  pulse/command-bus
  (pass-command [_ id cmd] (let [entity (read-entity id)
                                 _ (log/debug "Loaded entity with id[ " id "]: " entity)
                                 events (read-events id)
                                 entity (reduce #(pulse/process-event %1 %2) entity events)]
                             (pulse/process-command entity cmd)))
  (new-entity [_ type payload]
    (let [entity (mc/save-and-return ENTITY (assoc payload :_id (uuid) :type type))]
      (:_id entity)))

  )

(deftype logclient []
  pulse/client
  (notify [_ event] (log/debug "NOTIFIED" (:type event) ":" (:id event) ":" (:type (:payload event)))))


(defn create-pulse-simple []
  {
   :command-bus (simple-command-bus.)
   :event-bus   (simple-event-bus. [(logclient.)])
   })


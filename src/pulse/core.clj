(ns pulse.core
  (:require
    [jota.core :as log]
    [pulse.util :refer :all]
    ))

(declare ^:dynamic *pulse*)

(defn set-pulse! [pulse]
  "Sets given Pulse as default by altering *pulse* var"
  (alter-var-root (var *pulse*) (constantly pulse)))

(defprotocol event-bus
  (publish [this id event])
  (subscribe [this client])
  (unsubscribe [this client]))

(defprotocol client
  (notify [this id event]))

; Command
(defprotocol command-bus
  (new-entity [this type value])
  (pass-command [this id cmd])
  )

(defmulti process-event
          (fn[entity event] [(:type entity) (:type event)]))

(defmulti process-command
          (fn [entity cmd]
            (println "Process-command " cmd " on " entity)
            [(:type entity) (:type cmd)]))


;; Global Shortcuts


(defn pass-command [id cmd]
  {:pre [*pulse*]}
  (.pass-command (:command-bus *pulse*) id cmd))

(defn new-entity [type payload]
  {:pre [*pulse* type]}
  (.new-entity (:command-bus *pulse*) type payload))

(defn apply-event [ent evt]
  {:pre [*pulse*]}
  ;(.publish (:event-bus *pulse*) evt)
  (.process-event ent {} evt))








(ns pulse.sample-domain
  (:require
    [pulse.core :as pulse]))

(defmethod pulse/process-event [:doc :set] [entity evt]
  (merge entity (dissoc evt :type)))


(defmethod pulse/process-command [:doc :set] [entity cmd]
  (pulse/publish (:_id entity) cmd))




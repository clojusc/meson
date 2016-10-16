(ns meson.protobuf.scheduler
  "Utility functions to convert to and from scheduler API protobuf types."
  (:require [clojure.data.json :as json]
            [meson.protobuf :refer [Serializable data->pb pb->data]]
            [meson.protobuf.mesos :as mesos]
            [meson.util :refer [case-enum] :as util])
  (:import org.apache.mesos.v1.scheduler.Protos$Call
           org.apache.mesos.v1.scheduler.Protos$Call$Type
           org.apache.mesos.v1.scheduler.Protos$Call$Subscribe))

(declare ->pb)

(defmethod pb->data Protos$Call$Type
  [^Protos$Call$Type call-type]
  (case-enum call-type
    Protos$Call$Type/UNKNOWN                :unknown
    Protos$Call$Type/SUBSCRIBE              :subscribe
    Protos$Call$Type/TEARDOWN               :teardown
    Protos$Call$Type/ACCEPT                 :accept
    Protos$Call$Type/DECLINE                :decline
    Protos$Call$Type/ACCEPT_INVERSE_OFFERS  :accept-inverse-offers
    Protos$Call$Type/DECLINE_INVERSE_OFFERS :decline-inverse-offers
    Protos$Call$Type/REVIVE                 :revive
    Protos$Call$Type/KILL                   :kill
    Protos$Call$Type/SHUTDOWN               :shutdown
    Protos$Call$Type/ACKNOWLEDGE            :acknowledge
    Protos$Call$Type/RECONCILE              :reconcile
    Protos$Call$Type/MESSAGE                :message
    Protos$Call$Type/REQUEST                :request
    Protos$Call$Type/SUPPRESS               :suppress
    call-type))

(defrecord Subscribe [framework-info]
  Serializable
  (data->pb [this]
    (-> (Protos$Call$Subscribe/newBuilder)
        (.setFrameworkInfo (mesos/->pb :FrameworkInfo framework-info))
        (.build))))

(defmethod pb->data Protos$Call$Subscribe
  [^Protos$Call$Subscribe msg]
  (Subscribe. (.getFrameworkInfo msg)))

(extend-protocol Serializable
  clojure.lang.Keyword
  (data->pb [this]
    (case this
      :unknown                Protos$Call$Type/UNKNOWN
      :subscribe              Protos$Call$Type/SUBSCRIBE
      :teardown               Protos$Call$Type/TEARDOWN
      :accept                 Protos$Call$Type/ACCEPT
      :decline                Protos$Call$Type/DECLINE
      :accept-inverse-offers  Protos$Call$Type/ACCEPT_INVERSE_OFFERS
      :decline-inverse-offers Protos$Call$Type/DECLINE_INVERSE_OFFERS
      :revive                 Protos$Call$Type/REVIVE
      :kill                   Protos$Call$Type/KILL
      :shutdown               Protos$Call$Type/SHUTDOWN
      :acknowledge            Protos$Call$Type/ACKNOWLEDGE
      :reconcile              Protos$Call$Type/RECONCILE
      :message                Protos$Call$Type/MESSAGE
      :request                Protos$Call$Type/REQUEST
      :suppress               Protos$Call$Type/SUPPRESS)))

(defn ->pb
  [map-type this]
  (data->pb
   (if (record? this)
     this
     (case map-type
       :Subscribe (map->Subscribe this)))))

(ns meson.protobuf
  (:import org.apache.mesos.v1.Protos$Value
           org.apache.mesos.v1.Protos$Value$Type
           org.apache.mesos.v1.Protos$Value$Scalar
           org.apache.mesos.v1.Protos$Value$Range
           org.apache.mesos.v1.Protos$Value$Ranges
           org.apache.mesos.v1.Protos$Value$Set
           org.apache.mesos.v1.Protos$Value$Text))

;; Our two exported signatures: data->pb and pb->data

(defprotocol Serializable
  "Interface to convert from clojure data to mesos protobuf
   payloads."
  (data->pb [this]))

(extend-protocol Serializable
  java.lang.Integer
  (data->pb [this]
    (-> (Protos$Value$Scalar/newBuilder)
        (.setValue (double this))
        (.build)))
  java.lang.Long
  (data->pb [this]
    (-> (Protos$Value$Scalar/newBuilder)
        (.setValue (double this))
        (.build)))
  java.lang.Double
  (data->pb [this]
    (-> (Protos$Value$Scalar/newBuilder)
        (.setValue this)
        (.build)))
  clojure.lang.PersistentHashSet
  (data->pb [this]
    (-> (Protos$Value$Set/newBuilder)
        (.addAllItem (seq this))
        (.build)))
  java.lang.String
  (data->pb [this]
    (-> (Protos$Value$Text/newBuilder) (.setValue this) (.build))))

(defmulti pb->data
  "Open protocol to convert from mesos protobuf to clojure"
  class)

;; By default, yield the original payload.

(defmethod pb->data :default
  [this]
  this)

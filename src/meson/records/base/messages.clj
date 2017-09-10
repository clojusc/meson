(ns meson.records.base.messages
  "This namespace defines the Mesos message records derived from the Mesos
  source at the following location:
  * https://github.com/apache/mesos/blob/master/src/messages/messages.proto")

(defrecord StatusUpdate [
  ;; Required
  framework-id
  status
  timestamp
  ;; Optionsl
  executor-id
  slave-id
  uuid
  latest-state
  ])

(defrecord StatusUpdateRecord [
  ;; Required
  type ; an enum that can be one of `UPDATE` or `ACK`
  ;; Optional
  update ; an instance of the `StatusUpdate` record; required if type is `UPDATE`
  uuid ; required if type is `ACK`
  ])

(defrecord ExecutorToFrameworkMessage [
  ;; Required
  slave-id
  framework-id
  executor-id
  data
  ])

(defrecord FrameworkToExecutorMessage [
  ;; Required
  slave-id
  framework-id
  executor-id
  data
  ])

(defrecord RegisterFrameworkMessage [
  ;; Required
  framework ; an instance of the `FrameworkInfo` record)
  ;; Optional
  failover ; required when the framework has previously registered
  ])

(defrecord FrameworkRegisteredMessage [
  ;; Required
  framework-id ; an instance of the `FrameworkID` record
  master-info ; an instance of the `MasterInfo` record
  ])

(defrecord FrameworkReregisteredMessage [
  ;; Required
  framework-id ; an instance of the `FrameworkID` record
  master-info ; an instance of the `MasterInfo` record
  ])

(defrecord UnregisterFrameworkMessage [
  ;; Required
  framework-id ; an instance of the `FrameworkID` record
  ])

(defrecord DeactivateFrameworkMessage [
  ;; Required
  framework-id ; an instance of the `FrameworkID` record
  ])

(defrecord ResourceRequestMessage [
  ;; Required
  framework-id ; an instance of the `FrameworkID` record
  requests ; a sequence of `Request` record instances
  ])

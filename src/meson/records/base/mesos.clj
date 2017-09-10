(ns meson.records.base.mesos
  "This namespace defines the Mesos message records derived from the Mesos
  source at the following location:
  * https://github.com/apache/mesos/blob/master/include/mesos/mesos.proto")

(defrecord Scalar [
  ;; Required
  value ; float
  ])

(defrecord Range [
  ;; Required
  begin ; int
  end ; int
  ])

(defrecord Ranges [
  range ; a sequence of `Range` record instances
  ])

(defrecord Set [
  item ; a sequence of strings
  ])

(defrecord Text [
  ;; Required
  value ; string
  ])

(defrecord Value [
  ;; Required
  type ; one of `SCALAR`, `RANGES`, `SET`, or `TEXT`
  ;; Optional
  scalar ; an instance of the `Scalar` record
  ranges ; an instance of the `Ranges` record
  set ; an instance of the `Set` record
  text ; an instance of the `Text` record
  ])

(defrecord Attribute [
  ;; Required
  name ; string
  type ; The `type` field of a `Value` record
  ;; Optional
  scalar ; The `scalar` field of a `Value` record
  ranges ; The `ranges` field of a `Value` record
  set ; The `set` field of a `Value` record
  text ; The `text` field of a `Value` record
  ])

(defrecord FrameworkID [
  value ; string
  ])

(defrecord OfferID [
  value ; string
  ])

(defrecord SlaveID [
  value ; string
  ])

(defrecord TaskID [
  value ; string
  ])

(defrecord ExecutorID [
  value ; string
  ])

(defrecord ContainerID [
  ;; Required
  value ; string
  ;; Optional
  parent ; an instance of the `ContainerID` record
  ])

(defrecord ResourceProviderID [
  value ; string
  ])

(defrecord TimeInfo [
  nanoseconds ; int
  ])

(defrecord DurationInfo [
  nanoseconds ; int
  ])

(defrecord Address [
  ;; Required
  port ; int
  ;; Optional
  hostname ; string
  ip ; string
  ])

(defrecord Parameter [
  key ; string
  value ; string
  ])

(defrecord Parameters [
  parameter ; a sequence of `Parameter` record instances
  ])

(defrecord Label [
  key ; string
  value ; string
  ])

(defrecord Labels [
  label ; a sequence of `Label` record instances
  ])

(defrecord URL [
  ;; Required
  scheme ; string
  address ; instance of the `Address` record
  ;; Optional
  path ; string
  query ; a sequence of `Parameter` record instances
  fragment ; string
  ])

(defrecord Unavailability [
  ;; Required
  start ; an instance of the `TimeInfo` record
  ;; Optional
  duration ; an instance of the `DurationInfo` record
  ])

(defrecord MachineID [
  ;; Optional
  hostname ; string
  ip ; string
  ])

(defrecord MachineInfo [
  ;; Required
  id ; an instance of the `MachineID` record
  ;; Optional
  mode ; one of `UP`, `DRAINING`, or `DOWN`
  unavailability ; an instance of the `Unavailability` record
  ])


(defrecord FrameworkInfoCapability [
  type ; one of `UNKNOWN`, `REVOCABLE_RESOURCES`, `TASK_KILLING_STATE`
       ; `GPU_RESOURCES`, `SHARED_RESOURCES`, `PARTITION_AWARE`, `MULTI_ROLE`,
       ; `RESERVATION_REFINEMENT`, or `REGION_AWARE`
  ])

(defrecord FrameworkInfo [
  ;; Required
  user ; string
  name ; name
  ;; Optional
  id ; an instance of the `FrameworkID` record
  failover-timeout ; float, default `0.0`
  checkpoint ; bool, default `false`
  roles ; a sequence of strings
  hostname ; string
  principal ; string
  webui_url ; string
  capabilities ; a sequence of `FrameworkInfoCapability` record instances
  labels ; a sequence of `Label` record instances
  ])

(defrecord RegionInfo [
  ;; Required
  name ; string
  ])

(defrecord ZoneInfo [
  ;; Required
  name ; string
  ])

(defrecord FaultDomain [
  ;; Required
  region ; an instance of the `RegionInfo` record
  zone ; an instance of the `ZoneInfo` record
  ])

(defrecord DomainInfo [
  ;; Optional
  fault-domain
  ])

(defrecord MasterInfo [
  ;; Required
  id ; string
  ip ; int
  port ; int, default `5050`
  ;; Optional
  pid ; string
  hostname ; string
  address ; an instance of the `Address` record
  domain ; an instance of the `DomainInfo` record
  ])

(defrecord AllocationInfo [
  ;; Optional
  role ; string
  ])

(defrecord ReservationInfo [
  type ; one of `UNKNOWN`, `STATIC`, `DYNAMIC`
  role ; string
  principal ; string
  labels ; a sequence of `Label` record instances
  ])

(defrecord Resource [
  ;; Required
  name ; string
  type ; The `type` field of a `Value` record
  ;; Optional
  provider-id ; an instance of the `MachineID` record
  scalar ; The `scalar` field of a `Value` record
  ranges ; The `ranges` field of a `Value` record
  set ; The `set` field of a `Value` record
  allocation-info ; an instance of the `AllocationInfo` record
  reservation ;  an instance of the `ReservationInfo` record
  reservations ; a sequence of `ReservationInfo` record instances
  ])

(defrecord Request [
  ;; Optional
  slave-id ; an instance of the `SlaveID` record
  resources ; a sequence of `Resource` record instances
  ])

(defrecord Offer [
  ;; Required
  id ; an instance of the `OfferID` record
  ;; Optional
  ])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructors   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-framework-info
  [args]
  (map->FrameworkInfo args))

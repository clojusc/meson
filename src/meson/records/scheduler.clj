(ns meson.records.scheduler
  (:require [meson.records.base.mesos :as mesos]))

(defrecord Subscribe [
  ;; Required
  framework-info ; an instance of the `FrameworkInfo` record
  ;; Optional
  force ; bool
  suppressed-roles ; a sequence of strings
  ])

(defrecord Call [
  ;; Optional
  framework-id ; an instance of the `FrameworkID` record
  type ; one of `UNKNOWN`, `SUBSCRIBE`, `TEARDOWN`, `ACCEPT`, `DECLINE`,
       ; `ACCEPT_INVERSE_OFFERS`, `DECLINE_INVERSE_OFFERS`, `REVIVE`, `KILL`,
       ; `SHUTDOWN`, `ACKNOWLEDGE`, `RECONCILE`, `MESSAGE`, `REQUEST`, or
       ; `SUPPRESS`
  subscribe ; an instance of the `Subscribe` record
  ])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructors   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-subscribe
  [args]
  (->Subscribe
    (mesos/create-framework-info (:framework-info args))
    (:force args)
    (:suppressed-roles args)))

(defn create-call
  ([type]
    (create-call type nil))
  ([type args]
    (case type
      :subscribe (->Call (mesos/->FrameworkID (:framework-id args))
                         "SUBSCRIBE"
                         (create-subscribe args)))))

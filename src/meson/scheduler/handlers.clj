(ns meson.scheduler.handlers
  (:require [clojure.tools.logging :as log]))

(defn- debug-trace
  [type msg]
  (log/debugf "Got %s message." type)
  (log/trace msg))

(defmulti default
  "Provides a default handling for scheduler messages. No action is taken
  by the methods, rather the message types are logged (and the message
  contents are traced). The intent is to make available a simple default
  for developers creating new frameworks.

  For the types of messages that are handled, see the 'Events' section here:

  * http://mesos.apache.org/documentation/latest/scheduler-http-api/"
  (comp :type last vector))

(defmethod default :subscribed
  [state msg]
  (debug-trace "SUBSCRIBED" msg))

(defmethod default :offers
  [state msg]
  (debug-trace "OFFERS" msg))

(defmethod default :offers
  [state msg]
  (debug-trace "RESCIND" msg))

(defmethod default :offers
  [state msg]
  (debug-trace "UPDATE" msg))

(defmethod default :offers
  [state msg]
  (debug-trace "MESSAGE" msg))

(defmethod default :offers
  [state msg]
  (debug-trace "FAILURE" msg))

(defmethod default :offers
  [state msg]
  (debug-trace "ERROR" msg))

(defmethod default :heartbeat
  [state msg]
  (debug-trace "HEARTBEAT" msg))

(defmethod default :default
  [state msg]
  (debug-trace "unknown type of" msg))

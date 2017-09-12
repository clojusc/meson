(ns meson.api.scheduler.handlers
  (:require [clojusc.twig :as twig]
            [taoensso.timbre :as log]))

(defn- debug-trace
  [type msg]
  (log/debugf "Got %s message." type)
  (log/tracef "Message:\n %s" (twig/pprint msg)))

(defmulti default
  "Provides a default handling for scheduler messages. No action is taken
  by the methods, rather the message types are logged (and the message
  contents are traced). The intent is to make available a simple default
  for developers creating new frameworks.

  For the types of messages that are handled, see the 'Events' section here:

  * http://mesos.apache.org/documentation/latest/scheduler-http-api/"
  :type)

(defmethod default :subscribed
  [msg]
  (debug-trace "SUBSCRIBED" msg)
  msg)

(defmethod default :offers
  [msg]
  (debug-trace "OFFERS" msg)
  msg)

(defmethod default :rescind
  [msg]
  (debug-trace "RESCIND" msg)
  msg)

(defmethod default :update
  [msg]
  (debug-trace "UPDATE" msg)
  msg)

(defmethod default :message
  [msg]
  (debug-trace "MESSAGE" msg)
  msg)

(defmethod default :failure
  [msg]
  (debug-trace "FAILURE" msg)
  msg)

(defmethod default :error
  [msg]
  (debug-trace "ERROR" msg)
  msg)

(defmethod default :heartbeat
  [msg]
  (debug-trace "HEARTBEAT" msg)
  msg)

(defmethod default :default
  [msg]
  (debug-trace "unknown type of" msg)
  msg)

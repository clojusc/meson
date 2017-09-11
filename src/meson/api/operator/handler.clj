(ns meson.api.operator.handler
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :as twig]))

(defn- debug-trace
  [type msg]
  (log/debugf "Got %s message." type)
  (log/tracef "Message:\n %s" (twig/pprint msg)))

(defmulti default
  "Provides a default handling for operator messages. No action is taken
  by the methods, rather the message types are logged (and the message
  contents are traced). The intent is to make available a simple default
  for developers creating new frameworks.

  For the types of messages that are handled, see the 'Events' section here:

  * http://mesos.apache.org/documentation/latest/scheduler-http-api/"
  (comp :type last vector))

(defmethod default :subscribed
  [state msg]
  (debug-trace "SUBSCRIBED" msg))

(defmethod default :heartbeat
  [state msg]
  (debug-trace "HEARTBEAT" msg))

(defmethod default :task-added
  [state msg]
  (debug-trace "TASK_ADDED" msg))

(defmethod default :task-updated
  [state msg]
  (debug-trace "TASK_UPDATED" msg))

(defmethod default :framework-added
  [state msg]
  (debug-trace "FRAMEWORK_ADDED" msg))

(defmethod default :framework-updated
  [state msg]
  (debug-trace "FRAMEWORK_UPDATED" msg))

(defmethod default :framework-removed
  [state msg]
  (debug-trace "FRAMEWORK_REMOVED" msg))

(defmethod default :agent-added
  [state msg]
  (debug-trace "AGENT_ADDED" msg))

(defmethod default :agent-removed
  [state msg]
  (debug-trace "AGENT_REMOVED" msg))


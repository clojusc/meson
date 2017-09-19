(ns meson.scheduler.handlers
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :as twig]))

(defn- debug-trace
  [state payload type]
  (log/debugf "Got %s message." type)
  (log/tracef "Payload:\n %s" (twig/pprint payload))
  (log/tracef "State:\n %s" (twig/pprint state)))

(defmulti default
  "Provides a default handling for scheduler messages. The only requirement
  of a handler methods is to return the state. The `state` data strucuture
  is used by the Meson master/scheduler to provide information between client
  calls.

  Other than this, no significant action is taken by the methods, rather
  the state, messages, and message types are loggedthe first two using the
  `TRACE` log level, the last using the `DEBUG` log level).

  The intent for this particular default handler implementation is to not only
  make  available a functional example for developers creating new frameworks,
  but to also have a simple handler that logs messages and state
  out-of-the-box.

  For the types of messages that are handled, see the 'Events' section here:

  * http://mesos.apache.org/documentation/latest/scheduler-http-api/"
  (comp :type last vector))

(defmethod default :subscribed
  [state payload]
  (debug-trace state payload "SUBSCRIBED")
  state)

(defmethod default :offers
  [state payload]
  (debug-trace state payload "OFFERS")
  state)

(defmethod default :rescind
  [state payload]
  (debug-trace state payload "RESCIND")
  state)

(defmethod default :update
  [state payload]
  (debug-trace state payload "UPDATE")
  state)

(defmethod default :message
  [state payload]
  (debug-trace state payload "MESSAGE")
  state)

(defmethod default :failure
  [state payload]
  (debug-trace state payload "FAILURE")
  state)

(defmethod default :error
  [state payload]
  (debug-trace state payload "ERROR")
  state)

(defmethod default :heartbeat
  [state payload]
  (debug-trace state payload "HEARTBEAT")
  state)

(defmethod default :default
  [state payload]
  (debug-trace state payload "unknown type of")
  state)

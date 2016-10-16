(ns meson.client.impl.debug)

(defn toggle-logging [this level duration]
  :not-yet-implemented)

(defn start-profiler [this]
  :not-yet-implemented)

(defn stop-profiler [this]
  :not-yet-implemented)

(def behaviour
  {:toggle-logging toggle-logging
   :start-profiler start-profiler
   :stop-profiler stop-profiler})

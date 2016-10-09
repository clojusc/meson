(ns meson.client.agent
  (:require [clojusc.twig :as logger]
            [meson.client.impl.agent :as agent]
            [meson.client.impl.config :as config]
            [meson.client.impl.debug :as debug]
            [meson.client.impl.files :as files]
            [meson.client.impl.health :as health]
            [meson.client.protocols.agent :refer [IAgent]]
            [meson.client.protocols.common :refer
             [IConfig ; version, flags
              IDebug  ; logging profiler
              IFiles  ; all files methods
              IHealth ; metrics, system, health, monitor, metrics, state
              ]])
  (:refer-clojure :exclude [read]))

(defrecord MesonAgent [])

(extend MesonAgent IAgent agent/behaviour)
(extend MesonAgent IConfig config/behaviour)
(extend MesonAgent IDebug debug/behaviour)
(extend MesonAgent IFiles files/behaviour)
(extend MesonAgent IHealth health/behaviour)

(defn create
  ""
  []
  (->MesonAgent))

(potemkin/import-vars
  [meson.client.impl.agent
    get-container-status
    get-resource-status]
  [meson.client.impl.config
    get-version
    get-flags]
  [meson.client.impl.debug
    start-profiler
    stop-profiler]
  [meson.client.impl.files
    browse
    debug
    download
    read]
  [meson.client.impl.health
    get-health
    get-metrics
    get-state
    get-system-stats])

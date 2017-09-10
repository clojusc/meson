(ns meson.client.agent
  (:require [clojusc.twig :as logger]
            [meson.client :as client]
            [meson.client.common :as common]
            [meson.client.impl.agent :as agent]
            [meson.client.impl.config :as client-config]
            [meson.client.impl.debug :as debug]
            [meson.client.impl.files :as files]
            [meson.client.impl.health :as health]
            [meson.client.protocols.agent :refer [IAgent]]
            [meson.client.protocols.common :refer
              [IConfig IDebug IFiles IHealth]]
            [meson.config :as config]
            [potemkin :refer [import-vars]])
  (:refer-clojure :exclude [read]))

(def client-fields
  (merge
    client/fields
    {:agent config/docker-agent}))

(defrecord MesonAgent [])

(extend MesonAgent IAgent agent/behaviour)
(extend MesonAgent IConfig client-config/behaviour)
(extend MesonAgent IDebug debug/behaviour)
(extend MesonAgent IFiles files/behaviour)
(extend MesonAgent IHealth health/behaviour)

(defn create
  "A factory for the Agent client which takes a map as an arguement. If no
  map is provided, the default value of `agent/client-fields` is passed."
  ([]
    (create client-fields))
  ([fields]
    (->> fields
         (into client-fields)
         (common/check-fields)
         (common/add-host-port (:agent fields))
         (map->MesonAgent))))

(import-vars
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

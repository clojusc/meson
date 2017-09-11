(ns meson.client.master
  (:require [clojusc.twig :as logger]
            [meson.client.common :as common]
            [meson.client.impl.master.core :as master]
            [meson.client.impl.master.scheduler :as scheduler]
            [meson.client.impl.config :as client-config]
            [meson.client.impl.debug :as debug]
            [meson.client.impl.files :as files]
            [meson.client.impl.health :as health]
            [meson.config :as config]
            [meson.protocols.common :refer [IConfig IDebug IFiles IHealth]]
            [meson.protocols.master.core :refer [IMaster]]
            [meson.protocols.master.scheduler :refer [IScheduler]]
            [potemkin :refer [import-vars]])
  (:refer-clojure :exclude [read]))

(def client-fields
  (merge
    common/fields
    {:master config/docker-master}))

(defrecord MesonMaster [])

(extend MesonMaster IMaster master/behaviour)
(extend MesonMaster IScheduler scheduler/behaviour)
(extend MesonMaster IConfig client-config/behaviour)
(extend MesonMaster IDebug debug/behaviour)
(extend MesonMaster IFiles files/behaviour)
(extend MesonMaster IHealth health/behaviour)

(defn initialize
  ""
  [fields]
  (->> fields
       (into client-fields)
       (common/check-fields)
       (common/add-host-port (:master fields))
       (map->MesonMaster)))

(defn create
  "A factory for the Mater client which takes a map as an arguement. If no
  map is provided, the default value of `master/client-fields` is passed."
  ([]
    (create client-fields))
  ([fields & {:keys [start?]}]
    (let [c (initialize fields)]
      (if start?
        (scheduler/start c)
        c))))

(import-vars
  [meson.client.impl.master.core
    bring-down-machines
    bring-up-machines
    create-volumes
    destroy-volumes
    get-agents
    get-frameworks
    get-maintenance-schedule
    get-maintenance-status
    get-quota
    get-registry
    get-roles
    get-state-summary
    get-tasks
    redirect
    remove-quota
    reserve
    set-quota
    teardown-framework
    unreserve
    update-maintenance-scheduler
    update-role-weights]
  [meson.client.impl.master.scheduler
    accept
    acknowledge
    decline
    kill-task
    message
    reconcile
    request
    revive
    shutdown-executor
    start
    stop
    subscribe
    teardown]
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

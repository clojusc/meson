(ns meson.scheduler
  (:require [meson.client :as client])
  (:import [meson.client BaseClient BaseClientAPI]))

(def client-fields
  (into
    client/base-client-fields
    {:endpoint "/scheduler"}))

(defprotocol SchedulerClientAPI
  "The scheduler interacts with Mesos via the /api/v1/scheduler master
  endpoint. This endpoint accepts HTTP POST requests with data encoded as JSON,
  `Content-Type: application/json`, or binary Protobuf,
  `Content-Type: application/x-protobuf`."
  (get-context [this]
    "Get the context for this client, calculated using `:base-path` and
    `:version`.")
  (subscribe [self]
    "This is the first step in the communication process between the
    scheduler and the master. This is also to be considered as subscription
    to the “/scheduler” events stream."))

(def scheduler-behaviour
  {:get-context (fn [this]
    (format "%s%s" (client/get-context this) (:endpoint this)))
   :subscribe (fn [this] (str "POST to scheduler REST service ..."))})

(extend BaseClient SchedulerClientAPI scheduler-behaviour)

(defn ->client
  "A factory for the Scheduler client which takes a map as an arguement. If no
  map is provided, the default value of `scheduler/client-fields` is passed.
  Additionally, if no value for the `event-conn` and `cmd-conn` connections
  fields is provided, default connections are created."
  ([]
    (->> {:event-conn "do some http client stuff here ..."
          :cmd-conn "do some http client stuff here ..."}
         (into client-fields)
         (->client)))
  ([fields]
    (client/map->BaseClient fields)))


(ns meson.scheduler
  (:require [clojure.data.json :as json]
            [meson.client :as client]
            [meson.http :as http]
            [meson.types.json :as j-types])
  (:import meson.client.Client
           meson.client.ClientAPI))

(def client-fields
  (into
    client/fields
    {:master "localhost:5050"
     :endpoint "/scheduler"}))

(defprotocol SchedulerAPI
  "The scheduler interacts with Mesos via the /api/v1/scheduler master
  endpoint. This endpoint accepts HTTP POST requests with data encoded as JSON,
  `Content-Type: application/json`, or binary Protobuf,
  `Content-Type: application/x-protobuf`."
  (subscribe [self data]
    "This is the first step in the communication process between the
    scheduler and the master. This is also to be considered as subscription
    to the “/scheduler” events stream."))

(def scheduler-behaviour
  {:subscribe (fn [this data]
    (http/post
      this
      :body (json/write-str
              {:type :SUBSCRIBE
               :subscribe (j-types/->map :FrameworkInfo data)})
      :options {} ))});{:as :stream}))})

;; XXX This doesn't seem to be extending ... ?
(extend meson.client.Client SchedulerAPI scheduler-behaviour)

(defn ->client
  "A factory for the Scheduler client which takes a map as an arguement. If no
  map is provided, the default value of `scheduler/client-fields` is passed.
  Additionally, if no value for the `event-conn` and `cmd-conn` connections
  fields is provided, default connections are created."
  ([]
    (->client {}))
  ([fields]
    (->> fields
         (into {:event-conn "do some http client stuff here ..."
                :cmd-conn "do some http client stuff here ..."})
         (into client-fields)
         (client/check-fields)
         (client/map->Client))))


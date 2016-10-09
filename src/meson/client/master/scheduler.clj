(ns meson.client.master.scheduler
  (:require [clojure.data.json :as json]
            [meson.client :as client]
            [meson.client.master :as master]
            [meson.http :as http]
            [meson.protobuf.mesos :as mesos])
  (:import [meson.client.ClientAPI]))

(def client-fields
  (merge
    master/client-fields
    {:endpoint "/scheduler"
     :base-path "/api"}))

(defprotocol SchedulerAPI
  "The scheduler interacts with Mesos via the /api/v1/scheduler master
  endpoint. This endpoint accepts HTTP POST requests with data encoded as JSON,
  `Content-Type: application/json`, or binary Protobuf,
  `Content-Type: application/x-protobuf`."
  (get-context [this]
    "Get the context for this client, calculated using `:base-path` and
    `:version`.")
  (get-url [this path]
    "Get the context-based url for the client.")
  (get-version [this]
    "Get the client version.")
  (subscribe [this data]
    "This is the first step in the communication process between the
    scheduler and the master. This is also to be considered as subscription
    to the “/scheduler” events stream."))

(defrecord SchedulerClient [])

(def client-behaviour
  (merge
    client/client-behaviour
    {:get-context (fn [this]
       (format "%s/v%s%s" (:base-path this) (:version this) (:endpoint this)))
     :get-url (fn [this path]
       (format "%s://%s:%s%s%s"
         (:scheme this) (:host this) (:port this) (get-context this) path))
     :subscribe (fn [this data]
       (http/post
         this
         ""
         :body (json/write-str
                 {:type :SUBSCRIBE
                  :subscribe (mesos/->map :FrameworkInfo data)})
         :options {}))}));{:as :stream}))})

(extend SchedulerClient SchedulerAPI client-behaviour)

(defn ->client
  "A factory for the Scheduler client which takes a map as an arguement. If no
  map is provided, the default value of `scheduler/client-fields` is passed.
  Additionally, if no value for the `event-conn` and `cmd-conn` connections
  fields is provided, default connections are created."
  ([]
    (->client client-fields))
  ([fields]
    (->> fields
         (into {:event-conn "do some http client stuff here ..."
                :cmd-conn "do some http client stuff here ..."})
         (into client-fields)
         (client/check-fields)
         (client/add-host-port (:master fields))
         (map->SchedulerClient))))


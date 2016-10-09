(ns meson.client.master
  (:require [clojusc.twig :as logger]
            [meson.client :as client]))

(def client-fields
  (merge
    client/fields
    {:master "localhost:5050"}))

(defprotocol MasterAPI
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

(defrecord MasterClient [])

(extend MasterClient MasterAPI client/client-behaviour)

(defn ->client
  "A factory for the Mater client which takes a map as an arguement. If no
  map is provided, the default value of `master/client-fields` is passed."
  ([]
    (->client client-fields))
  ([fields]
    (->> fields
         (into client-fields)
         (client/check-fields)
         (client/add-host-port (:master fields))
         (map->MasterClient))))

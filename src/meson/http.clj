(ns meson.http
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clj-http.client :as httpc]
            [clojusc.twig :refer [pprint]]
            [meson.client.base :as base]
            [meson.protobuf.mesos :as pb-mesos]
            [meson.util :as util])
  (:refer-clojure :exclude [get]))

(def json-content-type "application/json")
(def protobuf-content-type "application/x-protobuf")

(defn convert-data
  ""
  [content-type record-name body]
  (case content-type
    json-content-type (pb-mesos/map->json record-name body)
    protobuf-content-type (pb-mesos/->map record-name body)))

(defn merge-options
  ""
  ([c opts]
    (merge-options c opts {}))
  ([c opts overrides]
    (->> overrides
         (into opts)
         (into (:options c)))))

(defn parse-response
  ""
  [{:keys [headers status body status-only] :as response}]
  (case (headers "Content-Type")
    "application/json" (json/read-str body :key-fn keyword)
    (if status-only
      status
      response)))

(defn delete
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (httpc/delete
      (base/get-url c path)
      options)))

(defn get
  ""
  [c path & {:keys [opts status-only] :as kwargs}]
  (-> (base/get-url c path)
      (httpc/get (:options (merge-options c opts)))
      (into {:status-only status-only})
      (parse-response)))

(defn post
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (httpc/post
      (base/get-url c path)
      options)))

(defn put
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (httpc/put
      (base/get-url c path)
      options)))

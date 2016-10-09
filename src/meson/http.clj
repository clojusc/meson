(ns meson.http
  (:require [clojure.tools.logging :as log]
            [clj-http.client :as httpc]
            [clojusc.twig :refer [pprint]]
            [meson.types.json :as j-types]
            [meson.types.protobuf :as p-types]
            [meson.util :as util])
  (:refer-clojure :exclude [get]))

(defn convert-data
  ""
  [content-type record-name body]
  (case content-type
    "application/json" (j-types/map->json record-name body)
    "application/x-protobuf" (p-types/->map record-name body)))

(defn merge-options
  ""
  ([c opts]
    (merge-options c opts {}))
  ([c opts overrides]
    (->> overrides
         (into opts)
         (into (:options c)))))

(defn get
  ""
  [c url & {:keys [opts]}]
  (httpc/get
    url
    (:options (merge-options c opts))))

(defn post
  ""
  [c url & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (httpc/post
      url
      options)))

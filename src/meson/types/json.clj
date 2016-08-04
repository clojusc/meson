(ns meson.types.json
  (:require [clojure.data.json :as json]
            [meson.types.protobuf :as p-types]
            [meson.util :as util]))

(defn ->map
  ""
  [^clojure.lang.Keyword record-name ^clojure.lang.PersistentArrayMap data]
  (->> data
       (p-types/->map record-name)
       (hash-map (util/camel->under record-name))))

(defn map->json
  "Given a type's record name (as a keyword) and a map for the type's data,
  return a nested JSON string containing the passed data and any default
  values provided by Mesos."
  [^clojure.lang.Keyword record-name ^clojure.lang.PersistentArrayMap data]
  (->> data
       (->map record-name)
       (json/write-str)))

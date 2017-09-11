(ns meson.records.core
  (:require [meson.util.core :as util]))

(defn prepare-element
  [element]
  (->> element
       (map util/mesosize-key)
       (filter second)
       (into {})))

(defn prepare-data
  [data]
  (util/walk-keys data prepare-element))

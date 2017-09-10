(ns meson.records.core
  (:require [clojure.walk :refer [postwalk]]
            [meson.util :as util]))

(defn prepare-element
  [element]
  (->> element
       (map util/mesosize-key)
       (filter second)
       (into {})))

(defn prepare-data
  [data]
  (postwalk (fn [x] (if (map? x) (prepare-element x) x)) data))

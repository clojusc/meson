(ns meson.client.impl.health
  (:require [meson.http.core :as http]))

(defn get-health
  ([this]
    (http/get this "health" :status-only true))
  ([this id]
    (http/get this
              (format "slave(%s)/health" id)
              :status-only true)))

(defn get-metrics
  ([this]
    (http/get this "metrics/snapshot"))
  ([this timeout]
    (http/get this (format "metrics/snapshot?timeout=%s" timeout))))

(defn get-state
  ([this]
    (http/get this "state"))
  ([this id]
    (http/get this (format "slave(%s)/state" id))))

(defn get-system-stats [this]
  (http/get this "system/stats.json"))

(def behaviour
  {:get-health get-health
   :get-metrics get-metrics
   :get-state get-state
   :get-system-stats get-system-stats})

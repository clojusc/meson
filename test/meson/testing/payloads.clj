(ns meson.testing.payloads
  (:require [meson.util.bytes :as util]))

(defn subscribe
  []
  (-> (str "139\n"
           "{\"type\":\"SUBSCRIBE\","
           "\"subscribe\":{\"framework_info\":{"
                               "\"user\":\"Alice\","
                               "\"name\":\"Test Clojure\\/HTTP Framework (Clojure)\","
                               "\"hostname\":\"127.0.0.1\"}}}")
      (util/str->stream)))

(defn subscribed
  []
  (-> (str "139\n"
           "{\"type\":\"SUBSCRIBED\","
           "\"subscribed\":{\"framework_id\":{"
                                "\"value\":\"5f1bf690-0145-44be-96ae-145e5c51fccc-0017\"},"
                           "\"heartbeat_interval_seconds\":15.0}}")
      (util/str->stream)))

(defn heartbeat
  []
  (-> (str "20\n"
           "{\"type\":\"HEARTBEAT\"}")
      (util/str->stream)))

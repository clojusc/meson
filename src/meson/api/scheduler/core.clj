(ns meson.api.scheduler.core
  (:require [clj-http.client :as httpc]
            [clojure.core.async :as async]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojusc.twig :as twig]
            [meson.api.scheduler.handlers :as scheduler-handlers]
            [meson.http.core :as http]
            [meson.records.core :as records]
            [meson.records.scheduler :as message]
            [meson.util.core :as util]
            [meson.util.recordio :as recordio]
            [taoensso.timbre :as log]))

;; XXX Need to move this code into mesos client scheduler implementation and
;;     then do a potemkin import here

(defn prepare-element
  [element]
  (->> element
       (map util/clojurize-key)
       (into {})))

(defn parse-data
  [stream]
  (-> stream
      (recordio/next! :json)
      (util/walk-keys prepare-element)))

(defn stream->msgs
  "As data is available on the stream, parse it and send the results to the
  given channel."
  [chan stream]
  (async/go
    (loop []
      (async/>! chan (parse-data stream))
      (recur))
    (async/close! chan)))

(defn msgs->handler
  "When messages are received on the channel, pass them to the given handler."
  [chan handler]
  (async/go
    (loop []
      (when-let [received (async/<! chan)]
        (handler received)
        (recur)))))

(defn subscribe
  "Subscribe a framework to a Mesos master scheduler.
   Provide a framework [`args`] or a minimal default is used.
   Provide handlers [`handler`] or defaults are used which log messages received only.
   Provide a url to the mesos master [`mesos-master`] scheduler api or localhost is used."
  ;; XXX arity-0 is just temporary, to ease dev
  ([]
    (subscribe {:framework-info {
                :user "user1"
                :name "a-framework"}}))
  ([args]
    (subscribe args scheduler-handlers/default))
  ([args handler]
    (subscribe args handler "http://localhost:5050/api/v1/scheduler"))
  ([args handler mesos-master]
    (log/info "args:" args)
    (let [chan (async/chan)
          data (message/create-call :subscribe args)
          prepared-data (records/prepare-data data)
          response (httpc/post mesos-master
                               (assoc http/stream-options
                                      :form-params prepared-data))
          stream (:body response)]
        (stream->msgs chan stream)
        (msgs->handler chan handler)
      chan)))

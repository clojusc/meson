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

(defn subscribe
  ;; XXX arity-0 is just temporary, to ease dev
  ([]
    (subscribe {:framework-info {
                :user "user1"
                :name "a-framework"}}))
  ([args]
    (subscribe args scheduler-handlers/default))
  ([args handler]
    (log/info "args:" args)
    (let [chan (async/chan)
          data (message/create-call :subscribe args)
          prepared-data (records/prepare-data data)
          response (httpc/post "http://localhost:5050/api/v1/scheduler"
                               {:accept :json
                                :content-type :json
                                :form-params prepared-data
                                :throw-exceptions false
                                :as :stream})
          stream (:body response)]
        ;; XXX move into dedicated function
        ;; Write the messages to the channel as they come in
        (async/go
          (loop []
            (async/>! chan (parse-data stream))
            (recur))
          (async/close! chan))
        ;; XXX move into dedicated function
        ;; When a message is received, call the handler
        (async/go
          (loop []
            (when-let [received (async/<! chan)]
              (handler received)
              (recur))))
      chan)))

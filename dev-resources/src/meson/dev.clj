(ns meson.dev
  (:require [clojure.core.async :as async :refer [<! >!]]
            [clojure.java.io :as io]
            [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [clojure.walk :refer [macroexpand-all]]
            ;; http
            [clj-http.client :as httpc]
            ;; data
            [clojure.data.json :as json]
            ;; data types
            [clojure.data.codec.base64 :as b64]
            [clojusc.twig :as twig]
            ;; meson
            [meson.client :as client]
            [meson.client.impl.master.scheduler :as scheduler]
            [meson.client.master :as master]
            [meson.core :as meson]
            [meson.error :as error]
            [meson.http :as http]
            [meson.protobuf :as protobuf]
            [meson.protobuf.mesos :as pb-mesos]
            [meson.protobuf.scheduler :as pb-scheduler]
            [meson.scheduler.handlers :refer [default]
                                      :rename {default default-handler}]
            [meson.util :as util]
            [meson.util.recordio :as recordio]))

(twig/set-level! '[meson] :debug)

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))

;;; Aliases

(def reload #'repl/refresh)


;;; XXX

(defn tryit
 []

(def host "172.29.92.1")
(def subscribe-state {})
(def testing-master
  (merge client/fields {
    :master (format "%s:5050" host)
    :options {
      :debug false
      :debug-body false
      :throw-entire-message? true
      :log-level :debug}}))
(def c (master/create testing-master :start? true))

(def framework-info {
  :framework_info {
    :user "oubiwann"
    :name "Test Clojure/HTTP Framework (Clojure)"
    :hostname host}})

(defn get-accept-info
  [agent-id framework-id offer-id resources]
  {:offer_ids [offer-id]
   :operations [{
     :type "LAUNCH"
     :launch {
        :task_infos [{
          :name "My Task"
          :task_id (util/get-uuid)
          :agent_id agent-id
          :executor {
            :executor_id (util/get-uuid)
            :command {
              :shell true
              :value "cal 2017"}
            :resources [{:name "cpus" :role "*" :type "SCALAR" :scalar {:value 1.0}}
                        {:name "mem" :role "*" :type "SCALAR" :scalar {:value 128.0}}]}
          :resources [{:name "cpus" :role "*" :type "SCALAR" :scalar {:value 1.0}}
                      {:name "mem" :role "*" :type "SCALAR" :scalar {:value 128.0}}]}]}}]})

(defn handle-offers
  [state payload]
  (let [offers (get-in payload [:offers :offers])
        agent-id (get-in offers [0 :agent_id])
        framework-id (get-in offers [0 :framework_id])
        offer-id (get-in offers [0 :id])
        resources (get-in offers [0 :resources])
        accept-info (get-accept-info
                      agent-id framework-id offer-id resources)
        new-state (assoc state :framework-id framework-id)
        response (master/accept c new-state accept-info)]
    (assoc new-state :response response)))

(defn handle-update
  [state payload]
  (let [status (get-in payload [:update :status :state])
        source (get-in payload [:update :status :source])
        reason (get-in payload [:update :status :reason])
        client (:client state)]
    (case status
      "TASK_FAILED" (let [response (master/teardown (:client state) state)]
                      (log/errorf "Got %s from %s (%s)"
                                  status source reason)
                      (async/close! (:channel state))
                      (assoc state :response response
                                   :stream nil
                                   :channel nil
                                   :client (master/stop client)))
      state)))

(defn handle-failure
  [state payload]
  (let [framework-id (:framework-id state)
        stream-id (:stream-id state)
        response (master/teardown (:client state) state)]
    (assoc state :response response
                 :stream nil)))

(defmethod default-handler :offers
  [state payload]
  (log/tracef "Offer payload:\n%s" (twig/pprint payload))
  (handle-offers state payload))

(defmethod default-handler :update
  [state payload]
  (log/infof "Update payload:\n%s" (twig/pprint payload))
  (handle-update state payload))

(defmethod default-handler :heartbeat
  [state payload]
  state)

(defmethod default-handler :failure
  [state payload]
  (log/tracef "Failure payload:\n%s" (twig/pprint payload))
  (log/tracef "Failure state:\n%s" (twig/pprint state))
  (log/errorf "Failure from executor %s with exit status %s"
              (get-in payload [:failure :executor_id :value])
              (get-in payload [:failure :status]))
  ;(handle-failure state payload))
  state)

(defmethod default-handler :default
  [state payload]
  (log/warn "Unknown message!")
  (log/debug "State:" state)
  (log/debug "Payload:" payload)
  state)

(defn read-stream [ch input-stream]
  (async/go-loop [stream input-stream]
    (if-not stream
      :done
      (do
        (async/put! ch (recordio/next! stream :json))
        (recur stream)))))

(def response (master/subscribe c subscribe-state framework-info))

;(log/debug "Got response in `tryit`:" response)

(def stream (http/get-response-body response))
(def ch (async/chan))

(async/reduce
  default-handler
  {:client c
   :stream stream
   :channel ch
   :response response
   :stream-id (http/get-response-stream-id response)
   :framework-id nil}
  ch)

(read-stream ch stream)

) ; end of `tryit`

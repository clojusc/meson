(ns meson.client.impl.master.scheduler
  (:require [clj-http.conn-mgr :as http-conn-mgr]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [meson.http.core :as http]
            [meson.util.core :as util])
  (:import [clojure.lang Keyword]))

(def scheduler-path "api/v1/scheduler")
(def default-scheduler-timeout 10)
(def default-scheduler-threads 4)
(def default-scheduler-opts
  {:timeout default-scheduler-timeout
   :threads default-scheduler-threads})

(defn start
  ""
  ([this]
    (start this {}))
  ([this opts]
    (log/debug "Starting connection manager for the scheduler ...")
    (->> opts
         (into default-scheduler-opts)
         (http-conn-mgr/make-reusable-conn-manager)
         (assoc this :conn-mgr))))

(defn stop
  ""
  [this]
  (log/debug "Stopping connection manager for the scheduler ...")
  (http-conn-mgr/shutdown-manager (:conn-mgr this)))

(defn call
  ([this ^Keyword type]
    (call this type nil nil))
  ([this ^Keyword type payload framework-id]
    (call this type payload framework-id http/json-content-type))
  ([this ^Keyword type payload framework-id content-type]
    (call this type payload framework-id content-type {}))
  ([this ^Keyword type payload framework-id content-type opts]
    (let [data {:type (util/keyword->upper type)
                (util/keyword->lower type) payload}]
      (http/post
        this
        scheduler-path
        :body (json/write-str (merge data (or framework-id {})))
        :opts (into opts {:content-type content-type
                          :accept content-type})))))

(defn accept
  ([this payload stream-id framework-id]
    (accept this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :accept
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn acknowledge
  ([this payload stream-id framework-id]
    (acknowledge this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :acknowledge
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn decline
  ([this payload stream-id framework-id]
    (decline this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :decline
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn kill-task
  ([this payload stream-id framework-id]
    (kill-task this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :kill
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn message
  ([this payload stream-id framework-id]
    (message this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :message
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn reconcile
  ([this payload stream-id framework-id]
    (reconcile this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :reconcile
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn request
  ([this payload stream-id framework-id]
    (request this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :request  ; type is request, json keyword is requests (plural)
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn revive
  ([this payload stream-id framework-id]
    (revive this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :revive
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn shutdown-executor
  ([this payload stream-id framework-id]
    (shutdown-executor this payload stream-id framework-id http/json-content-type))
  ([this payload stream-id framework-id content-type]
    (call
      this
      :shutdown
      payload
      framework-id
      content-type
      {:connection-manager (:conn-mgr this)
       :headers {:mesos-stream-id stream-id}})))

(defn subscribe
  ([this payload]
    (subscribe this payload http/json-content-type))
  ([this payload content-type]
    (call
      this
      :subscribe
      payload
      nil
      content-type
      {:as :stream
       :streaming? true
       :chunked? true
       :connection http/keep-alive
       :connection-manager (:conn-mgr this)})))

(defn teardown
  ([this stream-id framework-id]
    (teardown this stream-id framework-id http/json-content-type))
  ([this stream-id framework-id content-type]
    (call
      this
      :teardown
       nil
       framework-id
       content-type
       {:connection-manager (:conn-mgr this)
        :headers {:mesos-stream-id stream-id}})))

(def behaviour
  {:accept accept
   :acknowledge acknowledge
   :decline decline
   :kill-task kill-task
   :message message
   :reconcile reconcile
   :request request
   :revive revive
   :subscribe subscribe
   :shutdown-executor shutdown-executor
   :teardown teardown})

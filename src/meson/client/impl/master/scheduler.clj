(ns meson.client.impl.master.scheduler
  (:require [clj-http.conn-mgr :as http-conn-mgr]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [meson.error :as error]
            [meson.http :as http]
            [meson.http.status-code :as status-code]
            [meson.util :as util])
  (:import [clojure.lang Keyword]))

(def scheduler-path "api/v1/scheduler")
(def default-scheduler-timeout 10)
(def default-scheduler-threads 4)
(def default-scheduler-opts
  {:timeout default-scheduler-timeout
   :threads default-scheduler-threads})

(defn get-framework-id-payload
  ""
  [state]
  (if-let [framework-id (:framework-id state)]
    {:framework_id framework-id}
    {}))

(defn start
  ""
  ([master]
    (start master {}))
  ([master opts]
    (log/debug "Starting connection manager for the scheduler ...")
    (->> opts
         (into default-scheduler-opts)
         (http-conn-mgr/make-reusable-conn-manager)
         (assoc master :conn-mgr))))

(defn stop
  ""
  [master]
  (log/debug "Stopping connection manager for the scheduler ...")
  (http-conn-mgr/shutdown-manager (:conn-mgr master))
  (assoc master :conn-mgr nil))

(defn- call
  ""
  [type master state payload opts]
  (log/debug "Making general scheduler call ...")
  (log/trace "Got state:" state)
  ;; XXX move this data-generation into a dedicated function, complete
  ;; with conditional handling for differnet types of payloads
  (let [data (merge {:type (util/convert-upper type)
                     (util/convert-lower type) payload}
                    (get-framework-id-payload state))]
    (log/trace "Got payload data:" data)
    ;; Note - all scheduler calls are POSTs
    (http/post
      master
      scheduler-path
      :body (json/write-str data)
      :opts (-> (http/get-default-options master)
                (http/update-conn-mgr (:conn-mgr master))
                (http/update-default-headers (:stream-id state))
                (http/add-option-overrides opts)))))

(defn accept
  ([master state payload]
    (accept master state payload {}))
  ([master state payload opts]
    (call :accept master state payload opts)))

(defn acknowledge
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn decline
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn kill-task
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn message
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn reconcile
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn request
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn revive
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn shutdown-executor
  ([master state payload]
    :not-yet-implemented)
  ([master state payload opts]
    :not-yet-implemented))

(defn subscribe
  ([master state payload]
    (subscribe master state payload {}))
  ([master state payload opts]
    (call :subscribe master state payload
          (assoc opts :as :stream
                      :streaming? true
                      :chunked? true
                      :connection http/keep-alive))))

(defn teardown
  ([master state]
    (teardown master state {}))
  ([master state opts]
    (call :teardown master state {} opts)))

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

;;; Error Handling ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(error/add-handler
  #'call
  java.lang.Exception
  error/process-exception
  error/meson-scheduler-error)

(error/add-handler
  #'call
  [:status status-code/bad-request]
  error/process-http-client-exception
  error/meson-scheduler-error)

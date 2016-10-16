(ns meson.client.impl.master.scheduler
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [meson.http :as http]))

(defn convert-upper
  [type]
  (-> type
      (name)
      (string/upper-case)))

(defn convert-lower
  [type]
  (-> type
      (name)
      (string/lower-case)))

(defn call
  ([this type]
    (call this type nil))
  ([this type payload]
    (call this type payload "application/json")) ; put in a const
  ([this type payload content-type]
    (call this type payload content-type {}))
  ([this type payload content-type opts]
    (let [data {:type (convert-upper type)
                (convert-lower type) payload}]
      (http/post
        this
        "api/v1/scheduler"
        :body (json/write-str data)
        :opts (into opts {:content-type content-type
                          :accept content-type})))))

(defn accept
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn acknowledge
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn decline
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn kill-task
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn message
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn reconcile
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn request
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn revive
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn shutdown-executor
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(defn subscribe
  ([this payload]
    (subscribe this payload "application/json"))
  ([this payload content-type]
    (call
      this
      :subscribe
      payload
      content-type
      {:streaming? true
       :chunked? true
       :connection "keep-alive"})))

(defn teardown
  ([this payload]
    :not-yet-implemented)
  ([this payload content-type]
    :not-yet-implemented))

(def behaviour
  {:subscribe subscribe
   :teardown teardown
   :accept accept
   :decline decline
   :revive revive
   :kill-task kill-task
   :shutdown-executor shutdown-executor
   :acknowledge acknowledge
   :reconcile reconcile
   :message message
   :request request})

(ns meson.http
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clj-http.client :as httpc]
            [clojusc.twig :refer [pprint]]
            [meson.client.common :as common]
            [meson.error :as error]
            [meson.http.status-code :as status-code]
            [meson.protobuf.mesos :as pb-mesos]
            [meson.util :as util])
  (:refer-clojure :exclude [get]))

;;; HTTP Constants ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def json-content-type "application/json")
(def protobuf-content-type "application/x-protobuf")
(def keep-alive "keep-alive")

;; XXX Delete this function when we get rid of protobugs ... but remember
;; that the pb conversion functions will be put in meson.util.mesomatic,
;; and we might want to provide a means for users to easily perform those
;; conversions in the context of calls to the Mesos HTTP API ...
(defn convert-data
  ""
  [content-type record-name body]
  (case content-type
    json-content-type (pb-mesos/map->json record-name body)
    protobuf-content-type (pb-mesos/->map record-name body)))

;;; Headers Management ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn add-accept
  ""
  [headers content-type]
  (if content-type
    (assoc headers :accept content-type)
    headers))

(defn add-content-type
  ""
  [headers content-type]
  (if content-type
    (assoc headers :content-type content-type)
    headers))

(defn add-stream-id
  ""
  [headers stream-id]
  (if stream-id
    (assoc headers :mesos-stream-id stream-id)
    headers))

(defn add-default-headers
  ""
  [headers content-type stream-id]
  (-> headers
      (add-accept content-type)
      (add-content-type content-type)
      (add-stream-id stream-id)))

(defn get-content-type-header
  ""
  [headers]
  (headers "Content-Type"))

(defn get-stream-id-header
  ""
  [headers]
  (headers "Mesos-Stream-Id"))

;;; Options Management ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-default-options
  ""
  [c]
  (:options c))

(defn add-option-overrides
  ""
  [options overrides]
  (merge options overrides))

(defn get-options-headers
  ""
  [options]
  (or (:headers options) {}))

(defn get-content-type-option
  ""
  [options]
  (or (:content-type options) json-content-type))

(defn update-conn-mgr
  ""
  [options conn-mgr]
  (assoc options :connection-manager conn-mgr))

(defn update-default-headers
  ""
  [options stream-id]
  (let [content-type (get-content-type-option options)]
    (assoc options :headers
                   (-> options
                       (get-options-headers)
                       (add-default-headers content-type stream-id)))))

;;; Response Management ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-response
  ""
  [{:keys [headers status body status-only] :as response}]
  (case (headers "Content-Type")
    "application/json" (json/read-str body :key-fn keyword)
    (if status-only
      status
      response)))

(defn get-response-headers
  ""
  [response]
  (:headers response))

(defn get-response-body
  ""
  [response]
  (:body response))

(defn get-response-stream-id
  ""
  [response]
  (-> response
      (get-response-headers)
      (get-stream-id-header)))

(defn get-response-content-type
  ""
  [response]
  (-> response
      (get-response-headers)
      (get-content-type-header)))


;;; HTTP Verb Management ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- get-http-verb-func
  ""
  [verb]
  (case verb
    :delete httpc/delete
    :get httpc/get
    :post httpc/post
    :put httpc/put))

(defn- call
  "This private function was created in order that there would be one, single
  function that all HTTP-verb-based functions call in order to make error
  handling easier. With this function defined, we only ever have to wrap this
  function with error handlers, instead of all of the HTTP verb functions."
  [verb url options]
  (log/trace "HTTP call options:" (pprint options))
  ((get-http-verb-func verb) url options))

(defn delete
  ""
  [c path & {:keys [body opts]}]
  (call :delete
        (common/get-url c path)
        opts))

(defn get
  ""
  [c path & {:keys [opts status-only] :as kwargs}]
  (as-> (common/get-url c path) data
        (call :get
              data
              opts)
        (into data {:status-only status-only})
        (parse-response data)))

(defn post
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge opts {:body body})]
    (call :post
          (common/get-url c path)
          options)))

(defn put
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge opts {:body body})]
    (call :put
          (common/get-url c path)
          options)))

;;; Error Handling ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(error/add-handler
  #'call
  java.net.ConnectException
  error/process-exception
  error/no-master-connection)

(error/add-handler
  #'call
  java.lang.NullPointerException
  error/process-exception
  error/null-pointer)

(error/add-handler
  #'call
  [:status status-code/bad-request]
  error/process-http-client-exception
  error/mesos-http-client-error)

(error/add-handler
  #'parse-response
  java.lang.NullPointerException
  error/process-exception
  error/bad-parse-value)

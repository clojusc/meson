(ns meson.http
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clj-http.client :as httpc]
            [clojusc.twig :refer [pprint]]
            [meson.client.common :as common]
            [meson.error :as error]
            [meson.http.status-code :as status-code]
            [meson.util :as util])
  (:refer-clojure :exclude [get]))

(def json-content-type "application/json")
(def keep-alive "keep-alive")

(defn merge-options
  ""
  ([c opts]
    (merge-options c opts {}))
  ([c opts overrides]
    (->> overrides
         (into opts)
         (into (:options c)))))

(defn parse-response
  ""
  [{:keys [headers status body status-only] :as response}]
  (case (headers "Content-Type")
    "application/json" (json/read-str body :key-fn keyword)
    (if status-only
      status
      response)))

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
  ((get-http-verb-func verb) url options))

(defn delete
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (call :delete
          (common/get-url c path)
          options)))

(defn get
  ""
  [c path & {:keys [opts status-only] :as kwargs}]
  (as-> (common/get-url c path) data
        (call :get data (:options (merge-options c opts)))
        (into data {:status-only status-only})
        (parse-response data)))

(defn post
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (call :post
          (common/get-url c path)
          options)))

(defn put
  ""
  [c path & {:keys [body opts]}]
  (let [options (merge-options c opts {:body body})]
    (log/debug "Options:" (pprint options))
    (call :put
          (common/get-url c path)
          options)))

(error/add-handler
  #'call
  java.net.ConnectException
  status-code/bad-gateway)

(error/add-handler
  #'call
  [:status status-code/bad-request]
  error/mesos-client-error)

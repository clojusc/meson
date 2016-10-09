(ns meson.client
  (:require [clojure.string :as string]
            [clojusc.twig :as logger]
            [meson.client.base :as base]
            [meson.const :as const]
            [meson.http :as http]))

(def user-agent
  (format "Meson REST Client/%s (Clojure %s; Java %s) (+%s)"
          const/client-version
          const/clj-version
          const/java-version
          const/project-url))

(def fields
  "Fields are maintained separately from the record so that they may be
  re-used by concrete clients (e.g., Scheduler and Executor clients)."
  {:host nil
   :port nil
   :master nil
   :agent nil
   :base-path "/"
   :endpoint ""
   :version "1"
   :scheme "http"
   :event-conn nil
   :cmd-conn nil
   :options {
     ;:content-type "application/json" ; or application/x-protobuf
     ;:accept "application/json"
     :content-type "application/x-protobuf" ; or application/x-protobuf
     :accept "application/x-protobuf"
     :debug true
     :debug-body true
     :throw-entire-message? true
     :log-level :info
     :headers {:user-agent user-agent}
     :client-params {
       "http.useragent" user-agent}}})

(defprotocol ClientAPI
  (get-context [this]
    "Get the context for this client, calculated using `:base-path` and
    `:version`.")
  (get-url [this path]
    "Get the context-based url for the client."))

(defrecord Client [])

(def client-behaviour
  {;; XXX move this to master?
   :get-context #'base/get-context
   ;; XXX rename to get-context-url?
   :get-url #'base/get-url})

(extend Client ClientAPI client-behaviour)

(defn check-fields
  ""
  [fields]
  (logger/set-level! 'meson (get-in fields [:options :log-level]))
  (cond
    (get-in fields [:options :debug])
      (logger/set-level! 'meson :debug))
  fields)

(defn add-host
  ""
  [host-port fields]
  (if host-port
    (into fields {:host (first (string/split host-port #":" 2))})
    fields))

(defn add-port
  ""
  [host-port fields]
  (if host-port
    (into fields {:port (second (string/split host-port #":" 2))})
    fields))

(defn add-host-port
  ""
  [host-port fields]
  (->> fields
       (add-host host-port)
       (add-port host-port)))

(defn get-host-port
  ""
  [fields]
  (if-let [master (:master fields)]
    (add-host-port master fields)
    (if-let [agent (:agent fields)]
      (add-host-port agent fields)
      fields)))

(defn ->base-client
  "Unlike `->ClientAPI`, this factory function takes a map of the client
  fields as a single argument. As such, it is essentially an alias for
  `map->ClientAPI`. An important difference is that if no argument is given,
  the default base client map, `base-client-fields`, is used."
  ([]
    (->base-client {}))
  ([fs]
    (->> fs
         (into fields)
         (check-fields)
         (get-host-port)
         (map->Client))))

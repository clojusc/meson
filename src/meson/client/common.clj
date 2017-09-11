(ns meson.client.common
  (:require [clojure.string :as string]
            [clojusc.twig :as twig]
            [meson.config :as config]
            [meson.http.core :as http]))

;;; Common Data ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
     :content-type "application/json"
     :accept "application/json"
     :debug false
     :debug-body false
     :throw-entire-message? false
     :log-level config/log-level
     :headers {
       :user-agent http/user-agent}
     :client-params {
       "http.useragent" http/user-agent}}})

;;; Common Functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn check-fields
  ""
  [fields]
  (twig/set-level! 'meson (get-in fields [:options :log-level]))
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

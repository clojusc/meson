(ns meson.client
  (:require [clojure.string :as string]
            [clojusc.twig :as logger]
            [meson.client.common :as common]
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
     :debug false
     :debug-body false
     :throw-entire-message? false
     :log-level :error
     :headers {:user-agent user-agent}
     :client-params {
       "http.useragent" user-agent}}})


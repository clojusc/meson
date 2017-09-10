(ns meson.client
  (:require [clojure.string :as string]
            [clojusc.twig :as logger]
            [meson.client.common :as common]
            [meson.config :as config]
            [meson.const :as const]
            [meson.http.core :as http]))

(def user-agent
  (format "Meson REST Client/%s (Clojure %s; Java %s) (+%s)"
          config/client-version
          const/clj-version
          const/java-version
          config/project-url))

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
       :user-agent user-agent}
     :client-params {
       "http.useragent" user-agent}}})


(ns meson.client.impl.config
  (:require [meson.http.core :as http]))

(defn get-version
  ""
  [this]
  (http/get this "version"))

(defn get-flags
  ""
  ([this]
    (http/get this "flags"))
  ([this agent-id]
    (http/get this (format "slave(%s)/flags" agent-id))))

(def behaviour
  {:get-version get-version
   :get-flags get-flags})

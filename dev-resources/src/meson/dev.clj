(ns meson.dev
  (:require [clojure.core.async :as async :refer [<! >!]]
            [clojure.java.io :as io]
            [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [clojure.walk :refer [macroexpand-all]]
            ;; http
            [clj-http.client :as httpc]
            ;; data
            [clojure.data.json :as json]
            ;; data types
            [clojure.data.codec.base64 :as b64]
            [clojusc.twig :as twig]
            ;; meson
            [meson.client :as client]
            [meson.client.impl.master.scheduler :as impl-sched]
            [meson.client.master :as master]
            [meson.client.master.scheduler :as scheduler]
            [meson.core :as meson]
            [meson.http :as http]
            [meson.protobuf :as protobuf]
            [meson.protobuf.mesos :as pb-mesos]
            [meson.protobuf.scheduler :as pb-scheduler]
            [meson.scheduler.handlers :as scheduler-handlers]
            [meson.util :as util]
            [meson.util.recordio :as recordio]))

(twig/set-level! '[meson] :debug)

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))

;;; Aliases

(def reload #'repl/refresh)

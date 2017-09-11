(ns meson.dev
  (:require [clj-http.client :as httpc]
            [clojure.core.async :as async]
            [clojure.data.codec.base64 :as b64]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure.tools.namespace.repl :as repl]
            [clojure.walk :refer [walk macroexpand-all]]
            [clojusc.twig :as twig]
            [meson.client.agent :as agent]
            [meson.client.common :as common]
            [meson.client.master :as master]
            [meson.http.core :as http]
            [meson.records.core :as records]
            [meson.records.scheduler :as sched-records]
            [meson.api.scheduler.core :as scheduler-api]
            [meson.api.scheduler.handlers
             :refer [default] :rename {default default-scheduler-handler}]
            [meson.util.core :as util]
            [meson.util.recordio :as recordio]
            [taoensso.timbre :as log]))

(twig/set-level! '[meson] :debug)

;; XXX use the trifl lib for this
(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))

;;; Aliases

(def reload #'repl/refresh)

(ns meson.dev
  (:require [clj-http.client :as httpc]
            [clojure.core.async :as async :refer [<! >!]]
            [clojure.data.codec.base64 :as b64]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [clojure.walk :refer [macroexpand-all]]
            [clojusc.twig :as twig]
            [meson.client :as client]
            [meson.client.impl.master.scheduler :as scheduler]
            [meson.client.master :as master]
            [meson.core :as meson]
            [meson.http.core :as http]
            [meson.scheduler.handlers :refer [default]
                                      :rename {default default-handler}]
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

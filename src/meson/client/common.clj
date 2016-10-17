(ns meson.client.common
  (:require [clojure.string :as string]
            [clojusc.twig :as twig]))

;;; Common Methods ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  ""
  [this]
  (format "%s%s" (:base-path this) (:endpoint this)))

(defn get-url
  ""
  [this path]
  (format "%s://%s:%s%s%s"
    (:scheme this) (:host this) (:port this) (get-context this) path))

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

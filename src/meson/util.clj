(ns meson.util
  "Some convenience/utility functions used in the rest of Mesomatic."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojusc.twig :as logger]
            [meson.config :as config]
            [taoensso.timbre :as log])
  (:import [clojure.lang Keyword]
           [java.util UUID]))

(defn get-uuid
  "A Mesos-friendly UUID wrapper."
  []
  (->> (UUID/randomUUID)
       (str)
       (assoc {} :value)))

(defn camel->under
  "From Emerick, Grande, Carper 2012 p.70."
  [^clojure.lang.Keyword kwd]
  (as-> kwd val
        (name val)
        (string/split val #"(?<=[a-z])(?=[A-Z])")
        (map string/lower-case val)
        (interpose \_ val)
        (string/join val)))

(defn keyword->upper
  [^Keyword type]
  (-> type
      (name)
      (string/upper-case)))

(defn keyword->lower
  [^Keyword type]
  (-> type
      (name)
      (string/lower-case)))

(defn set-log-level
  []
  (logger/set-level! config/log-ns config/log-level))

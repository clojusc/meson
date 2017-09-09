(ns meson.config
  (:require [leiningen.core.project :as project]
            [taoensso.timbre :as log])
  (:refer-clojure :exclude [name]))

(defn all
  []
  (project/read))

(defn meson
  []
  (:meson (all)))

(defn log-level
  []
  (:log-level (meson)))

(defn log-ns
  []
  (:log-ns (meson)))

(defn mesos
  []
  (:mesos (meson)))

(defn cluster-deployment
  []
  (:cluster-deployment (mesos)))

(defn cluster-type
  []
  (:cluster-type (mesos)))

(defn docker
  []
  (:docker (mesos)))

(defn docker-container-id-file
  []
  (:container-id-file (docker)))

(defn docker-image-name
  []
  (:image-name (docker)))

(defn docker-port-mappings
  []
  (:port-mappings (docker)))

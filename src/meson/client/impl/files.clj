(ns meson.client.impl.files
  (:refer-clojure :exclude [read]))

(defn browse [this]
  :not-yet-implemented)

(defn debug [this]
  :not-yet-implemented)

(defn download
  ([this]
    :not-yet-implemented)
  ([this type]
    :not-yet-implemented))

(defn read [this path offset length]
  :not-yet-implemented)

(def behaviour
  {:browse browse
   :debug debug
   :download download
   :read read})

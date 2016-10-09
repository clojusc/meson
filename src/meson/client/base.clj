(ns meson.client.base
  (:require [clojure.data.json :as json]
            [meson.http :as http]))

(defn get-context
  ""
  [this]
  (format "%s%s" (:base-path this) (:endpoint this)))

(defn get-url
  ""
  [this path]
  (format "%s://%s:%s%s%s"
    (:scheme this) (:host this) (:port this) (get-context this) path))

(defn get-version
  ""
  [this]
  (-> this
      (http/get (get-url this "version"))
      :body
      (json/read-str :key-fn keyword)))

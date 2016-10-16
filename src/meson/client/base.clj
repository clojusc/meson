(ns meson.client.base)

(defn get-context
  ""
  [this]
  (format "%s%s" (:base-path this) (:endpoint this)))

(defn get-url
  ""
  [this path]
  (format "%s://%s:%s%s%s"
    (:scheme this) (:host this) (:port this) (get-context this) path))

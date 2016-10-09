(ns meson.client.imple.agent)

(defn get-container-status [this]
  :not-yet-implemented)

(defn get-resource-status [this id]
  :not-yet-implemented)

(def behaviour
  {:get-container-status get-container-status
   :get-resource-status get-resource-status})

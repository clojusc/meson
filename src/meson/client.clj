(ns meson.client
  (:require [clojusc.twig :as logger]
            [meson.const :as const]))

(def user-agent (str "Meson REST Client/"
                     const/client-version
                     " (Clojure "
                     const/clj-version
                     "; Java "
                     const/java-version
                     ") (+"
                     const/project-url
                     ")"))

(def fields
  "Fields are maintained separately from the record so that they may be
   re-used by concrete clients (e.g., Scheduler and Executor clients)."
  {:master nil
   :base-path "/api"
   :endpoint ""
   :version "1"
   :scheme "http"
   :event-conn nil
   :cmd-conn nil
   :options {
     :content-type "application/json" ; or application/x-protobuf
     :accept "application/json"
     :debug true
     :debug-body true
     :throw-entire-message? true
     :log-level :info
     :headers {:user-agent user-agent}
     :client-params {
       "http.useragent" user-agent}}})

(defprotocol ClientAPI
  (get-context [this]
    "Get the context for this client, calculated using `:base-path` and
    `:version`.")
  (get-url [this]
    "Get the context-based url for the client."))

(defrecord Client [])

(def client-behaviour
  {:get-context (fn [this]
    (format "%s/v%s%s" (:base-path this) (:version this) (:endpoint this)))
   :get-url (fn [this]
    (format "%s://%s%s" (:scheme this) (:master this) (get-context this)))})

(extend Client ClientAPI client-behaviour)

(defn check-fields
  ""
  [fields]
  (logger/set-level! 'meson (get-in fields [:options :log-level]))
  (cond
    (get-in fields [:options :debug])
      (logger/set-level! 'meson :debug))
  fields)

(defn ->base-client
  "Unlike `->BaseClient`, this factory function takes a map of the client
  fields as a single argument. As such, it is essentially an alias for
  `map->BaseClient`. An important difference is that if no argument is given,
  the default base client map, `base-client-fields`, is used."
  ([]
    (->base-client {}))
  ([fs]
    (->> fs
         (into fields)
         (check-fields)
         (map->Client))))

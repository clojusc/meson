(ns meson.client)

(def base-client-fields
  "Fields are maintained separately from the record so that they may be
   re-used by concrete clients (e.g., Scheduler and Executor clients)."
  {:base-path "/api"
   :version "1"
   :event-conn nil
   :cmd-conn nil
   :options {
     :debug false
     :log-level :info}})

(defprotocol BaseClientAPI
  (get-context [self]
    "Get the context for this client, calculated using `:base-path` and
    `:version`."))

(defrecord BaseClient [] BaseClientAPI
  (get-context [self]
    (format "%s/v%s" (:base-path self) (:version self))))

(defn ->base-client
  "Unlike `->BaseClient`, this factory function takes a map of the client
  fields as a single argument. As such, it is essentially an alias for
  `map->BaseClient`. An important difference is that if no argument is given,
  the default base client map, `base-client-fields`, is used."
  ([]
    (->base-client base-client-fields))
  ([fields]
    (map->BaseClient fields)))

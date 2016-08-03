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
    "Get the context for this client, calculated using :base-path and
    :version."))

(defrecord BaseClient [] BaseClientAPI
  (get-context [self]
    (format "%s/v%s" (:base-path self) (:version self))))

(defn make-base-client
  ""
  ([]
    (make-base-client base-client-fields))
  ([fields]
    (BaseClient. {} fields)))

(ns meson.client.impl.master.core)

(defn bring-down-machines
  [this machines]
  :not-yet-implemented)

(defn bring-up-machines
  [this machines]
  :not-yet-implemented)

(defn create-volumes
  [this agent-id volumes]
  :not-yet-implemented)

(defn destroy-volumes
  [this agent-id volumes]
  :not-yet-implemented)

(defn get-agents
  [this]
  :not-yet-implemented)

(defn get-frameworks
  [this]
  :not-yet-implemented)

(defn get-maintenance-schedule
  [this]
  :not-yet-implemented)

(defn get-maintenance-status
  [this]
  :not-yet-implemented)

(defn get-quota
  [this]
  :not-yet-implemented)

(defn get-registry
  [this]
  :not-yet-implemented)

(defn get-roles
  [this]
  :not-yet-implemented)

(defn get-state-summary
  [this]
  :not-yet-implemented)

(defn get-tasks
  ([this]
    :not-yet-implemented)
  ([this limit offset order]
    :not-yet-implemented))

(defn redirect
  [this]
  :not-yet-implemented)

(defn remove-quota
  [this role]
  :not-yet-implemented)

(defn reserve
  [this agent-id resources]
  :not-yet-implemented)

(defn set-quota
  [this role]
  :not-yet-implemented)

(defn teardown-framework
  [this framework-id]
  :not-yet-implemented)

(defn unreserve
  [this agent-id resources]
  :not-yet-implemented)

(defn update-maintenance-scheduler
  [this]
  :not-yet-implemented)

(defn update-role-weights
  [this data]
  :not-yet-implemented)

(def behaviour
  {:bring-down-machines bring-down-machines
   :bring-up-machines bring-up-machines
   :create-volumes create-volumes
   :destroy-volumes destroy-volumes
   :get-agents get-agents
   :get-frameworks get-frameworks
   :get-maintenance-schedule get-maintenance-schedule
   :get-maintenance-status get-maintenance-status
   :get-quota get-quota
   :get-registry get-registry
   :get-roles get-roles
   :get-state-summary get-state-summary
   :get-tasks get-tasks
   :remove-quota remove-quota
   :reserve reserve
   :set-quota set-quota
   :teardown-framework teardown-framework
   :unreserve unreserve
   :update-maintenance-scheduler update-maintenance-scheduler
   :update-role-weights update-role-weights})

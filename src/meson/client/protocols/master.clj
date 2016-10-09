(ns meson.client.protocols.master
  "Collects methods that are specific to the master and not contained in any of
  the `meson.client.protocols.common` protocols.

  See: http://mesos.apache.org/documentation/latest/endpoints/")

(defprotocol IMaster
  ""
  (bring-down-machines [this machines]
    "Transitions the list of machines into DOWN mode. Currently, only
    machines in DRAINING mode are allowed to be brought down.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/machine/up/")
  (bring-up-machines [this machines]
    "Transitions the list of machines into UP mode. This also removes the
    list of machines from the maintenance schedule.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/machine/up/")
  (create-volumes [this agent-id volumes]
    "Create persistent volumes on reserved resources.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/create-volumes/")
  (destroy-volumes [this agent-id volumes]
    "Destroy persistent volumes.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/destroy-volumes/")
  (get-agents [this]
    "Information about registered agents.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/slaves/")
  (get-frameworks [this]
    "Exposes the frameworks info.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/frameworks/")
  (get-maintenance-schedule [this]
    "Returns the current maintenance schedule as JSON.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/maintenance/schedule/")
  (get-maintenance-status [this]
    "Retrieves the maintenance status of the cluster.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/maintenance/status/")
  (get-quota [this]
    "Returns the currently set quotas as JSON.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/quota/")
  (get-registry [this]
    "Returns the current contents of the Registry in JSON.

    See: http://mesos.apache.org/documentation/latest/endpoints/registrar/registry/")
  (get-roles [this]
    "Information about roles.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/roles/")
  (get-state-summary [this]
    "Summary of state of all tasks and registered frameworks in cluster.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/state-summary/")
  (get-tasks [this] [this limit offset order]
    "Lists tasks from all active frameworks. If `limit`, `offset`, and `order`
    are not provided, the defaults of `100`, `0`, and `:descending` are used,
    respectively.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/tasks/")
  (redirect [this]
    "Redirects to the leading Master.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/redirect/")
  (remove-quota [this role]
    "Removes quota for a role.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/quota/")
  (reserve [this agent-id resources]
    "Reserve resources dynamically on a specific agent.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/reserve/")
  (set-quota [this role]
    "Sets the quota for a role.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/quota/")
  (teardown-framework [this framework-id]
    "Tears down a running framework by shutting down all tasks/executors and
    removing the framework.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/unreserve/")
  (unreserve [this agent-id resources]
    "Unreserve resources dynamically on a specific agent.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/unreserve/")
  (update-maintenance-scheduler [this]
    "updates the maintenance schedule.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/maintenance/schedule/")
  (update-role-weights [this data]
    "Updates weights for the specified roles.

    See: http://mesos.apache.org/documentation/latest/endpoints/master/weights/"))

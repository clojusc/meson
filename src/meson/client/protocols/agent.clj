(ns meson.client.protocols.agent
  "Collects methods that are specific to the agent and not contained in any of
  the `meson.client.protocols.common` protocols.

  See: http://mesos.apache.org/documentation/latest/endpoints/")

(defprotocol IAgent
  ""
  (get-container-status [this]
    "Retrieve container status and usage information.

    See: http://mesos.apache.org/documentation/latest/endpoints/slave/containers/")
  (get-resource-status [this id]
    "Retrieve resource monitoring information.

    See: http://mesos.apache.org/documentation/latest/endpoints/slave/monitor/statistics/"))

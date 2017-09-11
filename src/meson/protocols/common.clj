(ns meson.protocols.common
  "Collects Mesos HTTP API common to both masters and agents, collecting them
  into appropriate groupings.

  See: http://mesos.apache.org/documentation/latest/endpoints/"
  (:refer-clojure :exclude [read]))

(defprotocol IConfig
  ""
  (get-version [this]
    "Provides Mesos version information.

    See: http://mesos.apache.org/documentation/latest/endpoints/version/")
  (get-flags [this] [this id]
    "Exposes the flag configuration for an agent or a master.

    See the following:

    * http://mesos.apache.org/documentation/latest/endpoints/master/flags/
    * http://mesos.apache.org/documentation/latest/endpoints/slave/flags/"))

(defprotocol IDebug
  ""
  (toggle-logging [this level duration]
    "Sets the logging verbosity level for a specified duration.

    The libprocess library uses glog for logging. The library only uses verbose
    logging which means nothing will be output unless the verbosity level is
    set (by default it’s `0`, libprocess uses levels `1`, `2`, and `3`). Duration
    takes values such as `10secs`, `15mins`, etc.

    See: http://mesos.apache.org/documentation/latest/endpoints/logging/toggle/")
  (start-profiler [this]
    "Start google perftools to do profiling.

    See: http://mesos.apache.org/documentation/latest/endpoints/profiler/start/")
  (stop-profiler [this]
    "Stop using google perftools.

    See: http://mesos.apache.org/documentation/latest/endpoints/profiler/stop/"))

(defprotocol IFiles
  ""
  (browse [this]
    "Lists files and directories contained in the path as a JSON object.

    See: http://mesos.apache.org/documentation/latest/endpoints/files/browse/")
  (debug [this]
    "This endpoint shows the internal virtual path map as a JSON object.

    See: http://mesos.apache.org/documentation/latest/endpoints/files/debug/")
  (download [this] [this type]
    "If no type is provided, or if type has the value :raw, this endpoint will
    return the raw file contents for the given path. If type has the value
    :json, this endpoint shows the internal virtual path map as a JSON object.

    See the following:

    * http://mesos.apache.org/documentation/latest/endpoints/files/download/
    * http://mesos.apache.org/documentation/latest/endpoints/files/download.json/")
  (read [this path offset length]
    "This endpoint reads data from a file at a given offset and for a given
    length.

    See: http://mesos.apache.org/documentation/latest/endpoints/files/read/"))

(defprotocol IHealth
  ""
  (get-health [this] [this id]
    "Perform a health check of the master or a given agent.

    See the following:

    * http://mesos.apache.org/documentation/latest/endpoints/master/health/
    * http://mesos.apache.org/documentation/latest/endpoints/slave/health/")
  (get-metrics [this] [this timeout]
    "This endpoint provides information regarding the current metrics tracked
    by the system.

    The optional parameter ‘timeout’ determines the maximum amount of
    time the endpoint will take to respond. If the timeout is exceeded, some
    metrics may not be included in the response.

    See: http://mesos.apache.org/documentation/latest/endpoints/metrics/snapshot/")
  (get-state [this] [this id]
    "Information about state of a master or an agent.

    See the following:

    * http://mesos.apache.org/documentation/latest/endpoints/master/state/
    * http://mesos.apache.org/documentation/latest/endpoints/slave/state/")
  (get-system-stats [this]
    "Shows local system metrics.

    See: http://mesos.apache.org/documentation/latest/endpoints/system/stats.json/"))

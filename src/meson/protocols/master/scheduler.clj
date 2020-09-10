(ns meson.protocols.master.scheduler
  "For all of the below defined methods, more detailed information is available
  here:

  * http://mesos.apache.org/documentation/latest/scheduler-http-api/")

(defprotocol IScheduler
  ""
  (accept [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler when it accepts offer(s) sent by the master. The
    `ACCEPT` request includes the type of operations (e.g., launch task,
    reserve resources, create volumes) that the scheduler wants to perform on
    the offers.")
  (acknowledge [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to acknowledge a status update.")
  (decline [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to explicitly decline offer(s) received. Note that
    this is same as sending an `ACCEPT` call with no operations.")
  (kill-task [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to kill a specific task. If the scheduler has a
    custom executor, the kill is forwarded to the executor; it is up to the
    executor to kill the task and send a `TASK_KILLED` (or `TASK_FAILED`)
    update.")
  (message [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to send arbitrary binary data to the executor.")
  (reconcile [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to query the status of non-terminal tasks.")
  (request [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to request resources from the master/allocator.")
  (revive [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to remove any/all filters that it has previously
    set via `ACCEPT` or `DECLINE` calls.")
  (shutdown-executor [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler to shutdown a specific custom executor.")
  (subscribe [this payload] [this payload content-type]
    "This is the first step in the communication process between the scheduler
    and the master. This is also to be considered as subscription to the
    “/scheduler” events stream.")
  (teardown [this payload stream-id framework-id] [this payload stream-id framework-id content-type]
    "Sent by the scheduler when it wants to tear itself down."))

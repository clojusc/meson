(ns meson.ops.mesos
  (:require [clojure.java.shell :as shell]
            [clojure.pprint :refer [pprint]]
            [meson.config :as config]
            [taoensso.timbre :as log]
            [trifl.docs :as docs]))

(defn start-local-docker
  []
  ""
  (log/info "Starting local docker cluster ...")
  (let [id-file (config/docker-container-id-file)
        image-name (config/docker-image-name)]
    (shell/sh "rm" "-f" id-file)
    (shell/sh "docker" "pull" image-name)
    (shell/sh "docker" "run" "-d"
              (str "--cidfile=" id-file)
              "-p" (config/docker-port-mappings)
              image-name))
  (log/info "Done."))

(defn stop-local-docker
  []
  ""
  (log/info "Stoping local docker cluster ..."))

(defn restart-local-docker
  []
  ""
  (stop-local-docker)
  (start-local-docker))

(defn start
  ""
  []
  (if (and (= (config/cluster-deployment) :local)
           (= (config/cluster-type) :docker))
    (start-local-docker)
    (log/error "Unsupported cluster deployment and/or cluster type.")))

(defn stop
  ""
  []
  (if (and (= (config/cluster-deployment) :local)
           (= (config/cluster-type) :docker))
    (stop-local-docker)
    (log/error "Unsupported cluster deployment and/or cluster type.")))

(defn restart
  ""
  []
  (if (and (= (config/cluster-deployment) :local)
           (= (config/cluster-type) :docker))
    (restart-local-docker)
    (log/error "Unsupported cluster deployment and/or cluster type.")))

(defn run
  "
  Usage:
  ```
    lein meson mesos [SUBCOMMAND | help]
  ```

  If no SUBCOMMAND is provided, the default 'help' will be used.

  Subcommands:
  ```
    start     Start a mesos cluster (as configured by deployment and type)
    stop      Stop a running mesos cluster
    restart   Restart a running mesos cluster
  ```"
  ([]
    (docs/print-docstring #'run))
  ([[subcmd & args]]
    (log/debug "Got subcmd:" subcmd)
    (log/debug "Got args:" args)
    (case (keyword subcmd)
      nil (docs/print-docstring #'run)
      :start (start)
      :stop (stop)
      :restart (restart)
      :help (docs/print-docstring #'run))))

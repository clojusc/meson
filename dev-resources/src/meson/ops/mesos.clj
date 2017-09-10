(ns meson.ops.mesos
  (:require [clojure.pprint :refer [pprint]]
            [meson.config :as config]
            [meson.util.shell :as util]
            [taoensso.timbre :as log]
            [trifl.docs :as docs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Local Docker   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def local-docker?
  (and (= config/cluster-deployment :local)
       (= config/cluster-type :docker)))

(defn start-local-docker
  ""
  []
  (log/info "Starting local docker cluster ...")
  (log/debugf "Using image '%s'" config/docker-image-name)
  (util/shellf-silent "rm -f %s" config/docker-container-id-file)
  (util/shellf "docker pull %s" config/docker-image-name)
  (util/shellf-silent "docker run -d --cidfile %s --publish %s %s"
                      config/docker-container-id-file
                      config/docker-port-mappings
                      config/docker-image-name)
  (log/info "Done."))

(defn stop-local-docker
  ""
  []
  (log/info "Stoping local docker cluster ...")
  (let [id (util/shellf-silent "cat %s" config/docker-container-id-file)]
    (util/shellf-silent "docker stop %s" id))
  (log/info "Done."))

(defn restart-local-docker
  ""
  []
  (stop-local-docker)
  (start-local-docker))

(defn connect-local-docker
  ""
  []
  (log/warn "Note that clojure.java.shell doesn't directly support TTY"
            "connections.")
  (let [id (util/shellf-silent "cat %s" config/docker-container-id-file)]
    (log/info
      (format (str "To connect to the running docker container, execute the "
                   "following:\n\n docker exec -it %s bash\n")
               id))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Dispatch Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn start
  ""
  []
  (if local-docker?
    (start-local-docker)
    (log/error "Unsupported cluster deployment and/or cluster type.")))

(defn stop
  ""
  []
  (if local-docker?
    (stop-local-docker)
    (log/error "Unsupported cluster deployment and/or cluster type.")))

(defn restart
  ""
  []
  (if local-docker?
    (restart-local-docker)
    (log/error "Unsupported cluster deployment and/or cluster type.")))

(defn connect
  ""
  []
  (if local-docker?
    (connect-local-docker)
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
    connect   Get command to connect to running Docker container
  ```"
  ([]
    (docs/print-docstring #'run))
  ([[subcmd & args]]
    (log/trace "Got subcmd:" subcmd)
    (log/trace "Got args:" args)
    (case (keyword subcmd)
      nil (docs/print-docstring #'run)
      :start (start)
      :stop (stop)
      :restart (restart)
      :connect (connect)
      :help (docs/print-docstring #'run))))

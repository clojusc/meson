(ns meson.ops
  (:require [clojure.pprint :refer [pprint]]
            [meson.config :as config]
            [meson.ops.mesos :as mesos]
            [meson.util :as util]
            [taoensso.timbre :as log]
            [trifl.docs :as docs])
  (:gen-class))

(defn run
  "
  Usage:
  ```
    lein meson [help | COMMAND [arg...]]
    lein meson [-h | --help | -v | --version]
  ```

  Commands:
  ```
    help    Display this usage message
    config  Display current meson configuration
    mesos   Perform various operations on a mesos cluster
  ```

  More information:

    Each command takes an optional 'help' subcommand that will provide
    usage information about the particular command in question, e.g.:
  ```
    $ lein meson mesos help
  ```"
  ([]
    (docs/print-docstring #'run))
  ([[cmd & args]]
    (util/set-log-level)
    (log/trace "CLI got cmd:" cmd)
    (log/trace "CLI got args:" args)
    (case (keyword cmd)
      :help (docs/print-docstring #'run)
      :config (pprint config/meson)
      :mesos (mesos/run args)
      ;; Aliases
      :--help (docs/print-docstring #'run)
      :-h (docs/print-docstring #'run))
    (shutdown-agents)))

(defn -main
  "The entry point for ops-related CLI tasks."
  [& args]
  (run args))

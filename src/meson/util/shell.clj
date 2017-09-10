(ns meson.util.shell
  "Some convenience/utility functions used for making calls in the system
  shell."
  (:require [clojure.java.shell :as shell]
            [clojure.string :as string]
            [taoensso.timbre :as log]))

(defn shell
  ([cmd-str]
    (shell cmd-str nil nil))
  ([cmd-str out-fn err-fn]
    (log/debug "Got command string:" cmd-str)
    (let [args (string/split cmd-str #"\s")
          {:keys [out err]} (apply shell/sh args)
          formatted-out (str "Shell output:\n" out)
          formatted-err (str "Shell error:\n" err)]
      (when-not (or (nil? out) (empty? out))
        (if out-fn
          (out-fn formatted-out)
          (log/info formatted-out)))
      (when-not (or (nil? err) (empty? err))
        (if err-fn
          (err-fn formatted-err)
          (log/error formatted-err)))
      out)))

(defn shellf
  [cmd-str-tmpl & format-args]
  (shell (apply format (concat [cmd-str-tmpl] format-args))))

(defn shellf-silent
  [cmd-str-tmpl & format-args]
  (shell (apply format (concat [cmd-str-tmpl] format-args))
         (constantly nil)
         (constantly nil)))

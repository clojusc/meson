(ns meson.util
  "Some convenience/utility functions used in the rest of Mesomatic."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [clojure.string :as string]
            [clojusc.twig :as logger]
            [meson.config :as config]
            [taoensso.timbre :as log])
  (:import [clojure.lang Keyword]
           [java.util UUID]))

(defn camel->under
  "From Emerick, Grande, Carper 2012 p.70."
  [^clojure.lang.Keyword kwd]
  (as-> kwd val
        (name val)
        (string/split val #"(?<=[a-z])(?=[A-Z])")
        (map string/lower-case val)
        (interpose \_ val)
        (string/join val)))

(defmacro case-enum
  "Like `case`, but explicitly dispatch on Java enum ordinals."
  [e & clauses]
  (letfn [(enum-ordinal [e] `(let [^Enum e# ~e] (.ordinal e#)))]
    `(case ~(enum-ordinal e)
       ~@(concat
          (mapcat (fn [[test result]]
                    [(eval (enum-ordinal test)) result])
                  (partition 2 clauses))
          (when (odd? (count clauses))
            (list (last clauses)))))))

(defn convert-upper
  [^Keyword type]
  (-> type
      (name)
      (string/upper-case)))

(defn convert-lower
  [^Keyword type]
  (-> type
      (name)
      (string/lower-case)))

(defn newline?
  ""
  [byte]
  (= (char byte) \newline))

(defn fill
  ""
  [array val]
  (dotimes [n (alength array)]
    (aset-byte array n val)))

(defn bytes->str
  ""
  [bytes]
  (->> bytes
       (map #(char (bit-and % 255)))
       (apply str)
       (.trim)))

(defn bytes->int
  ""
  [bytes]
  (-> bytes
      (bytes->str)
      (Integer/parseInt)))

(defn bytes->json
  ""
  [bytes]
  (-> bytes
      (bytes->str)
      (json/read-str :key-fn keyword)
      (update :type #(keyword (string/lower-case %)))))

(defn str->bytes
  ""
  [str]
  (.getBytes str))

(defn str->stream
  ""
  [str]
  (io/input-stream (str->bytes str)))

(defn get-uuid
  "A Mesos-friendly UUID wrapper."
  []
  (->> (UUID/randomUUID)
       (str)
       (assoc {} :value)))

(defn set-log-level
  []
  (logger/set-level! (config/log-ns) (config/log-level)))

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

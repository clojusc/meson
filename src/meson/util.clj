(ns meson.util
  "Some convenience/utility functions used in the rest of Mesomatic."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [clojure.lang Keyword]))

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

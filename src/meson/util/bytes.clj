(ns meson.util.bytes
  "Some convenience/utility functions for working with bytes."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as string]))

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

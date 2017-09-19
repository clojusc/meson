(ns meson.util.recordio
  (:require [meson.error :as error]
            [meson.util :as util])
  (:import [clojure.lang Keyword]))

(defprotocol IRecordIOStream
  (get-size! [this]
    "Get the size of the next record in the stream.")
  (get-data! [this record-size]
    "Get the data of the next record in the stream.")
  (next! [this] [this return-type]
    "A wrapper method that gets the size and then the data for the next
    record in the stream."))

(defn- read-record-size
  [^java.io.InputStream stream]
  (loop [array []]
    (let [data (.read stream)]
      (if (pos? data)
        (do
          (if (util/newline? data)
            (util/bytes->int array)
            (recur (conj array data))))
        ; (throw (new Exception "End-of-Stream"))))))
        ; (throw
        ;   (error/end-of-stream "No more data in stream"
        ;                        (str "Received a negative value when reading "
        ;                             "from the input stream.")))))))
        0))))


(defn- read-record-data
  [^java.io.InputStream stream asize]
  (let [array (byte-array asize)]
    (loop [byte-index 0]
      (if (= byte-index asize)
        array
        (do
          (let [data (.read stream)]
            (if (pos? data)
              (do
                (aset-byte array byte-index data)
                (recur (inc byte-index)))
              array)))))))

(defn- read-next
  ([^java.io.InputStream stream]
    (let [size (read-record-size stream)]
      (read-record-data stream size)))
  ([^java.io.InputStream stream ^Keyword type]
    (let [data (read-next stream)]
      (case type
        :json (util/bytes->json data)
        :string (util/bytes->str data)
        :bytes data
        data))))

(def recordio-behaviour
  {:get-size! read-record-size
   :get-data! read-record-data
   :next! read-next})

(extend java.io.InputStream IRecordIOStream recordio-behaviour)

;;; Error Handling ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(error/add-handler
  #'read-record-size
  [:meson-error-type 'End-Of-Stream]
  error/process-exception
  error/end-of-stream)

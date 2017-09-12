(ns meson.api.scheduler.core
  (:require [clj-http.client :as httpc]
            [clojure.core.async :as async]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojusc.twig :as twig]
            [meson.http.core :as http]
            [meson.records.core :as records]
            [meson.records.scheduler :as message]
            [meson.util.core :as util]
            [meson.util.recordio :as recordio]
            [taoensso.timbre :as log]))

(defn prepare-element
  [element]
  (->> element
       (map util/clojurize-key)
       (into {})))

(defn parse-data
  [stream]
  (-> stream
      (recordio/next! :json)
      (util/walk-keys prepare-element)))

(defn subscribe
  ([]
    (subscribe {:framework-info {
                :user "user1"
                :name "a-framework"}}))
  ([args]
    (log/info "args:" args)
    (let [chan (async/chan)
          data (message/create-call :subscribe args)
          prepared-data (records/prepare-data data)
          response (httpc/post "http://localhost:5050/api/v1/scheduler"
                               {:accept :json
                                :content-type :json
                                :form-params prepared-data
                                :throw-exceptions false
                                :as :stream})
          stream (:body response)]
        (async/go
          (loop []
            (async/>! chan (parse-data stream))
            (recur))
          (async/close! chan))
      chan)))

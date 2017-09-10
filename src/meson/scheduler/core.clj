(ns meson.scheduler.core
  (:require [clj-http.client :as httpc]
            [clojure.data.json :as json]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.logging :as log]
            [clojusc.twig :as twig]
            [meson.http.core :as http]
            [meson.records.core :as records]
            [meson.records.scheduler :as message]))

(defn subscribe
  ([]
    (subscribe (message/create-call :subscribe
                                     {:framework-info {
                                      :user "user1"
                                      :name "a-framework"}})))
  ([args]
    (log/info "args:" args)
    (let [prepared-data (records/prepare-data args)
          response (httpc/post "http://localhost:5050/api/v1/scheduler"
                               {:accept :json
                                :content-type :json
                                :form-params prepared-data
                                :throw-exceptions false})]

      (log/info prepared-data)
      (log/info
        (select-keys response [:status :reason-phrase :body])))))


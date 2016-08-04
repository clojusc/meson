(ns meson.scheduler-test
  (:require [clojure.test :refer :all]
            [meson.client :as client]
            [meson.scheduler :as scheduler]))

(deftest ->client
  (let [c (scheduler/->client)]
    (is (= (:base-path c) "/api"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) true))))

(deftest get-context
  (let [c (scheduler/->client)]
    (is (= (client/get-context c) "/api/v1/scheduler"))))

(deftest get-url
  (let [c (scheduler/->client)]
    (is (= (client/get-url c) "http://localhost:5050/api/v1/scheduler")))
  (let [c (scheduler/->client {:master "myhost:8080"})]
    (is (= (client/get-url c) "http://myhost:8080/api/v1/scheduler"))))

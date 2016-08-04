(ns meson.scheduler-test
  (:require [clojure.test :refer :all]
            [meson.scheduler :as scheduler]))

(deftest ->client
  (let [c (scheduler/->client)]
    (is (= (:base-path c) "/api"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) false))))

(deftest get-context
  (let [c (scheduler/->client)]
    (is (= (scheduler/get-context c) "/api/v1/scheduler"))))

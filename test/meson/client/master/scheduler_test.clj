(ns meson.client.master.scheduler-test
  (:require [clojure.test :refer :all]
            [meson.client.master.scheduler :as scheduler]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit check-client-fields
  (is (= (:master scheduler/client-fields)
         "localhost:5050")))

(deftest ^:unit ->client
  (let [c (scheduler/->client)]
    (is (= (:base-path c) "/api"))
    (is (= (:version c) "1"))
    (is (= (:master c) "localhost:5050"))
    (is (= (:host c) "localhost"))
    (is (= (:port c) "5050"))
    (is (= (get-in c [:options :debug]) false))))

(deftest ^:unit get-context
  (let [c (scheduler/->client)]
    (is (= (scheduler/get-context c) "/api/v1/scheduler"))))

(deftest ^:unit get-url
  (let [c (scheduler/->client)]
    (is (= (scheduler/get-url c "")
           "http://localhost:5050/api/v1/scheduler")))
  (let [c (scheduler/->client {:master "myhost:8080"})]
    (is (= (scheduler/get-url c "")
           "http://myhost:8080/api/v1/scheduler"))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

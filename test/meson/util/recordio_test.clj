(ns meson.util.recordio-test
  (:require [clojure.test :refer :all]
            [meson.client.master :as master]
            [meson.testing.payloads :as payload]
            [meson.util.bytes :as util]
            [meson.util.recordio :as recordio]))

(def testing-host "127.0.0.1")
(def testing-master {:master (format "%s:5050" testing-host)})
(def framework-info
  {:framework_info
    {:user "Alice"
     :name "Test Clojure/HTTP Framework (Clojure)"
     :hostname testing-host}})

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit get-size
  (is (= (recordio/get-size! (payload/subscribed)) 139)))

(deftest ^:unit get-data
  (let [stream (payload/subscribed)
        record-size (recordio/get-size! stream)
        bytes (recordio/get-data! stream record-size)
        json (util/bytes->json bytes)]
    (is (= (:type json) :subscribed))
    (is (= (get-in json [:subscribed :framework_id :value])
           "5f1bf690-0145-44be-96ae-145e5c51fccc-0017"))))

(deftest ^:unit next!
  (let [bytes (recordio/next! (payload/subscribed))
        json (util/bytes->json bytes)]
    (is (= (:type json) :subscribed))
    (is (= (get-in json [:subscribed :framework_id :value])
           "5f1bf690-0145-44be-96ae-145e5c51fccc-0017"))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:integration recordio-methods
  (testing "get-size! and get-data! ..."
    (let [c (master/create testing-master)
          response (master/subscribe c framework-info)
          stream (:body response)
          record-size (recordio/get-size! stream)
          bytes (recordio/get-data! stream record-size)
          json (util/bytes->json bytes)]
      (is (= record-size 139))
      (is (= (:type json) :subscribed))
      (is (= (get-in json [:subscribed :heartbeat_interval_seconds]) 15.0))))
  (testing "just next! ..."
    (let [c (master/create testing-master)
          response (master/subscribe c framework-info)
          bytes (recordio/next! (:body response))
          json (util/bytes->json bytes)]
      (is (= (:type json) :subscribed))
      (is (= (get-in json [:subscribed :heartbeat_interval_seconds]) 15.0)))))

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

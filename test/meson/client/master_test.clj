(ns meson.client.master-test
  (:require [clojure.test :refer :all]
            [meson.http.core :as http]
            [meson.client.master :as master]))

(def testing-master {:master "127.0.0.1:5050"})

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit create
  (let [c (master/create)]
    (is (= (:base-path c) "/"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) false))))

(deftest ^:unit get-context
  (let [c (master/create)]
    (is (= (http/get-context c) "/"))))

(deftest ^:unit get-url
  (let [c (master/create {:master "myhost:8080"})]
    (is (= (http/get-url c "") "http://myhost:8080/"))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:integration get-health
  (let [c (master/create testing-master)]
    (is (= (master/get-health c)
           200))))

(deftest ^:integration get-metrics
  (let [c (master/create testing-master)
        metrics-keys (sort (keys (master/get-metrics c)))]
    (is (> (count metrics-keys) 100))
    (is (= (first metrics-keys) :allocator/event_queue_dispatches))
    (is (= (second metrics-keys) :allocator/mesos/allocation_run_ms))
    (is (= (last metrics-keys) :system/mem_total_bytes))))

(deftest ^:integration get-state
  (let [c (master/create testing-master)
        state-keys (sort (keys (master/get-state c)))]
    ;; On Travis, there are 20 state keys; locally, 18
    (is (> (count state-keys) 10))
    (is (= (first state-keys) :activated_slaves))
    (is (= (second state-keys) :build_date))
    (is (= (last state-keys) :version))))

(deftest ^:integration get-system-stats
  (let [c (master/create testing-master)]
    (is (= (sort (keys (master/get-system-stats c)))
           [:avg_load_15min :avg_load_1min :avg_load_5min :cpus_total
            :mem_free_bytes :mem_total_bytes]))))

(deftest ^:integration get-version
  (let [c (master/create testing-master)]
    (is (= (:version (master/get-version c))
           "1.0.1"))))

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

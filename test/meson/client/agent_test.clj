(ns meson.client.agent-test
  (:require [clojure.test :refer :all]
            [meson.client.agent :as agent]))

(def testing-agent {:agent "127.0.0.1:5051"})

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit create
  (let [c (agent/create)]
    (is (= (:base-path c) "/"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) false))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:integration get-health
  (let [c (agent/create testing-agent)]
    (is (= (agent/get-health c)
           200))))

(deftest ^:integration get-metrics
  (let [c (agent/create testing-agent)
        metrics-keys (sort (keys (agent/get-metrics c)))]
    (is (= (count metrics-keys) 56))
    (is (= (first metrics-keys) :containerizer/mesos/container_destroy_errors))
    (is (= (second metrics-keys) :containerizer/mesos/provisioner/bind/remove_rootfs_errors))
    (is (= (last metrics-keys) :system/mem_total_bytes))))

(deftest ^:integration get-state
  (let [c (agent/create testing-agent)
        state-keys (sort (keys (agent/get-state c)))]
    ;; On Travis, there are 20 state keys; locally,18
    (is (> (count state-keys) 10))
    (is (= (first state-keys) :attributes))
    (is (= (second state-keys) :build_date))
    (is (= (last state-keys) :version))))

(deftest ^:integration get-system-stats
  (let [c (agent/create testing-agent)]
    (is (= (sort (keys (agent/get-system-stats c)))
           [:avg_load_15min :avg_load_1min :avg_load_5min :cpus_total
            :mem_free_bytes :mem_total_bytes]))))

(deftest ^:integration get-version
  (let [c (agent/create testing-agent)]
    (is (= (:version (agent/get-version c))
           "1.0.1"))))

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

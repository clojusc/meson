(ns meson.client.master-test
  (:require [clojure.test :refer :all]
            [meson.client.master :as master]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit ->client
  (let [c (master/->client)]
    (is (= (:base-path c) "/"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) true))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:integration get-version
  (let [c (master/->client {:master "127.0.0.1:5050"})]
    (is (= (sort (keys (master/get-version c)))
           [:build_date :build_time :build_user :version]))))

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

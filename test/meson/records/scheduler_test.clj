(ns meson.records.scheduler-test
  (:require [clojure.test :refer :all]
            [meson.records.scheduler :as scheduler]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit create-call-subscribe
  (let [call-data (scheduler/create-call :subscribe
                                         {:framework-info {
                                            :user "user1"
                                            :name "a-framework"}})]
    (is (= "SUBSCRIBE" (:type call-data)))
    (is (= meson.records.scheduler.Subscribe (type (:subscribe call-data))))
    (is (= "user1" (get-in call-data [:subscribe :framework-info :user])))
    (is (= "a-framework"
           (get-in call-data [:subscribe :framework-info :name])))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

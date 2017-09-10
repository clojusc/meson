(ns meson.records.core-test
  (:require [clojure.test :refer :all]
            [meson.records.core :as records]
            [meson.records.scheduler :as scheduler]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit create-call-subscribe
  (let [call-data (scheduler/create-call :subscribe
                                         {:framework-info {
                                            :user "user1"
                                            :name "a-framework"}})
        preped-data (records/prepare-data call-data)]
    (is (= {:type "SUBSCRIBE", :subscribe {:framework_info {:user "user1", :name "a-framework"}}}
           preped-data))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

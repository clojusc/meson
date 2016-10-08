(ns meson.client-test
  (:require [clojure.test :refer :all]
            [meson.client :as client]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit user-agent
  (is
    (not
      (nil?
        (re-matches
          #"Meson REST Client/.* \(Clojure .*; Java .*\) \(\+https://.*\)"
          client/user-agent)))))

(deftest ^:unit ->base-client
  (let [c (client/->base-client)]
    (is (= (:base-path c) "/api"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) true))))

(deftest ^:unit get-context
  (let [c (client/->base-client)]
    (is (= (client/get-context c) "/api/v1"))))

(deftest ^:unit get-url
  (let [c (client/->base-client {:master "myhost:8080"})]
    (is (= (client/get-url c) "http://myhost:8080/api/v1"))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

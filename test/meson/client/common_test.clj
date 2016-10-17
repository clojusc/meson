(ns meson.client-test
  (:require [clojure.test :refer :all]
            [meson.client.common :as common]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit user-agent
  (is
    (not
      (nil?
        (re-matches
          #"Meson REST Client/.* \(Clojure .*; Java .*\) \(\+https://.*\)"
          client/user-agent)))))

(deftest ^:unit add-host
  (is (= (common/add-host "myhost:myport" {:a "a"})
         {:a "a" :host "myhost"})))

(deftest ^:unit add-port
  (is (= (common/add-port "myhost:myport" {:a "a"})
         {:a "a" :port "myport"})))

(deftest ^:unit add-host-port
  (is (= (common/add-host-port "myhost:myport" {:a "a"})
         {:a "a" :host "myhost" :port "myport"})))

(deftest ^:unit get-host-port
  (is (= (common/get-host-port {:a "a" :master "host:port"})
         {:a "a" :master "host:port" :host "host" :port "port"}))
  (is (= (common/get-host-port {:a "a" :agent "host:port"})
         {:a "a" :agent "host:port" :host "host" :port "port"}))
  (is (= (common/get-host-port {:a "a"})
         {:a "a"})))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

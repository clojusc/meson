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
    (is (= (:base-path c) "/"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) true))))

(deftest ^:unit get-context
  (let [c (client/->base-client)]
    (is (= (client/get-context c) "/"))))

(deftest ^:unit get-url
  (let [c (client/->base-client {:master "myhost:8080"})]
    (is (= (client/get-url c "") "http://myhost:8080/"))))

(deftest ^:unit add-host
  (is (= (client/add-host "myhost:myport" {:a "a"})
         {:a "a" :host "myhost"})))

(deftest ^:unit add-port
  (is (= (client/add-port "myhost:myport" {:a "a"})
         {:a "a" :port "myport"})))

(deftest ^:unit add-host-port
  (is (= (client/add-host-port "myhost:myport" {:a "a"})
         {:a "a" :host "myhost" :port "myport"})))

(deftest ^:unit get-host-port
  (is (= (client/get-host-port {:a "a" :master "host:port"})
         {:a "a" :master "host:port" :host "host" :port "port"}))
  (is (= (client/get-host-port {:a "a" :agent "host:port"})
         {:a "a" :agent "host:port" :host "host" :port "port"}))
  (is (= (client/get-host-port {:a "a"})
         {:a "a"})))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

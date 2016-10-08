(ns meson.client-test
  (:require [clojure.test :refer :all]
            [meson.client :as client]))

(deftest user-agent
  (is
    (not
      (nil?
        (re-matches
          #"Meson REST Client/.* \(Clojure .*; Java .*\) \(\+https://.*\)"
          client/user-agent)))))

(deftest ->base-client
  (let [c (client/->base-client)]
    (is (= (:base-path c) "/api"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) true))))

(deftest get-context
  (let [c (client/->base-client)]
    (is (= (client/get-context c) "/api/v1"))))

(deftest get-url
  (let [c (client/->base-client {:master "myhost:8080"})]
    (is (= (client/get-url c) "http://myhost:8080/api/v1"))))


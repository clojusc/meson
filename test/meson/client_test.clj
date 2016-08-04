(ns meson.client-test
  (:require [clojure.test :refer :all]
            [meson.client :as client]))

(deftest user-agent
  (is (= client/user-agent
         (str "Meson REST Client/0.1.0-SNAPSHOT "
              "(Clojure 1.8.0; Java 1.8.0_45-internal) "
              "(+https://github.com/oubiwann/meson)"))))

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

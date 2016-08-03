(ns meson.client-test
  (:require [clojure.test :refer :all]
            [meson.client :as client]))

(deftest make-base-client
  (let [c (client/make-base-client)]
    (is (= (:base-path c) "/api"))
    (is (= (:version c) "1"))
    (is (= (get-in c [:options :debug]) false))))

(deftest get-context
  (let [c (client/make-base-client)]
    (is (= (client/get-context c) "/api/v1"))))

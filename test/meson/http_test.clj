(ns meson.http-test
  (:require [clojure.test :refer :all]
            [meson.http :as http]))

(deftest get-verb
  (testing "I don't fail, but I don't do much."
    (is (= 1 1))))

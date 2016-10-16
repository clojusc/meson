(ns meson.util-test
  (:require [clojure.test :refer :all]
            [meson.core :as meson]
            [meson.util :as util]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit camel->under
  (is (= (util/camel->under :ACamel) "acamel"))
  (is (= (util/camel->under :MyCamel) "my_camel"))
  (is (= (util/camel->under :MyCrazyCamel) "my_crazy_camel")))

(deftest ^:unit str->bytes
  (is (= (type (util/str->bytes "a string"))
         (type (byte-array 0)))))

(deftest ^:unit str->stream
  (is (= (type (util/str->stream "a string"))
         java.io.BufferedInputStream)))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

(ns meson.util.bytes-test
  (:require [clojure.test :refer :all]
            [meson.core :as meson]
            [meson.util.bytes :as util]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

(ns meson.util.core-test
  (:require [clojure.test :refer :all]
            [meson.util.core :as util]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit camel->under
  (is (= "acamel" (util/camel->under :ACamel)))
  (is (= "my_camel" (util/camel->under :MyCamel)))
  (is (= "my_crazy_camel" (util/camel->under :MyCrazyCamel))))

(deftest ^:unit dash->under
  (is (= "" (util/dash->under "")))
  (is (= "AAaa" (util/dash->under "AAaa")))
  (is (= "AA_aa" (util/dash->under "AA-aa")))
  (is (= "AA_aa" (util/dash->under "AA_aa")))
  (is (= "AA_" (util/dash->under "AA-")))
  (is (= "AA_" (util/dash->under "AA_")))
  (is (= "_aa" (util/dash->under "-aa")))
  (is (= "_aa" (util/dash->under "_aa"))))

(deftest ^:unit mesosize-key
  (is (= {:AAaa "AA-aa"}
         (into {} (map util/mesosize-key {:AAaa "AA-aa"}))))
  (is (= {:AA_aa "AA-aa"}
         (into {} (map util/mesosize-key {:AA-aa "AA-aa"}))))
  (is (= {:AA_aa "AA-aa"}
         (into {} (map util/mesosize-key {:AA_aa "AA-aa"})))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

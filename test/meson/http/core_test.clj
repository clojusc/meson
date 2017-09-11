(ns meson.http.core-test
  (:require [clojure.test :refer :all]
            [meson.http.core :as http]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit user-agent
  (is
    (not
      (nil?
        (re-matches
          #"Meson REST Client/.* \(Clojure .*; Java .*\) \(\+https://.*\)"
          http/user-agent)))))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

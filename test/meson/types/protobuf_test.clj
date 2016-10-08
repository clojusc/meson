(ns meson.types.protobuf-test
  (:require [clojure.test :refer :all]
            [meson.types.protobuf :as p-types]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit ->map
  (is (= (p-types/->map :FrameworkInfo {:user "Alice" :name "myfrmwrk"})
         {:role "*", :capabilities {:labels []}, :labels [], :checkpoint false, :name "myfrmwrk", :failover-timeout 0.0, :hostname "", :id {:value ""}, :webui-url "", :principal "", :user "Alice"})))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

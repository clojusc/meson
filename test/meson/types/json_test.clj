(ns meson.types.json-test
  (:require [clojure.test :refer :all]
            [meson.types.json :as j-types]))

;;; Unit Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest ^:unit map->json
  (is (= (j-types/map->json :FrameworkInfo {:user "Alice" :name "myfrmwrk"})
         "{\"framework_info\":{\"role\":\"*\",\"capabilities\":{\"labels\":[]},\"labels\":[],\"checkpoint\":false,\"name\":\"myfrmwrk\",\"failover-timeout\":0.0,\"hostname\":\"\",\"id\":{\"value\":\"\"},\"webui-url\":\"\",\"principal\":\"\",\"user\":\"Alice\"}}")))

;;; Integration Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;; System Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

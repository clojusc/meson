(ns meson.protobuf.mesos-test
  (:require [clojure.test :refer :all]
            [meson.protobuf.mesos :as pb-mesos]))

(deftest ->map
  (is (= (pb-mesos/->map :FrameworkInfo {:user "Alice" :name "myfrmwrk"})
         {:role "*", :capabilities {:labels []}, :labels [], :checkpoint false, :name "myfrmwrk", :failover-timeout 0.0, :hostname "", :id {:value ""}, :webui-url "", :principal "", :user "Alice"})))

(deftest map->json
  (is (= (pb-mesos/map->json :FrameworkInfo {:user "Alice" :name "myfrmwrk"})
         "{\"framework_info\":{\"role\":\"*\",\"capabilities\":{\"labels\":[]},\"labels\":[],\"checkpoint\":false,\"name\":\"myfrmwrk\",\"failover-timeout\":0.0,\"hostname\":\"\",\"id\":{\"value\":\"\"},\"webui-url\":\"\",\"principal\":\"\",\"user\":\"Alice\"}}")))

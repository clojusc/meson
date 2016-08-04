(ns meson.util-test
  (:require [clojure.test :refer :all]
            [meson.core :as meson]
            [meson.util :as util]))

(deftest camel->under
  (is (= (util/camel->under :ACamel) "acamel"))
  (is (= (util/camel->under :MyCamel) "my_camel"))
  (is (= (util/camel->under :MyCrazyCamel) "my_crazy_camel")))

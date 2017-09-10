(ns meson.const
  (:require [leiningen.core.project :as lein]))

(def java-version (System/getProperty "java.version"))
(def clj-version (clojure-version))

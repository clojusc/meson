(ns meson.const
  (:require [leiningen.core.project :as lein]))

(def project-url (:url (lein/read)))
(def client-version (:version (lein/read)))
(def java-version (System/getProperty "java.version"))
(def clj-version (clojure-version))

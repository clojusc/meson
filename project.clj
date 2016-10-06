(defproject meson "0.1.0-SNAPSHOT"
  :description "Clojure Client Library for the Mesos HTTP API"
  :url "https://github.com/oubiwann/meson"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.apache.mesos/mesos "1.0.0"]
                 [clj-http "3.1.0"]
                 [clojusc/twig "0.2.1"]
                 [leiningen-core "2.6.1"]]
  :plugins [[lein-codox "0.9.3"]]
  :codox {
    :project {
      :name "meson"
      :description "Clojure Client Library for the Mesos HTTP API"}
    :namespaces [#"^meson\.(?!debug)"]
    :output-path "docs/master/current"
    :doc-paths ["docs/source"]
    :metadata {
      :doc/format :markdown
      :doc "Documentation forthcoming"}}
  :profiles {
    :uber {
      :aot :all}
    :test {
      :plugins [
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-kibit "0.1.2" :exclusions [org.clojure/clojure]]]
      :test-selectors {
        :default :unit
        :unit :unit
        :system :system
        :integration :integration}}
    :dev {
      :source-paths ["dev-resources/src"]
      :repl-options {:init-ns meson.debug}
      :dependencies [[org.clojure/tools.namespace "0.2.11"]]}})

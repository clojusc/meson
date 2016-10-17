(defproject meson "0.1.0-SNAPSHOT"
  :description "Clojure Client Library for the Mesos HTTP API"
  :url "https://github.com/clojusc/meson"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[clj-http "3.3.0"]
                 [clojusc/twig "0.2.6"]
                 [dire "0.5.4"]
                 [leiningen-core "2.7.1"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.395"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.apache.mesos/mesos "1.0.1"]
                 [potemkin "0.4.3"]]
  :profiles {
    :uber {
      :aot :all}
    :test {
      :dependencies [
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-kibit "0.1.2" :exclusions [org.clojure/clojure]]]
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
      :repl-options {:init-ns meson.dev}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
         :exclusions [org.clojure/clojure]]]}
    :docs {
      :plugins [[lein-codox "0.10.1"]
                [lein-simpleton "1.3.0"]]
      :codox {
        :project {
          :name "meson"
          :description "Clojure Client Library for the Mesos HTTP API"}
        :namespaces [#"^meson\.(?!dev)"]
        :output-path "docs/master/current"
        :doc-paths ["docs/source"]
        :metadata {
          :doc/format :markdown
          :doc "Documentation forthcoming"}}}})

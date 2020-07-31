(defproject meson "0.2.0-SNAPSHOT"
  :description "Clojure Client Library for the Mesos HTTP API"
  :url "https://github.com/clojusc/meson"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [cheshire "5.8.0"]
    [clj-http "3.7.0"]
    [clojusc/trifl "0.2.0-SNAPSHOT"]
    [clojusc/twig "0.3.2-SNAPSHOT"]
    [dire "0.5.4"]
    [leiningen-core "2.7.1"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/core.async "0.3.443"]
    [org.clojure/data.json "0.2.6"]
    [potemkin "0.4.4"]]
  :meson {
    :log-level :debug
    :log-ns [meson]
    :mesos {
      :cluster-deployment :local
      :cluster-type :docker
      :docker {
        :container-id-file "/tmp/meson-mesos-container-id"
        :image-name "clojusc/mesos:1.0.1"
        :master "localhost:5050"
        :agent "localhost:5051"
        :port-mappings "5050-5051:5050-5051"}}}
  :profiles {
    :uberjar {
      :aot :all}
    :test {
      :exclusions [org.clojure/clojure]
      :plugins [
        [jonase/eastwood "0.2.4"]
        [lein-ancient "0.6.10"]
        [lein-bikeshed "0.4.1"]
        [lein-kibit "0.1.5"]
        [lein-shell "0.5.0"]
        [venantius/yagni "0.1.4"]]
      :test-selectors {
        :default :unit
        :unit :unit
        :system :system
        :integration :integration}}
    :dev {
      :exclusions [org.clojure/clojure]
      :source-paths ["dev-resources/src"]
      :repl-options {
        :init-ns meson.dev}
      :dependencies [
        [org.clojure/data.codec "0.1.0"]
        [org.clojure/tools.namespace "0.2.11"]]
      :plugins [
        [lein-shell "0.5.0"]]}
    :docs {
      :dependencies [
        [codox-theme-rdash "0.1.2"]]
      :plugins [
        [lein-codox "0.10.3"]
        [lein-simpleton "1.3.0"]]
      :codox {
        :project {
          :name "meson"
          :description "Clojure Client Library for the Mesos HTTP API"}
        :namespaces [#"^meson\.(?!dev)"]
        :metadata {
          :doc/format :markdown
          :doc "Documentation forthcoming"}
        :themes [:rdash]
        :doc-paths ["docs/md"]
        :output-path "docs/current"}}}
  :aliases {
    "check-deps" [
      "with-profile" "+test" "ancient" "check" ":all"]
    "kibit" [
      "with-profile" "+test" "do"
        ["shell" "echo" "== Kibit =="]
        ["kibit"]]
    "outlaw" [
      "with-profile" "+test"
      "eastwood" "{:namespaces [:source-paths] :source-paths [\"src\"]}"]
    "lint" [
      "with-profile" "+test" "do"
        ["check"] ["kibit"] ["outlaw"]]
    "docs" [
      "with-profile" "+docs" "codox"]
    "travis" [
      "do"
        ["version"]
        ["check-deps"]
        ["compile"]
        ["uberjar"]
        ["test" ":unit"]
        ["docs"]]
    "meson" [
      "run" "-m" "meson.ops.core"]})

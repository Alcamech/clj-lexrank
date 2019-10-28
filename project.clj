(defproject clj-lexrank "0.1.1"
  :description "Clojure Lexrank implementation"
  :url "https://github.com/Alcamech/clj-lexrank"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clojure-opennlp "0.5.0"]
                 [net.mikera/core.matrix "0.62.0"]]
  :main ^:skip-aot clj-lexrank.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

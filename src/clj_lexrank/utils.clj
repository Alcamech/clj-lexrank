(ns clj-lexrank.utils
  (:require [clojure.spec.alpha :as spec]))

(defmacro validate-spec
  "Validates spec. Takes predicate and input."
  [spec input]
  `(if-not (spec/valid? ~spec ~input)
     (throw (AssertionError. (spec/explain-str ~spec ~input)))
     true))
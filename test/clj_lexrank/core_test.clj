(ns clj-lexrank.core-test
  (:require [clojure.test :refer :all]
            [clj-lexrank.core :refer :all]))

(deftest lexrank-test
  (testing "Testing lexrank implementation."
    (is (=
          (lexrank "test-documents/test4.txt" 0.2 0.1 3 :sdoc)
          '("In this blog post, we will walk you through the different steps that are necessary to get you started with Docker Compose and show you how to use it."
             "To demonstrate the benefits of Docker Compose, we will create a simple Node.js \"Hello World\" application, which will run on 3 Docker Node.js containers."
             "HTTP requests will be distributed to these Node.js")))))



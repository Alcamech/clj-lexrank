# clj-lexrank

[![Clojars Project](https://img.shields.io/clojars/v/clj-lexrank.svg)](https://clojars.org/clj-lexrank)

Clojure LexRank implementation for extractive summarization.

## Usage

Arguments
  - ingest - Ingest can either be a file path, directory path, or string.
  - cosine-threshold - Threshold to test against when determining similarity.
  - lexrank-error - Value to compare against sigma in power method computation.
  - topN  - Top N sentences to use in the summary.
  - doctype - Keyword value can either be :str, :sdoc or :dir.
  
```clojure
(lexrank "test-documents/docker-history.txt" 0.2 0.1 5 :sdoc)
=>
("In 2008, Solomon Hykes founded dotCloud to build a language-agnostic platform-as-a-service (PaaS) offering."
 "Docker 1.0 was\nannounced in June 2014, just 15 months after the 0.1 release."
 "With the release of Docker 1.8, Docker introduced the
  content trust feature, which verifies the integrity and publisher of Docker images."
 "The
  language-agnostic aspect was the unique selling point for dotCloud—existing PaaSs were tied to particular sets of languages
  (e.g., Heroku supported Ruby, and Google App Engine supported Java and Python)."
 "Early versions of Docker were little more than a wrapper around LXC paired with a union filesystem, but the uptake and
  speed of development was shockingly fast.")

(lexrank "In this blog post, we will walk you through the different steps that are necessary to get you started with Docker 
Compose and show you how to use it. To demonstrate the benefits of Docker Compose, we will create a simple Node.js \"Hello World\" application, 
which will run on 3 Docker Node.js containers. HTTP requests will be distributed to these Node.js nodes by an HAProxy instance running on another Docker container. 
Compose is a tool used to define and run complex applications with Docker. With compose, you can define a multicontainer application in a single file and then spin 
your application up in a single command, which does everything that needs to be done to get it running." 0.2 0.1 3 :str)
=>
("In this blog post, we will walk you through the different steps that are necessary to get you started with Docker Compose and show you how to use it."
 "To demonstrate the benefits of Docker Compose, we will create a simple Node.js \"Hello World\" application, which will run on 3 Docker Node.js containers."
 "HTTP requests will be distributed to these Node.js")
```

## License

Copyright 2019 Lawton C. Mizell

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the 
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific 
language governing permissions and limitations under the License.

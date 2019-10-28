# clj-lexrank

Clojure LexRank Implementation for extractive summarization.

## Usage

Arguments
  - path - Path to file or directory.
  - cosine-threshold - threshold to test against when determining similarity.
  - lexrank-error - value to compare against sigma in power method computation.
  - topN  - Top N sentences to use in the summary.
  - sdoc - Boolean value for if its a single document entity, otherwise will be considered as a directory.
```clojure
(lexrank "test-documents/docker-history.txt" 0.2 0.1 5 true)
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
```

## License

Copyright 2019 Lawton C. Mizell

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the 
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific 
language governing permissions and limitations under the License.

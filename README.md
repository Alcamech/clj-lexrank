# clj-lexrank

Clojure LexRank Implementation

## Usage

Arguments
  - path - Path to file or directory.
  - cosine-threshold - threshold to test against when determining similarity.
  - lexrank-error - value to compare against sigma in power method computation.
  - topN  - Top N sentences to use in the summary.
  - sdoc - Boolean value for if its a single document entity, otherwise will be considered as a directory.
```clojure
(lexrank "test-documents/docker-history.txt" 0.2 0.1 5 true)
```

## License

Copyright 2019 Lawton C. Mizell

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the 
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific 
language governing permissions and limitations under the License.

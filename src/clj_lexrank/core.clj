(ns clj-lexrank.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.matrix :as matrix]
            [clojure.core.matrix.operators :refer [* - + == /]])
  (:refer-clojure :exclude [* - + == /])
  (:import (java.io BufferedReader FileReader))
  (:import java.io.File))

(defn count-words-in-document
  "Counts the words in the documents."
  [word document]
  (->> document
       (filter #(contains? % word))
       count))

(defn idf-words
  "Compute inverse document frequency."
  [documents]
  (let [N (count documents)
        all-docs-words (apply clojure.set/union (mapv set documents))]
    (->> all-docs-words
         (map (fn [w] {w (Math/log (/ N (count-words-in-document w documents)))}))
         (into {}))))

(defn sentences-in-single-document
  "extracts the sentences in a particular document"
  [file-path]
  (with-open [rdr (BufferedReader. (FileReader. file-path))]
    (->> (line-seq rdr)
         (mapcat #(str/split % #"[.|!|?|:]+"))
         (map str/trim)
         (map str/trim-newline)
         (mapv str/lower-case))))

;; TODO: function that generates a vector of vector of sentences for a single document.

(defn sentences-in-multiple-documents
  "Generates a vector of vector of sentences, as per the documents containing them."
  [dir-path]
  (let [files (.listFiles (File. dir-path))]
    (mapv sentences-in-single-document files)))

(defn words-in-document-sentences
  "Extracts the words from all document sentences."
  [document-sentences]
  (mapcat #(str/split % #"[\s|,|;]") document-sentences))

(defn gen-idf-map-from-docs-sentences
  "Generates a map with all words IDF."
  [document-w-sentences]
  (let [document-w-words (map set (map words-in-document-sentences
                                       document-w-sentences))]
    (idf-words document-w-words)))

(defn tfidf-vector-from-sentence
  "Computes tf-idf vector from sentence. A sentence is a vector of words.
  idf must be computed beforehand."
  [idf-map sentence]
  (let [sentence-words (str/split sentence #"[\s|,|;|:]+")
        tf-sentence (->> sentence-words
                         (map (fn [k] {k 1}))
                         (reduce (partial merge-with +)))]
    ;;We compute the TF for the words based on their
    ;; frequency in every sentence.
    (->> tf-sentence
         (map (fn [[k v]] {k (* v (get idf-map k))}))
         ;; and then we generate TFIDF maps for each
         ;; sentence, referred by words. These
         ;; will be the vectors representing our sentences.
         (into {}))))

(defn cosine-similarity
  "Compute cosine-similarity."
  [tfidf-sentence1 tfidf-sentence2]
  (let [common-words (clojure.set/intersection (set (keys tfidf-sentence1))
                                               (set (keys tfidf-sentence2)))
        s1-common (select-keys tfidf-sentence1 common-words)
        s2-common (select-keys tfidf-sentence2 common-words)
        s1xs2 (reduce + (vals (merge-with * s1-common s2-common)))
        sqrt-s1pow2 (->> (vals tfidf-sentence1)
                         (map #(Math/pow % 2))
                         (reduce +)
                         Math/sqrt)
        sqrt-s2pow2 (->> (vals tfidf-sentence2)
                         (map #(Math/pow % 2))
                         (reduce +)
                         Math/sqrt)]
    (if (every? (comp not zero?)
                [sqrt-s1pow2 sqrt-s2pow2])
      (/ s1xs2 (* sqrt-s1pow2 sqrt-s2pow2))
      0)))

(defn power-method
  [mat error]
  (let [size (matrix/dimension-count mat 0)]
    (loop [p (matrix/matrix (into [] (repeat size
                                             (/ 1 size))))]
      (let [new-p (matrix/mmul (matrix/transpose mat) p)
            sigma (matrix/distance new-p p)]
        (if (< sigma error)
          new-p
          (recur new-p))))))

(defn lexrank
  [path cosine-threshold lexrank-error topN]
  (let [sentences-by-docs (sentences-in-multiple-documents path)
        idf-map (gen-idf-map-from-docs-sentences sentences-by-docs)
        all-sentences (into [] (mapcat identity sentences-by-docs))
        sentences-w-tfidf (into [] (reduce concat
                                           (for [s sentences-by-docs]
                                             (map (partial tfidf-vector-from-sentence idf-map)
                                                  s))))

        ;; we begin representing all of our sentences as TFIDF vectors
        cent-raw-matrix (matrix/matrix (into [] (for [i sentences-w-tfidf]
                                                  (into [] (for [j sentences-w-tfidf]
                                                             (let [cos-sim-i-j (cosine-similarity i j)]
                                                               (if (>= cos-sim-i-j cosine-threshold)
                                                                 cos-sim-i-j
                                                                 0)))))))
        ;; we build a centrality raw matrix as the relationship
        ;; between every couple of sentences.
        ;; if similarity is superior than threshold,
        ;; we put it for the corresponding sentences,
        ;; else, we put zero.
        degrees (->> (matrix/rows cent-raw-matrix)
                     (mapv (partial reduce +)))
        ;; we compute the degrees for every row,
        ;; as the number of not null elements.
        centrality-matrix (matrix/matrix (into []
                                               (for [i (range (count degrees))]
                                                 (/ (matrix/get-row cent-raw-matrix i)
                                                    (get degrees i)))))
        ;; we compute the matrix labeled B in the description,
        ;; dividing each element by the degree of hte row
        lexrank-vector (power-method centrality-matrix lexrank-error)
        ;; we apply the powermethod
        lexrank-v-w-indices (zipmap (iterate inc 0) lexrank-vector)]
    ;; we assign indices to the setnences in the lexrnak vector
    ;; so we are able to retrieve them
    (->> (sort-by val > lexrank-v-w-indices)
         (take topN)
         (map #(get % 0))
         ;; and we show the sentences corresponding to
         ;; the topN first highest LexRank scores.
         (map #(get all-sentences %)))))



(comment (defn lexrank-2
           [path cosine-threshold lexrank-error topN]
           (let [sentences-by-docs (sentences-in-multiple-documents path)
                 idf-map (gen-idf-map-from-doc-sentences sentences-by-docs)
                 all-sentences (into [] (mapcat identity sentences-by-docs))
                 sentences-w-tfidf (into [] (reduce concat (for [s sentences-by-docs] (map (partial tfidf-vector-from-sentence idf-map) s))))
                 cent-raw-matrix (matrix/matrix (into [] (for [i sentences-w-tfidf] (into [] (for [j sentences-w-tfidf]
                                                                                               (let [cos-sim-i-j (cosine-similarity i j)]
                                                                                                 (if (>= cos-sim-i-j cosine-threshold) cos-sim-i-j 0)))))))
                 degrees (->> (matrix/rows cent-raw-matrix)
                              (mapv (partial reduce +)))
                 centrality-matrix (matrix/matrix (into [] (for [i (range (count degrees))]
                                                             (/ (matrix/get-row cent-raw-matrix i) (get degrees i)))))
                 lexrank-vector (power-method centrality-matrix lexrank-error)
                 lexrank-v-w-indices (zipmap (iterate inc 0) lexrank-vector)]
             (->> (sort-by val > lexrank-v-w-indices)
                  (take topN)
                  (map #(get % 0))
                  (map #(get all-sentences %))))))






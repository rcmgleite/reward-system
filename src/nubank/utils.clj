(ns nubank.utils
  (:require [clojure.java.io :as io]))

(defn exp [x n]
  "Math exp function"
  (reduce * (repeat n x)))

(defn foreach-line-in-file [file-path callback]
  "For each line of the given file, executes a callback on it"
  (with-open [r (io/reader file-path)]
    (doseq [line (line-seq r)]
      (callback line))))
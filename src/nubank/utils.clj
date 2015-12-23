(ns nubank.utils)

(defn exp [x n]
  (reduce * (repeat n x)))
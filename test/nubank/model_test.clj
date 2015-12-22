(ns nubank.model-test
  (:require [clojure.test :refer :all]
            [nubank.model :as model]))

; ---------------------------------------------------
;                 MOCKED INVITATION TREE
; ---------------------------------------------------
(def mocked-inv-tree {1 (model/new-node 5 -1 [2 3]),
                      2 (model/new-node 1 1 [])
                      3 (model/new-node 4 1 [4])
                      4 (model/new-node 3 3 [5 6 7])
                      5 (model/new-node 2 4 [10])
                      6 (model/new-node 1 4 [8 9])
                      7 (model/new-node 1 4 [])
                      8 (model/new-node 0 6 [])
                      9 (model/new-node 0 6 [])
                      10 (model/new-node 1 5 [11])
                      11 (model/new-node 0 10 [])
                      })

; ---------------------------------------------------
; ---------------------------------------------------
;                FUNCTIONAL MODEL TESTS
; ---------------------------------------------------
; ---------------------------------------------------
; ---------------------------------------------------
;                 SCORE FACTOR TESTS
; ---------------------------------------------------
(deftest score-factor-test
  (println "[INFO] Running test: score-factor-test")
  (is (= (model/score-factor 1) 1.0))
  (is (= (model/score-factor 2) 1.5))
  (is (= (model/score-factor 3) 1.75))
  (is (= (model/score-factor 4) 1.875)))

; ---------------------------------------------------
; ---------------------------------------------------
;                 MUTABLE MODEL TESTS
; ---------------------------------------------------
; ---------------------------------------------------
; ---------------------------------------------------
;                 HEIGHT FOR NODE TESTS
; ---------------------------------------------------
(deftest height-for-node-test
  (println "[INFO] Running test: height-for-node-test")
  (dosync (ref-set model/invitations mocked-inv-tree))
  (is (= (model/height-from-node 1) 5))
  (is (= (model/height-from-node 2) 1))
  (is (= (model/height-from-node 3) 4))
  (is (= (model/height-from-node 4) 3))
  (is (= (model/height-from-node 5) 2))
  (is (= (model/height-from-node 6) 1))
  (is (= (model/height-from-node 7) 1))
  (is (= (model/height-from-node 8) 0))
  (is (= (model/height-from-node 9) 0))
  (is (= (model/height-from-node 10) 1))
  (is (= (model/height-from-node 11) 0)))

; ---------------------------------------------------
;                 SCORE FOR NODE TESTS
; ---------------------------------------------------
(deftest score-for-node-test
  (println "[INFO] Running test: score-for-node-test")
  (dosync (ref-set model/invitations mocked-inv-tree))
  (is (= (model/score-for-node 1) 2.875))
  (is (= (model/score-for-node 2) 0))
  (is (= (model/score-for-node 3) 1.75))
  (is (= (model/score-for-node 4) 3.5))
  (is (= (model/score-for-node 5) 1.0))
  (is (= (model/score-for-node 6) 0))
  (is (= (model/score-for-node 7) 0))
  (is (= (model/score-for-node 8) 0))
  (is (= (model/score-for-node 9) 0))
  (is (= (model/score-for-node 10) 0))
  (is (= (model/score-for-node 11) 0)))

; ---------------------------------------------------
;                ALREADY INVITED TEST
; ---------------------------------------------------
(deftest already-invited-test
  (println "[INFO] Running test: already-invited-test")
  (dosync (ref-set model/invitations mocked-inv-tree))
  (is (= (model/already-invited 4) true))
  (is (= (model/already-invited 5) true))
  (is (= (model/already-invited 6) true))
  (is (= (model/already-invited 10) true))
  (is (= (model/already-invited 12) false))
  (is (= (model/already-invited 14) false)))

; ---------------------------------------------------
;                   INSERTION TEST
; ---------------------------------------------------
(deftest insertion-test
  (println "[INFO] Running test: insertion-test")
  (dosync (ref-set model/invitations {}))
  (model/insert-invitation 1 2)
  (model/insert-invitation 1 3)
  (model/insert-invitation 3 4)
  (model/insert-invitation 4 3)
  (model/insert-invitation 2 4)
  (model/insert-invitation 4 5)
  (model/insert-invitation 4 6)
  (model/insert-invitation 4 7)
  (model/insert-invitation 5 6)
  (model/insert-invitation 6 8)
  (model/insert-invitation 6 9)
  (model/insert-invitation 7 1)
  (model/insert-invitation 5 10)
  (model/insert-invitation 10 11)
  (is (= @model/invitations mocked-inv-tree)))


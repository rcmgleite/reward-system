(ns nubank.model-test
  (:require [clojure.test :refer :all]
            [nubank.model :as model]))

; ---------------------------------------------------
;                 MOCKED INVITE TREES
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
                      11 (model/new-node 0 10 [])})

(def mocked-inv-tree-2 {1 (model/new-node 4 -1 [2 3])
                        2 (model/new-node 3 1 [4 5])
                        3 (model/new-node 2 1 [9 10])
                        4 (model/new-node 1 2 [8])
                        5 (model/new-node 2 2 [6])
                        6 (model/new-node 1 5 [7])
                        7 (model/new-node 0 6 [])
                        8 (model/new-node 0 4 [])
                        9 (model/new-node 1 3 [11])
                        10 (model/new-node 0 3 [])
                        11 (model/new-node 0 9 [])})

(def mocked-inv-tree-3 {1 (model/new-node 3 -1 [2 3])
                        2 (model/new-node 2 1 [7])
                        3 (model/new-node 2 1 [4])
                        4 (model/new-node 1 3 [5 6])
                        5 (model/new-node 0 4 [])
                        6 (model/new-node 0 4 [])
                        7 (model/new-node 1 2 [8])
                        8 (model/new-node 0 7 [])})

(def mocked-inv-tree-4 {1 (model/new-node 3 -1 [2 3])
                        2 (model/new-node 2 1 [7])
                        3 (model/new-node 2 1 [4 5 9])
                        4 (model/new-node 1 3 [6])
                        5 (model/new-node 0 3 [])
                        6 (model/new-node 0 4 [])
                        7 (model/new-node 1 2 [8])
                        8 (model/new-node 0 7 [])
                        9 (model/new-node 1 3 [10 11])
                        10 (model/new-node 0 9 [])
                        11 (model/new-node 0 9 [])})

(def mocked-inv-tree-5 {1 (model/new-node 4 -1 [2 3 12])
                        2 (model/new-node 3 1 [7])
                        3 (model/new-node 3 1 [4 5 9])
                        4 (model/new-node 2 3 [6])
                        5 (model/new-node 0 3 [])
                        6 (model/new-node 1 4 [])
                        7 (model/new-node 2 2 [8])
                        8 (model/new-node 1 7 [])
                        9 (model/new-node 1 3 [10 11])
                        10 (model/new-node 0 9 [])
                        11 (model/new-node 0 9 [])
                        12 (model/new-node 3 1 [13])
                        13 (model/new-node 2 12 [14])
                        14 (model/new-node 1 13 [])})

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
  (is (= (model/score-factor 1) 1))
  (is (= (model/score-factor 2) 1.5))
  (is (= (model/score-factor 3) 1.75))
  (is (= (model/score-factor 4) 1.875)))

; ---------------------------------------------------
;                 HEIGHT FOR NODE TESTS
; ---------------------------------------------------
(deftest height-for-node-test
  (println "[INFO] Running test: height-for-node-test")
  (is (= (model/height-from-node mocked-inv-tree 1) 5))
  (is (= (model/height-from-node mocked-inv-tree 2) 1))
  (is (= (model/height-from-node mocked-inv-tree 3) 4))
  (is (= (model/height-from-node mocked-inv-tree 4) 3))
  (is (= (model/height-from-node mocked-inv-tree 5) 2))
  (is (= (model/height-from-node mocked-inv-tree 6) 1))
  (is (= (model/height-from-node mocked-inv-tree 7) 1))
  (is (= (model/height-from-node mocked-inv-tree 8) 0))
  (is (= (model/height-from-node mocked-inv-tree 9) 0))
  (is (= (model/height-from-node mocked-inv-tree 10) 1))
  (is (= (model/height-from-node mocked-inv-tree 11) 0)))

; ---------------------------------------------------
;                 SCORE FOR NODE TESTS
; ---------------------------------------------------
(deftest score-for-node-test
  (println "[INFO] Running test: score-for-node-test")
  (is (= (model/score-for-node mocked-inv-tree 1) 2.875))
  (is (= (model/score-for-node mocked-inv-tree 2) 0))
  (is (= (model/score-for-node mocked-inv-tree 3) 1.75))
  (is (= (model/score-for-node mocked-inv-tree 4) 3.5))
  (is (= (model/score-for-node mocked-inv-tree 5) 1))
  (is (= (model/score-for-node mocked-inv-tree 6) 0))
  (is (= (model/score-for-node mocked-inv-tree 7) 0))
  (is (= (model/score-for-node mocked-inv-tree 8) 0))
  (is (= (model/score-for-node mocked-inv-tree 9) 0))
  (is (= (model/score-for-node mocked-inv-tree 10) 0))
  (is (= (model/score-for-node mocked-inv-tree 11) 0)))

; ---------------------------------------------------
;                ALREADY INVITED TEST
; ---------------------------------------------------
(deftest already-invited-test
  (println "[INFO] Running test: already-invited-test")
  (is (= (model/already-invited mocked-inv-tree 4) true))
  (is (= (model/already-invited mocked-inv-tree 5) true))
  (is (= (model/already-invited mocked-inv-tree 6) true))
  (is (= (model/already-invited mocked-inv-tree 10) true))
  (is (= (model/already-invited mocked-inv-tree 12) false))
  (is (= (model/already-invited mocked-inv-tree 14) false)))

; ---------------------------------------------------
;                 APPEND CHILDREN TESTS
; ---------------------------------------------------
(deftest append-children-test
  (println "[INFO] Running test: append-children-test")
  (def t (model/append-children {1 {:parent -1 :subtree-h 0 :children [2]}} 1 3))
  (is (= (t {1 {:parent -1  :subtree-h 0 :children [2 3]}})))
  (def t (model/append-children {1 {:parent -1  :subtree-h 0 :children [2]}} 1 4))
  (is (= (t {1 {:parent -1  :subtree-h 0 :children [2 3 4]}})))
  )

; ---------------------------------------------------
;                 INC NODE HEIGHT TESTS
; ---------------------------------------------------
(deftest inc-node-height-test
  (println "[INFO] Running test: inc-node-height-test")
  (def t {1 {:parent -1 :subtree-h 0 :children [2]} 2 {:parent 1 :subtree-h -1 :children []}})
  (def t (model/inc-node-height t 1))
  (is (= t {1 {:parent -1 :subtree-h 1 :children [2]} 2 {:parent 1 :subtree-h -1 :children []}}))
  (def t (model/inc-node-height t 1))
  (def t (model/inc-node-height t 1))
  (def t (model/inc-node-height t 1))
  (is (= t {1 {:parent -1 :subtree-h 4 :children [2]} 2 {:parent 1 :subtree-h -1 :children []}}))
  (def t (model/inc-node-height t 2))
  (def t (model/inc-node-height t 2))
  (is (= t {1 {:parent -1 :subtree-h 4 :children [2]} 2 {:parent 1 :subtree-h 1 :children []}})))

; ---------------------------------------------------
;                 UPDATE HEIGHT TESTS
; ---------------------------------------------------
(deftest update-height-test
  (println "[INFO] Running test: update-height-test")
  (def t {1 {:parent -1 :subtree-h 0 :children [2]} 2 {:parent 1 :subtree-h -1 :children []}})
  (def t (model/update-height t 2))
  (is (= t {1 {:parent -1 :subtree-h 1 :children [2]} 2 {:parent 1 :subtree-h 0 :children []}}))
  (def t  (merge t {2 {:parent 1 :subtree-h 0 :children [3]} 3 {:parent 2 :subtree-h -1 :children []}}))
  (def t (model/update-height t 3))
  (is (= t {1 {:parent -1 :subtree-h 2 :children [2]} 2 {:parent 1 :subtree-h 1 :children [3]} 3 {:parent 2 :subtree-h 0 :children []}})))

; ---------------------------------------------------
;                 INSERT ROOT NODE TESTS
; ---------------------------------------------------
(deftest insert-root-node-test
  (println "[INFO] Running test: insert-root-node-test")
  (is (= (model/insert-root-node {} 1 2) {1 {:parent -1 :subtree-h 0 :children [2]}})))

; ---------------------------------------------------
;                 INSERT INVITED NODE TESTS
; ---------------------------------------------------
(deftest insert-invited-node-test
  (println "[INFO] Running test: insert-invited-node-test")
  (is (= (model/insert-invited-node {} 1 2) {2 {:parent 1 :subtree-h -1 :children []}})))

; ---------------------------------------------------
;                 INSERT NEW INVITE TESTS
; ---------------------------------------------------
(deftest insert-new-invite-test
  (println "[INFO] Running test: insert-new-invite-test")
  (def t (model/insert-new-invite {} 1 2))
  (is (= t {1 {:parent -1 :subtree-h 1 :children [2]} 2 {:parent 1 :subtree-h 0 :children []}}))
  (def t (model/insert-new-invite t 1 3))
  (is (= t {1 {:parent -1 :subtree-h 1 :children [2 3]} 2 {:parent 1 :subtree-h 0 :children []} 3 {:parent 1 :subtree-h 0 :children []}}))
  (def t (model/insert-new-invite t 2 4))
  (is (= t {1 {:parent -1 :subtree-h 2 :children [2 3]} 2 {:parent 1 :subtree-h 1 :children [4]} 3 {:parent 1 :subtree-h 0 :children []} 4 {:parent 2 :subtree-h 0 :children []}}))
  (def t (model/insert-new-invite t 4 5))
  (is (= (get-in t [1 :subtree-h]) 3))
  (is (= (get-in t [2 :subtree-h]) 2))
  (is (= (get-in t [3 :subtree-h]) 0)))

; ---------------------------------------------------
; ---------------------------------------------------
;                 MUTABLE MODEL TESTS
; ---------------------------------------------------
; ---------------------------------------------------

; ---------------------------------------------------
;                   INSERTION TEST
; ---------------------------------------------------
(deftest insertion-test-1
  (println "[INFO] Running test: insertion-test")
  (dosync
    (ref-set model/invites {}))
  (model/insert-invite 1 2)
  (model/insert-invite 1 1)
  (model/insert-invite 1 3)
  (model/insert-invite 3 4)
  (model/insert-invite 4 3)
  (model/insert-invite 2 4)
  (model/insert-invite 2 2)
  (model/insert-invite 4 5)
  (model/insert-invite 4 6)
  (model/insert-invite 4 7)
  (model/insert-invite 5 6)
  (model/insert-invite 6 8)
  (model/insert-invite 6 9)
  (model/insert-invite 7 1)
  (model/insert-invite 5 10)
  (model/insert-invite 10 11)
  (model/insert-invite 2 1)
  (model/insert-invite 2 3)
  (model/insert-invite 5 8)
  (model/insert-invite 5 6)
  (model/insert-invite 5 10)
  (model/insert-invite 5 5)
  (is (= @model/invites mocked-inv-tree))
  (def score (model/calc-score))
  (is (= (get score 1) 2.875))
  (is (= (get score 2) 0))
  (is (= (get score 3) 1.75))
  (is (= (get score 4) 3.5))
  (is (= (get score 5) 1))
  (is (= (get score 7) 0)))

(deftest insertion-test-2
  (println "[INFO] Running test: insertion-test-2")
  (dosync
    (ref-set model/invites {}))
  (model/insert-invite 1 2)
  (model/insert-invite 1 3)
  (model/insert-invite 2 4)
  (model/insert-invite 2 5)
  (model/insert-invite 3 9)
  (model/insert-invite 3 10)
  (model/insert-invite 4 8)
  (model/insert-invite 5 6)
  (model/insert-invite 6 7)
  (model/insert-invite 9 11)
  (is (= @model/invites mocked-inv-tree-2))
  (def score (model/calc-score))
  (is (= (get score 1) 3.25))
  (is (= (get score 2) 2.5))
  (is (= (get score 3) 1))
  (is (= (get score 4) 0))
  (is (= (get score 5) 1))
  (is (= (get score 6) 0))
  (is (= (get score 7) 0))
  (is (= (get score 8) 0))
  (is (= (get score 9) 0))
  (is (= (get score 10) 0))
  (is (= (get score 11) 0)))

(deftest insertion-test-3
  (println "[INFO] Running test: insertion-test-3")
  (dosync
    (ref-set model/invites {}))
  (model/insert-invite 1 2)
  (model/insert-invite 1 3)
  (model/insert-invite 3 4)
  (model/insert-invite 2 4)
  (model/insert-invite 4 5)
  (model/insert-invite 4 6)
  (model/insert-invite 2 7)
  (model/insert-invite 7 8)
  (is (= @model/invites mocked-inv-tree-3))
  (def score (model/calc-score))
  (is (= (get score 1) 3.0))
  (is (= (get score 2) 1))
  (is (= (get score 3) 1))
  (is (= (get score 4) 0))
  (is (= (get score 5) 0))
  (is (= (get score 6) 0))
  (is (= (get score 7) 0))
  (is (= (get score 8) 0)))

(deftest insertion-tree-4
  (println "[INFO] Running test: insertion-test-4")
  (dosync
    (ref-set model/invites {}))
  (model/insert-invite 1 2)
  (model/insert-invite 1 3)
  (model/insert-invite 3 4)
  (model/insert-invite 2 4)
  (model/insert-invite 3 5)
  (model/insert-invite 4 6)
  (model/insert-invite 2 7)
  (model/insert-invite 7 8)
  (model/insert-invite 3 9)
  (model/insert-invite 9 5)
  (model/insert-invite 9 10)
  (model/insert-invite 9 11)
  (is (= @model/invites mocked-inv-tree-4))
  (def score (model/calc-score))
  (is (= (get score 1) 3.0))
  (is (= (get score 2) 1))
  (is (= (get score 3) 2))
  (is (= (get score 4) 0))
  (is (= (get score 5) 0))
  (is (= (get score 6) 0))
  (is (= (get score 7) 0))
  (is (= (get score 8) 0))
  (is (= (get score 9) 0)))

(deftest insertion-test-5
  (println "[INFO] Running test: insertion-test-5")
  (dosync
    (ref-set model/invites {}))
  (model/insert-invite 1 2)
  (model/insert-invite 1 3)
  (model/insert-invite 2 7)
  (model/insert-invite 7 8)
  (model/insert-invite 8 2)
  (model/insert-invite 3 4)
  (model/insert-invite 3 5)
  (model/insert-invite 3 9)
  (model/insert-invite 9 10)
  (model/insert-invite 9 11)
  (model/insert-invite 4 6)
  (model/insert-invite 6 10)
  (model/insert-invite 1 12)
  (model/insert-invite 12 9)
  (model/insert-invite 12 11)
  (model/insert-invite 12 13)
  (model/insert-invite 13 14)
  (model/insert-invite 14 11)
  (is (= @model/invites mocked-inv-tree-5))
  (def score (model/calc-score))
  (is (= (get score 1) 5.25))
  (is (= (get score 2) 1.5))
  (is (= (get score 3) 2.5))
  (is (= (get score 4) 1))
  (is (= (get score 5) 0))
  (is (= (get score 6) 0))
  (is (= (get score 7) 1))
  (is (= (get score 8) 0))
  (is (= (get score 9) 0))
  (is (= (get score 10) 0))
  (is (= (get score 11) 0))
  (is (= (get score 12) 1.5))
  (is (= (get score 13) 1))
  (is (= (get score 14) 0)))


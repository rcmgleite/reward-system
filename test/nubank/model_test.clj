(ns nubank.model-test
  (:require [clojure.test :refer :all]
            [nubank.model :as model]))

; ---------------------------------------------------
;                 MOCKED INVITATION TREE
; ---------------------------------------------------
(reset! model/invitations {1 (model/new-node 3 -1 [2 3]),
                      2 (model/new-node 1 1 [])
                      3 (model/new-node 2 1 [4])
                      4 (model/new-node 1 3 [5 6])
                      5 (model/new-node 0 4 [])
                      6 (model/new-node 0 4 [])
                      })

; ---------------------------------------------------
; ---------------------------------------------------
;                FUNCTIONAL MODEL TESTS
; ---------------------------------------------------
; ---------------------------------------------------

; ---------------------------------------------------
;                 SCORE FACTOR TESTS
; ---------------------------------------------------
(deftest score-factor-test1
  (is (= (model/score-factor 1) 1.0))
  (is (= (model/score-factor 2) 1.5))
  (is (= (model/score-factor 3) 1.75))
  (is (= (model/score-factor 4) 1.875)))

; ---------------------------------------------------
;                 HEIGHT FOR NODE TESTS
; ---------------------------------------------------
(deftest heigth-for-node
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 1) 3))
    (is (= (model/heigth-from-node @inv-tree 2) 1))
    (is (= (model/heigth-from-node @inv-tree 3) 2))
    (is (= (model/heigth-from-node @inv-tree 4) 1))
    (is (= (model/heigth-from-node @inv-tree 5) 0))
    (is (= (model/heigth-from-node @inv-tree 6) 0))))

; ---------------------------------------------------
;                 SCORE FOR NODE TESTS
; ---------------------------------------------------
(deftest score-for-node-test1
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 1) 2.5))
    (is (= (model/score-for-node @inv-tree 2) 0.0))
    (is (= (model/score-for-node @inv-tree 3) 1.0))
    (is (= (model/score-for-node @inv-tree 4) 0.0))
    (is (= (model/score-for-node @inv-tree 5) 0.0))
    (is (= (model/score-for-node @inv-tree 6) 0.0))))

; ---------------------------------------------------
;                ALREADY INVITED TEST
; ---------------------------------------------------
(deftest already-invited-1
  (let [inv-tree model/invitations]
    (is (= (model/already-invited 4) true))
    (is (= (model/already-invited 10) false))))

; ---------------------------------------------------
;                COMPLETE SCORE TESTS
; ---------------------------------------------------
; TODO


; ---------------------------------------------------
; ---------------------------------------------------
;                 MUTABLE MODEL TESTS
; ---------------------------------------------------
; ---------------------------------------------------
(reset! model/invitations {})


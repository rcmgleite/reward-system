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
;                 SCORE FACTOR TESTS
; ---------------------------------------------------
(deftest score-factor-test1
  (is (= (model/score-factor 1) 1.0)))

(deftest score-factor-test2
  (is (= (model/score-factor 2) 1.5)))

(deftest score-factor-test3
  (is (= (model/score-factor 3) 1.75)))

(deftest score-factor-test4
  (is (= (model/score-factor 4) 1.875)))

; ---------------------------------------------------
;                 HEIGHT FOR NODE TESTS
; ---------------------------------------------------
(deftest heigth-for-node-1
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 1) 3))))

(deftest heigth-for-node-2
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 2) 1))))

(deftest heigth-for-node-3
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 3) 2))))

(deftest heigth-for-node-4
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 4) 1))))

(deftest heigth-for-node-5
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 5) 0))))

(deftest heigth-for-node-6
  (let [inv-tree model/invitations]
    (is (= (model/heigth-from-node @inv-tree 6) 0))))

; ---------------------------------------------------
;                 SCORE FOR NODE TESTS
; ---------------------------------------------------
(deftest score-for-node-test1
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 1) 2.5))))

(deftest score-for-node-test2
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 2) 0.0))))

(deftest score-for-node-test3
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 3) 1.0))))

(deftest score-for-node-test4
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 4) 0.0))))

(deftest score-for-node-test5
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 5) 0.0))))

(deftest score-for-node-test6
  (let [inv-tree model/invitations]
    (is (= (model/score-for-node @inv-tree 6) 0.0))))
; ---------------------------------------------------
;                COMPLETE SCORE TESTS
; ---------------------------------------------------
; TODO

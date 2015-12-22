(ns nubank.model
  (:require [clojure.math.numeric-tower :as math]))
; -------------------------------------
;           FUNCTIONAL MODEL
; -------------------------------------
(defn score-factor [h]
    (loop [result 0.0 curr-h 0]
    (if (= curr-h h)
      result
      (recur (+ result (math/expt 0.5 curr-h)) (inc curr-h)))))

(defn heigth-from-node [invitation-tree n]
  (:subtree-h (get invitation-tree n)))

(defn score-for-node [invitation-tree n]
  (loop [lst (:children (get invitation-tree n)) score 0.0]
    (if (empty? lst)
      score
      (recur (rest lst) (+ score (score-factor (heigth-from-node invitation-tree (first lst))))))))


; -------------------------------------
;            MUTABLE MODEL
; -------------------------------------
; node struct representation
(defstruct node :subtree-h :parent :children)

; function that returns a new node
(defn new-node [h parent children]
  (struct node h parent children))

; global variable that holds all invitations
(def invitations (atom {}))

; global variable that holds the calculated score for the invitations given
(def score-result (atom {}))

(defn calc-score []
  (reset! score-result (into {} (for [[k v] @invitations] [k (score-for-node @invitations k)]))))

; function that updates the h of a sub-tree on the invitations tree
(defn update_heigth [n]
  ; TODO
  )

; function to insert an invitation
(defn insert-invitation [inviter invited]
  ; TODO
  (swap! invitations assoc inviter (struct node 0 0 (conj ((:children (get invitations inviter))) invited)))
  (swap! invitations assoc invited (struct node 0 inviter (get invitations invited))))

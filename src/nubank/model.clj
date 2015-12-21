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

(defn score [invitation-tree]
  )

; -------------------------------------
;            MUTABLE MODEL
; -------------------------------------
;; node struct
(defstruct node :subtree-h :parent :children)

(defn new-node [h parent children]
  (struct node h parent children))

;; global variable that holds all invitations
(def invitations (atom {}))

(defn update_heigth [n]
  )

;; function to insert an invitation
(defn insert-invitation [inviter invited]
  (swap! invitations assoc inviter (struct node 0 0 (conj ((:children (get invitations inviter))) invited)))
  (swap! invitations assoc invited (struct node 0 inviter (get invitations invited))))

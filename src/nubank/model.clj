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

; returns the subtree-h for a given node
(defn height-from-node [invitation-tree n]
  (:subtree-h (get invitation-tree n)))

; returns the score for a given node
(defn score-for-node [invitation-tree n]
  (loop [lst (:children (get invitation-tree n)) score 0.0]
    (if (empty? lst)
      score
      (recur (rest lst) (+ score (score-factor (height-from-node invitation-tree (first lst))))))))

; -------------------------------------
;            MUTABLE MODEL
; -------------------------------------
; node struct representation
(defstruct node :subtree-h :parent :children)

; function that returns a new node
(defn new-node [h parent children]
  (struct node h parent children))

; global variable that holds all invitations
(def invitations (ref {}))

; global variable that holds the calculated score for the invitations given
(def score-result (atom {}))

; Calculate score for all nodes on tree
; it acts like a cache so that a request to score doesn't have to calculate it every time.
(defn calc-score []
  (reset! score-result (into {} (for [[k v] @invitations] [k (score-for-node @invitations k)]))))

; Verify if node n was already invited
(defn already-invited [n]
  (if (= (get @invitations n) nil)
    false
    true))

; insert root node on tree
(defn insert-root [inviter invited]
  (dosync (ref-set invitations (assoc @invitations inviter (new-node 0 -1 [invited])))))

; return new map to be used on swap! for invitatoins atom
(defn append-children [inv-tree inviter invited]
  (let [node-to-update (get inv-tree inviter)]
  (assoc inv-tree inviter (new-node (:subtree-h node-to-update) (:parent node-to-update) (conj (:children node-to-update) invited)))))

; add the newest invited to the inviter children vec. Executes only if
; invited was not previously invited.
(defn update-inviter-children [inviter invited]
  (dosync
    (if (not (already-invited invited))
      (alter invitations append-children inviter invited))))

; Insert an inviter
(defn insert-inviter [inviter invited]
  (if (empty? @invitations)
    (insert-root inviter invited)
    (update-inviter-children inviter invited)))

; return new map to be used on swap! for invitatoins atom
(defn update-node-height [inv-tree n]
  (let [node-to-update (get inv-tree n)]
    (assoc inv-tree n (new-node (inc (:subtree-h node-to-update)) (:parent node-to-update) (:children node-to-update)))))

; return parent for the node given
(defn get-parent-from [node]
  (get @invitations (:parent node)))

; return true if the 2 nodes have the same height
(defn same-height [n1 n2]
  (if (= (:subtree-h n1) (:subtree-h n2))
    true
    false))

; function that updates the h of a sub-tree on the invitations tree
(defn update-height [n]
  (dosync
    (loop [n n]
      (if (not= n -1)
        (do
          (alter invitations update-node-height n)
          (let [curr-node (get @invitations n)]
            (if (same-height curr-node (get-parent-from curr-node))
              (recur (:parent curr-node)))))))))


; Insert an invited
(defn insert-invited [inviter invited]
  (dosync
    (if (not (already-invited invited))
      (do
        (alter invitations assoc invited (new-node -1 inviter []))
        (update-height invited))
      (do
        (if (= (height-from-node @invitations inviter) 0)
          (update-height inviter))))))


(defn insert-invitation [ inviter invited]
  (dosync
    (insert-inviter inviter invited)
    (insert-invited inviter invited)))


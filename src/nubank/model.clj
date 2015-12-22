(ns nubank.model
  (:require [clojure.math.numeric-tower :as math]))
; -------------------------------------
;           FUNCTIONAL MODEL
; -------------------------------------

(defn score-factor [h]
  "(1/2)^k where k"
  (let [hs (take h (range))]
     (reduce + (map (fn [curr-h]
                 (math/expt 0.5 curr-h)) hs))))

; -------------------------------------
;            MUTABLE MODEL
; -------------------------------------
; node struct representation
(defstruct node :subtree-h :parent :children)

(defn new-node [h parent children]
  "function that returns a new node"
  (struct node h parent children))

; global variable that holds all invitations-tree
(def invitations-tree (ref {}))

; global variable that holds the calculated score for the invitations-tree given
(def score-result (atom {}))

(defn height-from-node [n]
  "returns the subtree-h for a given node"
  (:subtree-h (get @invitations-tree n)))

(defn score-for-node [n]
  "returns the score for a given node"
  (let [children (:children (get @invitations-tree n))]
    (reduce + (map (fn [curr-child]
                     (score-factor (height-from-node curr-child)))children))))

; it acts like a cache so that a request to score doesn't have to calculate it every time.
(defn calc-score []
  "Calculate score for all nodes on tree"
  (reset! score-result (into {} (for [[k v] @invitations-tree] [k (score-for-node k)]))))

(defn already-invited [n]
  "Verify if node n was already invited"
  (if (= (get @invitations-tree n) nil)
    false
    true))

(defn append-children [inv-tree inviter invited]
  "return new map to be used on ref alter functions like alter or swap for invitatoins atom"
  (let [node-to-update (get inv-tree inviter)]
  (assoc inv-tree inviter (new-node (:subtree-h node-to-update) (:parent node-to-update) (conj (:children node-to-update) invited)))))

(defn inc-node-height [inv-tree n]
  "return new map to be used on ref alter functions like alter or swap for invitatoins atom"
  (let [node-to-update (get inv-tree n)]
    (assoc inv-tree n (new-node (inc (:subtree-h node-to-update)) (:parent node-to-update) (:children node-to-update)))))

(defn get-parent-from [node]
  "return parent for the node given"
  (get @invitations-tree (:parent node)))

(defn same-height [n1 n2]
  "return true if the 2 nodes have the same height"
  (if (= (:subtree-h n1) (:subtree-h n2))
    true
    false))

(defn update-height [n]
  "function that updates the h of a sub-tree on the invitations-tree tree"
  (dosync
    (loop [n n]
      (when (not= n -1)
        (alter invitations-tree inc-node-height n)
        (let [curr-node (get @invitations-tree n)]
          (if (same-height curr-node (get-parent-from curr-node))
            (recur (:parent curr-node))))))))

(defn insert-invitation [inviter invited]
  "insert new invitation on tree. uses refs for thread-safety"
  (dosync
    (if (not (already-invited invited))
      (do
        (if (empty? @invitations-tree)
          (do
            (ref-set invitations-tree (assoc @invitations-tree inviter (new-node 0 -1 [invited])))
            (alter invitations-tree assoc invited (new-node -1 inviter []))
            (update-height invited))
          (do
            (if (already-invited inviter)
              (do
                (alter invitations-tree append-children inviter invited)
                (alter invitations-tree assoc invited (new-node -1 inviter []))
                (update-height invited))))))
      (do
        (update-height inviter)))))

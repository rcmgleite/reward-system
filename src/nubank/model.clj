(ns nubank.model
  (:require [clojure.math.numeric-tower :as math]))
; -------------------------------------
;           FUNCTIONAL MODEL
; -------------------------------------

; (1/2)^k where k
(defn score-factor [h]
  (let [hs (take h (range))]
     (reduce + (map (fn [curr-h]
                 (math/expt 0.5 curr-h)) hs))))

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

; returns the subtree-h for a given node
(defn height-from-node [n]
  (:subtree-h (get @invitations n)))

; returns the score for a given node
(defn score-for-node [n]
  (let [children (:children (get @invitations n))]
    (reduce + (map (fn [curr-child]
                     (score-factor (height-from-node curr-child)))children))))

; Calculate score for all nodes on tree
; it acts like a cache so that a request to score doesn't have to calculate it every time.
(defn calc-score []
  (reset! score-result (into {} (for [[k v] @invitations] [k (score-for-node k)]))))

; Verify if node n was already invited
(defn already-invited [n]
  (if (= (get @invitations n) nil)
    false
    true))

; insert root node on tree
(defn insert-root [inviter invited]
  (dosync (ref-set invitations (assoc @invitations inviter (new-node 0 -1 [invited])))))

; return new map to be used on ref alter functions like alter or swap for invitatoins atom
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
    (do
      (if (not= (get @invitations inviter) nil)
        (update-inviter-children inviter invited)))))

; return new map to be used on ref alter functions like alter or swap for invitatoins atom
(defn inc-node-height [inv-tree n]
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
          (alter invitations inc-node-height n)
          (let [curr-node (get @invitations n)]
            (if (same-height curr-node (get-parent-from curr-node))
              (recur (:parent curr-node)))))))))


; Insert an invited
(defn insert-invited [inviter invited]
  (dosync
    (cond
      (and (already-invited invited) (= (height-from-node inviter) 0))
        (update-height inviter)
        :else (do
                (alter invitations assoc invited (new-node -1 inviter []))
                (update-height invited)))))

(defn insert-invitation [ inviter invited]
  (dosync
    (insert-inviter inviter invited)
    (insert-invited inviter invited)))


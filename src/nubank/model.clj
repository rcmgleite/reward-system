(ns nubank.model
  (:require [clojure.math.numeric-tower :as math]))
; -------------------------------------
;           FUNCTIONAL MODEL
; -------------------------------------
; node struct representation
(defstruct node :subtree-h :parent :children)

(defn new-node [h parent children]
  "function that returns a new node"
  (struct node h parent children))

(defn score-factor [h]
  "(1/2)^k"
  (let [hs (take h (range))]
     (reduce + (map (fn [curr-h]
                 (math/expt 0.5 curr-h)) hs))))

(defn get-parent-from [tree node]
  "return parent for the node given"
  (get tree (:parent node)))

(defn height-from-node [tree n]
  "returns the subtree-h for a given node"
  (:subtree-h (get tree n)))

(defn score-for-node [tree n]
  "returns the score for a given node"
  (let [children (:children (get tree n))]
    (reduce + (map (fn [curr-child]
                     (score-factor (height-from-node tree curr-child)))children))))

(defn already-invited [tree n]
  "Verify if node n was already invited"
  (if (= (get tree n) nil)
    false
    true))

(defn append-children [inv-tree inviter invited]
  "return new map to be used on ref alter functions like alter or swap for invitatoins atom"
  (let [node-to-update (get inv-tree inviter)]
    (assoc-in inv-tree [inviter :children] (conj (:children node-to-update) invited))))

(defn inc-node-height [inv-tree n]
  "return new map to be used on ref alter functions like alter or swap for invitatoins atom"
  (let [node-to-update (get inv-tree n)]
    (assoc-in inv-tree [n :subtree-h] (inc (:subtree-h node-to-update)))))

(defn update-height [tree n]
  "function that updates the h of a sub-tree on the invites tree."
  (loop [tree tree n n]
    (if (not= n -1)
      (do
        (let [curr-node (get tree n)]
          (if (= (inc (:subtree-h curr-node)) (:subtree-h (get-parent-from tree curr-node)))
            (recur (inc-node-height tree n) (:parent curr-node))
            (inc-node-height tree n))))
      tree)))

(defn insert-root-node [tree inviter invited]
  "Insert root node on the given tree."
  (assoc tree inviter (new-node 0 -1 [invited])))

(defn insert-invited-node [tree inviter invited]
  "Insert invited node on the given tree tree."
  (assoc tree invited (new-node -1 inviter [])))

; FIXME - smells... Maybe cond?
(defn insert-new-invite [tree inviter invited]
  "Insert invitation on tree. Must be called inside transaction"
  (if (empty? tree)
    (update-height (insert-invited-node (insert-root-node tree inviter invited) inviter invited) invited)
    (update-height (insert-invited-node (append-children tree inviter invited) inviter invited) invited)))

; -------------------------------------
;            MUTABLE MODEL
; -------------------------------------
; global variable that holds all invites
(def invites (atom {}))

; global variable that holds the calculated score for the invites given
(def score-result (atom {}))

; it acts like a cache so that a request to score doesn't have to calculate it every time.
(defn calc-score []
  "Calculate score for all nodes on tree"
  (reset! score-result (into {} (for [[k v] @invites] [k (score-for-node @invites k)]))))

(defn update-invites [updated-tree]
  "update atom variable using alter with merge as fn"
  (swap! invites merge updated-tree))

(defn insert-invite [inviter invited]
  "Insert new invitation to global invites"
  (if (already-invited @invites invited)
    (do
      (when (= (height-from-node @invites inviter) 0) (update-invites (update-height @invites inviter))))
    (update-invites (insert-new-invite @invites inviter invited))))

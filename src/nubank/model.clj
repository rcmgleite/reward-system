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

; Calculate score for all nodes on tree
(defn calc-score []
  (reset! score-result (into {} (for [[k v] @invitations] [k (score-for-node @invitations k)]))))

; Verify if node n was already invited
(defn already-invited [n]
  (if (= (get @invitations n) nil)
    false
    true))

; insert root node on tree
(defn insert-root [inviter invited]
  (swap! invitations assoc inviter (new-node -1 -1 [invited])))

; return new map to be used on swap! for invitatoins atom
(defn append-children [inv-tree inviter invited]
  (let [node-to-update (get inv-tree inviter)]
  (assoc inv-tree inviter (new-node (:subtree-h node-to-update) (:parent node-to-update) (conj (:children node-to-update) invited)))))

; add the newest invited to the inviter children vec. Executes only if
; invited was not previously invited.
(defn update-inviter-children [inviter invited]
  (if (not (already-invited invited))
    (swap! invitations append-children inviter invited)))

; Insert an inviter
(defn insert-inviter [inviter invited]
  (if (empty? @invitations)
    (insert-root inviter invited)
    (update-inviter-children inviter invited)))

; function that updates the h of a sub-tree on the invitations tree
(defn update-height [n]
  ; TODO
  )

; Insert an invited
(defn insert-invited [inviter invited]
  (if (not (already-invited invited))
    (do
      (println invited)
      (swap! invitations assoc invited (new-node -1 inviter []))
      (update-height invited))
    (do
      (if (= (heigth-from-node @invitations inviter) 0)
        (update-height inviter)))))

(defn insert-invitation [ inviter invited]
  (insert-inviter inviter invited)
  (insert-invited inviter invited))


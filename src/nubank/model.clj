(ns nubank.model)

;; node struct
(defstruct node :subtree-h :parent :children)

;; global variable that holds all invitations
(def invitations (atom {}))

;; function to insert an invitation
(defn insert-invitation [inviter invited]
  (swap! invitations assoc inviter (struct node 0 0 invited)))

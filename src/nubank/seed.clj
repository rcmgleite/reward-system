(ns nubank.seed (:require [nubank.model :as model]
                          [nubank.utils :as utils]
                          [clojure.string :as str]))

(defn insert-invite [line]
  "Function that reads a line like '1 2' and insert the given invite on model/invites atom"
  (let [lst (map read-string (str/split line #" "))]
    (apply model/insert-invite lst)))

(defn seed []
  "Function used to seed model/invites at server startup"
  (println "[INFO] Seeding invites")
  (utils/foreach-line-in-file "input.txt" insert-invite)
  (println "[INFO] Done!"))
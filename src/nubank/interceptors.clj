(ns nubank.interceptors
  (:require [io.pedestal.interceptor :refer [interceptor]]
            [io.pedestal.interceptor.helpers :as interceptor]))

(def insert-invite-validator
  "Function that validates the input from insert-invite request"
  (interceptor/on-request
    (fn [context]
      (let [json-params (get context :json-params)]
        (if (and (number? (get json-params :inviter)) (number? (get json-params :invited)))
          (assoc context :valid-input true)
          (assoc context :valid-input false))))))


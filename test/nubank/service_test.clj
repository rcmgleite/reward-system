(ns nubank.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [nubank.service :as service]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(deftest get-score
  (is (=
       (:body (response-for service :get "/api/score"))
       "{\"1\":2.5,\"2\":0.0,\"3\":1.0,\"4\":0.0,\"5\":0.0,\"6\":0.0}")))

(deftest insert-invitation
  (is (.contains
       (:body (response-for service :post "/api/invitations"))
       "TODO")))


(ns nubank.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [nubank.service :as service]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(deftest get-score-endpoint-test
  (is (=
       (:body (response-for service :get "/api/score"))
       "{\"1\":2.875,\"2\":0,\"3\":1.75,\"4\":3.5,\"5\":1,\"6\":0,\"7\":0,\"8\":0,\"9\":0,\"10\":0,\"11\":0}")))

(deftest insert-invite-endpoint-test
  (is (.contains
       (:body (response-for service :post "/api/invite"))
       "{}")))


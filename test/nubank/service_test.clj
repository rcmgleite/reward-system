(ns nubank.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [nubank.service :as service]
            [nubank.model :as model]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(deftest endpoint-insert-correct-invite-test
  (println "[INFO] Running test: endpoint-insert-correct-invite-test")
  (dosync (ref-set model/invites {}))
  (let [resp (response-for service :post "/api/invite" :headers {"Content-Type" "application/json"} :body "{\"inviter\":190, \"invited\":200}")]
    (is (= (:status resp) 200))
    (is (.contains (:body resp) service/success-msg))))

(deftest endpoint-insert-invite-with-inviter-as-string-test
  (println "[INFO] Running test: endpoint-insert-invite-with-inviter-as-string-test")
  (dosync (ref-set model/invites {}))
  (let [resp (response-for service :post "/api/invite" :headers {"Content-Type" "application/json"} :body "{\"inviter\":\"190\", \"invited\":200}")]
    (is (= (:status resp) 400))
    (is (.contains (:body resp) service/invalid-args-error-msg))))

(deftest endpoint-insert-invite-with-bizarre-input-test
  (println "[INFO] Running test: endpoint-insert-invite-with-bizarre-input-test")
  (dosync (ref-set model/invites {}))
  (let [resp (response-for service :post "/api/invite" :headers {"Content-Type" "application/json"} :body "{\"inviter\":\"abcd%.9\", \"invited\":\"!@#$%\"}")]
    (is (= (:status resp) 400))
    (is (.contains (:body resp) service/invalid-args-error-msg))))

(deftest endpoint-insert-invite-no-post-body-test
  (println "[INFO] Running test: endpoint-insert-invite-no-post-body-test")
  (dosync (ref-set model/invites {}))
  (let [resp (response-for service :post "/api/invite")]
    (is (= (:status resp) 400))
    (is (.contains (:body resp) service/invalid-args-error-msg))))

(deftest endpoint-insert-invite-inviter-not-invited-test
  (println "[INFO] Running test: endpoint-insert-invite-inviter-not-invited-test")
  (dosync (ref-set model/invites {}))
  (response-for service :post "/api/invite" :headers {"Content-Type" "application/json"} :body "{\"inviter\": 1, \"invited\": 2}")
  (let [resp (response-for service :post "/api/invite" :headers {"Content-Type" "application/json"} :body "{\"inviter\": 3, \"invited\": 4}")]
    (is (= (:status resp) 400))
    (is (.contains (:body resp) service/inviter-not-invited-msg))))


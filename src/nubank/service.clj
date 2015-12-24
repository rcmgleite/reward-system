(ns nubank.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [clojure.data.json :as json]
            [ring.util.response :as ring-resp]
            [nubank.model :as model]
            [nubank.interceptors :as interceptors]))

(def success-msg "invite inserted successfully")
(def error-msg "Inviter and invited must be integers")

; Score endpoint
(defn score-handler [request]
  (ring-resp/response (json/write-str (into (sorted-map) (model/calc-score)))))

; New invitation endpoint
(defn invitation-handler [request]
  (let [inviter (get-in request [:json-params :inviter]) invited (get-in request [:json-params :invited])]
    (if (get request :valid-input)
      (do
        (model/insert-invite inviter invited)
        (ring-resp/response (json/write-str {:message success-msg})))
      {:status 400 :headers {"Content-Type" "application/json"} :body (json/write-str {:message error-msg})})))

(defroutes routes
    [[["/api/score" {:get score-handler}]
      ["/api/invite" {:post invitation-handler}
       ^:interceptors [(body-params/body-params) interceptors/insert-invite-validator]]]])
; interceptors/insert-invite-validator
(def service {:env :prod
                            ::bootstrap/routes routes
                            ::bootstrap/resource-path "/public"
                            ::bootstrap/type :jetty
                            ::bootstrap/port 8080})

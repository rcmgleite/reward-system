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
(def invalid-args-error-msg "Inviter and invited must be integers")
(def inviter-not-invited-msg "Inviter not previously invited")

(defn err-response [msg]
  (ring-resp/status (ring-resp/header (ring-resp/response (json/write-str {:message msg})) "Content-Type" "application/json") 400))

(defn succeess-response [msg]
  (ring-resp/header (ring-resp/response (json/write-str {:message msg})) "Content-Type" "application/json"))

; Score endpoint
(defn score-handler [request]
  (ring-resp/header (ring-resp/response (json/write-str (model/calc-score))) "Content-Type" "application/json"))

(defn do-invite [inviter invited]
  (model/insert-invite-async inviter invited)
  (succeess-response success-msg))

; New invitation endpoint
(defn invitation-handler [request]
  (let [inviter (get-in request [:json-params :inviter]) invited (get-in request [:json-params :invited])]
    (if (get request :valid-input)
      (do
        (cond
          (or (empty? @model/invites) (model/already-invited @model/invites inviter)) (do-invite inviter invited)
          :else (err-response inviter-not-invited-msg)))
      (err-response invalid-args-error-msg))))

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

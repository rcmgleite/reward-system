(ns nubank.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [clojure.data.json :as json]
            [ring.util.response :as ring-resp]
            [nubank.model :as model]))

; Score endpoint
(defn score-handler [request]
  (ring-resp/response (json/write-str (into (sorted-map) (model/calc-score)))))

; New invitation endpoint
(defn invitation-handler [request]
  (let [inviter (get-in request [:params :inviter]) invited (get-in request [:params :invited])]
    (model/insert-invite inviter invited)
    (ring-resp/response (json/write-str {}))))

(defroutes routes
    [[["/api/score" {:get score-handler}] ["/api/invitations" {:post invitation-handler}]]])

(def service {:env :prod
                            ::bootstrap/routes routes
                            ::bootstrap/resource-path "/public"
                            ::bootstrap/type :jetty
                            ::bootstrap/port 8080})

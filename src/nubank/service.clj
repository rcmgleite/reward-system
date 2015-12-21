(ns nubank.service
      (:require [io.pedestal.http :as bootstrap]
                              [io.pedestal.http.route :as route]
                              [io.pedestal.http.body-params :as body-params]
                              [io.pedestal.http.route.definition :refer [defroutes]]
                              [ring.util.response :as ring-resp]))

(defn score-handler [request]
  (ring-resp/response "TODO"))

(defn invitation-handler [request]
  (ring-resp/response "TODO"))

(defroutes routes
    [[["/api/score" {:get score-handler}] ["/api/invitations" {:post invitation-handler}]]])

;; Consumed by helloworld.server/create-server
(def service {:env :prod
                            ::bootstrap/routes routes
                            ::bootstrap/resource-path "/public"
                            ::bootstrap/type :jetty
                            ::bootstrap/port 8080})

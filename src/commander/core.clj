(ns commander.core
  (:require [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.adapter.jetty :as jetty]))

(def router
  (ring/router
   [["/api"
     ["/ping"
      {:get (constantly {:status 200 :body "pong"})}]]
    ["/swagger.json"
     {:get {:no-doc  true
            :handler (swagger/create-swagger-handler)}}]]
   {:data
    {:muuntaja   m/instance
     :middleware [swagger/swagger-feature
                  muuntaja/format-negotiate-middleware
                  muuntaja/format-response-middleware
                  muuntaja/format-request-middleware]}}))

(def default-handler
  (ring/routes
   (ring/redirect-trailing-slash-handler)
   (swagger-ui/create-swagger-ui-handler {:path "/api-docs"})
   (ring/create-resource-handler)
   (ring/create-default-handler)))

(def app
  (ring/ring-handler
   router
   default-handler))

(defn start
  []
  (jetty/run-jetty #'app {:port 3000 :join? false})
  (println "server running in port 3000"))

(comment

  (start)

  (app {:request-method :get :uri "/swagger.json"})

  (app {:request-method :get :uri "/404"})

  (app {:request-method :get :uri "/css/hi.css"})

  (require '[reitit.core :as r])

  (r/match-by-path router "/ping"))

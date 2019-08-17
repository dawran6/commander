(ns commander.core
  (:require [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]))

(def router
  (ring/router
   ["/ping" handler]))

(def default-handler
  (ring/routes
   (ring/redirect-trailing-slash-handler)
   (ring/create-resource-handler)
   (ring/create-default-handler)))

(def app
  (ring/ring-handler
   router
   default-handler))

(app {:request-method :get :uri "/ping/"})

(app {:request-method :get :uri "/404"})

(app {:request-method :get :uri "/css/hi.css"})

(require '[reitit.core :as r])

(r/match-by-path router "/ping")

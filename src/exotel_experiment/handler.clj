(ns exotel-experiment.handler
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

(declare callee-number)

(defn- passthru [digits]
  (def callee-number digits)
  (println "Calling number:" callee-number)
  {:status 200
   :headers {"Content-Type" "text/plain"}})

(defn- connect [request]
  (pprint request)
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (string/replace callee-number #"%22" "")})

(defroutes app-routes
  (GET "/passthru" [digits]
       (passthru digits))

  (GET "/connect" request
       (connect request))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

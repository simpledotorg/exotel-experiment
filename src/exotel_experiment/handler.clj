(ns exotel-experiment.handler
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

(defonce sessions
  (atom {}))

(defn- passthru [From digits]
  (swap! sessions
         assoc From digits)
  {:status 200
   :headers {"Content-Type" "text/plain"}})

(defn- connect [From]
  (let [digits (-> (get @sessions From)
                   (string/replace #"%22" ""))]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body digits}))

(defroutes app-routes
  (GET "/passthru" [From digits]
       (passthru digits))

  (GET "/connect" [From]
       (connect From))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

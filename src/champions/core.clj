(ns champions.core
  (:gen-class)
  (:require [champions.config :as config]
            [champions.api-sports :as api]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [org.httpkit.server :refer [run-server]]
            [org.httpkit.sni-client :as sni-client]))

;; Change default client for your whole application:
(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

(defn not-found [_]
  {:status 404
   :body "Not Found"})

(defn predict [_]
  {:status 200
   :body (json/write-str (api/get-standings))})

(defn app [req]
  (let [uri (:uri req)
        handler (condp = uri
                  "/predict" predict
                  not-found)]
    (handler req)))

(defn -main [& _]
    (run-server #'app {:port config/port}))



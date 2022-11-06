(ns champions.core
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [org.httpkit.server :refer [run-server]]
            [org.httpkit.sni-client :as sni-client]))

;; Change default client for your whole application:
(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

(defonce server (atom nil))

(defn not-found [_]
  {:status 404
   :body "Not Found"})

(defn meaning [_]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body "{\"answer\": 42}"})

(defn app [req]
  (let [uri (:uri req)
        handler (condp = uri
                  "/ali" meaning
                  not-found)]
    (handler req)))

(defn stop-server []
  (when (some? @server)
    (@server :timeout 100)
    (reset! server nil)))

(def port (Integer/parseInt
           (or (System/getenv "PORT") "8000")))

(defn -main [& args]
    (reset! server (run-server #'app {:port port})))



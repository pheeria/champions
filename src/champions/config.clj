(ns champions.config)

(def port (Integer/parseInt
           (or (System/getenv "PORT") "8000")))

(def api-key (System/getenv "API_KEY"))

(ns champions.api-sports
  (:require [champions.config :as config]
            [champions.utils :as utils]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]))

(def url "https://v3.football.api-sports.io/standings?league=39&season=2022")

(def options {:headers {"x-apisports-key" config/api-key}
              :timeout 1000
              :keepalive 30000})

(def response (json/read-str (slurp "test.json")
               :key-fn
               keyword))

(defn response->teams [response]
  (first
    (:standings
      (:league
        (first
          (:response response))))))

(defn parse-team [team]
  {:rank (:rank team)
   :points (:points team)
   :games (:played (:all team))
   :logo (:logo (:team team))
   :name (:name (:team team))})


(defn min-points-to-win [teams]
  (:points
    (first teams)))


(let [teams (map parse-team (response->teams response))
      leader-points (min-points-to-win teams)]
  (filter (fn [team]
            (let [remaining-games (- 19 (:games team))
                  remaining-points (* 3 remaining-games)
                  max-possible-points (+ (:points team) remaining-points)]
            (< max-possible-points leader-points)))
          teams))

 (min-points-to-win (map parse-team (response->teams response)))

(defn get-standings []
  (map parse-team 
       (response->teams @(http/get url options utils/str->json))))


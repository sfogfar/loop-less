(ns free-retro-two.firebase.firebase
  (:require ["firebase/app" :as firebase]
            ["firebase/database" :as rtdb]
            [free-retro-two.firebase.firebase-config :refer [firebase-config]]))

(defonce firebase-app
  (firebase/initializeApp (clj->js firebase-config)))

(defonce real-time-db
  (rtdb/getDatabase firebase-app))

(defn push-data! [path data]
  (let [ref (rtdb/ref real-time-db path)]
    (-> (rtdb/push ref (clj->js data))
        (.then (fn [new-ref]
                 (println "Data pushed with key:" (.-key new-ref))
                 new-ref))
        (.catch (fn [error]
                  (println "Error pushing data:" error))))))

(push-data! "retros" {:name "no more vinci loop" :template "msg" :kudos true})

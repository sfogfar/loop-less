(ns loop-less.firebase.firebase
  (:require ["firebase/app" :as firebase]
            ["firebase/database" :as rtdb]
            [loop-less.firebase.firebase-config :refer [firebase-config]]))

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

(defn fetch-retro [retro-id]
  (let [ref (rtdb/ref real-time-db (str "retros/" retro-id))]
    (-> (rtdb/get ref)
        (.then (fn [snapshot]
                 (if (.exists snapshot)
                   (let [data (js->clj (.val snapshot) :keywordize-keys true)]
                     (println "Fetched retro data:" data)
                     data)
                   (throw (js/Error. (str "Retro not found: " retro-id))))))
        (.catch (fn [error]
                  (println "Error fetching retro:" error)
                  (throw error))))))

(defn update-retro [retro-id data]
  (let [ref (rtdb/ref real-time-db (str "retros/" retro-id))]
    (-> (rtdb/update ref (clj->js data))
        (.then #(println "Retro updated successfully"))
        (.catch #(println "Error updating retro:" %)))))

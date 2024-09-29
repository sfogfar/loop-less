(ns free-retro-two.frontend.views.add-tickets
  (:require [reagent.core :as r]))

(defn add-ticket-page[match]
  (let [retro-id (get-in match [:parameters :path :id])]
    [:div.container
     [:h1.title.is-1 (str "Welcome to Retro " retro-id)]]))

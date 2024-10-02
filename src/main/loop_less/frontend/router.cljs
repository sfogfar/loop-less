(ns loop-less.frontend.router
  (:require [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]
            [reagent.core :as r]
            [loop-less.frontend.views.create-retro :as create-retro]
            [loop-less.frontend.views.add-tickets :as add-tickets]))

(def routes
  [["/"
    {:name :home
     :view #'create-retro/create-retro-page}]
   ["/retro/:id"
    {:name :retro
     :view #'add-tickets/add-tickets-page
     :parameters {:path {:id string?}}}]])

(defonce match (r/atom nil))

(defn init-routes! []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [m] (reset! match m))
   {:use-fragment false}))

(defn current-page []
  (when @match
    (let [view (get-in @match [:data :view])]
      [view @match])))

(ns free-retro-two.frontend.app
  (:require [reagent.dom :as rdom]
            [free-retro-two.frontend.router :as router]))

(defn app []
  [:div
   [router/current-page]])

(defn mount-root []
  (router/init-routes!)
  (rdom/render [app] (.getElementById js/document "app")))

(defn init []
  (mount-root))

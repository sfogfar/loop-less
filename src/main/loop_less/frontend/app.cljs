(ns loop-less.frontend.app
  (:require [reagent.dom :as rdom]
            [loop-less.frontend.router :as router]))

(defn app []
  [:div
   [router/current-page]])

(defn mount-root []
  (router/init-routes!)
  (rdom/render [app] (.getElementById js/document "app")))

(defn init []
  (mount-root))

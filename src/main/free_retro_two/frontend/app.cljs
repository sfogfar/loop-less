(ns free-retro-two.frontend.app
  (:require [reagent.dom :as rd]
            [free-retro-two.frontend.views.create-retro :refer [create-retro-page]]))

(defn init []
  (rd/render [create-retro-page] (js/document.getElementById "app")))

(ns free-retro-two.frontend.views.create-retro
  (:require [reagent.core :as r]
            [free-retro-two.firebase.firebase :as firebase]
            [free-retro-two.specs.retro :refer [valid-retro?]]))

(def retro-templates
  [{:id :mad-sad-glad :name "Mad, Sad, Glad" :icon "😃😢😠"}
   {:id :start-stop-continue :name "Start, Stop, Continue" :icon "🟢🔴🔄"}])

(defn retro-name-input [value]
  [:div.field
   [:label.label "Retro Name"]
   [:div.control
    [:input.input {:type "text"
                   :placeholder "Enter retro name"
                   :value @value
                   :on-change #(reset! value (-> % .-target .-value))}]]])

(defn template-selector [selected]
  [:div.field
   [:label.label "Choose a template"]
   [:div.columns.is-multiline
    (for [{:keys [id name icon]} retro-templates]
      ^{:key id}
      [:div.column.is-half
       [:div.box {:class (when (= @selected id) "has-background-primary-light")
                  :on-click #(do
                               (reset! selected id)
                               (js/console.log "clicked template" id)
                               (js/console.log "selected" @selected))}
        [:p.is-size-5.has-text-weight-bold name]
        [:p.is-size-3 icon]]])]])

(defn kudos-toggle [include-kudos]
  [:div.field
   [:div.control
    [:label.checkbox
     [:input {:type "checkbox"
              :checked @include-kudos
              :on-change #(swap! include-kudos not)}]
     " Include Kudos column"]]])

(defn create-retro [retro-data]
  (if (valid-retro? retro-data)
  (firebase/push-data! "retros" retro-data)
  (js/console.error "Invalid data")))

(defn create-retro-page []
  (let [retro-name (r/atom "")
        selected-template (r/atom :mad-sad-glad)
        include-kudos (r/atom false)]
    (js/console.log selected-template)
    (fn []
      (add-watch selected-template :debug
                 (fn [key atom old-state new-state]
                   (println "selected-template changed from" old-state "to" new-state)))
      [:section.section
       [:div.container
        [:h1.title.has-text-centered "Create a new retro"]
        [:div.box
         [retro-name-input retro-name]
         [template-selector selected-template]
         [kudos-toggle include-kudos]
         [:div.field.is-grouped.is-grouped-centered
          [:div.control
           [:button.button.is-primary.is-large
            {:on-click #(create-retro {:retro-name @retro-name
                                       :selected-template @selected-template
                                       :include-kudos @include-kudos})}
            "Create Retro"]]]]]])))

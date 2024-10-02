(ns loop-less.frontend.views.add-tickets
  (:require [reagent.core :as r]
            [loop-less.firebase.firebase :as firebase]))

(def template-columns
  {"mad-sad-glad" ["Mad" "Sad" "Glad"]
   "start-stop-continue" ["Start" "Stop" "Continue"]})

(defn add-ticket [retro-id column-name new-ticket retro-data]
  (let [tickets (get @retro-data column-name)
        new-ticket-with-id {:id (str (random-uuid))
                            :text new-ticket}
        updated-tickets (conj tickets new-ticket-with-id)
        updated-data (assoc @retro-data column-name updated-tickets)]
    (-> (firebase/update-retro retro-id {column-name updated-tickets})
        (.then #(reset! retro-data updated-data))
        (.catch #(js/console.error "Error adding ticket:" %)))))

(defn ticket-form [retro-id column-name retro-data]
  (let [new-ticket (r/atom "")]
    (fn []
      [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (when (not-empty @new-ticket)
                             (add-ticket retro-id column-name @new-ticket retro-data)
                             (reset! new-ticket "")))}
       [:div.field.has-addons
        [:div.control
         [:input.input {:type "text"
                        :value @new-ticket
                        :placeholder "Add a ticket"
                        :on-change #(reset! new-ticket (.. % -target -value))}]]
        [:div.control
         [:button.button.is-primary {:type "submit"} "Add"]]]])))

(defn retro-column [retro-id column-name items retro-data]
  [:div.column
   [:h2.title.is-4 column-name]
   [:div.content
    (for [{:keys [id text]} items]
      ^{:key id} [:div.box [:p text]])]
   [ticket-form retro-id column-name retro-data]])

(defn add-tickets-page [{:keys [path-params]}]
  (let [retro-data (r/atom nil)
        loading? (r/atom true)
        error (r/atom nil)]
    (fn []
      (let [retro-id (:id path-params)]
        (when (and @loading? (not @retro-data))
          (reset! loading? true)
          (-> (firebase/fetch-retro retro-id)
              (.then (fn [data]
                       (reset! retro-data data)
                       (reset! loading? false)))
              (.catch (fn [err]
                        (reset! error (.-message err))
                        (reset! loading? false)))))
        
        (cond
          @loading?
          [:div.container [:h1.title "Loading..."]]
          
          @error
          [:div.container [:h1.title.has-text-danger @error]]
          
          @retro-data
          (let [{:keys [name selected-template include-kudos]} @retro-data
                columns (template-columns selected-template)
                columns (if include-kudos (conj columns "Kudos") columns)]
            [:div.container
             [:h1.title name]
             [:div.columns
              (for [column columns]
                ^{:key column}
                [retro-column column])]])
          
          :else
          [:div.container [:h1.title "Something went wrong"]])))))

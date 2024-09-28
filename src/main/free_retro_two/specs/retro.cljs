(ns free-retro-two.specs.retro
  (:require [clojure.spec.alpha :as s]))

(s/def ::retro-name (s/and string? not-empty))
(s/def ::selected-template keyword?)
(s/def ::include-kudos boolean?)

(s/def ::retro (s/keys :req-un [::retro-name ::selected-template ::include-kudos]))

;; Function to validate retro data
(defn valid-retro? [retro-data]
  (s/valid? ::retro retro-data))

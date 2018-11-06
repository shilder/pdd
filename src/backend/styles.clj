(ns backend.styles
  (:require [garden.core :refer [css]]
            [garden.color :as color]
            [clojure.string :as str]))

(defonce more-styles (atom {}))

(defn register-style! [id value]
  (swap! more-styles assoc id value))

(def font-family
  (str/join ","
            (map
              pr-str
              ["-apple-system", "BlinkMacSystemFont", "Segoe UI", "Roboto",
               "Helvetica Neue", "Arial", "sans-serif", "Apple Color Emoji",
               "Segoe UI Emoji", "Segoe UI Symbol"])))

(def primary-color "#41bbdb")
(def primary-color-dark (color/darken primary-color 10))
(def secondary-color "#f8f9fa")
(def secondary-color-dark (color/darken secondary-color 10))

(def btn-base
  {:display         :flex
   :justify-content :center
   :align-items     :center
   :font-weight     400
   :border          :none
   :user-select     :none
   :border-radius   "0.25em"
   :padding         "0.4em 0.75em"
   :transition      "all .2s"})

(defn styles []
  (apply css
         [:html
          {:line-height 1.15}]
         ;; parts of normalize.css
         [:body
          {:margin 0}]
         [:pre
          {:font-family "monospace, monospace"
           :font-size   "1em"
           :margin      0}]
         [:button :input :select :textarea
          {:font-family :inherit
           :font-size   "100%"
           :line-height 1.15
           :margin      0}]

         [:*
          {:box-sizing :border-box}]

         ;; customizations
         [:body
          {:font-family font-family
           :font-size   "16px"
           :height      "100%"
           :width       "100%"}]

         [:input
          {:display       :block
           :width         "100%"
           :height        "calc(2.25em + 2px)"
           :padding       ".375em .75em"
           :font-size     "1rem"
           :line-height   1.5
           :border        "1px solid #ced4da"
           :border-radius ".25em"
           :transition    "border-color .15s ease-in-out, box-shadow .15s ease-in-out"}

          [:&:focus
           {:color            "#495057"
            :background-color "#fff"
            :border-color     "#80bdff"
            :outline          0
            :box-shadow       "0 0 0 0.2rem rgba (0, 123, 255, 0.25)"}]]

         [:button.btn-primary
          (merge btn-base
                 {:background primary-color})

          [:&:hover
           {:background primary-color-dark}]]

         [:button.btn-secondary
          (merge btn-base
                 {:background secondary-color})

          [:&:hover
           {:background secondary-color-dark}]]

         [:div.container
          {:display         :flex
           :position        :relative
           :height          "100%"
           :justify-content :center
           :align-items     :center}]

         (vals @more-styles)))

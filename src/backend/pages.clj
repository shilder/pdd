(ns backend.pages
  (:require [rum.core :as rum]
            [backend.styles :as css]
            [clojure.java.io :as io]))

(rum/defc main-template [body]
  [:html
   [:head
    [:meta {:name    "viewport"
            :content "width=device-width, initial-scale=1"}]
    [:title "Live coding chat app for java.ural.Meetup #2"]
    [:style
     {:type                    "text/css"
      :dangerouslySetInnerHTML {:__html (css/styles)}}]]
   [:body
    body]])

;; Login form

(def padding-margin "0.5em")

(css/register-style!
  ::login
  [:form.login-form
   {:display        :flex
    :flex-direction :column
    :align-items    :center
    :min-width      "100px"}

   [:*
    {:margin "0.2em"}]

   [:input :button
    {:width "100%"}]])

(rum/defc index []
  (main-template
    [:div.container
     [:form.login-form {:method "post" :action "/chat"}
      [:label {:for "login"} "Введите ваш ник"]
      [:input#login {:name      "login"
                     :type      "text"
                     :autofocus true}]
      [:button.btn-primary "Вход"]]]))

;; chat page

(css/register-style!
  ::chat
  [[:div.chat-splitter
    {:display        :flex
     :width          "100%"
     :flex-direction :row
     :flex-grow      1
     :align-items    :stretch
     :overflow       :auto}]

   [:div.chat-container
    {:display        :flex
     :width          "100%"
     :height         "100%"
     :flex-direction :column
     :align-items    :center}

    [:span
     {:padding-top "0.5em"}]

    [:form
     {:width          "100%"
      :margin-top     "0.2em"
      :padding-left   padding-margin
      :padding-right  padding-margin
      :display        :flex
      :align-items    :stretch
      :flex-direction :row}

     [:input
      {:width "1%"
       :flex  "1 1 auto"}]]

    (let [common {:border        "1px solid #ced4da"
                  :border-radius "2px"
                  :padding-left  padding-margin
                  :overflow      :auto}]

      [[:div.chat-area
        (merge common
               {:width       "70%"
                :margin-left padding-margin})]

       [:div.users-area
        (merge common
               {:width        "30%"
                :margin-right padding-margin
                :margin-left  "-1px"})

        [:ul
         {:padding    0
          :margin     0
          :list-style :none}

         [:li
          {:font-weight "600"}]]]])]])

(rum/defc chat-page [login]
  (main-template
    [:div.container
     [:input#login {:type  "hidden"
                    :value login}]
     [:script
      {:type                    "text/javascript"
       :dangerouslySetInnerHTML {:__html (slurp (io/resource "client.js"))}}]


     [:div.chat-container
      [:span "Hello, " [:strong login]]

      [:div.chat-splitter
       [:div.chat-area
        [:pre#messages]]
       [:div.users-area#users]]
      [:form {:on-submit "return false;"}
       [:input#message
        {:name      "message"
         :autofocus true}]
       [:button.btn-primary
        {:type     "submit"
         :on-click "sendMessage(); return false;"}
        "Send"]
       [:button.btn-secondary
        {:type     "button"
         :on-click "clearChat(); return false;"}
        "Clear"]]]]))

(defn render [component]
  {:status  200
   :headers {"Content-Type" "text/html;charset=UTF-8"}
   :body    (rum/render-static-markup component)})

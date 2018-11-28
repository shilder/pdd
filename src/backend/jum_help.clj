(ns backend.jum-help)

;; подсказки если вдруг забуду

(comment
  {:type :text
   :from (escape-html from)
   :text (escape-html text)}

  (defn text-message [from text]
    {:type :text
     :from (escape-html from)
     :text (escape-html text)})

  (defn send-message! [client message]
    (when-let [socket (:stream client)]
      (s/put! socket (json/generate-string message))))

  (defn broadcast-message! [message]
    (doseq [c (vals @clients)]
      (send-message! c message)))

  (rum/defc users-list [clients]
            [:ul
             (for [[k v] clients]
               [:li (:login v)])])

  (defn broadcast-clients! [clients]
    (let [s (rum/render-static-markup (users-list clients))]
      (broadcast-message! {:type  :users
                           :users s})))

  (add-watch clients
             :client-watcher
             (fn [k r o n]
               (broadcast-clients! n)))

  (defn handle-message [client message]
    (log/info "Message from" (:id client) (:login client) message)

    ;; broadcast message to everyone
    (broadcast-message! (text-message (:login client)
                                      (:text message)))))

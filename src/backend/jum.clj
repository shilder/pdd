(ns backend.jum
  (:require [aleph.http :as http]
            [ds.logging :as log]
            [rum.server-render]
            [ring.middleware.params]
            [ring.middleware.keyword-params]
            [compojure.core :refer :all]
            [manifold.deferred :as d]
            [manifold.stream :as s]
            [cheshire.core :as json]
            [clojure.pprint :as pprint]
            [clojure.tools.nrepl.server :as nrepl]
            [rum.core :as rum]

            [backend.pages :as pages]
            [backend.mystem :as mystem]))

; java.ural.Meetup live coding demo
(defonce clients (atom {}))
(defonce bans (atom #{}))

(declare handle-message)
(declare additional-check)
(declare escape-html)

(defn websocket-handler [request]
  (let [id (get-in request [:params :session])
        login (get-in request [:params :login])
        ip (or (get-in request [:headers "x-real-ip"])
               (get request :remote-addr))]

    (cond
      (contains? @bans ip)
      {:status 403
       :body   "You've been banned"}

      (additional-check login)
      {:status 403
       :body   "Sorry, not allowed"}

      (not (and id login))
      {:status 400
       :body   "Need session ID and login"}

      :else
      (-> (http/websocket-connection request)
          ;; handshake succeeded
          (d/chain'
            (fn [stream]
              (let [client {:id     id
                            :login  login
                            :ip     ip
                            :ua     (get-in request [:headers "user-agent"])
                            :stream stream}]

                (log/info "Connected client" id login ip)

                (s/on-closed stream
                             (fn []
                               (log/info "Disconnected client" id login)
                               (swap! clients dissoc id)))

                (s/consume #(handle-message client
                                            (json/parse-string-strict % true))
                           stream)

                (swap! clients assoc id client)
                "OK")))))))

;; не забыть про `escape-html`

;; TODO: реализовать отправку сообщений с сервера на клиент
;; TODO: реализовать рассылку сообщений для всех клиентов
;; TODO: реализовать функцию формирования списка пользователей
;; TODO: отправлять список пользователей клиенту
;; TODO: перенаправлять сообщения полученные от пользователей в общий чат
(defn handle-message [client message]
  (log/debug "Message from"
             (:id client) (:login client)
             (:type message) (escape-html (:text message))))

(defroutes app
  (GET "/" []
    (pages/render
      (pages/index)))

  ; redirect for invalid GET request
  (GET "/chat" []
    {:status  302
     :headers {"Location" "/"}})
  (POST "/chat" [login]
    (pages/render
      (pages/chat-page login)))

  ; websocket handler
  (GET "/ws" request
    (websocket-handler request)))

(defn pprint [o]
  (with-out-str
    (pprint/pprint o)))

(defn router [request]
  (log/debug (pprint request))
  (app request))

(defonce server nil)

(defn start-server []
  (alter-var-root
    #'server
    (fn [o]
      (when o
        (.close server))

      (http/start-server (-> #'router
                             (ring.middleware.keyword-params/wrap-keyword-params)
                             (ring.middleware.params/wrap-params))
                         {:port 8080}))))

(defn stop-server []
  (when server
    (.close server)))
































(defn disconnect-client! [id]
  (when-let [stream (get-in @clients [id :stream])]
    (s/close! stream)))

(defn ban! [id]
  (when-let [client (get @clients id)]
    (let [ip (:ip client)
          stream (:stream client)]
      ; disconnect client
      (s/close! stream)
      ; add ip to ban set
      (swap! bans conj ip))))

(defonce ms (mystem/start-mystem))

(defn additional-check [login]
  (seq (mystem/search-obscene ms login)))

(defn escape-html [text]
  (rum.server-render/escape-html
    (mystem/filter-obscene ms text)))

(defonce nrepl
         (let [s (nrepl/start-server :port 40760)]
           (log/info "Started nREPL server on port 40760")
           s))

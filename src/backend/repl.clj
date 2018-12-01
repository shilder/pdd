(ns backend.repl
  (:require [clojure.java.io :as io]
            [aleph.http :as http]
            [byte-streams :as bs]
            [cheshire.core :as json]
            [ds.logging :as log]))

(comment
  (log/set-pattern! "%highlight([%-5level]) %cyan([%logger{15}]) %msg%n")

  (with-open [r (io/reader "README.md" :encoding "UTF-8")
              w (io/writer "README1251.md" :encoding "cp1251")]
    (io/copy r w))

  (-> @(http/request
         {:request-method :get
          :url            "https://api.github.com/search/repositories"
          :headers        {"Accept"     "application/vnd.github.v3+json"
                           "User-Agent" "Awesome-Octocat-App"}
          :query-params   {"q"    "stars:>=100 fork:true language:clojure"
                           "sort" "stars"}})
      :body
      (io/reader)
      (json/parse-stream true)
      :items))

(defn whois-attr [attrs name]
  (->> (:attribute attrs)
       (filter #(= (:name %) name))
       (first)
       (:value)))

(defn- select-attrs [data]
  (let [attrs (:attributes (first (get-in data [:objects :object])))]
    {:netname (whois-attr attrs "netname")
     :description (whois-attr attrs "descr")
     :inetnum (whois-attr attrs "inetnum")}))

(defn ip-whois* [ip]
  (-> @(http/request
         {:request-method :get
          :url            "https://rest.db.ripe.net/search"
          :headers        {"Accept"     "application/json"
                           "User-Agent" "Awesome-Octocat-App"}
          :query-params   {"query-string" ip
                           "type-filter" "inetnum"}})
      :body
      (io/reader)
      (json/parse-stream true)
      (select-attrs)))

(def ip-whois (memoize ip-whois*))

(defn lispy-function []
  (let [a (repeatedly #(rand-int 10))
        b (repeatedly #(rand-int 20))]
    (take 10 (map vector
                  (filter odd? a)
                  (filter even? b)))))


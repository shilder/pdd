(ns backend.mystem
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]
            [clojure.string :as str]
            [clojure.set :as set])
  (:import (java.io BufferedReader BufferedWriter Reader)))

; Mystem wrapper for obscene words filtering
(defn start-mystem []
  (let [p (.exec (Runtime/getRuntime)
                 ^"[Ljava.lang.String;" (into-array String
                                                    ["./mystem"
                                                     "-i"
                                                     "--format"
                                                     "json"])
                 )]
    {:process p
     :stdin   (io/writer (.getOutputStream p))
     :stderr  (io/reader (.getErrorStream p))
     :stdout  (io/reader (.getInputStream p))}))

(defn stop-mystem [ms]
  (locking ms
    (.destroy (:process ms))))

(defn- send-string [ms ^String s]
  (let [w ^BufferedWriter (:stdin ms)]
    (doto w
      (.write s)
      (.write "\n")
      (.flush))))

(defn- read-string-ready [ms]
  (let [r ^BufferedReader (:stdout ms)]
    (when (.ready r)
      (json/parse-string-strict (.readLine r) true))))

(defn- purge-reader! [^Reader r]
  (while (.ready r)
    (.read r)))

(defn split [s m]
  (when s
    (str/split s m)))

(defn parse-gr [gr]
  (let [[p1 p2] (split gr #"=")]
    {:p1 (set (split p1 #","))
     :p2 (set (split p2 #","))}))

(defn analyze [ms ^String s to]
  (when (string? s)
    (when-not (.isAlive (:process ms))
      (throw (ex-info "MyStem process is dead - need to restart !" {})))

    (let [start (System/nanoTime)]
      ;; TODO: timeout for locking ?
      (locking ms
        (purge-reader! (:stdout ms))
        (send-string ms (.replaceAll s "\n" " "))

        (loop []
          (Thread/yield)
          (if-let [o (read-string-ready ms)]
            (map (fn [v]
                   (update v :analysis
                           (fn [a]
                             (mapv #(update % :gr parse-gr) a))))
                 o)
            ; no string ready - recur
            (let [elapsed (/ (- (System/nanoTime)
                                start)
                             1000000)]
              (if (> elapsed to)
                nil
                (recur)))))))))

(defn search-obscene [ms ^String s]
  (let [data (analyze ms s 100)]
    (into #{}
          (comp
            (filter
              (fn [d]
                (let [allp1 (apply set/union
                                   (map #(get-in % [:gr :p1]) (:analysis d)))]
                  (contains? allp1 "обсц"))))
            (map :text))
          data)))

;; little inefficient but ok
(defn filter-obscene [ms ^String s]
  (reduce
    (fn [a v]
      (str/replace a v "****"))
    s
    (search-obscene ms s)))

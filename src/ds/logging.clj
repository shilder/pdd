(ns ds.logging
  (:require [clojure.string :as str])
  (:import (org.slf4j LoggerFactory)
           (ch.qos.logback.classic.util ContextInitializer)
           (ch.qos.logback.classic LoggerContext Logger Level)
           (org.apache.commons.io.output WriterOutputStream)
           (java.nio.charset Charset)
           (ch.qos.logback.core ConsoleAppender)
           (java.io Writer)))

(set! *warn-on-reflection* true)

;; logging for Java
(defn- line [form]
  (-> form
      meta
      :line))

(defn format-message [line & args]
  (str/join " "
            (cons (str "[line:" line "]")
                  args)))

; ported from https://stackoverflow.com/a/9320198
(defn- reload-config! []
  (let [ctx ^LoggerContext (LoggerFactory/getILoggerFactory)
        ci (ContextInitializer. ctx)
        url (.findURLOfDefaultConfigurationFile ci true)]
    (.reset ctx)
    (.configureByResource ci url)))

(defmacro trace [& args]
  (let [ns (str *ns*)]
    `(let [logger# (LoggerFactory/getLogger ~ns)]
       (if (.isTraceEnabled logger#)
         (.trace logger# (format-message ~(line &form) ~@args))))))

(defmacro debug [& args]
  (let [ns (str *ns*)]
    `(let [logger# (LoggerFactory/getLogger ~ns)]
       (if (.isDebugEnabled logger#)
         (.debug logger# (format-message ~(line &form) ~@args))))))

(defmacro info [& args]
  (let [ns (str *ns*)]
    `(let [logger# (LoggerFactory/getLogger ~ns)]
       (if (.isInfoEnabled logger#)
         (.info logger# (format-message ~(line &form) ~@args))))))

(defmacro warn [& args]
  (let [ns (str *ns*)]
    `(let [logger# (LoggerFactory/getLogger ~ns)]
       (if (.isWarnEnabled logger#)
         (.warn logger# (format-message ~(line &form) ~@args))))))

(defmacro warn-ex [^Throwable ex & args]
  (let [ns (str *ns*)]
    `(let [logger# (LoggerFactory/getLogger ~ns)]
       (if (.isWarnEnabled logger#)
         (.warn logger# ^String (format-message ~(line &form) ~@args) ~ex)))))

(defn set-log-level! [^String name level]
  (let [logger ^Logger (LoggerFactory/getLogger name)
        old (.getLevel logger)]
    (.setLevel logger
               (case level
                 :trace Level/TRACE
                 :debug Level/DEBUG
                 :info Level/INFO))
    old))

(defn install-nrepl-handler! []
  (.setOutputStream
    ^ConsoleAppender
    (.getAppender ^Logger (LoggerFactory/getLogger "ROOT") "STDOUT")
    (WriterOutputStream.
      ^Writer *out*
      (.newDecoder (Charset/forName "UTF-8")))))

(defn set-pattern! [pattern]
  (let [encoder (.getEncoder (.getAppender ^Logger (LoggerFactory/getLogger "ROOT") "STDOUT"))
        old (.getPattern encoder)]
    (.setPattern encoder pattern)
    (.start encoder)
    old))
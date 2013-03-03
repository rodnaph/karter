
(ns karter.core
  (:use confo.core)
  (:require [karter.web :as web]
            [ring.adapter.jetty :as jetty]))

(def config (confo :karter
                   :port 3456))

(defn start []
  (jetty/run-jetty web/app config))

(defn -main []
  (start))


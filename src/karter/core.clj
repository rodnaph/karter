
(ns karter.core
  (:require [karter.web :as web]
            [ring.adapter.jetty :as jetty]))

(defn start []
  (jetty/run-jetty
    web/app
    {:port 3456}))

(defn -main []
  (start))


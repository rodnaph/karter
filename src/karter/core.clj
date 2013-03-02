
(ns karter.core
  (:require [karter.web :as web]
            [ring.adapter.jetty :as jetty]))

(def port (Long/parseLong
            (or (System/getenv "KARTER_PORT") "3456")))

(defn start []
  (jetty/run-jetty web/app {:port port}))

(defn -main []
  (start))


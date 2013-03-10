
(ns karter.core
  (:use confo.core
        [org.httpkit.server :only [run-server]])
  (:require [karter.web :as web]))

(def config (confo :karter
                   :port 3456))

(defn start []
  (run-server web/app config))

(defn -main []
  (start))


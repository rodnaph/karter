
(ns confo.core
  (:use [clojure.string :only [lower-case]]))

(defn- to-hash-map [prefix entry]
  (hash-map (-> (.getKey entry)
                (subs (inc (count prefix)))
                (lower-case)
                (keyword))
            (.getValue entry)))

(defn- has-prefix [prefix string]
  (= 0 (.indexOf (lower-case string)
                 (str (lower-case prefix) "_"))))

;; Public
;; ------

(defn confo [prefix & options]
  (let [pf (name prefix)
        config (->> (System/getenv)
                    (filter (partial has-prefix pf))
                    (map (partial to-hash-map pf)))]
    (merge (apply hash-map options)
           (into {} config))))



(ns karter.dates
  (:use [clj-time.core :only [interval in-minutes]]
        [clj-time.local :only [local-now]]
        [clj-time.format :only [parse formatters]]))

(defn age-of [k item]
  (let [fmt (formatters :date-time-no-ms)
        age (interval (parse fmt (k item))
                      (local-now))]
    (in-minutes age)))


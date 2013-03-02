
(ns karter.web
  (:use compojure.core
        ring.middleware.reload
        ring.middleware.stacktrace
        [clj-time.core :only [interval in-minutes]]
        [clj-time.local :only [local-now]]
        [clj-time.format :only [parse formatters]])
  (:require (compojure [handler :as handler]
                       [route :as route])
            [tentacles.orgs :as orgs]
            [tentacles.pulls :as prs]
            [karter.html :as html]))

(defn env [k] (System/getenv k))

(def auth {:auth (env "KARTER_AUTH")})
(def user (env "KARTER_USER"))

(defn with-age [pull]
  (let [fmt (formatters :date-time-no-ms)
        age (interval (parse fmt (:created_at pull))
                      (local-now))]
    (assoc pull :age (in-minutes age))))

(defn aged-prs [repo]
  (->> (prs/pulls user repo auth)
       (map with-age)
       (sort-by :age)))

(defn show-org [req]
  (html/layout (str "Repositories for " user)
               (html/organisation (orgs/repos user auth))))

(defn show-repo [repo req]
  (html/layout (str "Repository - " repo)
               (html/repository (aged-prs repo))))

(defroutes app-routes
  (GET "/" [] show-org)
  (GET "/repo/:repo" [repo] (partial show-repo repo))
  (route/resources "/assets")
  (route/not-found "404..."))

(def app
  (-> #'app-routes
    (wrap-reload)
    (wrap-stacktrace)
    (handler/site)))


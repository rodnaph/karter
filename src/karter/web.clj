
(ns karter.web
  (:use compojure.core
        ring.middleware.reload
        ring.middleware.stacktrace
        confo.core
        [clj-time.core :only [interval in-minutes]]
        [clj-time.local :only [local-now]]
        [clj-time.format :only [parse formatters]])
  (:require (compojure [handler :as handler]
                       [route :as route])
            [tentacles.repos :as repos]
            [tentacles.pulls :as prs]
            [karter.html :as html]))

(def config (confo :karter))
(def auth {:auth (:auth config)})
(def user (:user config))
(def perPage 30)

(defn all-repos
  ([user] (all-repos user 1))
  ([user page]
   (let [opts (merge auth {:page page :perPage perPage})
         repos (repos/org-repos user opts)]
     (if (= perPage (count repos))
         (concat repos (all-repos user (inc page)))
         repos))))

(defn with-age [pull]
  (let [fmt (formatters :date-time-no-ms)
        age (interval (parse fmt (:created_at pull))
                      (local-now))]
    (assoc pull :age (in-minutes age))))

(defn aged-prs [repo]
  (->> (prs/pulls user repo auth)
       (map with-age)
       (sort-by :age >)))

(defn show-org [req]
  (html/layout (str "Repositories for " user)
               (html/organisation (all-repos user))))

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


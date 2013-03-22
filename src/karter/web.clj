
(ns karter.web
  (:use compojure.core
        ring.middleware.reload
        ring.middleware.stacktrace
        confo.core
        [karter.dates :only [age-of]])
  (:require (compojure [handler :as handler]
                       [route :as route])
            [tentacles.repos :as repos]
            [tentacles.pulls :as prs]
            [karter.html :as html]))

(def config (confo :karter))
(def auth (select-keys config [:auth]))
(def user (:user config))

(defn repos-for
  ([user] (repos-for user 1))
  ([user page]
   (let [opts (merge auth {:page page})
         repos (repos/org-repos user opts)]
     (if (= 30 (count repos))
         (concat repos (repos-for user (inc page)))
         repos))))

(defn aged-by
  [k comparer lst]
  (sort-by (partial age-of k) comparer lst))

(defn with-issues [repo]
  (> (:open_issues_count repo) 0))

(defn aged-repos []
  (->> (repos-for user)
       (filter with-issues)
       (aged-by :updated_at <)))

(defn aged-prs [repo]
  (->> (prs/pulls user repo auth)
       (aged-by :created_at >)))

(defn show-org [req]
  (html/layout (str "Repositories for " user)
               (html/organisation (aged-repos))))

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


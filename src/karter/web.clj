
(ns karter.web
  (:use compojure.core
        net.cgrand.enlive-html
        ring.middleware.reload
        ring.middleware.stacktrace
        net.cgrand.enlive-html)
  (:require (compojure [handler :as handler]
                       [route :as route])
            [tentacles.orgs :as orgs]
            [tentacles.pulls :as pulls]
            [karter.html :as html]))

(defn env [k] (System/getenv k))

(def auth {:auth (env "KARTER_AUTH")})
(def user (env "KARTER_USER"))

(defn show-org [req]
  (html/layout (str "Repositories for " user)
               (html/organisation (orgs/repos user auth))))

(defn show-repo [repo req]
  (html/layout (str "Repository - " repo)
               (html/repository (pulls/pulls user repo auth))))

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


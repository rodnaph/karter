
(ns karter.web
  (:use compojure.core
        net.cgrand.enlive-html
        ring.middleware.reload
        ring.middleware.stacktrace
        net.cgrand.enlive-html)
  (:require (compojure [handler :as handler]
                       [route :as route])
            [tentacles.orgs :as orgs]
            [tentacles.pulls :as pulls]))

(def auth {:auth (System/getenv "GITHUB_AUTH")})

(defsnippet repo-summary "index.html" [:.repos :li]
  [repo]
  [:a] (do-> (content (:name repo))
             (set-attr "href" (format "/repo/%s" (:name repo)))))

(deftemplate index-tpl "index.html" [repos]
  [:.repos] (content (map repo-summary repos)))

(defn index-page []
  (let [repos (orgs/repos "boxuk" auth)]
    (index-tpl repos)))

(defn project-page [project]
  (let [issues (pulls/pulls "boxuk" project "open")]
    (pr-str issues)))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/repo/:project" [project] (project-page project))
  (route/resources "/assets")
  (route/not-found "404..."))

(def app
  (-> #'app-routes
    (wrap-reload)
    (wrap-stacktrace)
    (handler/site)))


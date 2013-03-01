
(ns karter.html
  (:use net.cgrand.enlive-html))

(defn author [pull]
  (let [login (get-in pull [:user :login])]
    (do-> (content login)
          (set-attr :href
                    (format "https://github.com/%s" login)))))

;; Public
;; ------

(deftemplate layout "layout.html"
  [title body]
  [:title] (content (str "Karter: " title))
  [:.content] (substitute body))

(defsnippet organisation "repos.html" [:.span8]
  [repos]
  [:li] (clone-for [repo repos]
          [:a] (do-> (content (:name repo))
                     (set-attr :href (format "/repo/%s" (:name repo))))
          [:span] (content (:description repo))))

(defsnippet repository "pulls.html" [:.span8]
  [pulls]
  [:li] (clone-for [pull pulls]
          [:a] (do-> (content (:title pull)))
          [:.author] (author pull)))


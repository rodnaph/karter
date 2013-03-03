
(ns karter.html
  (:use net.cgrand.enlive-html))

(defmacro deftransform [defname args & body]
  `(defn ~defname [~@args]
     #(at % ~@body)))

(deftransform author [{:keys [login avatar_url]}]
  [:img] (set-attr :src avatar_url)
  [:a] (do-> (set-attr :href (format "https://github.com/%s" login))
             (set-attr :alt login)
             (set-attr :title login)))

(defn age-class [pull]
  (condp >= (:age pull)
    1 "seconds"
    60 "minutes"
    (* 60 60) "hours"
    "days"))

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
          [:li] (add-class (str "age-" (age-class pull)))
          [:.title] (do-> (content (:title pull))
                          (set-attr :href "asd"))
          [:.author] (author (:user pull))
          [:.body] (content (:body pull))))


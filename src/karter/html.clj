
(ns karter.html
  (:use net.cgrand.enlive-html))

(defmacro deftransform [defname args & body]
  `(defn ~defname [~@args]
     #(at % ~@body)))

(deftransform author [{:keys [user]}]
  [:img] (set-attr :src (:avatar_url user))
  [:a] (do-> (set-attr :href (format "https://github.com/%s" (:login user)))
             (set-attr :alt (:login user))
             (set-attr :title (:login user))))

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
          [:li] (add-class "foo")
          [:.title] (do-> (content (:title pull)))
          [:.author] (author pull)
          [:.description] (content (:body pull))))


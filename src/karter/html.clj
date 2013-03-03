
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

(defn div
  "Divide to an integer"
  [& args]
  (int (apply / args)))

(defn age-class [pull]
  (let [one-hour 60
        one-day (* one-hour 24)
        age (:age pull)]
    (condp >= age
      one-hour [age "minutes"]
      one-day [(div age one-hour)  "hours"]
      [(div age one-day) "days"])))

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
          [:.age] (let [[age cls] (age-class pull)]
                    (do-> (add-class (str "age-" cls))
                          (content (format "%s %s" age cls))))
          [:.title] (do-> (content (:title pull))
                          (set-attr :href "asd"))
          [:.author] (author (:user pull))
          [:.body] (content (:body pull))))


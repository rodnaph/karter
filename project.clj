
(defproject karter "0.0.1"
  :description "Pull Request Viewer"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [http-kit "2.0.0-RC4"]
                 [ring/ring-devel "1.1.8"]
                 [enlive "1.0.1"]
                 [tentacles "0.2.4"]
                 [clj-time "0.4.4"]
                 [confo "0.1.1"]]
  :source-paths ["src" "resources/html"]
  :main karter.core)


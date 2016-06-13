(defproject kicktracker "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [http-kit "2.1.16"]

                 [clj-time "0.9.0"]
                 [ring/ring-devel "1.3.2"]
                 [cheshire "5.4.0"]
                 [hiccup "1.0.5"]]
  :main kicktracker.core
  :aot [kicktracker.core])

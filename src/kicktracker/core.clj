(ns kicktracker.core
  (:require [org.httpkit.server :as server]
            [hiccup.page :as pg]
            [hiccup.core :as hic]
            [kicktracker.query :as q]
            clj-time.coerce)
  (:import [java.net URLDecoder])
  (:gen-class))

;;;;;;;;;; Feed emission
(defn html-escape [str]
  (clojure.string/escape str {\< "&lt;", \> "&gt;", \& "&amp;"}))

(defn projects->feed [title subtitle projects]
  (let [ps (sort-by :launched-at > projects)]
    (str "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
         (hic/html
          [:feed {:xmlns "http://www.w3.org/2005/Atom"}
           [:title title] [:subtitle subtitle]
           [:link {:href "http://kicktracker.inaimathi.ca/feed/atom" :rel "self"}]
           [:link {:href "http://kicktracker.inaimathi.ca"}]
           (map
            (fn [p]
              [:entry 
               [:title (html-escape (p :name))] [:link {:href (p :url)}] [:id (str "project-" (p :id))]
               [:updated (clj-time.coerce/from-long (p :launched-at))]
               [:summary (html-escape (p :blurb))]
               [:content {:type "html"} [:img {:src (p :pic)}]]
               [:author [:name (p :creator)]]])
            ps)]))))

;;;;;;;;;; Handlers
(defn handler [req]
  (let [uri (req :uri)
        body (case uri
               ("/" "/recently-launched") (projects->feed "Recent" "All recently launched projects" (q/recently-launched))
               "/staff-picks" (projects->feed "Picks" "Recent KS staff picks" (q/staff-picks))
               "/board-games" (projects->feed 
                               "Boardgames" "Recently launched tabletop game projects" 
                               (clojure.set/union 
                                (q/search "tabletop+game")
                                (q/search "board+game")
                                (q/by-category (q/category :board-games))))
               (cond 
                 (.startsWith uri "/by-category/id/") (let [[_ num-str] (re-find #"/by-category/id/(\d+)" uri)
                                                            num (Integer. num-str)]
                                                        (projects->feed "Category Feed" (str "category feed for ID:" num-str) (q/by-category num)))
                 (.startsWith uri "/custom/") (let [[match term] (re-find #"/custom/(.+)" uri)]
                                                (projects->feed 
                                                 "Custom Feed" (str "feed for the search term \"" (URLDecoder/decode term) "\"")
                                                 (q/raw-search term)))
                 :else nil))]
    (if body
      {:status 200
       :headers {"Content-Type" "application/atom+xml"}
       :body body}
      {:status 404
       :headers {"Content-Type" "text/plain"}
       :body "Nope, don't have that..."})))

;;;;;;;;;; Main entry point
(defn -main 
  ([]     (-main "8000"))
  ([port]
   (prn (str "Listening on port " port))
   (server/run-server handler {:port (read-string port)})))

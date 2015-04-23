(ns kicktracker.core
  (:require [org.httpkit.server :as server]
            [hiccup.page :as pg]
            [hiccup.core :as hic]
            [kicktracker.query :as q]
            clj-time.coerce)
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
  (let [body (case (req :uri)
               ("/" "/recently-launched") (projects->feed "Recent" "All recently launched projects" (q/recently-launched))
               "/staff-picks" (projects->feed "Picks" "Recent KS staff picks" (q/staff-picks))
               "/board-games" (projects->feed "Boardgames" "Recently launched tabletop game projects" 
                                              (clojure.set/union (q/specific "tabletop game") (q/specific "board game")))
               nil)]
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

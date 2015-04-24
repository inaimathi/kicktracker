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
  {:status 200
   :headers {"Content-Type" "application/atom+xml"}
   :body (let [ps (sort-by :launched-at > projects)]
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
                      ;; [:content {:type "html"} [:img {:src (p :pic)}]]
                      [:author [:name (html-escape (p :creator))]]])
                   ps)])))})

(defn home []
  (pg/html5
   {:lang "en"}
   [:head
    [:meta {:content "width=device-width, initial-scale=1" :name "viewport"}]
    [:title "Kicktracker: the Kickstarter RSS Feed"]]
   [:body
    [:h1 "Kicktracker"]
    [:h3 [:i "The kickstarter RSS feed"]]
    [:p "I got sick of missing interesting game projects on Kickstarter, so I made this."]
    [:p "There are a number of ATOM feeds you can subscribe to. All are sorted by descending order of launch-date, and include only live projects."]
    [:ul 
     [:li [:a {:href "/recently-launched"} [:b [:code "/recently-launched"]]] " - covers all recently launched projects. This one tends to move pretty fast."]
     [:li [:a {:href "/staff-picks"} [:b [:code "/staff-picks"]]] " - covers the projects hand-picked by the Kickstarter staff."]
     [:li [:a {:href "/board-games"} [:b [:code "/board-games"]]] " - covers most of the things I'm interested in. It's a combination of searches for \"tabletop game\", \"board game\", \"card game\" and category #34"]
     [:li [:b [:code "/by-category/id/[category-id-int]"]] " - lets you pick a category to subscribe to. For instance, " [:a {:href "/by-category/id/3"} [:code "/by-category/id/3"]] " will give you the Comics feed."]
     [:li [:b [:code "/custom/[search-term]"]] " - lets you get the feed of an arbitrary search term. For instance, " [:a {:href "/custom/ryan laukat"} [:code "/custom/ryan laukat"]] " shows you the feed of all kickstarted "  [:a {:href "http://www.redravengames.com/"} "Red Raven games"] ". As of this writing, there aren't any new ones, but that's what these feeds are for."]]]))

;;;;;;;;;; Handlers
(defn handler [req]
  (let [uri (req :uri)]
    (case uri
      "/" {:status 200
           :headers {"Content-Type" "text/html"}
           :body (home)} 
      "/recently-launched" (projects->feed "Recent" "All recently launched projects" (q/recently-launched))
      "/staff-picks" (projects->feed "Picks" "Recent KS staff picks" (q/staff-picks))
      "/board-games" (projects->feed 
                      "Boardgames" "Recently launched tabletop game projects" 
                      (clojure.set/union 
                       (q/search "tabletop game")
                       (q/search "board game")
                       (q/search "card game")
                       (q/by-category (q/category :board-games))))
      (cond 
        (.startsWith uri "/by-category/id/") (let [[_ num-str] (re-find #"/by-category/id/(\d+)" uri)
                                                   num (Integer. num-str)]
                                               (projects->feed "Category Feed" (str "category feed for ID:" num-str) (q/by-category num)))
        (.startsWith uri "/custom/") (let [[match term] (re-find #"/custom/(.+)" uri)]
                                       (projects->feed 
                                        "Custom Feed" (str "feed for the search term \"" (URLDecoder/decode term) "\"")
                                        (q/raw-search term)))
        :else {:status 404
               :headers {"Content-Type" "text/plain"}
               :body "Nope, don't have that..."}))))

;;;;;;;;;; Main entry point
(defn -main 
  ([]     (-main "8000"))
  ([port]
   (prn (str "Listening on port " port))
   (server/run-server handler {:port (read-string port)})))

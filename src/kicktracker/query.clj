(ns kicktracker.query
  (:require [org.httpkit.client :as client]
            [cheshire.core :as json])
  (:import [java.net URLEncoder])
  (:gen-class))

(defn gets [map & keys]
  (reduce (fn [memo k] (get memo k)) map keys))

(defn query-project [proj]
  {:id (proj "id")
   :url (gets proj "urls" "web" "project")
   :launched-at (* (proj "launched_at") 1000)
   :deadline (* (proj "deadline") 1000)
   :category (gets proj "category" "name")
   :name (proj "name")
   :blurb (proj "blurb")
   :pic (gets proj "photo" "med")
   :creator (gets proj "creator" "name")})

(defn kick [path]
  (let [res (client/get (str "http://www.kickstarter.com" path))]
    (set (map query-project ((json/parse-string (@res :body)) "projects")))))

(defn recently-launched [] 
  (kick "/discover/recently-launched?format=json"))
(defn ending-soon []
  (kick "/discover/ending-soon?format=json"))
(defn staff-picks []
  (kick "/discover/recommended?format=json"))
(defn specific [search-term]
  (kick (str "/projects/search.json?search=&term=" (URLEncoder/encode search-term))))

;;;;;;;;;; Sample raw project map
;; {"launched_at" 1394151758, 
;;  "currency_trailing_code" true, 
;;  "urls" {"web" 
;;          {"project" "https://www.kickstarter.com/projects/953146955/the-ancient-world?ref=discovery", 
;;           "rewards" "https://www.kickstarter.com/projects/953146955/the-ancient-world/rewards"}}, 
;;  "currency_symbol" "$", 
;;  "profile" {"project_id" 889004, 
;;             "feature_image_attributes" 
;;             {"image_urls" 
;;              {"default" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=1536&h=1152&fit=crop&auto=format&q=92&s=a66d45892af9bb9caa7250061f5140d5", 
;;               "baseball_card" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=1536&h=1152&fit=crop&auto=format&q=92&s=a66d45892af9bb9caa7250061f5140d5"}}, 
;;             "link_url" nil, 
;;             "name" nil, 
;;             "state_changed_at" 1425915848, 
;;             "show_feature_image" false, 
;;             "state" "inactive", 
;;             "text_color" nil, 
;;             "background_color" nil, 
;;             "link_text" nil, 
;;             "link_text_color" nil, 
;;             "background_image_opacity" 0.8, 
;;             "link_background_color" nil, 
;;             "blurb" nil, 
;;             "id" 889004, 
;;             "should_show_feature_image" true}, 
;;  "created_at" 1392317561, 
;;  "static_usd_rate" "1.0", 
;;  "pledged" 49024.43, 
;;  "name" "The Ancient World", 
;;  "slug" "the-ancient-world", 
;;  "creator" {"id" 953146955, 
;;             "name" "Ryan Laukat", 
;;             "avatar" {"thumb" "https://ksr-ugc.imgix.net/avatars/1351145/logo-181.original.jpg?v=1338088793&w=40&h=40&fit=crop&auto=format&q=92&s=89d95aafdc72165c830bc9730d1ce57d", 
;;                       "small" "https://ksr-ugc.imgix.net/avatars/1351145/logo-181.original.jpg?v=1338088793&w=80&h=80&fit=crop&auto=format&q=92&s=48910f853c698ee4c77f05c8019aaa31", 
;;                       "medium" "https://ksr-ugc.imgix.net/avatars/1351145/logo-181.original.jpg?v=1338088793&w=160&h=160&fit=crop&auto=format&q=92&s=352b594574024e05d902eb3dfdf29687"}, 
;;             "urls" {"web" {"user" "https://www.kickstarter.com/profile/953146955"}, 
;;                     "api" {"user" "https://api.kickstarter.com/v1/users/953146955?signature=1429748549.68078a586e47124bc79415b2a45946738f9a4af5"}}}, 
;;  "state_changed_at" 1396567358, 
;;  "backers_count" 918, 
;;  "currency" "USD", 
;;  "state" "successful", 
;;  "usd_pledged" "49024.43", 
;;  "photo" {"full" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=560&h=420&fit=crop&auto=format&q=92&s=11bc249c443e0788e6d529686d492a34", 
;;           "ed" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=338&h=250&fit=crop&auto=format&q=92&s=bdc260ed5fa7bbcd5cce5e3724365f3e", 
;;           "med" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=266&h=200&fit=crop&auto=format&q=92&s=f20d45d6be102879ba72794c8b17466b", 
;;           "little" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=200&h=150&fit=crop&auto=format&q=92&s=f70c6a5bf899421a45c419929b34d435", 
;;           "small" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=160&h=120&fit=crop&auto=format&q=92&s=12dd9d9920a7a8e3f8de00c7656aead3", 
;;           "thumb" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=40&h=30&fit=crop&auto=format&q=92&s=867bcd3f8b2735db4f4b3ad6407716e5", 
;;           "1024x768" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=1024&h=768&fit=crop&auto=format&q=92&s=58b6a32a8163091f5ff06f33a8efb8b0", 
;;           "1536x1152" "https://ksr-ugc.imgix.net/projects/868982/photo-original.jpg?v=1397867905&w=1536&h=1152&fit=crop&auto=format&q=92&s=a66d45892af9bb9caa7250061f5140d5"}, 
;;  "goal" 15000.0, 
;;  "spotlight" true, 
;;  "location" {"urls" 
;;              {"web" {"discover" "https://www.kickstarter.com/discover/places/sandy-ut", 
;;                      "location" "https://www.kickstarter.com/locations/sandy-ut"}, 
;;               "api" {"nearby_projects" "https://api.kickstarter.com/v1/discover?signature=1429747654.3c4f4d5ccdae14e7e0d19fa1d006179a067e0d99&woe_id=2488558"}}, 
;;              "name" "Sandy", 
;;              "slug" "sandy-ut", 
;;              "short_name" "Sandy, UT", 
;;              "displayable_name" "Sandy, UT", 
;;              "state" "UT", 
;;              "country" "US", 
;;              "is_root" false, 
;;              "type" "Town", 
;;              "id" 2488558}, 
;;  "country" "US", 
;;  "deadline" 1396567358, 
;;  "blurb" "Fight rampaging titans and build a powerful civilization in an ancient, mythological world.", 
;;  "id" 1574430381, 
;;  "category" {"id" 34, 
;;              "name" "Tabletop Games", 
;;              "slug" "games/tabletop games", 
;;              "position" 6, 
;;              "parent_id" 12, 
;;              "urls" {"web" {"discover" "http://www.kickstarter.com/discover/categories/games/tabletop%20games"}}}, 
;;  "disable_communication" false}

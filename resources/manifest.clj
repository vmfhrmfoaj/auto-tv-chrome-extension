(ns manifest
  (:require [clojure.edn :as edn]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(def template
  {:name nil
   :description nil
   :version nil
   :permissions ["tabs" "http://*/*" "https://*/*"]
   :background {:scripts []}
   :content-scripts [{:matches ["*://*/*"] :js []}]
   :manifest_version 2})

(let [get-build #(->> %1 (filter (comp (partial = %2) :id)) (first))
      project (-> "project.clj"
                  (slurp)
                  (edn/read-string))
      name    (str (nth project 1))
      version (-> project
                  (nth 2)
                  (str/split #"-")
                  (first))
      project (->> project (drop 3) (apply hash-map))]
  (-> template
      (assoc :name name)
      (assoc :description (get project :description))
      (assoc :version version)
      (assoc-in [:background :scripts 0]
                (-> project
                    (get-in [:cljsbuild :builds])
                    (get-build "background")
                    (get-in [:compiler :output-to])
                    (str/split #"/")
                    (rest)
                    (->> (interpose "/") (apply str))))
      (assoc-in [:content-scripts 0 :js 0]
                (-> project
                    (get-in [:cljsbuild :builds])
                    (get-build "content")
                    (get-in [:compiler :output-to])
                    (str/split #"/")
                    (rest)
                    (->> (interpose "/") (apply str))))
      (json/write-str :escape-slash false
                      :key-fn (comp #(str/replace % #"-" "_") clojure.core/name))
      (->> (spit "resources/manifest.json"))))

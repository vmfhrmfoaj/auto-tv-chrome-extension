(ns manifest
  (:require [clojure.edn :as edn]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(let [proj (-> "project.clj"
               (slurp)
               (edn/read-string))
      [_ name version] proj
      proj (->> proj (drop 3) (apply hash-map))
      get-build #(->> %1 (filter (comp (partial = %2) :id)) (first))
      get-js #(-> proj
                  (get-in [:cljsbuild :builds])
                  (get-build %1)
                  (get-in [:compiler :output-to])
                  (str/split #"/")
                  (rest)
                  (->> (interpose "/") (apply str)))
      manifest {:name (str name)
                :description (get proj :description)
                :version (-> version (str/split #"-") (first))
                :permissions ["tabs" "http://*/*" "https://*/*"]
                :background {:scripts [(get-js "background")]}
                :content-scripts [{:matches ["*://*/*"] :js [(get-js "content")]}]
                :manifest_version 2}]
  (spit "resources/manifest.json"
        (json/write-str manifest
                        :escape-slash false
                        :key-fn (comp #(str/replace % #"-" "_") clojure.core/name))))

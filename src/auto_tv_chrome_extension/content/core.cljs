(ns auto-tv-chrome-extension.content.core
  (:require [clojure.string :as str]
            [cljs.core.async :refer [<! timeout]]
            [domina]
            [domina.xpath :refer [xpath]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defn get-tv-schedule-info []
  (try
    (some-> "//*[@id='main_pack']/script[10]"
            (xpath)
            (domina/html)
            (js/eval))
    (js->clj js/htInitDataForTvtimeSchedule :keywordize-keys true)
    (catch js/Error _
      nil)))

(defn copy-to-clipboard [str]
  (.. js/clipboard (copy str)))

(go
  (enable-console-print!)
  (<! (timeout (* 1000 1)))
  (when-let [info (get-tv-schedule-info)]
    (-> (hash-map (-> info
                      :apiQuery
                      (str/split #" ?편성표")
                      (first)
                      (str/upper-case)
                      (keyword))
                  (:os info))
        (str)
        (copy-to-clipboard))))

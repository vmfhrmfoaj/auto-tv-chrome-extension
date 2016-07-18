(defproject auto-tv-chrome-extension
  "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :license {:name "GPLv3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}

  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                 [org.clojure/clojurescript "1.9.89"]
                 [domina "1.0.3"]
                 [org.clojure/core.async "0.2.385"]]
  :plugins [[lein-npm "0.6.1"]]
  :npm {:root "resources/content"
        :dependencies [[source-map-support "0.4.0"]
                       [clipboard-js "0.2.0"]]}
  :profiles {:dev {:dependencies [[expectations "2.1.8"]
                                  [org.clojure/test.check "0.9.0"]]
                   :plugins [[lein-expectations "0.0.8"]
                             [lein-autoexpect "1.9.0"]
                             [lein-cljsbuild "1.1.3"]]}}

  :jvm-opts ["-server"]

  :aliases {"autotest" ["autoexpect" :notify]}

  :cljsbuild {:builds [{:id "background"
                        :source-paths ["src/auto_tv_chrome_extension/background/"]
                        :compiler {:output-to "resources/background/main.js"
                                   :output-dir "resources/background/"
                                   :source-map "resources/background/main.js.map"
                                   :closure-output-charset "US-ASCII"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:id "content"
                        :source-paths ["src/auto_tv_chrome_extension/content/"]
                        :compiler {:output-to "resources/content/main.js"
                                   :output-dir "resources/content/"
                                   :source-map "resources/content/main.js.map"
                                   :closure-output-charset "US-ASCII"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})

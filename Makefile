BACKGROUND_SRCS := $(shell find src/auto_tv_chrome_extension/background -name "*.cljs" -o -name "*.cljc")
CONTENT_SRCS    := $(shell find src/auto_tv_chrome_extension/content    -name "*.cljs" -o -name "*.cljc")
IGNORED := $(shell git check-ignore **/*)

all: release
release: resources/background/main.js resources/content/main.js resources/manifest.json

resources/background/main.js: $(BACKGROUND_SRCS)
	lein cljsbuild once background

resources/content/main.js: $(CONTENT_SRCS)
	lein cljsbuild once content

resources/manifest.json: resources/manifest.clj
	lein trampoline run -m clojure.main resources/manifest.clj

clean:
	@$(foreach file, $(IGNORED), rm -rf $(file))

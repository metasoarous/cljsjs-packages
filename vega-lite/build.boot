(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[org.clojure/clojurescript "1.10.597"]
                  [cljsjs/boot-cljsjs "0.10.4" :scope "test"]
                  [cljsjs/vega "5.3.2-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "3.4.0")
(def +version+ (str +lib-version+ "-0"))

(task-options!
  pom {:project     'cljsjs/vega-lite
       :version     +version+
       :description "A high-level grammar for visual analysis, built on top of Vega."
       :url         "https://vega.github.io/vega-lite"
       :scm         {:url "https://github.com/cljsjs/packages"}})

(deftask package []
  (task-options! push {:ensure-branch nil})
  (comp
    (download
     :url (format "https://unpkg.com/vega-lite@%s/build/vega-lite.js" +lib-version+)
     :checksum "3670000978f7b7e0f815187fcd05b2bb")
    (download
     :url (format "https://unpkg.com/vega-lite@%s/build/vega-lite.min.js" +lib-version+)
     :checksum "16f8338449da83a0f1d8c79dbf4af042")
    (sift :move {(re-pattern "^vega-lite.js$") "cljsjs/development/vega-lite.inc.js"
                 (re-pattern "^vega-lite.min.js$") "cljsjs/production/vega-lite.min.inc.js"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.vega-lite"
               :requires ["cljsjs.vega"])
    (pom)
    (jar)))

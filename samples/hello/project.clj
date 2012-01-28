(defproject hello "1.1.0"
  :description "helloworld clojurescript"
  :dev-dependencies [[lein-clojurescript "1.1.0"]]
  :extra-classpath-dirs ["src"]
  :cljs-output-to "resources/public/js/hello.js"
  :cljs-output-dir "resources/public/js/out"
  :cljs-optimizations :advanced
  :cljs-externs ["externs.js"]
  :cljs-libs ["js/foobar.js"]
  :cljs-foreign-libs [{:file "http://example.com/example.js"
                       :provides ["example.example"]}]
  :cljs-test-cmd ["phantomjs" "test.js"])

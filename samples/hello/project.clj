(defproject hello "1.1.0"
  :description "helloworld clojurescript"
  :dev-dependencies [[lein-clojurescript "1.1.0"]]
  :cljs-output-to "resources/public/js/hello.js"
  :cljs-output-dir "resources/public/js/out"
  :cljs-optimizations :advanced)
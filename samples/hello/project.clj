(defproject hello "1.0.0-SNAPSHOT"
  :description "helloworld clojurescript"
  :dev-dependencies [[lein-clojurescript "1.0.2-SNAPSHOT"]]
  :cljs-output-to "resources/public/js/hello.js"
  :cljs-output-dir "resources/public/js/out"
  :cljs-optimizations :advanced)
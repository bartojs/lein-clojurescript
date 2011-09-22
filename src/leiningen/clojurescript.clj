(ns ^{:doc "clojurescript leiningen plugin" :author "justin barton"}
  leiningen.clojurescript
  (:require leiningen.clojurescript.compile)
  )

(defn clojurescript
  "lein-clojurescript: Compiles clojurescript (.cljs) files in src to google closure compatible javascript (.js) files.
   Can use as a standalone task or can hook into the normal compile task.
   Uses project name or group for outputfile. Accepts commandline args.
   examples: lein clojurescript
   lein compile '{:output-dir \"myout\" :output-to \"bla.js\" :optimizations :advanced}'"
  [project & args]
  (apply leiningen.clojurescript.compile/compile project args)
  )




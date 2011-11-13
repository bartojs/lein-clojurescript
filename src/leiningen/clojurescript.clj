(ns ^{:doc "clojurescript leiningen plugin" :author "justin barton"}
  leiningen.clojurescript
  (:require [leiningen.clojurescript compile repl])
  )

(defn- show-usage []
  (println "clojurescript plugin. Useage: lein clojurescript cmd [args].\n"
		   " cmd = help | compile | compile-dev | compile-prod | repl-rhino | repl-browser\n"
		   " args = currnetly only relevant for compile cmd and is the map passed to compiler.\n"
		   " example: lein clojurescript compile '{:output-dir \"myout\" :output-to \"bla.js\" :optimizations :advanced}'"))

(defn clojurescript
  "lein-clojurescript: Compiles clojurescript (.cljs) files in src to google closure compatible javascript (.js) files.
   Can use as a standalone task or can hook into the normal compile task.
   Uses project name or group for outputfile. Accepts commandline args.
   examples: lein clojurescript
   lein compile '{:output-dir \"myout\" :output-to \"bla.js\" :optimizations :advanced}'"
  [project & args]
  ;; TODO: clojruescript will be the control command
  ;;       cmd can be compile {opts}
  ;;                  compile-dev
  ;;                  compile-prod
  ;;                  repl-rhino
  ;;                  repl-browser
  ;;                  help
  ;;       default of help.
  (let [cmd (or (first args) (:clojurescript-default-command project) "help")]
	(case cmd
		  "compile" (apply leiningen.clojurescript.compile/compile-task project args)
		  "compile-dev" (leiningen.clojurescript.compile/compile-task project nil)
		  "compile-prod" (leiningen.clojurescript.compile/compile-task project "{:optimizations :advanced}")		  
		  "repl-rhino" (apply leiningen.clojurescript.repl/repl-task (assoc project :repl-mode :rhino) args)
		  "repl-browser" (apply leiningen.clojurescript.repl/repl-task (assoc project :repl-mode :browser) args)
	      (show-usage))))




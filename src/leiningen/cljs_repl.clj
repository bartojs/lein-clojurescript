(ns leiningen.cljs-repl
  (:require cljs.repl
			[cljs.repl rhino]
			)
  )

(defn cljs-repl [project & args]
  (cljs.repl/repl (cljs.repl.rhino/repl-env))) 
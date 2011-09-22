(ns leiningen.cljs-repl
  (:require leiningen.clojurescript.repl)
  )

(defn cljs-repl [project & args]
  (let [repl-mode (cond (= (first args) "rhino") :rhino
						(= (first args) "browser") :browser
						(contains? project :repl-mode) (:repl-mode project)
						:else :browser)]
	(apply leiningen.clojurescript.repl/repl-task
		   (cons (assoc project :repl-mode repl-mode)
				 args))))
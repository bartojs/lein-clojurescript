(ns ^{:doc "clojurescript leiningen plugin " :author "justin barton"}
  leiningen.clojurescript.repl
  (:require leiningen.clojurescript.repl-run)
  )

(defn repl-hook [task & args]
  (let [project (first args)]
	(if (contains? project :repl-init)
	  (apply task args)
	  (do
		(println "starting clojurescript repl")
		(alter-var-root #'leiningen.clojurescript.repl-run/*repl-mode* (fn [_] (if (contains? project :repl-mode) (:repl-mode project) :browser)))
		(apply task (cons (assoc project :repl-init 'leiningen.clojurescript.repl-run) (rest args)))
		)
	  )
	)
  )


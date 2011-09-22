(ns ^{:doc "clojurescript leiningen plugin " :author "justin barton"}
  leiningen.clojurescript.repl
  (:require cljs.repl cljs.repl.rhino cljs.repl.browser)
  )

(defn repl-task [project & args]
  (cljs.repl/repl
     (if (= :rhino (:repl-mode project))
		 (do
		   (println "starting clojurescript rhino repl")
		   (cljs.repl.rhino/repl-env))
		 (do
		   (println "starting clojurescript browser repl")
		   (cljs.repl.browser/repl-env))))
  )

(defn repl-hook [orig-task project & args]
    (println "repl hook called")
	(if (contains? project :repl-mode)
	  (apply repl-task project args)
	  (apply orig-task project args)
	  )
	)


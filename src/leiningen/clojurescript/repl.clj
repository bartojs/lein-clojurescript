(ns ^{:doc "clojurescript leiningen plugin " :author "justin barton"}
  leiningen.clojurescript.repl
;;  (:require cljs.repl cljs.repl.rhino cljs.repl.browser)
  )

(defn- repl-browser [project]
  (leiningen.compile/eval-in-project
     project
	 `(cljs.repl/repl (cljs.repl.browser/repl-env))
     nil nil
     '(require 'cljs.repl 'cljs.repl.browser)))

(defn- repl-rhino [project]
   (leiningen.compile/eval-in-project
     project
	 `(cljs.repl/repl (cljs.repl.rhino/repl-env))
     nil nil
     '(require 'cljs.repl 'cljs.repl.rhino)))

(defn repl-task [project & args]
     (if (= :rhino (:repl-mode project))
		 (do
		   (println "starting clojurescript rhino repl")
		   (repl-rhino project))
		 (do
		   (println "starting clojurescript browser repl")
		   (repl-browser project))))

(defn repl-hook [orig-task project & args]
    (println "repl hook called")
	(if (contains? project :repl-mode)
	  (apply repl-task project args)
	  (apply orig-task project args)
	  )
	)


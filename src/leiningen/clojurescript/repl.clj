(ns ^{:doc "clojurescript leiningen plugin " :author "justin barton"}
  leiningen.clojurescript.repl
  (:require cljs.repl cljs.repl.rhino cljs.repl.browser)
  )
;; cant seem to require cljs.repl.browser and rhino in the same ns
;; otherwise repl always has exception todo with browser

(defn repl-task [project & args]
  	  (cljs.repl/repl (if (= :rhino (:repl-mode project))
						  (do
									   	(println "starting clojurescript rhino repl")
									   ;; (require 'cljs.repl.rhino)
										(cljs.repl.rhino/repl-env))
						  ;;))
	  						  (do (println "starting clojurescript browser repl")
								  ;;(require 'cljs.repl.browser)
								  (cljs.repl.browser/repl-env))))
  )

(defn repl-hook [orig-task project & args]
    (println "repl hook called")
	(if (contains? project :repl-mode)
	  (apply repl-task (cons project args))
	  (apply orig-task (cons project args))
	  )
	)


(ns ^{:doc "lein-clojurescript hooks to standard tasks" :author "justin barton"}
  leiningen.clojurescript.hooks
  (:require [robert.hooke :as hooke]
            [leiningen compile repl]
			[leiningen.clojurescript compile repl]
			)
  )

(println "adding compile hook")
(hooke/add-hook #'leiningen.compile/compile leiningen.clojurescript.compile/compile-hook)

(println "adding repl hook")
(hooke/add-hook #'leiningen.repl/repl leiningen.clojurescript.repl/repl-hook)
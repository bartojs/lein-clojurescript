(ns ^{:doc "lein-clojurescript hooks" :author "justin barton"}
  leiningen.clojurescript.hooks
  (:require [robert.hooke :as hooke]
            [leiningen compile repl]
			[leiningen.clojurescript compile repl]
			)
  )

(hooke/add-hook #'leiningen.compile/compile leiningen.clojurescript.compile/compile-hook)

(hooke/add-hook #'leiningen.repl/repl leiningen.clojurescript.repl/repl-hook)
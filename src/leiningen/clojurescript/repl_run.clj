(ns leiningen.clojurescript.repl-run
  (:require cljs.repl
			[cljs.repl rhino browser]
			)
  )

(def ^{:dynamic true} *repl-mode* :none)

(when (not (= *repl-mode* :none))
  (cljs.repl/repl (condp *repl-mode*
					  :rhino (cljs.repl.rhino/repl-env)
					  (cljs.repl.browser/repl-env)))
  )
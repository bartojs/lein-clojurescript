(ns ^{:doc "clojurescript leiningen compile" :author "justin barton"}
  leiningen.clojurescript.compile
  (:require [cljs.closure :as closure]
			[clojure.string :as string]
			[clojure.java.io :as io])
  (:import java.util.Date)
  )

(defn- clojurescript-arg? [arg] (-> arg str string/trim seq first (= \{)))
(defn- clojurescript-file? [filename] (.endsWith (string/lower-case filename) ".cljs"))
(def getName (memfn getName))

(defn compile-task [project & args]
  (let [outputfile (str (or (:name project) (:group project)) ".js")
		opts (apply merge {:output-to outputfile :output-dir "out"} (map read-string (filter clojurescript-arg? args)))
		sourcedir (or (:clojurescript-src project) (:src-dir opts) "src")		
		starttime (.getTime (Date.))]
	(print (str "Compiling clojurescript in " sourcedir " ... "))
	(if-let [cljsfiles (seq (filter (comp clojurescript-file? getName) (file-seq (io/file sourcedir))))]
	  (do
		(closure/build sourcedir opts)
		(println (format "compiled %d files to %s/ and '%s' (took %d ms)" (count cljsfiles) (:output-dir opts) (:output-to opts) (- (.getTime (Date.)) starttime)))
		)
	  (println "no cljs files found.")
	  )
	)
  )

(defn compile-hook [orig-task project & args]
  (let [js-args (filter clojurescript-arg? args)
		clj-args (remove clojurescript-arg? args)]
	(apply compile-task project js-args)
	(when (or (contains? project :aot) (seq clj-args))
	  (apply orig-task project clj-args))
	)
  )
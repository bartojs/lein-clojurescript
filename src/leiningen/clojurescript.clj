(ns ^{:doc "clojurescript leiningen plugin" :author "justin barton"}
  leiningen.clojurescript
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [robert.hooke :as hooke]
            leiningen.compile)
  (:import java.util.Date))

(defn- clojurescript-arg? [arg]
  (-> arg str string/trim seq first (= \{)))

(defn- clojurescript-file? [filename]
  (.endsWith (string/lower-case filename) ".cljs"))

(def getName (memfn getName))

(defn- cljsc [project source-dir options]
  (leiningen.compile/eval-in-project
   (dissoc project :source-path)
   `(cljsc/build ~source-dir ~options)
   nil true
   '(require '[cljs.closure :as cljsc])))

(defn clojurescript
  "lein-clojurescript: Compiles clojurescript (.cljs) files in src to google
closure compatible javascript (.js) files.
Can use as a standalone task or can hook into the normal compile task.
Uses project name or group for outputfile. Accepts commandline args.
examples: lein clojurescript
          lein compile '{:output-dir \"myout\" :output-to \"bla.js\" \\
              :optimizations :advanced}'"
  [project & args]
  (let [outputfile (str (or (:name project) (:group project)) ".js")
        opts (apply merge {:output-to outputfile :output-dir "out"}
                    (map read-string (filter clojurescript-arg? args)))
        sourcedir (or (:clojurescript-src project) (:src-dir opts) "src")
        starttime (.getTime (Date.))]
    (print (str "Compiling clojurescript in " sourcedir " ... "))
    (if-let [cljsfiles (seq (filter (comp clojurescript-file? getName)
                                    (file-seq (io/file sourcedir))))]
      (do
        (cljsc project sourcedir opts)
        (println (format "compiled %d files to %s/ and '%s' (took %d ms)"
                         (count cljsfiles) (:output-dir opts) (:output-to opts)
                         (- (.getTime (Date.)) starttime))))
      (println "no cljs files found."))))

(defn compile-clojurescript-hook [task & args]
  (let [project (first args)
        js-args (filter clojurescript-arg? (rest args))
        clj-args (remove clojurescript-arg? (rest args))]
    (apply clojurescript (cons project js-args))
    (when (or (contains? project :aot) (seq clj-args))
      (apply task (cons project clj-args)))))

(hooke/add-hook #'leiningen.compile/compile compile-clojurescript-hook)

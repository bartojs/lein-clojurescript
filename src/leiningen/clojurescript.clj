(ns ^{:doc "clojurescript leiningen plugin" :author "justin barton"}
  leiningen.clojurescript
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [robert.hooke :as hooke]
            leiningen.compile)
  (:use [watcher :only (with-watch-paths)])
  (:import java.util.Date))

(defn- clojurescript-arg? [arg]
  (-> arg str string/trim seq first (= \{)))

(defn- clojurescript-file? [filename]
  (.endsWith (string/lower-case filename) ".cljs"))

(def getName (memfn getName))

(defn- cljsc [project source-dir options]
  (binding [leiningen.compile/*skip-auto-compile* true]
    (leiningen.compile/eval-in-project
     (dissoc project :source-path)
     `(cljsc/build ~source-dir ~options)
     nil nil
     '(require '[cljs.closure :as cljsc]))))

(defn clojurescript
  "lein-clojurescript: Compiles clojurescript (.cljs) files in src to google
closure compatible javascript (.js) files.
Can use as a standalone task or can hook into the normal compile task.
Uses project name or group for outputfile. Accepts commandline args. If the
argument 'watch' is present, the sources are monitored and recompiled when they
change.
examples: lein clojurescript
          lein clojurescript watch
          lein compile '{:output-dir \"myout\" :output-to \"bla.js\" \\
              :optimizations :advanced}'"
  [project & args]
  (let [outputfile (str (or (:name project) (:group project)) ".js")
        opts (apply merge {:output-to (or (:cljs-output-to project) outputfile)
                           :output-dir (or (:cljs-output-dir project) "out")
                           :optimizations (:cljs-optimizations project)}
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
                         (- (.getTime (Date.)) starttime)))
        (when (some #{"watch"} args)
          (let [events? (atom false)]
            (future
              (println "Watching...")
              (with-watch-paths [sourcedir]
                (fn [_] (reset! events? true))
                :recursive))
            (while true
              (if @events?
                (do
                  (println "Compiling...")
                  (cljsc project sourcedir opts)
                  (println "Watching...")
                  (Thread/sleep 500)
                  (reset! events? false))
                (Thread/sleep 100))))))
      (println "no cljs files found."))))

(defn compile-clojurescript-hook [task & args]
  (let [project (first args)
        js-args (filter clojurescript-arg? (rest args))
        clj-args (remove clojurescript-arg? (rest args))]
    (apply clojurescript (cons project js-args))
    (when (or (contains? project :aot) (seq clj-args))
      (apply task (cons project clj-args)))))

(hooke/add-hook #'leiningen.compile/compile compile-clojurescript-hook)

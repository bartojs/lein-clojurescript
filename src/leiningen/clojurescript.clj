(ns ^{:doc "clojurescript leiningen plugin" :author "justin barton"}
  leiningen.clojurescript
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [robert.hooke :as hooke]
            leiningen.compile
            fs)
  (:use [watcher :only (with-watch-paths)])
  (:import java.util.Date))

(defn- clojurescript-arg? [arg]
  (-> arg str string/trim seq first (= \{)))

(defn- clojurescript-file? [filename]
  (.endsWith (string/lower-case filename) ".cljs"))

(def getName (memfn getName))

(defn- maybe-test [project args]
  (when-let [cmd (and (some #{"test"} args)
                    (:cljs-test-cmd project))]
    `(do
       (println (str "Running `" ~(clojure.string/join " " cmd) "`."))
       (let [res# (clojure.java.shell/sh ~@cmd)]
         (println (:out res#))
         (println (:err res#))
         (:exit res#)))))

(defn- build-once [project source-dir opts args cljsfiles]
  `(try
     (println "Compiling ...")
     (let [starttime# (.getTime (Date.))]
       (cljs.closure/build ~source-dir ~opts)
       (println (format "Compiled %d files to %s/ and '%s' (took %d ms)."
                        ~(count cljsfiles)
                        ~(:output-dir opts)
                        ~(:output-to opts)
                        (- (.getTime (Date.)) starttime#))))
     (System/exit (or ~(maybe-test project args) 0))
     (catch Throwable e#
       (clj-stacktrace.repl/pst+ e#)
       (System/exit 1))))

(defn- build-loop [project source-dir opts args] 
  `(let [events?# (atom true)]
     (future
       (watcher/with-watch-paths [~source-dir]
         (fn [ignored#] (reset! events?# true))
         :recursive))
     (while true
       (if @events?#
         (do
           (println "Compiling ...")
           (try
             (cljs.closure/build ~source-dir ~opts)
             ~(maybe-test project args)
             (catch Throwable e#
               (clj-stacktrace.repl/pst+ e#)))
           (println "Watching ...")
           (Thread/sleep 500)
           (reset! events?# false))
         (Thread/sleep 100)))))

(defn- build [project source-dir opts args cljsfiles]
  (binding [leiningen.compile/*skip-auto-compile* true]
    (leiningen.compile/eval-in-project
      (dissoc project :source-path)
      (if (some #{"watch"} args) 
        (build-loop project source-dir opts args)
        (build-once project source-dir opts args cljsfiles))
      nil
      nil
      '(require 'cljs.closure
                'clj-stacktrace.repl
                'clojure.java.shell
                'clojure.string
                'watcher))))

(defn clojurescript
  "lein-clojurescript: Compiles clojurescript (.cljs) files in src to google
closure compatible javascript (.js) files.
Can use as a standalone task or can hook into the normal compile task.
Uses project name or group for outputfile. Accepts the following commandline
arguments:

watch  monitor sources and recompile when they change.
test   run (apply clojure.java.shell/sh (:cljs-test-cmd project)) after each
       compile.
fresh  remove output files before doing anything else.
clean  remove output files and do nothing else (ignores other args).
{   }  an option map to pass to cljs.closure/build.

examples: lein clojurescript
          lein clojurescript watch
          lein clojurescript fresh '{:output-dir \"myout\" }'
          lein compile '{:output-dir \"myout\" \\
                         :output-to \"bla.js\" \\
                         :optimizations :advanced}'"
  [project & args]
  (let [outputfile (str (or (:name project) (:group project)) ".js")
        opts (apply merge {:output-to (or (:cljs-output-to project) outputfile)
                           :output-dir (or (:cljs-output-dir project) "out")
                           :externs (:cljs-externs project)
                           :libs (:cljs-libs project)
                           :foreign-libs (:cljs-foreign-libs project)
                           :optimizations (:cljs-optimizations project)}
                    (map read-string (filter clojurescript-arg? args)))
        sourcedir (or (:clojurescript-src project) (:src-dir opts) "src")
        starttime (.getTime (Date.))]
    (when (some #{"clean" "fresh"} args)
      (println (str "Removing '" (:output-dir opts)
                  "' and '" (:output-to opts) "' ..."))
      (fs/delete (:output-to opts))
      (fs/deltree (:output-dir opts)))
    (when-not (some #{"clean"} args)
      (if-let [cljsfiles (seq (filter (comp clojurescript-file? getName)
                                      (file-seq (io/file sourcedir))))]
        (build project sourcedir opts args cljsfiles)
        (do
          (println "No cljs files found.")
          1)))))

(defn compile-clojurescript-hook [task & args]
  (let [project (first args)
        js-args (filter clojurescript-arg? (rest args))
        clj-args (remove clojurescript-arg? (rest args))]
    (apply clojurescript (cons project js-args))
    (when (or (contains? project :aot) (seq clj-args))
      (apply task (cons project clj-args)))))

(hooke/add-hook #'leiningen.compile/compile compile-clojurescript-hook)

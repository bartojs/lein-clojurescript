# lein-clojurescript

[leiningen](https://github.com/technomancy/leiningen) plugin for clojurescript compilation.

NOTE! as of 1.0.1-SNAPSHOT lein-clojurescript works with lein 1.6.1.1
      (thanks to Felix H. Dahlke)

See clojure.org for clojurescript details.
See code.google.com/closure for google closure details.
See github.com/technomancy/leiningen for lein details.

## Usage

```
lein plugin install lein-clojurescript 1.1.0
```

Or in your project.clj add a dev-dependency
```
:dev-dependencies [[lein-clojurescript "1.1.0"] ...]
```

To compile clojurescript files in src:
```
lein clojurescript
```

To start the compile with a clean output directory and output file:
```
lein clojurescript fresh
```

To clean without compiling:
```
lein clojurescript clean
```

To watch the sources and recompile when they change:
```
lein clojurescript watch
```

To run a test command after compiling (defined by :cljs-test-cmd):
```
lein clojurescript test
```

Combine some of the above:
```
lein clojurescript fresh watch test
```

If you'd like the plugin to hook into the normal compile add to the hooks list:
```
:hooks [leiningen.clojurescript ...]
``` 

To compile clojurescript along with a normal compile:
```
lein compile
```

Compile with advanced mode: 
```
lein compile '{:optimizations :advanced}'
```

Or add to project.clj:

```
:cljs-optimizations :advanced
```

Additional plugin-specific project.clj settings include:

```
:cljs-output-to
:cljs-output-dir
:cljs-externs
:cljs-libs
:cljs-foreign-libs
:cljs-test-cmd
```

`:cljs-test-cmd` must be in a format useable by `clojure.java.shell/sh`. E.g.
```
:cljs-test-cmd ["phantomjs" "tests.js"]
```

See <http://lukevanderhart.com/2011/09/30/using-javascript-and-clojurescript.html>
for more information about `:cljs-externs`, `:cljs-libs`, and
`:cljs-foreign-libs`.

For an example usage see samples/hello/project.clj.



## Authors
   * fhd (Felix H. Dahlke)
   * rplevy (Robert Levy)
   * mmwaikar (Manoj Waikar)
   * bartonj (Justin Barton)

## License
Copyright (C) 2011 Justin Barton
Distributed under the Eclipse Public License, the same as Clojure.

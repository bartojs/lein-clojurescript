# lein-clojurescript

[leiningen](https://github.com/technomancy/leiningen) plugin for clojurescript compilation.

NOTE! as of 1.0.1-SNAPSHOT lein-clojurescript works with lein 1.6.1.1
      (thanks to Felix H. Dahlke)

See clojure.org for clojurescript details.
See code.google.com/closure for google closure details.
See github.com/technomancy/leiningen for lein details.

## Usage

```
lein plugin install lein-clojurescript 1.0.2-SNAPSHOT
```

Or in your project.clj add a dev-dependency
```
:dev-dependencies [[lein-clojurescript "1.0.2-SNAPSHOT"] ...]
```

To compile clojurescript files in src:
```
lein clojurescript
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
```

and

```
:cljs-output-dir
```

For an example usage see samples/hello/project.clj.



## Authors
   * fhd (Felix H. Dahlke)
   * rplevy (Robert Levy)
   * bartonj (Justin Barton)

## License
Copyright (C) 2011 Justin Barton
Distributed under the Eclipse Public License, the same as Clojure.

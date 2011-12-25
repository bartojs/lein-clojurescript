# hello

helloworld for clojurescript using the lein-clojurescript plugin

## Usage

* to setup:      lein deps

* to compile:

```
lein clojurescript
```

view resources/public/hello-dev.html in a browser.

* to compile in *advanced mode*:

```
lein clojurescript '{:optimizations :advanced}' ;; or specify cljs-optimizations :advanced in project.clj
```

view resources/public/hello.html in a browser.

## License
Copyright (C) 2011 justin barton
Distributed under the Eclipse Public License, the same as Clojure.

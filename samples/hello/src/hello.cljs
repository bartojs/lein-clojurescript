(ns hello)

(defn ^{:export greet} greet [n]
  (str "Hello " n))

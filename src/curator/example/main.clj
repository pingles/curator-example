(ns curator.example.main
  (:require [curator.example.system :refer (make-system)]
            [com.stuartsierra.component :refer (start)]))

;; this is for @tgk ;)
(defn wait! []
  (let [s (java.util.concurrent.Semaphore. 0)]
    (.acquire s)))

(defn -main [& args]
  (let [system (make-system (read-string (slurp "./etc/config.edn")))]
    (start system)
    (wait!)))

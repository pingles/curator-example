(ns user
  (:require [clojure.tools.logging :refer (info)]
            [curator.example.system :refer (make-system)]
            [clojure.tools.namespace.repl :refer (refresh)]
            [com.stuartsierra.component :as component]))

(def system nil)

(defn init []
  (alter-var-root #'system
                  (constantly (make-system (read-string (slurp "./etc/config.edn"))))))

(defn start []
  (info "Starting system")
  (alter-var-root #'system component/start))

(defn stop []
  (info "Stopping system")
  (alter-var-root #'system (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))

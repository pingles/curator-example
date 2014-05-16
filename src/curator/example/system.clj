(ns curator.example.system
  (:require [com.stuartsierra.component :refer (system-map Lifecycle using start stop)]
            [clojure.tools.logging :refer (info)]
            [curator.framework :refer (curator-framework)]
            [curator.leader :refer (leader-selector)]))

(defrecord LeaderTask []
  Lifecycle
  (start [this]
    (info "Starting leader task")
    this)
  (stop [this]
    (info "Stopping leader task")
    this))

(defn leader-task []
  (LeaderTask. ))

(defrecord NodeTask []
  Lifecycle
  (start [this]
    (info "Starting node task")
    this)
  (stop [this]
    (info "Stopping node task")
    this))

(defn node-task []
  (NodeTask. ))


(defn make-leader-system []
  (system-map :leader-task (leader-task)))

(defrecord LeaderSelector [zookeeper-connect]
  Lifecycle
  (start [this]
    (info "Starting leader selector. Connecting to ZooKeeper" zookeeper-connect)
    (let [curator (doto (curator-framework zookeeper-connect
                                           :namespace "curator-example")
                    (.start))
          leader-system (make-leader-system)]
      (letfn [(became-leader [_ id]
                (info "Became leader, starting leader sub-system instance" id)
                (start leader-system)
                ; sleep for now, should block fn return forever... SEMAPHORE :)
                (Thread/sleep (* 20 1000)))
              (losing-leadership [_ new-state id]
                (info "Losing leadership on" id "for connection state" new-state)
                (info "Stopping leader sub-system")
                (stop leader-system))]
        (assoc this
          :curator curator
          :selector (let [path "/curator/example/leader"]
                      (doto (leader-selector curator
                                        path
                                        became-leader
                                        :losingfn losing-leadership)
                        (.start)))
          :leader-system leader-system))))
  (stop [this]
    (info "Stopping leader selector")
    (when-let [selector (:selector this)]
      (.close selector))
    (when-let [curator (:curator this)]
      (.close curator))
    (when-let [leader-system (:leader-system this)]
      (stop leader-system))
    (dissoc this :leader-system :curator :selector)
    this))

(defn mk-leader-selector [{:keys [zookeeper-connect] :as config}]
  (map->LeaderSelector {:zookeeper-connect zookeeper-connect}))



(defn make-system [config]
  (system-map :leader-selector (mk-leader-selector config)
              :node-task       (node-task)))

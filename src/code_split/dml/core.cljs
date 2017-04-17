(ns code-split.dml.core
  (:require [promesa.core :as p]
            [code-split.dml.loader :as loader]))


;; Load all modules for specific path
;; expect owner (compassus root) and route path and dependencies map
;; expect callback (compassus setRoute)

(defn load-modules [owner path deps & c]
  (let [dependencies (get-in deps [path])]
    (let [p (p/all [(loader/load-modules owner dependencies)])]
      (p/then p (fn [])))))
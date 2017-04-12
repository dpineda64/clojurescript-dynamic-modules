(ns code-split.config.page-dispatch
  (:require [bidi.bidi :as bidi]
            [code-split.routes :as routes]))


(defn active-page-dispatch [app]
  (->
    (bidi/match-route routes/routes (get-in app [:route]))
    :handler))

(defmulti active-page active-page-dispatch)
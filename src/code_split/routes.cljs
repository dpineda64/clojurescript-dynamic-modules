(ns code-split.routes
  (:require [bidi.bidi :as bidi]
            [code-split.views.accounts :as accounts]))

(def routes-inner
  {"about" :about})

(def routes-outer
  {"" :index })

(def routes ["/" (merge-with merge routes-inner routes-outer)])

(defn inner? [path]
  (boolean (bidi/match-route ["/" routes-inner] path)))


(def load-for
  {:index ["inner" "outer"]
   :about ["outer"]})
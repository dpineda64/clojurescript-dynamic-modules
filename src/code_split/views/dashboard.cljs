(ns code-split.views.dashboard
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]))


(defui dashboard
  Object
  (render [this]
    (html
      [:div "Dashboad"])))

(def Dashboard (om/factory dashboard))

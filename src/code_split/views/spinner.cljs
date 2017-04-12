(ns code-split.views.spinner
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]))


(defui spinner
  Object
  (render [this]
    (html
      [:div :class "spinner"
       [:div :class "rect1"]
       [:div :class "rect2"]
       [:div :class "rect3"]
       [:div :class "rect4"]
       [:div :class "rect5"]])))


(def Spinner (om/factory spinner))
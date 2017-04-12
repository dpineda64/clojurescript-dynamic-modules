(ns code-split.views.home
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]))


(defui home-page
  Object
  (render [this]
    (html
      [:div "home page"])))

(def Home (om/factory home-page))

(defui spinner
  Object
  (render [this]
    (dom/div #js {:className "spinner"} nil
             (dom/div #js {:className "rect1"} nil)
             (dom/div #js {:className "rect2"} nil)
             (dom/div #js {:className "rect3"} nil)
             (dom/div #js {:className "rect4"} nil)
             (dom/div #js {:className "rect5"} nil))))


(def Spinner (om/factory spinner))
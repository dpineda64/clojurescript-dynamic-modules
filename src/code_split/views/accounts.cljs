(ns code-split.views.accounts
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]))


(defui accounts
  Object
  (render [this]
    (html
      [:div "Accounts"])))

(def Accounts (om/factory accounts))

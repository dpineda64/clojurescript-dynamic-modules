(ns code-split.render-outer
  (:require [code-split.config.page-dispatch :as page]
            [code-split.views.home :as home]
            [goog.module :as a]
            [goog.module.ModuleManager :as module-manager]
            [goog.module.ModuleLoader])
  (:import goog.module.ModuleManager))

(defmethod page/active-page :home [_]
  home/Home)

(.log js/console "hello world form outer2")

(-> goog.module.ModuleManager .getInstance (.setLoaded "outer"))
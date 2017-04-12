(ns code-split.render-inner
  (:require [code-split.config.page-dispatch :as page]
            [code-split.views.dashboard :as dashboard]
            [goog.module :as a]
            [goog.module.ModuleManager :as module-manager]
            [goog.module.ModuleLoader])
  (:import goog.module.ModuleManager))


(defmethod page/active-page :dashboard [_]
  dashboard/Dashboard)

(.log js/console "hello world form inner")


(-> goog.module.ModuleManager .getInstance (.setLoaded "inner"))
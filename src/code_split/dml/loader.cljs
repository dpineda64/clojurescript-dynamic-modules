(ns code-split.dml.loader
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as a]
            [om.next :as om]
            [cljs.core :as c]
            [cljs.core.async :as async]
            [goog.module.ModuleManager :as module-manager]
            [promesa.core :as p]
            [goog.module.ModuleLoader]
            [goog.module.AbstractModuleLoader])
  (:import goog.module.ModuleManager
           goog.module.AbstractModuleLoader))


(def manager (module-manager/getInstance))
(def loader (goog.module.ModuleLoader.))

;; Initialize loader
(defn initialize [modules-map  modules-info]
  (.setLoader manager loader)
  (.setAllModuleInfo manager modules-info)
  (.setModuleUris manager modules-map))

;; Load modules
;; @params {Modules|<array> ModuleInfo owner}
(defn- load [modules mapInfo owner]
  (let [chan (a/chan)]
    (.loadModules loader modules mapInfo (fn []
                                           (om/react-set-state! owner {:loaded true})
                                           (a/close! chan)))))


;; Get owner and modules and convert to js array and extract
;; the infoMap

(defn load-modules [owner modules]
  (when (and (not (om/get-state owner [:loaded])))
    (load (clj->js modules) (-> manager (.-moduleInfoMap_)) owner)))




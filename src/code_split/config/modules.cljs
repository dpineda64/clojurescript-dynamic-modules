(ns code-split.config.modules
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as a]
            [om.next :as om]
            [cljs.core :as c]
            [cljs.core.async :as async]
            [goog.module.ModuleManager :as module-manager]
            [promesa.core :as p]
            [goog.module.ModuleLoader]
            [goog.module.AbstractModuleLoader])
  (:use [cljs.pprint :only [pprint]])
  (:import goog.module.ModuleManager
           goog.module.AbstractModuleLoader))


(def production? false)

(def modules
  ;; "map of id -> urls"
  #js {"inner" "/js/compiled/out/code_split/render_inner.js"
       "outer" "/js/compiled/out/code_split/render_outer.js"})

(def module-info
  #js {"inner" []
       "outer" []})


(def module-data
  #js ["inner","outer"])


(def manager (module-manager/getInstance))
(def loader (goog.module.ModuleLoader.))
(.setLoader manager loader)
(.setAllModuleInfo manager module-info)
(.setModuleUris manager modules)


(defn loaded? [id]
  (let [mo (.getModuleInfo manager id)]
    (.log js/console manager))
  (if-let [module (.getModuleInfo manager id)]
    (.isLoaded module)
    false))

(defn require-module
  "Loads module from the network if necessary. Always returns a
  channel that will be closed when the module is loaded (sometimes
  immediately)"
  [id]
  (let [chan (a/chan)]
    (.execOnLoad manager id (fn []
                              (a/close! chan)))
    chan))

(defn load-m [m info owner]
  (.log js/console m)
  (let [chan (a/chan)]
    (.loadModules loader m info (fn []
                                  (om/react-set-state! owner {:loaded true})
                                  (a/close! chan))
                  (fn [error]
                    (.log js/console "error "error)))))

(defn load-module-production [owner module]
  (.log js/console (om/get-state owner [:loaded module]))
  (.log js/console (not (om/get-state owner [:loading module])))
  (when (and
          (not (om/get-state owner [:loading module])))
    ;; actually load the module
    (let [loading (require-module module)]
      (om/react-set-state! owner {:loaded {module false}} #(.log js/console false))
      (om/react-set-state! owner {:loading {module loading}} #(.log js/console loading))
      (go
        (a/<! loading)
        (p/promise (fn [resolve reject]
                     (om/react-set-state! owner {:loaded {module true}} #(.log js/console true))
                     (resolve)))))))



(defn load-multiple [owner modules]
 (when (and (not (om/get-state owner [:loading true])))
   (let [loading (load-m (clj->js modules) (-> manager (.-moduleInfoMap_)) owner)]
     (om/react-set-state! owner {:loaded {modules false}} #())
     (om/react-set-state! owner {:loading {modules loading}} #()))))

(defn is-last [data id]
  (let [last-item (last data)]
    (if (= id last-item)
      true
      false)))

(defn load-module-map [owner modules]
  (load-multiple owner modules))



(defn load-multiples [modules]
  (let [active (:loader manager)]
    (.log js/console active)))

(defn test-future [owner modules]
  ;;  (let [map=])
  )


(defn load-module-dev [owner module]
  ;; in dev mode, everything gets loaded, so just wait until the
  ;; setLoaded call
  )
(ns code-split.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as gdom]
            [cljs.core.async :as a]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [promesa.core :as p]
            [bidi.bidi :as bidi]
            [code-split.routes :as r :refer [routes]]
            [code-split.views.home :as home]
            [code-split.views.dashboard :as dashboard]
            [sablono.core :as html :refer-macros [html]]
            [code-split.config.page-dispatch :as page  :refer [active-page]]
    ;; [code-split.config.modules :as modules :refer [load-module-production load-module-dev]]
            [compassus.core :as compassus]
            [pushy.core :as pushy]
            [code-split.dml.core :as dml]
            [code-split.dml.loader :as loader])
  (:use [cljs.pprint :only [pprint]]))

(enable-console-print!)
(defonce app-state (atom {:text "Hello world2!"}))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state query]} k _]
  {:value (get @state k)})

(declare app)

(defn update-route!
  [{:keys [handler] :as route}]
  (let [current-route (compassus/current-route app)]
    (when (not= handler current-route)
      (compassus/set-route! app handler))))

(def history
  (pushy/pushy update-route!
               (partial bidi/match-route routes)))

(defn- change-route [c route e]
  (.preventDefault e)
  (dml/load-modules c route r/load-for (compassus/set-route! c route)))


(defui render
  Object
  (componentWillMount [this]
    (let [path (compassus/current-route this)]
      (let [{:keys [owner]} (om/props this)]
        (om/set-state! owner {:loading {} :loaded false}))))
  (render [this]
    (let [{:keys [owner factory props]} (om/props this)]
      (let [route (compassus/current-route owner)
            module (get-in r/load-for [route])
            m (js->clj (om/get-state owner [:loaded]))]
        (dom/div nil
                 (dom/a #js {:href "#"
                             :style (when (= route :app/about)
                                      #js {:color "black"
                                           :cursor "text"})
                             :onClick #(change-route owner :index %)}
                        "Home")
                 (dom/a #js {:href "#"
                             :style (when (= route :app/about)
                                      #js {:color "black"
                                           :cursor "text"})
                             :onClick #(change-route owner :about %)}
                        "About")
                 (if (om/get-state owner [:loaded])
                   (factory props)
                   (home/Spinner))))))
  )



(def app
  (compassus/application
    {:routes  {:index home/Home
               :about dashboard/Dashboard}
     :index-route :index
     :reconciler (om/reconciler
                   {:state (atom { :loading {}})
                    :parser (compassus/parser {
                                               :read read
                                               })})
     :mixins [(compassus/wrap-render render)
              (compassus/will-mount (fn [_]
                                      (dml/load-modules _ (compassus/current-route _) r/load-for)))
              (compassus/did-mount (fn [_]
                                     (pushy/start! history)))
              (compassus/will-unmount (fn [_]
                                        (pushy/stop! history)))]}))

(def modules
  ;; "map of id -> urls"
  #js {"inner" "/js/compiled/out/code_split/render_inner.js"
       "outer" "/js/compiled/out/code_split/render_outer.js"})

(def module-info
  #js {"inner" []
       "outer" []})
(loader/initialize modules module-info)
(compassus/mount! app (js/document.getElementById "app"))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

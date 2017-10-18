(ns the-great-face-off.ring
  (:use [ring.middleware defaults params multipart-params keyword-params nested-params json])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as http-server]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [the-great-face-off.ring-routes :refer [app-routes]]))

(defn -main []
  ;; Purists tell you to not do this.  :-)
  (def server-stop-fn
    "The callback allowing us to shut this thing off, if need be."
    (http-server/run-server #'app-routes {:port 9001})))

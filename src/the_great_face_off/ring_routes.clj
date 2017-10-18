(ns the-great-face-off.ring-routes
  (:require
   [clojure.pprint :refer [cl-format]]
   [hiccup.core :refer [html]]
   [ring.mock.request]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [compojure.core :refer [GET POST ANY defroutes routes]]
   [compojure.route :as route :refer [not-found resources files]]
   [ring.util.response :refer [response header]]))


(defroutes app-routes                   ; This makes one route out of many
  (GET "/" [] (html [:h1 "Hello, Clojure-Syd Hacker!"]))
  (route/not-found "For there is nothing lost, that may be found, if sought."))

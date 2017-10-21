(ns the-great-face-off.service
  (:require
    [clojure.pprint :refer [pprint]]
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]
    [io.pedestal.http.body-params :as body-params]
    [ring.util.response :as ring-resp]
    [hiccup.core :refer [html]]
    [io.pedestal.interceptor :as interceptor]
    [org.httpkit.client :as http-client]
    [cheshire.core :as json]))

(comment
  @(http-client/request {:url "http://localhost:8080/db/"})
  @(http-client/request {:url     "http://localhost:8080/db"
                         :method  :put
                         :headers {"Content-Type" "application/json"}
                         :body    (json/encode {"foo" "some value"
                                                "bar" "some other val"})}))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn db-list-enter
  [context]
  (assoc context :response
                 (ring-resp/response
                   (html
                     (let [db @(::db context)]
                       [:div
                        [:h1 {} "Database List"]
                        (for [k (keys db)]
                          [:div {}
                           [:a {:href (route/url-for :db/record :params {:id k})}
                            (get db k)]
                           [:form {:action (route/url-for :db/delete :params {:id      k
                                                                              :_method "delete"})
                                   :method "POST"}
                            [:button {:type "submit"} "Delete"]]])])))))

(defn db-record-enter
  [context]
  (assoc context :response
                 (let [db @(::db context)
                       id (get-in context [:request :path-params :id])]
                   (if-let [v (get db id)]
                     (ring-resp/response
                       (html
                         [:div
                          [:h1 {} (get db id)]]))
                     (ring-resp/not-found "No record found")))))

(defn db-delete-enter
  [context]
  (swap! (::db context)
         dissoc (get-in context [:request :path-params :id]))
  (assoc context :response
                 (ring-resp/redirect
                   (route/url-for :db/list)
                   :moved-permanently)))

(defn db-reset
  [context]
  (reset! (::db context)
          (reduce-kv
            (fn [m k v]
              (assoc m (name k) v))
            {}
            (get-in context [:request :json-params])))
  (assoc context :response
                 (ring-resp/redirect
                   (route/url-for :db/list)
                   :moved-permanently)))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defonce database (atom {"foo" "some value 1"
                         "bar" "another value 2"}))

(defn database-interceptor
  [db]
  (interceptor/interceptor
    {:name  ::load-database
     :enter (fn [context]
              (assoc context ::db db))}))

;; Terse/Vector-based routes
(def routes
  `[[["/" {:get home-page}
      ^:interceptors [(body-params/body-params)
                      http/html-body]
      ["/about" {:get about-page}]
      ["/db"
       ^:interceptors [(database-interceptor database)]
       {:put [:db/reset (interceptor/interceptor
                          {:name  ::db-reset
                           :enter db-reset})]}
       ["/"
        {:get [:db/list (interceptor/interceptor
                          {:name  ::db-list
                           :enter db-list-enter})]}]
       ["/:id"
        {:get    [:db/record
                  (interceptor/interceptor
                    {:name  ::db-record
                     :enter db-record-enter})]
         :delete [:db/delete
                  (interceptor/interceptor
                    {:name  ::db-delete
                     :enter db-delete-enter})]}]]]]])


;; Consumed by the-great-face-off.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env                     :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes            routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Tune the Secure Headers
              ;; and specifically the Content Security Policy appropriate to your service/application
              ;; For more information, see: https://content-security-policy.com/
              ;;   See also: https://github.com/pedestal/pedestal/issues/499
              ;;::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
              ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
              ;;                                                          :frame-ancestors "'none'"}}

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path     "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
              ::http/type              :jetty
              ;;::http/host "localhost"
              ::http/port              8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2?  false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})


(ns meson.http.status-code
  "")

;;; Status code aliases

;; 2xx
(def ok 200)
(def pending 202)

;; 3xx
(def pending-link 307)
(def permanant-link 308)

;; 4xx
(def bad-request 400)
(def client-error 400)
(def unauthorized 401)
(def forbidden 403)
(def not-found 404)
(def no-resource 404)
(def auth-timeout 419)
(def token-invalid 498)
(def token-required 499)

;; 5xx
(def server-error 500)
(def bad-gateway 502)

;;; Status code predicates

;; 2xx
(defn ok? [status] (= status ok))
(defn pending? [status] (= status pending))

;; 3xx
(defn pending-link? [status] (= status pending-link))
(defn permanant-link? [status] (= status permanant-link))

;; 4xx
(defn bad-request? [status] (= status bad-request))
(defn client-error? [status] (= status client-error))
(defn unauthorized? [status] (= status unauthorized))
(defn forbidden? [status] (= status forbidden))
(defn not-found? [status] (= status not-found))
(defn no-resource? [status] (= status no-resource))
(defn auth-timeout? [status] (= status auth-timeout))
(defn token-invalid? [status] (= status token-invalid))
(defn token-required? [status] (= status token-required))

;; 5xx
(defn server-error? [status] (= status server-error))

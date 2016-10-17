(ns meson.error
  (:require [clojure.tools.logging :as log]
            [dire.core :refer [with-handler!]]))

(defn error [type msg]
  (ex-info (str type) {:msg msg :type type}))

(defn mesos-client-error [msg]
  (error 'Mesos-Client-Error msg))

(defn meson-error [msg]
  (error 'Meson-Error msg))

(defn no-master-connection [msg]
  (error 'No-Maste-Connection-Error msg))

(defn get-message [exception]
  (if (map? exception)
    exception
    (.getMessage exception)))

(defn process-error [error-data exception err-func]
  (let [error-msg (get-message exception)
        reason (:body error-msg)
        error (err-func reason)]
    (log/error error)
    (assoc error-data :msg error-msg
                      :reason-phrase (:reason-phrase error-msg)
                      :status (:status error-msg)
                      :error error
                      :exception exception)))

(defn add-handler
  "The error handler needs to return a response, since the response headers
  need to be updated with the problem HTTP content type. This does mean,
  though, that to support a symmetry-of-results between a function that
  returns successfully (e.g., login) and the handler that is called in the
  event of the function raising an exception, each must return the same
  data type. In other words, (login ...) cannot be wrapped in a response,
  since -- when it fails -- its error handler will return one. This would
  result in a regular response upon success and a nested (improper) response
  upon failure."
  [func ex err-func]
  (with-handler!
    func
    ex
    (fn [e & args]
      (-> {:args args}
          (process-error e err-func)))))

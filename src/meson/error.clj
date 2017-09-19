(ns meson.error
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :refer [pprint]]
            [dire.core :refer [with-handler!]]))

(defn error [type summary details]
  (ex-info (str type) {:meson-error-type type
                       :meson-error-summary summary
                       :meson-error-details details}))

(defn mesos-client-error [summary ex]
  (error 'Mesos-Client-Error summary ex))

(defn mesos-http-client-error [summary ex]
  (error 'Mesos-HTTPClient-Error summary ex))

(defn meson-error [summary ex]
  (error 'Meson-Error summary ex))

(defn meson-scheduler-error [summary ex]
  (error 'Meson-Scheduler-Error summary ex))

(defn no-master-connection [summary ex]
  (error 'No-Master-Connection-Error summary ex))

(defn bad-parse-value [summary ex]
  (error 'Unexepected-Nil-Value summary ex))

(defn null-pointer [summary ex]
  (error 'Unexepected-Nil-Value summary ex))

(defn end-of-stream [summary ex]
  (error 'End-Of-Stream summary ex))

(defn get-message [exception]
  (if (map? exception)
    exception
    (if (= (type exception) java.lang.Exception)
      (.getMessage exception)
      (format "No message; got unexpected type (%s)" (type exception)))))

(defn process-http-client-exception [error-criteria response err-func func args]
  (let [summary (:body response)
        details {:calling-function func
                 :passed-args args
                 :error-criteria error-criteria
                 :response response}
        error (err-func summary details)]
    (log/error summary)
    (log/debug "Error data:\n" (pprint (.getData error)))
    (log/debug "Full exception: "(pprint error))))

(defn process-exception [error-criteria exception err-func func args]
  (let [summary (get-message exception)
        details {:calling-function func
                 :passed-args args
                 :error-criteria error-criteria
                 :exception exception}
        error (err-func summary details)]
    (log/error summary)
    (log/debug "Error data:\n" (pprint (.getData error)))
    (log/debug "Full exception: "(pprint error))))

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
  [func error-criteria err-proc-func err-type-func]
  (with-handler!
    func
    error-criteria
    (fn [e & args]
      (err-proc-func error-criteria e err-type-func func args))))

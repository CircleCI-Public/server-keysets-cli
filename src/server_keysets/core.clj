(ns server-keysets.core
  (:require [clojure.stacktrace :refer (print-stack-trace)]
            [clojure.string :as string]
            [clj-keyczar.keyset :as keyset])
  (:gen-class))

(defn- generate
  [keyset-name purpose]
  (-> (keyset/create purpose)
      (keyset/addkey)
      (assoc-in [:meta "name"] keyset-name)))

(def ^:private program-help
  (string/join \newline
               ["Generate encryption/signing keysets for CircleCI Server."
                ""
                "Usage: program-name command <command-arguments>"
                ""
                "Commands:"
                "  generate   generate a new keyset."
                "     Arguments:"
                "       * encryption - generate a keyset for encryption."
                "       * signing - generate a keyset for signing/signature verification."
                ""
                "     Examples:"
                "       * program-name generate encryption"
                "       * program-name generate signing"
                ""]))

(defn- handle-generate
  [args]
  (let [keyset-type (first args)]
    (case keyset-type
      "encryption" (try
                     {:ok? true
                      :output (pr-str (generate "encryption" :crypt))}
                     (catch Exception e
                       {:ok? false
                        :output (with-out-str (print-stack-trace e))}))
      "signing" (try
                  {:ok? true
                   :output (pr-str (generate "signing" :sign))}
                  (catch Exception e
                    {:ok? false
                     :output (with-out-str (print-stack-trace e))}))
      {:ok? false
       :output program-help})))

(defn- exit
  [status message]
  (binding [*out* *err*]
    (println message))
  (System/exit status))

(defn -main
  [& args]
  (let [action (first args)
        command-args (rest args)
        {:keys [ok? output]} (case action
                               "generate" (handle-generate command-args)
                               {:ok? false
                                :output program-help})]
    (if (not ok?)
      (exit 1 output)
      (println output))))

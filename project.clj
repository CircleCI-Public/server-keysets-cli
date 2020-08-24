(defproject server-keysets (if-let [build-num (System/getenv "CIRCLE_BUILD_NUM")]
                             (str "0.1." build-num)
                             "0.1.0-SNAPSHOT")
  :description "CLI wrapper for circleci/clj-keyczar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [circleci/clj-keyczar "0.1.3"]]
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow"]
  :main server-keysets.core
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-shell "0.5.0"]]}}
  :repl-options {:init-ns server-keysets.core}
  :global-vars {*warn-on-reflection* true}
  :aliases {"native" ["shell"
                      "./bin/compile"
                      "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
                      "./target/${:name}"]
            "generate-assisted-configuration" ["shell"
                                               "./bin/generate-assisted-configuration"
                                               "target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"]})

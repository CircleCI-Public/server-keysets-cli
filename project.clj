(defproject server-keysets (if-let [build-num (System/getenv "CIRCLE_BUILD_NUM")]
                             (str "0.1." build-num)
                             "0.1.0-SNAPSHOT")
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [circleci/clj-keyczar "0.1.3"]]
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow"]
  :main server-keysets.core
  :profiles {:uberjar {:aot :all
                       #_#_:jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
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

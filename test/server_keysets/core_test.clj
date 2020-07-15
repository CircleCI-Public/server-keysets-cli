(ns server-keysets.core-test
  (:require [clojure.stacktrace :refer (print-stack-trace)]
            [clojure.test :refer (deftest testing is)]
            [server-keysets.core :as keysets]))

(deftest can-generate-keysets
  (testing "encryption"
    (let [k (#'keysets/generate "my-encryption-keyset" :crypt)]
      (is (= "my-encryption-keyset"
             (get-in k [:meta "name"])))
      (is (= "DECRYPT_AND_ENCRYPT"
             (get-in k [:meta "purpose"])))
      (is (= 1 (count (:keys k))))

      (testing "generates different keysets"
        (let [new-k (#'keysets/generate "my-encryption-keyset" :crypt)]
          (is (= (:meta k) (:meta new-k)))
          (is (not= (:keys k) (:keys new-k)))))))
  (testing "signing"
    (let [k (#'keysets/generate "my-signing-keyset" :sign)]
      (is (= "my-signing-keyset"
             (get-in k [:meta "name"])))
      (is (= "SIGN_AND_VERIFY"
             (get-in k [:meta "purpose"])))
      (is (= 1 (count (:keys k))))

      (testing "generates different keysets"
        (let [new-k (#'keysets/generate "my-signing-keyset" :sign)]
          (is (= (:meta k) (:meta new-k)))
          (is (not= (:keys k) (:keys new-k))))))))

(deftest handle-generate
  (testing "successful"
    (with-redefs-fn {#'keysets/generate (fn [_n _p]
                                          {:a "keyset"})}
      #(is (= {:ok? true
               :output "{:a \"keyset\"}"}
              (#'keysets/handle-generate ["encryption"])
              (#'keysets/handle-generate ["signing"])))))
  (testing "bad argument"
    (is (= {:ok? false
            :output @#'keysets/program-help}
           (#'keysets/handle-generate ["bad-command"]))))
  (testing "generation throws"
    (let [test-exc (ex-info "divaricate" {})]
      (with-redefs-fn {#'keysets/generate (fn [_n _p]
                                            (throw test-exc))}
        #(let [expected-output (with-out-str (print-stack-trace test-exc))]
           (is (= "divaricate" (ex-message test-exc)))
           (is (= {:ok? false
                   :output expected-output}
                  (#'keysets/handle-generate ["encryption"])
                  (#'keysets/handle-generate ["signing"]))))))))

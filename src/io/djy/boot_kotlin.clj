(ns io.djy.boot-kotlin
  {:boot/export-tasks true}
  (:require [boot.core :as core]
            [boot.pod  :as pod]
            [boot.util :as util]))

(defn kotlin-deps
  [version]
  [['org.jetbrains.kotlin/kotlin-compiler version]
   ['org.jetbrains.kotlin/kotlin-runtime  version]
   ['org.jetbrains.kotlin/kotlin-reflect  version]])

(defn compile-env
  [version]
  {:dependencies (kotlin-deps version)})

(defn compile-pod
  [version]
  (pod/make-pod (merge-with into (core/get-env) (compile-env version))))

(core/deftask kotlin-repl
  "Start a Kotlin REPL session."
  [v version VERSION str "The desired version of Kotlin. (default: 1.1.1)"]
  (let [version (or version "1.1.1")
        pod     (future (compile-pod version))]
    (core/merge-env! :dependencies (kotlin-deps version))
    (core/with-pass-thru fileset
      (pod/with-eval-in @pod
        (org.jetbrains.kotlin.cli.jvm.K2JVMCompiler/main
          (into-array String ["-no-jdk"
                              "-no-stdlib"
                              "-nowarn"
                              "-Xskip-runtime-version-check"]))))))

(core/deftask kotlinc
  "Compile Kotlin source files into Java class files."
  [v version VERSION str "The desired version of Kotlin. (default: 1.1.1)"]
  (let [out      (core/tmp-dir!)
        out-path (.getPath out)
        version  (or version "1.1.1")
        pod      (future (compile-pod version))]
    (core/merge-env! :dependencies (kotlin-deps version))
    (fn [next-handler]
      (fn [fileset]
        (let [kotlin-files       (->> fileset
                                      core/input-files
                                      (core/by-ext [".kt"]))
              kotlin-files-paths (->> kotlin-files
                                      (map #(.getPath (core/tmp-file %)))
                                      vec)]
          (util/info "Compiling %d Kotlin source files...\n" (count kotlin-files))
          (when (util/without-exiting
                  (try
                    (pod/with-eval-in @pod
                      (org.jetbrains.kotlin.cli.jvm.K2JVMCompiler/main
                        (into-array String (->> ["-cp" (System/getProperty "fake.class.path")
                                                 "-d" ~out-path
                                                 "-no-jdk"
                                                 "-no-stdlib"
                                                 "-nowarn"
                                                 "-Xskip-runtime-version-check"
                                                 ~kotlin-files-paths]
                                                flatten
                                                vec))))
                    ;; workaround for a bug in the kotlin compiler
                    ;; fixed by this unreleased commit:
                    ;; https://github.com/JetBrains/kotlin/commit/f38753ee3c575a75679718d09a6a4026044aa47b
                    (catch IllegalStateException e
                      (when-not (= "Already shutdown" (.getMessage e))
                        (throw e))))
                  true)
            (-> fileset
                (core/add-resource out)
                (core/rm kotlin-files)
                core/commit!
                next-handler)))))))

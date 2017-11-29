(set-env!
  :source-paths #{"src"}
  :dependencies '[[adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def ^:const +version+ "0.3.0")

(bootlaces! +version+)

(task-options!
  pom {:project 'io.djy/boot-kotlin
       :version +version+
       :description "Boot tasks for Kotlin development"
       :url "https://github.com/daveyarwood/boot-kotlin"
       :scm {:url "https://github.com/daveyarwood/boot-kotlin"}
       :license {"name" "Eclipse Public License"
                 "url"  "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask deploy
  "Builds uberjar, installs it to local Maven repo, and deploys it to Clojars."
  []
  (comp (build-jar) (push-release)))

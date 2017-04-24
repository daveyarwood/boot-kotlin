(set-env!
  :source-paths #{"src"}
  :dependencies '[[io.djy/boot-kotlin       "0.1.0" :scope "test"]
                  [alandipert/boot-trinkets "2.0.0" :scope "test"]])

(require '[io.djy.boot-kotlin       :refer (kotlinc)]
         '[alandipert.boot-trinkets :refer (run)])

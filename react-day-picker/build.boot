(def version-number "1.1.5")
(def npm-version (str "v" version-number))
(def revision 0)
(def +version+ (str version-number "-" revision))

(set-env!
  :resource-paths #{"resources"}
  :dependencies [['adzerk/bootlaces   "0.1.9" :scope "test"]
                 ['cljsjs/boot-cljsjs "0.5.0" :scope "test"]
                 ['cljsjs/react       "0.14.3-0" :scope "provided"]])

(require '[adzerk.bootlaces :refer :all]
         '[cljsjs.boot-cljsjs.packaging :refer :all])

(bootlaces! +version+)

(task-options!
 pom  {:project     'cljsjs/react-day-picker
       :version     +version+
       :description "A Select control built with and for React JS"
       :url         "http://jedwatson.github.io/react-day-picker"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package  []
  (task-options! push {:ensure-branch nil :tag false})
  (comp
    (download :url (str "https://raw.githubusercontent.com/gpbl/react-day-picker/" npm-version "/dist/DayPicker.js")
              :checksum "A7F8F2251ACF74D2D8129C755F28AB42")
    (download :url (str "https://raw.githubusercontent.com/gpbl/react-day-picker/" npm-version "/src/style.css")
              :checksum "61DBCCFA489CDD52090BCE7900800F28")
    (sift :move {#"^DayPicker.js$"
                 "cljsjs/react-day-picker/development/react-day-picker.inc.js"

                 #"^style.css$"
                 "cljsjs/react-day-picker/common/react-day-picker.inc.css"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.react-day-picker" :requires ["cljsjs.react"])))

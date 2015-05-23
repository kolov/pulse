(defproject pulse "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [com.novemberain/monger "1.4.2"]
                           [com.novemberain/monger "1.4.2"]
                           [jota "0.4.0"]
                           ]
            :profiles {
                       :dev {:dependencies   [[midje "1.6.3"]
                                              [midje-junit-formatter "0.1.0-SNAPSHOT"]]
                             :resource-paths ["profiles/dev"]
                             :plugins        [[lein-deps-tree "0.1.2"]
                                              [lein-midje "3.0.0"]
                                              [test2junit "1.0.1"]
                                              ]
                             }}
            )

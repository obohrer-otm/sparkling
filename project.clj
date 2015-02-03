(defproject gorillalabs/sparkling "1.0.1-SNAPSHOT"
            :description "A Clojure Library for Apache Spark"
            :url "https://github.com/chrisbetz/sparkling"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :scm {:name "git"
                  :url  "https://github.com/gorillalabs/sparkling"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [org.clojure/tools.logging "0.3.1"]
                           [com.twitter/carbonite "1.4.0"
                            :exclusions [com.twitter/chill-java]]
                           [com.twitter/chill_2.10 "0.5.0"
                            :exclusions [org.scala-lang/scala-library]]

                           ;; [AVRO Feature] This adds support for reading avro files
                           [com.damballa/parkour "0.6.0" :exclusions [org.codehaus.jackson/jackson-mapper-asl org.codehaus.jackson/jackson-core-asl]]
                           [com.damballa/abracad "0.4.11" :exclusions [org.apache.avro/avro]]
                           ;; [/AVRO Feature]
                           ]

            :aliases {"all" ["with-profile" "dev,spark-1.1.0:dev,spark-1.2.0"]
                      }
            :profiles {:dev         {:dependencies   [[midje "1.6.3" :exclusions [joda-time commons-codec]]
                                                      [commons-codec "1.9"]
                                                      [criterium "0.4.3"]]
                                     :plugins        [[lein-marginalia "0.8.0"]
                                                      [lein-ancient "0.5.4"]
                                                      [codox "0.8.9"]
                                                      [lein-release "1.0.5"]
                                                      [lein-pprint "1.1.1"]]
                                     :resource-paths ["data"]
                                     ;; so gen-class stuff works in the repl
                                     :aot            [sparkling.api
                                                      sparkling.function
                                                      sparkling.scalaInterop]}
                       :jenkins     {:plugins [[lein-test-out "0.3.1"]]
                                     }
                       :default [:base :system :user :spark-1.2.0 :provided :dev]
                       :spark-1.1.0 {:dependencies
                                     [[org.apache.spark/spark-core_2.10 "1.1.0"  :exclusions [commons-io com.thoughtworks.paranamer/paranamer]]
                                      #_[org.apache.spark/spark-streaming_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer com.fasterxml.jackson.core/jackson-databind]]
                                      #_[org.apache.spark/spark-streaming-kafka_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer com.fasterxml.jackson.core/jackson-databind]]
                                      #_[org.apache.spark/spark-sql_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer org.scala-lang/scala-compiler com.fasterxml.jackson.core/jackson-databind]]

                                      [org.apache.hadoop/hadoop-client "2.2.0" :exclusions [commons-net]]
                                      [org.apache.hadoop/hadoop-hdfs "2.2.0"]

                                      ;; [AVRO Feature] This adds support for reading avro files
                                      [org.apache.avro/avro "1.7.6" :exclusions [org.codehaus.jackson/jackson-mapper-asl org.codehaus.jackson/jackson-core-asl]]
                                      [org.apache.avro/avro-mapred "1.7.6" :classifier "hadoop2" :exclusions [io.netty/netty commons-lang com.thoughtworks.paranamer/paranamer org.slf4j/slf4j-log4j12 org.mortbay.jetty/servlet-api org.codehaus.jackson/jackson-mapper-asl org.codehaus.jackson/jackson-core-asl]]
                                      ;; [/AVRO Feature]

                                      ]}

                       :spark-1.2.0 {:dependencies
                                     [[org.apache.spark/spark-core_2.10 "1.2.0" :exclusions [commons-net commons-io  com.thoughtworks.paranamer/paranamer]]
                                      #_[org.apache.spark/spark-streaming_2.10 "1.2.0" :exclusions [com.thoughtworks.paranamer/paranamer com.fasterxml.jackson.core/jackson-databind]]
                                      #_[org.apache.spark/spark-streaming-kafka_2.10 "1.2.0" :exclusions [com.thoughtworks.paranamer/paranamer com.fasterxml.jackson.core/jackson-databind]]
                                      #_[org.apache.spark/spark-sql_2.10 "1.2.0" :exclusions [com.thoughtworks.paranamer/paranamer org.scala-lang/scala-compiler com.fasterxml.jackson.core/jackson-databind]]

                                      ;; [AVRO Feature] This adds support for reading avro files
                                      [org.apache.avro/avro "1.7.6" :exclusions [org.codehaus.jackson/jackson-mapper-asl]]
                                      [org.apache.avro/avro-mapred "1.7.6" :classifier "hadoop2" :exclusions [io.netty/netty commons-lang com.thoughtworks.paranamer/paranamer org.slf4j/slf4j-log4j12 org.mortbay.jetty/servlet-api org.codehaus.jackson/jackson-mapper-asl]]
                                      ;; [/AVRO Feature]

                                      ]
                                     :checksum :warn ;https://issues.apache.org/jira/browse/SPARK-5308
                                     }


                       :provided    {:dependencies
                                     [
                                      ]}
                       :test        {:resource-paths ["dev-resources" "data"]
                                     :aot            [sparkling.api
                                                      sparkling.function
                                                      sparkling.scalaInterop
                                                      sparkling.destructuring
                                                      sparkling.debug
                                                      sparkling.rdd.hadoopAvro
                                                      sparkling.rdd.jdbc
                                                      sparkling.api-test
                                                      sparkling.function-test
                                                      sparkling.conf-test
                                                      sparkling.rdd.hadoopAvro-test
                                                      sparkling.rdd.jdbc-test
                                                      ]}
                       :uberjar     {:aot :all}
                       }
            :source-paths ["src/clojure"]
            :java-source-paths ["src/java"]
            :codox {:defaults                  {:doc/format :markdown}
                    :include                   [sparkling.api sparkling.conf sparkling.kryo sparkling.broadcast sparkling.debug sparkling.destructuring]
                    :output-dir                "doc"
                    :src-dir-uri               "https://raw.githubusercontent.com/gorillalabs/sparkling/1.0.0/"
                    :src-linenum-anchor-prefix "L"}
            :javac-options ["-Xlint:unchecked" "-source" "1.6" "-target" "1.6"]
            :jvm-opts ^:replace ["-server" "-Xmx1g"]
            :global-vars {*warn-on-reflection* false}
            :lein-release {:deploy-via :clojars}
            )

;; test with
;;     lein do clean, with-profile +spark-1.1.0 test



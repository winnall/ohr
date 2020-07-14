#! /bin/bash

JAVA_HOME=`/usr/libexec/java_home -v 11`

OH_DIR=`dirname $0`

OH_EXEC="$OH_DIR/target/OpenHAB-Report-1.0.0-SNAPSHOT-jar-with-dependencies.jar"

java -jar $OH_EXEC $1 $2 $3 $4 $5 $6 $7 $8 $9

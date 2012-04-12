#!/bin/bash

LIB_DIR="`dirname $0`/dist/lib"
OPTIONS="-Xms512m -Xmx2048m"
CP=

if [ ! -d ${LIB_DIR} ]; then
    echo "Please run \`ant dist\` then try again."
    exit
fi

#java ${OPTIONS} -agentlib:hprof=cpu=samples,interval=1 -jar ${LIB_DIR}/mallard.jar ${@:1}
java ${OPTIONS} -jar ${LIB_DIR}/mallard.jar ${@:1}

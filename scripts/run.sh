#!/bin/bash
BASEDIR=$(dirname "$0")
echo "$BASEDIR"
classpath=''
for i in `find ${BASEDIR}/../lib/*`
do
    echo $i;
    classpath="$classpath:$i"
done
echo $classpath
export CLASSPATH="${classpath}:${BASEDIR}/../target/classes"
echo $CLASSPATH
java  org.bahmni.custom.common.App
exit 0
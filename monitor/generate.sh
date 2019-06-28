#!/bin/sh
# This is generator script.
# Compiles and Builds project.
# Creates output directory.
# Writs log files into the directory.


# Check if the command line is empty or not
if [ ! -z "$1" ]&& [ "$1" != " " ];then

    echo "Building..."
    ./gradlew clean build

    if [ ! -z "$2" ]&& [ "$2" != " " ];then
        java -jar ./build/libs/monitor-1.0-SNAPSHOT.jar "GENERATE" $1 $2
    else
        java -jar ./build/libs/monitor-1.0-SNAPSHOT.jar "GENERATE" $1
    fi
else
    echo "Please Enter Valid Data path.Example. ./generate.sh <data_path> <optional_date>"
fi

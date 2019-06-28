#!/bin/sh
# This is query script.
# Compiles and Builds project.
# Reads files and creates cache
# Run query on cached data


# Check if the command line is empty or not
if [ ! -z "$1" ]&& [ "$1" != " " ];then

    echo "Building..."
    ./gradlew clean build

    java -jar ./build/libs/monitor-1.0-SNAPSHOT.jar "QUERY" $1

else
    echo "Please Enter Valid Data path.Example. ./query.sh <data_path>"
fi

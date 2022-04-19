#!/bin/sh

LOCATION='https://dlcdn.apache.org//commons/cli/binaries/commons-cli-1.5.0-bin.tar.gz'

rm -v commons-cli-1.5.0*.jar
# wget -c "${LOCATION}"
# tar --wildcards -xzvf commons-cli-1.5.0-bin.tar.gz "commons-cli-1.5.0/commons-cli-1.5.0.jar"
# rm "commons-cli-1.5.0-bin.tar.gz"

wget -O - "${LOCATION}" | tar --wildcards -xzv "commons-cli-1.5.0/commons-cli-1.5.0.jar"

mkdir lib
mv -iv commons-cli-1.5.0/*.jar lib
rmdir commons-cli-1.5.0

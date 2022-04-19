#!/bin/sh

JAVABASE=/usr/lib/jvm/graalvm-ce-java17
JAVAC=$JAVABASE/bin/javac
JAVA=$JAVABASE/bin/java
NATIVEIMAGE=$JAVABASE/bin/native-image
CLASSPATH=commons-cli-1.5.0.jar:entities:handlers:.

find entities handlers -name "*.java" -exec $JAVAC "{}" \;
$JAVAC -Xlint:deprecation -cp $CLASSPATH *.java
if [ $? != 0 ] ; then
	echo "Compilation failed!"
	exit
fi

echo "Test run"
$JAVA -cp $CLASSPATH MainProgram

$NATIVEIMAGE -cp $CLASSPATH MainProgram
if [ $? != 0 ] ; then
	echo "Native Image failed!"
	exit
fi

upx mainprogram
find -iname "*.class" -delete

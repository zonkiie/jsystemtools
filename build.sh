#!/bin/sh

/usr/lib/jvm/graalvm-ce-java17/bin/javac -Xlint:deprecation -cp commons-cli-1.5.0.jar *.java
if [ $? != 0 ] ; then
	echo "Compilation failed!"
	exit
fi

echo "Test run"
/usr/lib/jvm/graalvm-ce-java17/bin/java -cp commons-cli-1.5.0.jar:. MainProgram

/usr/lib/jvm/graalvm-ce-java17/bin/native-image -cp commons-cli-1.5.0.jar:. MainProgram
if [ $? != 0 ] ; then
	echo "Native Image failed!"
	exit
fi

upx mainprogram
rm -fv *.class

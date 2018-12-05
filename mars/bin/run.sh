#!/bin/sh

CP=../build/mars.jar:../build/test/conf:
for loop in `ls ../lib/compile/*.jar ../lib/runtime/*.jar `
do
	CP=$CP$loop:
done

if [ "$1" = "F" ]; then
	java -cp $CP -Dfile.encoding=UTF-8 net.ninecube.console.FormulaMain $2 $3 $4 $5 $6 $7 $8 $9
elif [ "$1" = "R" ]; then
	java -cp $CP -Dfile.encoding=UTF-8 net.ninecube.console.RuleMain $2 $3 $4 $5 $6 $7 $8 $9
else
	echo "Usage ./run.sh [F|R] ..."
	echo "     F calculate metrics' value."
	echo "     R calculate promotion plan."
fi

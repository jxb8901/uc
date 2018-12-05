#!/bin/sh

while [ $# -gt 0 ]; do
	echo "cp build/ucbase.jar $1"
	cp build/ucbase.jar "$1"
	shift
done


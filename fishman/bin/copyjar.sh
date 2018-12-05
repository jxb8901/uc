#!/bin/sh

while [ $# -gt 0 ]; do
	echo "cp build/fishman.jar $1"
	cp build/fishman.jar "$1"
	shift
done


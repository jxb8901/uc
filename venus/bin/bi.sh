#!/bin/sh

#
# 将当前目录下的文件由GB2312转换为UTF－8编码
# 仅转换特定后缀的文件
#

BACKUP=false
FROMENCODE=GB2312
TOENCODE=UTF-8
FILEEXT="txt java jsp ftl xml properties sql js css "

function iconvfile() {
	PWD=`pwd`
	echo "$PWD/$1"
	cp "$1" "$1.bak"
	iconv -f "$FROMENCODE" -t "$TOENCODE" -o "$1" "$1.bak"
	if [ "false" = "$BACKUP" ]; then rm "$1.bak"; fi
}

function iconvdir() {
	for f in `ls`; do
		if [ -d "$f" ]; then
			cd "$f"
			iconvdir
			cd ..
		else
			for ext in $FILEEXT; do
				FEXT=`echo $f | awk -F. '{print $NF}'`
				if [ "$ext" = "$FEXT" ]; then
					iconvfile "$f"
				fi
			done
		fi
	done
}

iconvdir


@echo off
cd ..\hsqldb
rem del /Q *.*
rem copy backup\*.*
set CP=..\web\WEB-INF\lib\hsqldb.jar
java -classpath "%CP%" org.hsqldb.Server

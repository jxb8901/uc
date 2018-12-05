@echo off
cd ..\hsqldb
set CP=..\web\WEB-INF\lib\hsqldb.jar
java -classpath "%CP%" org.hsqldb.util.DatabaseManager

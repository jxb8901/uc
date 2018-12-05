
@echo off


if "%JAVA_HOME%" == "" goto error


set JAVA=%JAVA_HOME%\bin\java
set CP=../build/mars.jar;../build/test/conf;

for %%i in (..\lib\compile\*.jar) do call cp.bat %%i
for %%i in (..\lib\runtime\*.jar) do call cp.bat %%i

set CP=%JAVA_HOME%\lib\tools.jar;%CP%

@rem JAVA_HOME=%JAVA_HOME%
@rem CLASSPATH=%CP%

if "%1" == "F" goto F
if "%1" == "R" goto R
echo "Usage ./run.sh [F|R] ..."
echo "     F calculate metrics' value."
echo "     R calculate promotion plan."
goto end

:F
"%JAVA%" -classpath "%CP%" -Dfile.encoding=GB2312 net.ninecube.console.FormulaMain %2 %3 %4 %5 %6 %7 %8 %9
goto end

:R
"%JAVA%" -classpath "%CP%" -Dfile.encoding=GB2312 net.ninecube.console.RuleMain %2 %3 %4 %5 %6 %7 %8 %9
goto end

:error
@echo 未设置JAVA_HOME环境变量,请将JAVA_HOME环境变量指向Java的安装目录

:end


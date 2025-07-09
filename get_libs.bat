@echo off
cd /d %~dp0
mkdir SRC\lib 2>nul
cd SRC\lib

echo Downloading libraries...

:: Gson
curl -LO https://repo1.maven.org/maven2/com/google/code/gson/gson/2.9.1/gson-2.9.1.jar

echo Libraries downloaded to SRC\lib
pause

@echo off
setlocal

:: Verificar si el primer argumento es "interficie"
if "%~1"=="all" goto dominiSection
if "%~1"=="domini" goto dominiSection
if "%~1"=="controladors" goto controladorsSection
if "%~1"=="persistencia" goto persistenciaSection
if "%~1"=="interficie" goto interficieSection


:: Configura el classpath
set CLASSPATH=.\lib\junit-4.13.2.jar;.\lib\hamcrest-core-1.3.jar;.\lib\gson-2.9.1.jar


:: Compila classes base
:dominiSection
echo Compilant Classes de Domini...

javac -d ../EXE/CLASS -cp "%CLASSPATH%" domini\excepcions\*.java
javac -d ../EXE/CLASS -cp "%CLASSPATH%" domini\scrabble\Fitxa.java

javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\auxiliars\*.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\diccionari\*.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\scrabble\Multiplicador.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\scrabble\Casella.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\scrabble\Taulell.java

javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\jugadors\*.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" domini\scrabble\*.java

:: Compila persistencia
:persistenciaSection
echo Compilant Classes de Persistencia...
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" persistencia\*.java domini\sessio\*.java

:: Compila controladors
:controladorsSection
echo Compilant Controladors...
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" controladors\CtrlPersistencia.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" controladors\CtrlDomini.java controladors\CtrlPartida.java

:: Compila interfície
:interficieSection
echo Compilant Interficie...
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" interficie\utils\*.java
javac -d ../EXE/CLASS -cp "../EXE/CLASS;%CLASSPATH%" controladors\CtrlPresentacio.java interficie\*.java


echo.
echo Compilacio completa.

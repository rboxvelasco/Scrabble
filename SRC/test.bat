@echo off
setlocal EnableDelayedExpansion

:: --------------------------------------------------
::  Comprovar que rebem un argument
:: --------------------------------------------------
if "%~1"=="" (
  echo Usage: per executar el Test Unitari d'una Classe ^"ClassName^":
  echo   tests.bat ClassName
  echo.
  echo Usage: per executar tots els test unitaris:
  echo   tests.bat all
  echo.
  echo Usage: per llistar les classes amb Test Unitari:
  echo   tests.bat help
  echo.
  exit /b 1
)

:: --------------------------------------------------
::  Classpath per a la compilacio i ejecucio amb JUnit
:: --------------------------------------------------
set "CP=..\EXE\CLASS;lib\*"

:: --------------------------------------------------
::  Llistat de classes per paquet
:: --------------------------------------------------
set "AUX=ColorTerminal Pair FastDeleteList MaxWord"
set "DIC=DAWG"
set "JUG=Avatar Jugador Maquina"
set "SCR=Casella Fitxa Taulell Partida Multiplicador Bossa Ranking"
set "CTRL=CtrlDomini CtrlPartida"

:: ----------------------------------------------------
::  Si l'argumento es "help", llistar classes disponibles
:: ----------------------------------------------------
if /I "%~1"=="help" (
  echo Classes disponibles amb Test Unitari:
  echo  - auxiliars: %AUX%
  echo  - diccionari: %DIC%
  echo  - jugadors: %JUG%
  echo  - scrabble: %SCR%
  echo  - controladors: %CTRL%
  echo.
  echo Usage: tests.bat ^<ClassName^> ^| all ^| help
  exit /b 0
)

:: -----------------------------------------------------
::  Si l'argument es "all" recorrem totes, sino nomes 1
:: -----------------------------------------------------
if /I "%~1"=="all" (
  for %%C in (%AUX% %DIC% %JUG% %SCR% %CTRL%) do (
    call :run_test %%C
  )
  exit /b
) else (
  call :run_test %~1
  exit /b
)

:: --------------------------------------------------
::  Funcio que compila y executa el test d'una classe
::  Parametre: nom de la classe (sense "Test")
:: --------------------------------------------------
:run_test
set "CL=%~1"
set "PKG="

:: Detectar package
echo %AUX%| findstr /i /r "\<%CL%\>" >nul && set PKG=auxiliars
echo %DIC%| findstr /i /r "\<%CL%\>" >nul && set PKG=diccionari
echo %JUG%| findstr /i /r "\<%CL%\>" >nul && set PKG=jugadors
echo %SCR%| findstr /i /r "\<%CL%\>" >nul && set PKG=scrabble
echo %CTRL%| findstr /i /r "\<%CL%\>" >nul && set PKG=controladors


if "%PKG%"=="" (
  echo [ERROR] La classe "%CL%" no existeix o no te Test Unitari.
  echo         Comprova que hagis escrit be el nom.
  goto :eof
)

:: Assignar rutes segons el package
if /I "%PKG%"=="controladors" (
  set "DOMAIN_CLASS=..\EXE\CLASS\controladors\%CL%.class"
  set "TEST_SRC=tests\controladors\%CL%Test.java"
  set "TEST_CLASS=controladors.%CL%Test"
  set "TEST_CLASSFILE=..\EXE\CLASS\controladors\%CL%Test.class"
) else (
  set "DOMAIN_CLASS=..\EXE\CLASS\domini\%PKG%\%CL%.class"
  set "TEST_SRC=tests\domini\%PKG%\%CL%Test.java"
  set "TEST_CLASS=domini.%PKG%.%CL%Test"
  set "TEST_CLASSFILE=..\EXE\CLASS\domini\%PKG%\%CL%Test.class"
)

:: --------------------------------------------------
::  1) Comprovar que la classe de domini esta compilada
:: --------------------------------------------------
if not exist "%DOMAIN_CLASS%" (
  echo [ERROR] Classe "%CL%" NO compilada:
  echo         falta l'arxiu "%DOMAIN_CLASS%"
  echo.
  exit /b
  ) else (
  echo [ OK  ] Classe de domini "%CL%" compilada.
)

:: --------------------------------------------------
::  2) Comprovar que existix el .java del test
:: --------------------------------------------------
if not exist "%TEST_SRC%" (
  echo [ERROR] Test per "%CL%" no trobat:
  echo         falta l'arxiu "%TEST_SRC%"
  goto :eof
)

:: --------------------------------------------------
::  3) Compilar el test si cal
:: --------------------------------------------------
::if not exist "%TEST_CLASSFILE%" (
echo [COMP ] Compilant test %CL%Test...
javac -d ..\EXE\CLASS -cp "%CP%" "%TEST_SRC%"
if errorlevel 1 (
  echo [ERROR] Fallo al compilar "%TEST_SRC%"
  goto :eof
)
::) else (
::  echo [ OK  ] Test "%CL%Test" compilat.
::)

:: --------------------------------------------------
::  4) Ejecutar el test con JUnit
:: --------------------------------------------------
echo [ RUN ] Executant %CL%Test...
echo.
java -cp "%CP%" org.junit.runner.JUnitCore %TEST_CLASS%
echo.
goto :eof
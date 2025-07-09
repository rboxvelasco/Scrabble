#!/bin/bash

# -----------------------------------------
# Comprovar que rebem un argument
# -----------------------------------------
if [ -z "$1" ]; then
  echo "Usage: per executar el Test Unitari d'una Classe \"ClassName\":"
  echo "  ./tests.sh ClassName"
  echo
  echo "Usage: per executar tots els test unitaris:"
  echo "  ./tests.sh all"
  echo
  echo "Usage: per llistar les classes amb Test Unitari:"
  echo "  ./tests.sh help"
  echo
  exit 1
fi

# -----------------------------------------
# Classpath per a la compilació i execució amb JUnit
# -----------------------------------------
CP="../EXE/CLASS:lib/*"

# -----------------------------------------
# Llistat de classes per paquet
# -----------------------------------------
AUX="ColorTerminal Pair FastDeleteList MaxWord"
DIC="DAWG"
JUG="Avatar Jugador Maquina"
SCR="Casella Fitxa Taulell Partida Multiplicador Bossa Ranking"
CTRL="CtrlDomini CtrlPartida"

# -----------------------------------------
# Si l'argument és "help", llistar classes disponibles
# -----------------------------------------
if [[ "$1" == "help" ]]; then
  echo "Classes disponibles amb Test Unitari:"
  echo " - auxiliars: $AUX"
  echo " - diccionari: $DIC"
  echo " - jugadors: $JUG"
  echo " - scrabble: $SCR"
  echo " - controladors: $CTRL"
  echo
  echo "Usage: ./tests.sh <ClassName> | all | help"
  exit 0
fi

# ---------------------------------------------------
# Si l'argument és "all", recorrer totes sinó només 1
# ---------------------------------------------------
if [[ "$1" == "all" ]]; then
  for C in $AUX $DIC $JUG $SCR $CTRL; do
    ./test.sh "$C"
  done
  exit 0
else
  CLASS="$1"
fi

# -----------------------------------------
# Funció per detectar el package
# -----------------------------------------
detect_package() {
  if [[ " $AUX " =~ " $CLASS " ]]; then PKG="auxiliars"
  elif [[ " $DIC " =~ " $CLASS " ]]; then PKG="diccionari"
  elif [[ " $JUG " =~ " $CLASS " ]]; then PKG="jugadors"
  elif [[ " $SCR " =~ " $CLASS " ]]; then PKG="scrabble"
  elif [[ " $CTRL " =~ " $CLASS " ]]; then PKG="controladors"
  else
    echo "[ERROR] La classe \"$CLASS\" no existeix o no te Test Unitari."
    echo "        Comprova que hagis escrit be el nom."
    exit 1
  fi
}

# -----------------------------------------
# Execució del test
# -----------------------------------------
detect_package

if [[ "$PKG" == "controladors" ]]; then
  DOMAIN_CLASS="../EXE/CLASS/controladors/${CLASS}.class"
  TEST_SRC="tests/controladors/${CLASS}Test.java"
  TEST_CLASS="controladors.${CLASS}Test"
  TEST_CLASSFILE="../EXE/CLASS/controladors/${CLASS}Test.class"
else
  DOMAIN_CLASS="../EXE/CLASS/domini/${PKG}/${CLASS}.class"
  TEST_SRC="tests/domini/${PKG}/${CLASS}Test.java"
  TEST_CLASS="domini.${PKG}.${CLASS}Test"
  TEST_CLASSFILE="../EXE/CLASS/domini/${PKG}/${CLASS}Test.class"
fi

# 1) Comprovar que la classe de domini esta compilada
if [[ ! -f "$DOMAIN_CLASS" ]]; then
  echo "[ERROR] Classe \"$CLASS\" NO compilada:"
  echo "        falta l'arxiu \"$DOMAIN_CLASS\""
  echo
  exit 1
else
  echo "[ OK  ] Classe de domini \"$CLASS\" compilada."
fi

# 2) Comprovar que existeix el .java del test
if [[ ! -f "$TEST_SRC" ]]; then
  echo "[ERROR] Test per \"$CLASS\" no trobat:"
  echo "        falta l'arxiu \"$TEST_SRC\""
  exit 1
fi

# 3) Compilar el test
echo "[COMP] Compilant test ${CLASS}Test..."
javac -d ../EXE/CLASS -cp "$CP" "$TEST_SRC"
if [[ $? -ne 0 ]]; then
  echo "[ERROR] Fallo al compilar \"$TEST_SRC\""
  exit 1
fi

# 4) Executar el test amb JUnit
echo "[ RUN ] Executant ${CLASS}Test..."
echo
java -cp "$CP" org.junit.runner.JUnitCore "$TEST_CLASS"
echo

#!/bin/bash
# Descarga librerías necesarias en FONTS/lib

mkdir -p FONTS/lib
cd FONTS/lib || exit

echo "Downloading libraries..."

# Gson
curl -LO https://repo1.maven.org/maven2/com/google/code/gson/gson/2.9.0/gson-2.9.0.jar

echo "✅ Libraries downloaded to FONTS/lib"

CLASS_OUTPUT = ../EXE/CLASS
LIBS = ./lib/junit-4.13.2.jar:./lib/hamcrest-core-1.3.jar:./lib/gson-2.9.1.jar
JAVAC_FLAGS = -d $(CLASS_OUTPUT) -cp "$(CLASS_OUTPUT):$(LIBS)"

# Source Directories
SRC_EXCEPCIONS = $(wildcard domini/excepcions/*.java)
SRC_FITXA = domini/scrabble/Fitxa.java
SRC_AUXILIARS = $(wildcard domini/auxiliars/*.java)
SRC_DICCIONARI = $(wildcard domini/diccionari/*.java)
SRC_CASELLA = domini/scrabble/Casella.java
SRC_TAULELL = domini/scrabble/Taulell.java
SRC_MULTIPLICADOR = domini/scrabble/Multiplicador.java
SRC_JUGADORS = $(wildcard domini/jugadors/*.java)
SRC_SCRABBLE_RESTANT = $(filter-out $(SRC_FITXA) $(SRC_TAULELL) $(SRC_CASELLA) $(SRC_MULTIPLICADOR), $(wildcard domini/scrabble/*.java))

SRC_SESSIO = $(wildcard domini/sessio/*.java)
SRC_PERSISTENCIA = $(wildcard persistencia/*.java)

SRC_CTRL_PERSISTENCIA = controladors/CtrlPersistencia.java
SRC_CTRL_DOMINI_PARTIDA = controladors/CtrlDomini.java controladors/CtrlPartida.java
SRC_CTRL_PRESENTACIO = controladors/CtrlPresentacio.java

SRC_DRIVERS = $(wildcard drivers/*.java)

SRC_INTERFICIE_UTILS = $(wildcard interficie/utils/*.java)
SRC_INTERFICIE = $(wildcard interficie/*.java)

.PHONY: all clean jar

all: $(CLASS_OUTPUT) domini persistencia controladors interficie

$(CLASS_OUTPUT):
	mkdir -p $(CLASS_OUTPUT)

domini: $(CLASS_OUTPUT)
	javac -d $(CLASS_OUTPUT) -cp "$(LIBS)" $(SRC_EXCEPCIONS)
	javac -d $(CLASS_OUTPUT) -cp "$(LIBS)" $(SRC_FITXA)
	javac $(JAVAC_FLAGS) $(SRC_AUXILIARS)
	javac $(JAVAC_FLAGS) $(SRC_DICCIONARI)
	javac $(JAVAC_FLAGS) $(SRC_MULTIPLICADOR)
	javac $(JAVAC_FLAGS) $(SRC_CASELLA)
	javac $(JAVAC_FLAGS) $(SRC_TAULELL)
	javac $(JAVAC_FLAGS) $(SRC_JUGADORS)
	javac $(JAVAC_FLAGS) $(SRC_SCRABBLE_RESTANT)

persistencia: $(CLASS_OUTPUT)
	javac $(JAVAC_FLAGS) $(SRC_PERSISTENCIA) $(SRC_SESSIO)

controladors: $(CLASS_OUTPUT)
	javac $(JAVAC_FLAGS) $(SRC_CTRL_PERSISTENCIA)
	javac $(JAVAC_FLAGS) $(SRC_CTRL_DOMINI_PARTIDA)

interficie: $(CLASS_OUTPUT)
	javac $(JAVAC_FLAGS) $(SRC_INTERFICIE) $(SRC_CTRL_PRESENTACIO)

# jar: all
#	echo "Main-Class: $(MAIN_CLASS)" > $(MANIFEST_FILE)
#	jar cmvf $(MANIFEST_FILE) $(JAR_OUTPUT)/$(JAR_NAME) -C $(CLASS_OUTPUT) .

# jar: all
#	jar cfm $(JAR_OUTPUT)/$(JAR_NAME) $(MAIN_CLASS) -C $(CLASS_OUTPUT) .

clean:
	rm -rf $(CLASS_OUTPUT)/*
#	rm -f $(JAR_OUTPUT)/*.jar

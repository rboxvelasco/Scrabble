package domini.auxiliars;

import java.util.*;
import domini.scrabble.Fitxa;

/**
 * Classe auxiliar de Maquina per representar la millor jugada trobada en una partida de Scrabble.
 * 
 * La classe "MaxWord" emmagatzema la informació sobre la paraula amb la puntuació
 * més alta trobada, incloent les fitxes que la formen, la puntuació, les coordenades
 * inicials i la direcció (horitzontal o vertical).
 */
public class MaxWord {
    private List<Fitxa> word;
    private int points;
    private Pair<Integer, Integer> coordinates;
    private boolean h;

    /**
     * Constructor per defecte de la classe MaxWord.
     *
     * Inicialitza una instància amb una paraula buida, puntuació 0, coordenades
     * inicials (-1, -1) i direcció horitzontal.
     */
    public MaxWord() {
        this.word = new ArrayList<Fitxa>();
        this.points = 0;
        this.coordinates = new Pair<Integer, Integer>(-1, -1);
        this.h = true;
    }

    /**
     * Obté la llista de fitxes que formen la paraula.
     * 
     * @return Llista de fitxes.
     */
    public List<Fitxa> getWord() {
        return this.word;
    }

    /**
     * Obté la puntuació de la paraula.
     * 
     * @return Puntuació total.
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Obté les coordenades inicials de la paraula.
     * 
     * @return Coordenades (fila, columna).
     */
    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    /**
     * Indica si la paraula es col·loca horitzontalment.
     * 
     * @return true si és horitzontal, false si és vertical.
     */
    public boolean getHorizontal() {
        return this.h;
    }

    /**
     * Assigna una nova llista de fitxes a la paraula.
     * 
     * @param word Nova llista de fitxes.
     */
    public void setWord(List<Fitxa> word) {
        this.word = word;
    }

    /**
     * Assigna una nova puntuació a la paraula.
     * 
     * @param points Nova puntuació.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Assigna unes noves coordenades inicials a la paraula.
     * 
     * @param coords Noves coordenades (fila, columna).
     */
    public void setCoordinates(Pair<Integer, Integer> coords) {
        this.coordinates = coords;
    }

    /**
     * Defineix la direcció de la paraula.
     * 
     * @param h true per a horitzontal, false per a vertical.
     */
    public void setHorizontal(boolean h) {
        this.h = h;
    }
}

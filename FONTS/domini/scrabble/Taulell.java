package domini.scrabble;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.util.List;

import domini.auxiliars.Pair;

/**
 * Representa el Taulell d'Scrabble.
 *
 * Un taulell està composat per una matriu de Caselles, que contenen la
 * informació útil.
 *
 */
public class Taulell {
    int n;
    private final Casella[][] taulell;

    /**
     * Constructora de la classe Taulell. Rep les referències a les instàncies
     * ja creades de Casella des del ctrlDomini.
     *
     * @param n Mida del taulell: haurà de tenir mida n x n
     */
    public Taulell(int n) {
        if (n <= 0) throw new IllegalArgumentException("El taulell no pot estar buit.");
        else if (n % 2 == 0) throw new IllegalArgumentException("El taulell ha de tenir mida senar.");
        else {
            this.n = n;
            taulell = new Casella[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    taulell[i][j] = new Casella(i, j, n);
                }
            }
        }
    }

    /**
     *
     * Donades unes coordenades, retorna la Casella que es troba en aquelles
     * coordenades.
     *
     * @param x Indica la fila del taulell.
     * @param y Indica la columna del taulell.
     *
     * @return La casella guardada a les coordenades indicades.
     */
    public Casella getCasella(int x, int y) {
        if (limits(x, y)) return taulell[x][y];
        throw new IllegalArgumentException("Posició fora dels límits (cridada des del taulell)");
    }

    /**
     * Retorna el nombre de files/columnes del taulell
     *
     * @return Retorna la mida d'una dimensió del taulell
     */
    public int getMida() {
        return n;
    }

    /**
     * Comprova si el taulell està completament buit (cap casella ocupada).
     *
     * @return True si no hi ha cap fitxa col·locada al taulell, False en cas
     *         contrari.
     */
    public boolean estaBuit() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (taulell[i][j].EstaOcupat())
                    return false;
            }
        }
        return true;
    }

    /**
     *
     * @param x Indica la fila del taulell.
     * @param y Indica la columna del taulell.
     *
     * @return True si les coordenades estan dins dels límits del taulell, False en
     *         cas contrari
     */
    public boolean limits(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    /**
     * Retorna un Set amb les coordenades de les caselles que son anchors.
     *
     * @return Un Set amb les coordenades de les caselles que son anchors.
     */
    public Set<Pair<Integer, Integer>> getAnchors() {
        Set<Pair<Integer, Integer>> ret = new HashSet<>();

        if (estaBuit()) {
            ret.add(getCentreTaulell());
        } else {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (getCasella(i, j).isAnchor())
                        ret.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }

        return ret;
    }

    /**
     * Retorna la transposició del taulell actual.
     *
     * La transposició intercanvia les files i les columnes del taulell.
     *
     * @return Un nou objecte `Taulell` que representa la transposició del taulell
     *         actual.
     */
    public Taulell transpose() {
        Taulell ret = new Taulell(n);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                ret.setCasella(i, j, taulell[j][i]);
            }
        }
        return ret;
    }

    /**
     * Retorna la casella de la posició indicada.
     *
     * @param i Indica la fila del taulell.
     * @param j Indica la columna del taulell.
     *
     * @return La casella guardada a les coordenades indicades.
     */
    public void setCasella(int i, int j, Casella c) {
        taulell[i][j] = c;
    }

    /**
     * Insereix una paraula al Taulell.
     *
     * Insereix la paraula desitjada al taulell delegant cada lletra a la classe
     * Casella i modificant els cross-checks i anchors de les caselles adjacents.
     *
     * @param x         Indica la fila de la primera lletra de la paraula
     * @param y         Indica la columna de la primera lletra de la paraula
     * @param h         True si la paraula és horitzontal, False si és vertical
     * @param word      Paraula a inserir en el taulell
     * @param lletra    En cas que s'utilitzi un comodí, lletra a substituir.
     * @param lletra2   En cas que s'utilitzin dos comodins, segona lletra a substituir.
     *
     * @return La puntuació aconseguida en inserir la paraula.
     *
     */
    public int inserirParaula(int x, int y, boolean h, List<Fitxa> word, String lletra, String lletra2) {

        // 1. Inserim la paraula
        int fila_aux = x;
        int columna_aux = y;
        Set<Integer> alreadyOnBoard = new HashSet<Integer>();

        for (int i = 0; i < word.size(); ++i) {
            if (!taulell[fila_aux][columna_aux].EstaOcupat()) {
                taulell[fila_aux][columna_aux].afegirFitxa(word.get(i));
                taulell[fila_aux][columna_aux].setAnchor(false);

                if (limits(fila_aux, columna_aux + 1) && !taulell[fila_aux][columna_aux + 1].EstaOcupat())
                    taulell[fila_aux][columna_aux + 1].setAnchor(true);

                if (limits(fila_aux, columna_aux - 1) && !taulell[fila_aux][columna_aux - 1].EstaOcupat())
                    taulell[fila_aux][columna_aux - 1].setAnchor(true);

                if (limits(fila_aux + 1, columna_aux) && !taulell[fila_aux + 1][columna_aux].EstaOcupat())
                    taulell[fila_aux + 1][columna_aux].setAnchor(true);

                if (limits(fila_aux - 1, columna_aux) && !taulell[fila_aux - 1][columna_aux].EstaOcupat())
                    taulell[fila_aux - 1][columna_aux].setAnchor(true);
            }
            else alreadyOnBoard.add(i);

            if (h) ++columna_aux;
            else ++fila_aux;
        }

        // 2. Calculem la puntuació obtinguda
        int puntuacio = 0;

        // 2.1 Paraula principal
        puntuacio += calcularPuntatge(word, x, y, h, lletra, lletra2);

        int x_aux = x;
        int y_aux = y;
        // 2.2 Per a cada lletra de la paraula principal, generem i sumem les paraules transversals
        for (int i = 0; i < word.size(); i++) {
            if (!alreadyOnBoard.contains(i)) {
                boolean transversal = false;
                if (h) {
                    if (limits(x_aux + 1, y_aux) && getCasella(x_aux + 1, y_aux).EstaOcupat()) transversal = true;
                    if (limits(x_aux - 1, y_aux) && getCasella(x_aux - 1, y_aux).EstaOcupat()) transversal = true;
                }
                else {
                    if (limits(x_aux, y_aux + 1) && getCasella(x_aux, y_aux + 1).EstaOcupat()) transversal = true;
                    if (limits(x_aux, y_aux - 1) && getCasella(x_aux, y_aux - 1).EstaOcupat()) transversal = true;  
                }

                if (transversal) {
                    // Generem la paraula transversal
                    List<Fitxa> prefix = new ArrayList<Fitxa>();
                    List<Fitxa> sufix = new ArrayList<Fitxa>();

                    int row = x_aux;
                    int col = y_aux;
                    if (h) ++row; else ++col;

                    while (limits(row, col) && getCasella(row, col).EstaOcupat()) {
                        sufix.add(getCasella(row, col).getFitxa());
                        if (h) ++row; else ++col;
                    }

                    row = x_aux;
                    col = y_aux;
                    if (h) --row; else --col;

                    while (limits(row, col) && getCasella(row, col).EstaOcupat()) {
                        prefix.add(0, getCasella(row, col).getFitxa());
                        if (h) --row; else --col;
                    }

                    List<Fitxa> traversalWord = new ArrayList<Fitxa>();
                    traversalWord.addAll(prefix);
                    traversalWord.add(word.get(i));
                    traversalWord.addAll(sufix);

                    if (h) ++row; else ++col;
                    puntuacio += calcularPuntatge(traversalWord, row, col, !h, lletra, lletra2);
                }
            }
            if (h) ++y_aux; else ++x_aux;
        }

        return puntuacio;
    }

    /**
     * Mètode que calcula la puntuació que donaria col·locar una paraula en una
     * determinada posició del taulell.
     *
     * @param word   Llista ordenada de fitxes que formen la paraula a comprovar.
     * @param x      Fila de la primera lletra de la paraula.
     * @param y      Columna de la primera lletra de la paraula.
     * @param h      Indica si la paraula és horitzontal (True) o vertical (False).
     * 
     * @return Retorna els punts que s'obtindrien si es dugués a terme la jugada.
     */
    private int calcularPuntatge(List<Fitxa> word, int x, int y, boolean h, String lletra, String lletra2) {
        int points = 0;
        int doubleWord = 0;
        int tripleWord = 0;

        for (int i = 0; i < word.size(); ++i) {
            // if (x >= 0 && x < n && y >= 0 && y < n) {
            Multiplicador multi = taulell[x][y].getMultiplicador();

            if (multi == Multiplicador.CAP) {
                if (!word.get(i).getLletra().equals(lletra)
                        && !word.get(i).getLletra().equals(lletra2)) {
                    points += word.get(i).getPuntuacio();
                }
            } else if (multi == Multiplicador.DOBLE_LLETRA) {
                if (!word.get(i).getLletra().equals(lletra)
                        && !word.get(i).getLletra().equals(lletra2))
                    points += 2 * word.get(i).getPuntuacio();
            } else if (multi == Multiplicador.TRIPLE_LLETRA) {
                if (!word.get(i).getLletra().equals(lletra)
                        && !word.get(i).getLletra().equals(lletra2))
                    points += 3 * word.get(i).getPuntuacio();
            } else if (multi == Multiplicador.DOBLE_PARAULA) {
                ++doubleWord;
                if (!word.get(i).getLletra().equals(lletra)
                        && !word.get(i).getLletra().equals(lletra2))
                    points += word.get(i).getPuntuacio();
            } else if (multi == Multiplicador.TRIPLE_PARAULA) {
                ++tripleWord;
                if (!word.get(i).getLletra().equals(lletra)
                        && !word.get(i).getLletra().equals(lletra2))
                    points += word.get(i).getPuntuacio();
            }

            if (h)
                ++y;
            else
                ++x;
        }

        for (int i = 0; i < doubleWord; ++i) points *= 2;
        for (int i = 0; i < tripleWord; ++i) points *= 3;

        return points;
    }

    /**
     * Mètode que calcula les coordenades del centre del taulell.
     *
     * @return Retorna un pair amb les coordenades del centre del taulell.
     */
    public Pair<Integer, Integer> getCentreTaulell() {
        return new Pair<Integer, Integer>(n / 2, n / 2);
    }

    /**
     * Mètode per imprimir el taulell per pantalla.
     */
    public void print() {

        // Bucle que recorre les files
        for (int i = 0; i < n; i++) {
            // Bucle que recorre les columnes
            for (int j = 0; j < n; ++j) {
                Casella cas = getCasella(i, j);

                Fitxa f = cas.getFitxa();
                String lletra;
                if (f != null) lletra = f.getLletra();
                //else lletra = " ";

            }
        }
    }
}
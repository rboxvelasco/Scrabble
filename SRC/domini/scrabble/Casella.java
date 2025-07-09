package domini.scrabble;

import domini.auxiliars.Pair;

/**
 * Representa una casella del tauler d'Scrabble.
 *
 * Cada casella té una posició específica al tauler, un multiplicador de lletra
 * o de paraula i pot contenir una fitxa o estar buida.
 *
 */
public class Casella {
    private final Pair<Integer, Integer> coordenades;
    private final Multiplicador multiplicador;
    private Fitxa fitxa;
    private boolean[] cross_checks;
    private boolean anchor;

    /**
     * Constructor de la classe Casella.
     *
     * Inicialitza una casella amb unes coordenades, i un multiplicador de puntuació.
     * Al principi, la casella no conté cap fitxa, i el vector de cross_checks
     * s'inicialitza a cert.
     *
     * @param eix_x       La posició x de la casella en el tauler.
     * @param eix_y       La posició y de la casella en el tauler.
     * @param midaTaulell La mida del tauler de joc.
     */
    public Casella(int eix_x, int eix_y, int midaTaulell) {
        this.coordenades = new Pair<>(eix_x, eix_y);
        this.fitxa = null;
        this.multiplicador = setMultiplicador(eix_x, eix_y, midaTaulell);
        this.cross_checks = new boolean[34];
        this.anchor = false;

        for (int i = 0; i < cross_checks.length; i++) {
            cross_checks[i] = true;
        }
    }

    /**
     * Assigna un multiplicador a la casella.
     *
     * Aquest mètode afegeix un multiplicador a la casella segons la seva posició al taulell.
     *
     * @param i posició i del taulell.
     * @param j posició j del taulell.
     * @param n mida del taulell.
     */
    public Multiplicador setMultiplicador(int i, int j, int n) {
        // Triple Paraula
        if (i == (n / 2) && (j == 0 || j == n - 1) ||
                i == 0 && (j == 0 || j == n - 1 || j == n / 2) ||
                i == (n - 1) && (j == 0 || j == n - 1 || j == n / 2)) {

            // Inicialitzar casella amb triple paraula
            return Multiplicador.TRIPLE_PARAULA;
        }
        // Doble Paraula
        else if (i == n / 2 && j == n / 2 ||
                (i == j || i == n - 1 - j) && (i < n / 2 - 2 || i > n / 2 + 2)) {

            // Inicialitzar casella amb doble paraula
            return Multiplicador.DOBLE_PARAULA;

        }
        // Doble Lletra
        else if ((j == n / 4 || j == 3 * n / 4) && (i == 0 || i == n - 1) ||
                (i == n / 4 || i == 3 * n / 4) && (j == 0 || j == n - 1) ||
                (i == n / 2 + 1 || i == n / 2 - 1) && (j == n / 2 + 1 || j == n / 2 - 1) ||
                (i > 1 && i < n / 2 - 3) && ((i == j - n / 4 - 1) && j <= n / 2) ||
                (i > 1 && i < n / 2 - 3) && ((i == n - 2 - j - n / 4) && j > n / 2) ||
                (i > n / 2 + 3 && i < n - 2) && ((j == i - n / 4 - 1) && j >= n / 2) ||
                (i > n / 2 + 3 && i < n - 2) && ((j == n - i + n / 4) && j < n / 2) ||
                (j > 1 && j < n / 2 - 3) && ((j == i - n / 4 - 1) && i <= n / 2) ||
                (j > 1 && j < n / 2 - 3) && ((j == n - 2 - i - n / 4) && i > n / 2) ||
                (j > n / 2 + 3 && j < n - 2) && ((i == j - n / 4 - 1) && i >= n / 2) ||
                (j > n / 2 + 3 && j < n - 2) && ((i == n - j + n / 4) && i < n / 2)) {

            // Inicialitzar casella amb doble lletra
            return Multiplicador.DOBLE_LLETRA;
        }
        // Triple Lletra
        else if ((i == n / 2 + 2 || i == n / 2 - 2) && (j == n / 2 + 2 || j == n / 2 - 2) ||
                (i == 1 || i == n - 2) && ((j == n / 2 + n / 4 - 1) || (j == n / 2 - n / 4 + 1)) ||
                (j == 1 || j == n - 2) && ((i == n / 2 + n / 4 - 1) || (i == n / 2 - n / 4 + 1))) {

            // Inicialitzar casella amb triple lletra
            return Multiplicador.TRIPLE_LLETRA;
        }
        
        // Inicialitzar casella normal;
        else return Multiplicador.CAP;
    }

    /**
     * Comprova si la casella està ocupada per una fitxa.
     *
     * Aquest mètode retorna true si la casella conté una fitxa, i false si està
     * buida.
     *
     * @return true si la casella està ocupada per una fitxa, false si està buida.
     */
    public boolean EstaOcupat() {
        return fitxa != null;
    }

    /**
     * Afegeix una fitxa a la casella.
     *
     * Aquest mètode assigna una fitxa a la casella.
     *
     * @param fitxa La fitxa que es vol afegir a la casella.
     * @return true si la fitxa s'ha afegit correctament, false si la casella ja
     *         estava ocupada.
     */
    public boolean afegirFitxa(Fitxa fitxa) {
        if (!EstaOcupat()) {
            this.fitxa = fitxa;
            return true;
        } else
            return false;
    }

    /**
     * Canvia el valor de anchor.
     *
     * Aquest mètode estableix el valor de anchor.
     *
     * @param anchor El valor que prendrà el camp anchor de la Casella
     */
    public void setAnchor(boolean anchor) {
        this.anchor = anchor;
    }

    /**
     * Mètode que indica si la casella es anchor.
     *
     * @return True si és anchor, false en cas contrari.
     */
    public boolean isAnchor() {
        return anchor;
    }

    /**
     * Retorna l'identificador de la casella.
     *
     * @return L'identificador de la casella.
     */
    public Pair<Integer, Integer> getCoord() {
        return coordenades;
    }

    /**
     * Retorna el multiplicador de la casella.
     *
     * @return El multiplicador associat a la casella.
     */
    public Multiplicador getMultiplicador() {
        return multiplicador;
    }

    /**
     * Retorna la fitxa de la casella.
     *
     * @return La fitxa continguda en la casella, o null si està buida.
     */
    public Fitxa getFitxa() {
        return fitxa;
    }

    /**
     * Retorna el vector de cross_checks.
     *
     * @return El vector de booleans cross_checks associat a la casella.
     */
    public boolean[] getCrossChecks() {
        return cross_checks;
    }

    /**
     * Actualitza els cross_checks després d'inserir una Fitxa.
     *
     * @param new_cross Indica les lletres valides.
     */
    public void actualitzarCrossChecks(boolean[] new_cross) {
        for (int i = 0; i < cross_checks.length; ++i) {
            cross_checks[i] = cross_checks[i] && new_cross[i];
        }
    }

    /**
     * Reinicia els cross_cheks després de retirar una Fitxa.
     */
    public void reiniciarCrossCheck() {
        for (int i = 0; i < 34; ++i) {
            cross_checks[i] = true;
        }
    }

    /**
     * Treure fitxa de la casella.
     *
     * @param fitxaARetirar La fitxa que es vol retirar de la casella.
     * @return true si la fitxa s'ha retirat correctament, false en cas contrari.
     */
    public boolean retirarFitxa(Fitxa fitxaARetirar) {
        if (this.fitxa != null && this.fitxa.equals(fitxaARetirar)) {
            this.fitxa = null;
            return true;
        }
        return false;
    }
}
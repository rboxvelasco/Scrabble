package domini.scrabble;

/**
 * Representa els diferents tipus de multiplicadors d'Scrabble.
 *
 * Els multiplicadors poden afectar les lletres o les paraules, i tenen valors
 * associats que multipliquen la puntuació corresponent.
 */
public enum Multiplicador {
    CAP(1),
    DOBLE_LLETRA(2),
    TRIPLE_LLETRA(3),
    DOBLE_PARAULA(2),
    TRIPLE_PARAULA(3);

    private final int multiplicador;

    /**
     * Constructor del multiplicador.
     *
     * @param multiplicador El valor associat al multiplicador.
     */
    private Multiplicador(int multiplicador) {
        this.multiplicador = multiplicador;
    }

    /**
     * Obté el valor del multiplicador.
     *
     * @return El valor del multiplicador.
     */
    public int getMultiplicador() {
        return multiplicador;
    }
}

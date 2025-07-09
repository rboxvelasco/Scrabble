package domini.scrabble;

/**
 * Representa l'estat 'Finalitzada' d'una partida.
 *
 * Aquest estat indica que la partida ha acabat i no es poden realitzar més
 * accions.
 */
public class EstatFinalitzada extends Estat {
    /**
     * Constructor de la classe EstatFinalitzada.
     *
     * @param partida La partida associada a aquest estat.
     */
    public EstatFinalitzada(Partida partida) {
        super(partida);
    }

    /**
     * Realitza les accions necessàries per iniciar la partida.
     * En aquest estat, no es pot iniciar una nova partida.
     */
    public void iniciar() {
    }

    /**
     * Realitza les accions necessàries per pausar la partida.
     * En aquest estat, no es pot pausar la partida.
     */
    public void pausar() {
    }

    /**
     * Realitza les accions necessàries per guardar la partida.
     * En aquest estat, no es pot guardar la partida.
     */
    public void encurs() {
    }

    /**
     * Realitza les accions necessàries per finalitzar la partida.
     * En aquest estat, no es pot finalitzar la partida.
     */
    public void finalitzar() {
    }
}
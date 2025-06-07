package domini.scrabble;

/**
 * Representa l'estat 'Pausada' d'una partida.
 *
 * Aquest estat indica que la partida està pausada i es poden realitzar accions
 * per reiniciar o finalitzar la partida.
 */
public class EstatPausada extends Estat {
    /**
     * Constructor de la classe EstatPausada.
     *
     * @param partida La partida associada a aquest estat.
     */
    public EstatPausada(Partida partida) {
        super(partida);
    }

    /**
     * Realitza les accions necessàries per iniciar la partida.
     * En aquest estat, es pot reiniciar la partida.
     */
    public void iniciar() {
        partida.canviarEstat(new EstatEnCurs(partida));
    }

    /**
     * Realitza les accions necessàries per pausar la partida.
     * En aquest estat, la partida ja està pausada.
     */
    public void pausar() {
    }

    /**
     * Realitza les accions necessàries per guardar la partida.
     * En aquest estat, es pot reiniciar la partida.
     */
    public void encurs() {
        this.iniciar(); // Reutilitzem la lògica de iniciar()
    }

    /**
     * Realitza les accions necessàries per finalitzar la partida.
     * En aquest estat, es pot finalitzar la partida.
     */
    public void finalitzar() {
        partida.canviarEstat(new EstatFinalitzada(partida));
    }
}
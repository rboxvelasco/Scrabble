package domini.scrabble;

/**
 * Representa l'estat 'EnCurs' d'una partida.
 *
 * Aquest estat indica que la partida està activa i en desenvolupament.
 * Es poden realitzar accions com pausar o guardar la partida.
 */
public class EstatEnCurs extends Estat {
    /**
     * Constructor de la classe EstatEnCurs.
     *
     * @param partida La partida associada a aquest estat.
     */
    public EstatEnCurs(Partida partida) {
        super(partida);
    }

    /**
     * Constructor de la classe EstatEnCurs.
     *
     * @param partida La partida associada a aquest estat.
     */
    public EstatEnCurs() {
        super(null);
    }

    /**
     * Realitza les accions necessàries per iniciar la partida.
     * En aquest estat, no es pot iniciar una nova partida.
     */
    public void iniciar() {
    }

    /**
     * Realitza les accions necessàries per pausar la partida.
     * En aquest estat, es pot canviar a l'estat 'Pausada'.
     */
    public void pausar() {
        partida.canviarEstat(new EstatPausada(partida));
    }

    /**
     * Realitza les accions necessàries per guardar la partida.
     * En aquest estat, es pot canviar a l'estat 'Guardada'.
     */
    public void encurs() {
    }

    /**
     * Realitza les accions necessàries per finalitzar la partida.
     * En aquest estat, es pot canviar a l'estat 'Finalitzada'.
     */
    public void finalitzar() {
        partida.canviarEstat(new EstatFinalitzada(partida));
    }
}

package domini.scrabble;

/**
 * Classe abstracta que defineix el comportament genèric per als diferents
 * estats d'una partida.
 *
 * Aquesta classe actua com a base per als diferents estats possibles d'una
 * partida de Scrabble, incloent-hi:
 * - EstatEnCurs: quan la partida està activa.
 * - EstatPausada: quan la partida està temporalment aturada.
 * - EstatFinalitzada: quan la partida està finalitzada.
 */
public abstract class Estat {
    protected transient Partida partida;

    /**
     * Constructor de la classe Estat.
     *
     * @param partida La partida associada a aquest estat.
     */
    public Estat(Partida partida) {
        this.partida = partida;
    }

    /**
     * Mètode que assigna la partida del paràmetre a l'estat.
     * 
     * @param partida La partida a referenciar.
     */
    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    /**
     * Mètode abstracte que s'ha d'implementar en subclasses per iniciar
     * l'estat de la partida.
     */
    public abstract void iniciar();

    /**
     * Mètode abstracte que s'ha d'implementar en subclasses per gestionar
     * l'estat de la partida quan està en curs.
     */
    public abstract void encurs();

    /**
     * Mètode abstracte que s'ha d'implementar en subclasses per gestionar
     * l'estat de la partida quan està pausada.
     */
    public abstract void pausar();

    /**
     * Mètode abstracte que s'ha d'implementar en subclasses per gestionar
     * l'estat de la partida quan està finalitzada.
     */
    public abstract void finalitzar();
}
package domini.jugadors;

/**
 * Representa un jugador del tipus Avatar.
 *
 * Cada avatar té un identificador, un nom, una contrasenya i una puntuació
 *
 */
public class Avatar extends Jugador {
    private int partides_guanyades;
    private int partides_guanyades_maquina;
    private int partides_guanyades_jugador;
    private int partidesJugadesGeneral;
    private int partides_jugades_maquina;
    private int partides_jugades_jugador;
    private int puntuacio_max_paraula;
    private String paraula_llarga;
    private int partides_abandonades;

    /**
     * Constructor per defecte de la classe Avatar.
     *
     * Inicialitza un avatar amb un identificador i un nom, i una puntuació
     * inicial de 0 punts.
     *
     * @param idJugador  L'identificador del jugador.
     * @param nom         El nom del jugador.
     */
    public Avatar(int idJugador, String nom, String rutaImatge) {
        super(idJugador, nom, rutaImatge);
        this.partides_guanyades = 0;
        this.partides_guanyades_maquina = 0;
        this.partides_guanyades_jugador = 0;
        this.partides_jugades_maquina = 0;
        this.partides_jugades_jugador = 0;
        this.puntuacio_max_paraula = 0;
        this.partides_abandonades = 0;
        this.partidesJugadesGeneral = 0;
    }

    /**
     * Obté el nombre total de partides jugades.
     *
     * @return El nombre total de partides jugades.
     */
    public int getPartidesJugades() {
        return partidesJugadesGeneral;
    }

    /**
     * Obté el nombre total de partides guanyades.
     *
     * @return El nombre total de partides guanyades.
     */
    public int getPartides_guanyades() {
        return partides_guanyades;
    }

    /**
     * Obté el nombre de partides guanyades contra la màquina.
     *
     * @return El nombre de partides guanyades contra la màquina.
     */
    public int getPartides_guanyades_maquina() {
        return partides_guanyades_maquina;
    }

    /**
     * Obté el nombre de partides guanyades contra altres jugadors.
     *
     * @return El nombre de partides guanyades contra altres jugadors.
     */
    public int getPartides_guanyades_jugador() {
        return partides_guanyades_jugador;
    }

    /**
     * Obté el nombre de partides jugades contra la màquina.
     *
     * @return El nombre de partides jugades contra la màquina.
     */
    public int getPartides_jugades_maquina() {
        return partides_jugades_maquina;
    }

    /**
     * Obté el nombre de partides jugades contra altres jugadors.
     *
     * @return El nombre de partides jugades contra altres jugadors.
     */
    public int getPartides_jugades_jugador() {
        return partides_jugades_jugador;
    }

    /**
     * Obté la puntuació màxima obtinguda en una paraula.
     *
     * @return La puntuació màxima obtinguda en una paraula.
     */
    public int getPuntuacio_max_paraula() {
        return puntuacio_max_paraula;
    }

    /**
     * Obté la puntuació total del jugador.
     *
     * @return La puntuació total del jugador.
     */
    public int getPuntuacio_total() {
        return puntuacio_actual;
    }

    /**
     * Obté la paraula més llarga jugada per l'avatar.
     *
     * @return La paraula més llarga jugada.
     */
    public String getParaula_llarga() {
        return paraula_llarga;
    }

    /**
     * Obté el nombre de partides abandonades.
     *
     * @return El nombre de partides abandonades.
     */
    public int getPartides_abandonades() {
        return partides_abandonades;
    }

    /**
     * Actualitza la paraula més llarga si la nova paraula és més llarga que
     * l'actual.
     *
     * @param paraula La nova paraula a comprovar.
     * @return La paraula més llarga actualitzada.
     */
    public String setParaulaMesLlarga(String paraula) {
        if (paraula != null && (this.paraula_llarga == null || paraula.length() > this.paraula_llarga.length())) {
            this.paraula_llarga = paraula;
        }
        return this.paraula_llarga;
    }

    /**
     * Incrementa el nombre total de partides guanyades.
     */
    public void incrementarPartidesGuanyades() {
        this.partides_guanyades++;
    }

    /**
     * Incrementa el nombre de partides guanyades contra la màquina.
     */
    public void incrementarPartidesGuanyadesMaquina() {
        this.partides_guanyades_maquina++;
    }

    /**
     * Incrementa el nombre de partides guanyades contra altres jugadors.
     */
    public void incrementarPartidesGuanyadesJugador() {
        this.partides_guanyades_jugador++;
    }

    /**
     * Incrementa el nombre de partides jugades contra la màquina.
     */
    public void incrementarPartidesJugadesMaquina() {
        this.partides_jugades_maquina++;
    }

    /**
     * Incrementa el nombre de partides jugades contra altres jugadors.
     */
    public void incrementarPartidesJugadesJugador() {
        this.partides_jugades_jugador++;
    }

    /**
     * Actualitza la puntuació màxima obtinguda en una paraula si la nova puntuació
     * és més alta.
     *
     * @param puntuacio La nova puntuació a comprovar.
     */
    public void actualitzarPuntuacioMaxParaula(int puntuacio) {
        if (puntuacio > this.puntuacio_max_paraula) {
            this.puntuacio_max_paraula = puntuacio;
        }
    }

    /**
     * Incrementa el nombre de partides abandonades.
     */
    public void incrementarPartidesAbandonades() {
        this.partides_abandonades++;
    }

    /**
     * Incrementa el nombre total de partides jugades.
     */
    public void incrementarPartidesJugades() {
        this.partidesJugadesGeneral++;
    }
}

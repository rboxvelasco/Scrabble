package domini.jugadors;

import java.util.ArrayList;
import java.util.List;
import domini.scrabble.Fitxa;

/**
 * Representa un jugador del joc de Scrabble.
 *
 * Cada jugador té un identificador, un nom, una puntuació i unes fitxes.
 * També es gestionen els torns passats consecutius, els torns invàlids
 * consecutius
 * i la contrasenya associada al jugador.
 */
public abstract class Jugador {
    private String tipus;
    protected int idJugador;
    protected String nom;
    protected int puntuacio_actual;
    protected List<Fitxa> fitxes_actuals;
    protected static final int MAX_FITXES = 7;
    protected int tornsPassatsConsecutius;
    private String rutaImatge;

    /**
     * Constructor de la classe Jugador.
     *
     * Inicialitza un jugador amb un identificador, un nom i una contrasenya.
     *
     * @param idJugador  L'identificador del jugador.
     * @param nom         El nom del jugador.
     */
    public Jugador(int idJugador, String nom, String rutaImatge) {
        this.idJugador = idJugador;
        this.nom = nom;
        this.puntuacio_actual = 0;
        this.fitxes_actuals = new ArrayList<>();
        this.tornsPassatsConsecutius = 0;
        this.rutaImatge = rutaImatge;
        if (idJugador == -1) this.tipus = "maquina";
        else this.tipus = "avatar";
    }

    /**
     * Retorna l'identificador del jugador.
     *
     * @return Torna l'identificador del jugador
     */
    public int getIdJugador() {
        return idJugador;
    }

    /**
     * Obté a ruta a la imatge de l'avatar.
     *
     * @return La ruta a la imatge de l'avatar.
     */
    public String getrutaImatge() {
        return rutaImatge;
    }

    /**
     * Retorna el nom del jugador.
     *
     * @return Torna el nom del jugador
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retorna la llista amb les fitxes actuals del jugador.
     *
     * @return Torna les fitxes del jugador
     */
    public List<Fitxa> getFitxes_actuals() {
        return new ArrayList<>(fitxes_actuals);
    }

    /**
     * Assigna una nova llista de fitxes al jugador.
     *
     * @param novesFitxes La nova llista de fitxes.
     */
    public void setFitxesActuals(List<Fitxa> novesFitxes) {
        this.fitxes_actuals.clear();
        this.fitxes_actuals.addAll(novesFitxes);
    }

    /**
     * Buida totes les fitxes del jugador.
     */
    public void buidarFitxesJugador() {
        this.fitxes_actuals.clear();
    }

    /**
     * Retorna la puntuació actual del jugador.
     *
     * @return Torna la puntuació del jugador
     */
    public int getPuntuacio_actual() {
        return this.puntuacio_actual;
    }

    /**
     * Afegeix punts a la puntuació actual del jugador.
     *
     * @param punts Els punts a afegir.
     */
    public void incrementar_puntuacio_actual(int punts) {
        this.puntuacio_actual += punts; // punts és un atribut de la partida
        if (this.puntuacio_actual < 0) puntuacio_actual = 0; // evitar ranking amb puntuacions negatives
    }

    /**
     * El jugador afegeix una fitxa a la seva llista de fitxes
     *
     * @param fitxa És la nova fitxa que afegeix
     */
    public boolean afegirFitxa(Fitxa fitxa) {
        if (fitxes_actuals.size() < MAX_FITXES) {
            fitxes_actuals.add(fitxa);
            return true;
        }
        return false;
    }

    /**
     * El jugador elimina una fitxa de la seva llista de fitxes
     *
     * @param lletra És la fitxa que elimina
     */
    public Fitxa eliminarFitxa(String lletra) {
        Fitxa a_retornar = null;

        for (Fitxa f : fitxes_actuals) {
            if (f.getLletra().equals(lletra)) {
                fitxes_actuals.remove(f);
                a_retornar = f;
                break;
            }
        }
        return a_retornar;
    }

    /**
     * Incrementa el comptador de torns passats consecutius.
     */
    public void incrementarTornsPassatsConsecutius() {
        this.tornsPassatsConsecutius++;
    }

    /**
     * Reinicia el comptador de torns passats consecutius.
     */
    public void reiniciarTornsPassatsConsecutius() {
        this.tornsPassatsConsecutius = 0;
    }

    /**
     * Retorna el nombre de torns passats consecutius.
     *
     * @return El nombre de torns passats consecutius.
     */
    public int getTornsPassatsConsecutius() {
        return tornsPassatsConsecutius;
    }
}

package domini.scrabble;

import domini.auxiliars.*;
import domini.diccionari.*;
import domini.jugadors.*;
import java.io.IOException;
import java.util.List;

/**
 * Classe que representa una partida d'Scrabble.
 *
 * Gestiona els jugadors, el taulell, la bossa de fitxes, el diccionari i
 * l'estat de la partida. També inclou la lògica per gestionar les accions
 * dels jugadors i de la màquina, així com la puntuació i els estats de la
 * partida.
 */
public class Partida {
    private Jugador jugador1; // primer jugador
    private Jugador jugador2; // segon jugador
    private Taulell taulell; // el taulell de la partida
    private Bossa bossa; // bossa amb totes les fitxes
    private String idioma; // necessari per a persistencia
    private Estat estat; // estat actual de la partida
    private int torn;
    private int idP;
    private int puntuacio_jugador_1;
    private int puntuacio_jugador_2;

    /**
     * Constructor de la classe "Partida".
     *
     * Inicialitza una nova partida amb els jugadors, el taulell, la bossa de fitxes
     * i el diccionari indicats. L'estat inicial de la partida és "en curs".
     *
     * @param jugador1  El primer jugador.
     * @param jugador2  El segon jugador.
     * @param mida      La mida del taulell.
     * @param idioma    L'idioma del diccionari.
     * @param idPartida L'identificador de la partida.
     * @throws IOException Si hi ha un problema en carregar el diccionari.
     */
    public Partida(Jugador jugador1, Jugador jugador2, int mida, String idioma, int idPartida) throws IOException {
        this.idP = idPartida;
        this.idioma = idioma;
        this.puntuacio_jugador_1 = 0;
        this.puntuacio_jugador_2 = 0;
        this.torn = 1;
        jugador1.reiniciarTornsPassatsConsecutius();
        jugador2.reiniciarTornsPassatsConsecutius();
        reiniciar_puntuacio_partida(jugador1);
        reiniciar_puntuacio_partida(jugador2);

        this.jugador1 = jugador1;
        this.jugador2 = jugador2;

        if (jugador1.getIdJugador() == -1 && jugador2.getIdJugador() != -1) {
            Avatar avatar2 = (Avatar) jugador2;

            avatar2.incrementarPartidesJugades();
            avatar2.incrementarPartidesJugadesMaquina();

        } else if (jugador2.getIdJugador() == -1 && jugador2.getIdJugador() != -1) {
            Avatar avatar1 = (Avatar) jugador1;

            avatar1.incrementarPartidesJugades();
            avatar1.incrementarPartidesJugadesMaquina();

        } else if (jugador1.getIdJugador() != -1 && jugador2.getIdJugador() != -1) {
            Avatar avatar1 = (Avatar) jugador1;
            Avatar avatar2 = (Avatar) jugador2;
            avatar1.incrementarPartidesJugades();
            avatar2.incrementarPartidesJugades();
            avatar1.incrementarPartidesJugadesJugador();
            avatar2.incrementarPartidesJugadesJugador();
        }

        this.taulell = new Taulell(mida);
        this.estat = new EstatEnCurs(this); // Inicialment en curs

        // Selecció del fitxer del diccionari
        String nomFitxer = switch (idioma) {
            case "1" -> "catalan.txt";
            case "2" -> "castellano.txt";
            case "3" -> "english.txt";
            default -> "catalan.txt";
        };

        this.bossa = new Bossa("letras" + nomFitxer);
    }

    /**
     * Retorna el torn de la partida.
     *
     * @return El torn de la partida.
     */
    public int getTorn() {
        return torn;
    }

    /**
     * Incrementa el torn de la partida actual.
     */
    public void incrementarTorn() {
        torn++;
    }

    /**
     * Incrementa la puntuació de la partida per al jugador actual.
     *
     * @param puntuacio     La puntuació a afegir.
     * @param jugadorActual El jugador al qual s'assigna la puntuació.
     */
    public void incrementar_puntuacio_partida(int puntuacio, Jugador jugadorActual) {
        if (jugadorActual == jugador1) {
            puntuacio_jugador_1 += puntuacio;
        } else if (jugadorActual == jugador2) {
            puntuacio_jugador_2 += puntuacio;
        }
    }

    /**
     * Retorna l'idioma de la partida.
     *
     * @return L'idioma de la partida..
     */

    public String getIdioma() {
        return idioma;
    }

    /**
     * Reinicia la puntuació de la partida per al jugador actual.
     *
     * @param jugadorActual El jugador al qual es reinicia la puntuació.
     */
    public void reiniciar_puntuacio_partida(Jugador jugadorActual) {
        if (jugadorActual == jugador1) {
            puntuacio_jugador_1 = 0;
        } else if (jugadorActual == jugador2) {
            puntuacio_jugador_2 = 0;
        }
    }

    /**
     * Retorna la puntuació del jugador 1.
     *
     * @return La puntuació del jugador 1.
     */
    public int getPuntuacioJugador1() {
        return puntuacio_jugador_1;
    }

    /**
     * Retorna la puntuació del jugador 2.
     *
     * @return La puntuació del jugador 2.
     */
    public int getPuntuacioJugador2() {
        return puntuacio_jugador_2;
    }

    /**
     * Retorna el tauler de la partida.
     *
     * @return El tauler de la partida.
     */
    public Taulell getTaulell() {
        return this.taulell;
    }

    /**
     * Imprimeix el taulell de la partida.
     */
    public void imprimirtaulell() {
        taulell.print(); // taulell és un atribut del tipus Taulell
    }

    /**
     * Inicia la partida. Activa l'estat inicial de la partida.
     */
    public void iniciar() {
        estat.iniciar();
    }

    /**
     * Retorna l'identificador de la partida.
     *
     * @return L'identificador de la partida.
     */
    public int getIdPartida() {
        return idP;
    }

    /**
     * Assigna fitxes inicials als jugadors.
     *
     * Omple la mà de cada jugador fins al màxim permès amb fitxes de la bossa.
     */
    public void inicialitzarJugadors() {
        jugador1.buidarFitxesJugador();
        jugador2.buidarFitxesJugador();
        reomplirFitxesJugador(jugador1, bossa);
        reomplirFitxesJugador(jugador2, bossa);
    }

    /**
     * Reomple les fitxes d'un jugador fins al màxim permès.
     *
     * @param jugador El jugador al qual es reomplen les fitxes.
     * @param bossa   La bossa de fitxes.
     */
    public void reomplirFitxesJugador(Jugador jugador, Bossa bossa) {
        while (jugador.getFitxes_actuals().size() < 7) {
            Fitxa fitxa = bossa.agafarFitxa();
            if (fitxa == null) {
                break; // Si no hi ha més fitxes a la bossa, sortim del bucle
            }
            jugador.afegirFitxa(fitxa);
        }
    }

    /**
     * Canvia l'estat de la partida.
     *
     * @param nouEstat El nou estat de la partida.
     * @return true si el canvi d'estat és vàlid, false en cas contrari.
     */
    public boolean canviarEstat(Estat nouEstat) {

        if (estat instanceof EstatFinalitzada) {
            return false;
        }

        boolean transicioPermesa = (estat instanceof EstatEnCurs
                && (nouEstat instanceof EstatPausada || nouEstat instanceof EstatFinalitzada))
                || (estat instanceof EstatPausada
                        && (nouEstat instanceof EstatEnCurs || nouEstat instanceof EstatFinalitzada));

        if (transicioPermesa) {
            this.estat = nouEstat;
            return true;
        }

        return false;
    }

    /**
     * Finalitza la partida i canvia l'estat a finalitzat.
     */
    public void finalitzar() {
        estat.finalitzar();
    }

    /**
     * Pausa la partida, delegant la funcionalitat a l'objecte estat.
     */
    public void pausar() {
        estat.pausar();
    }

    /**
     * Reanuda la partida si està en pausa.
     */
    public void encurs() {
        estat.encurs();
    }

    /**
     * Retorna l'estat actual de la partida.
     *
     * @return L'estat de la partida.
     */
    public Estat getEstat() {
        return this.estat;
    }

    /**
     * Comprova si la partida està finalitzada.
     *
     * @return true si la partida està finalitzada, false en cas contrari.
     */
    public boolean estaFinalitzada() {
        return estat instanceof EstatFinalitzada;
    }

    /**
     * Permet que la màquina faci una jugada.
     *
     * @param diccionari El diccionari de paraules vàlides.
     * @return el nombre de punts que ha fet la màquina en una jugada.
     */
    public int ferJugadaMaquina(DAWG diccionari) {
        if (jugador1.getIdJugador() == -1 && jugador2.getIdJugador() != -1) {
            MaxWord mw = ((Maquina) jugador1).ferJugada(taulell, diccionari);
            if (mw.getPoints() == 0)
                return mw.getPoints(); // canviar fitxes;

            else {
                int x = mw.getCoordinates().getFirst();
                int y = mw.getCoordinates().getSecond();
                boolean h = mw.getHorizontal();
                List<Fitxa> word = mw.getWord();

                taulell.inserirParaula(x, y, h, word, "", "");
                incrementar_puntuacio_partida(mw.getPoints(), jugador1);
                int fitxes1 = 0;
                // Eliminar fitxes utilitzades
                for (Fitxa f : word) {
                    if (jugador1.eliminarFitxa(f.getLletra()) != null) {
                        ++fitxes1;
                    } else if (jugador1.eliminarFitxa("#") != null) {
                        ++fitxes1;
                    }
                }
                if (fitxes1 == 7) {
                    incrementar_puntuacio_partida(50, jugador1);
                }

                // Reposar fitxes
                reomplirFitxesJugador(jugador1, bossa);
                return mw.getPoints();
            }
        } else if (jugador2.getIdJugador() == -1 && jugador1.getIdJugador() != -1) {

            MaxWord mw = ((Maquina) jugador2).ferJugada(taulell, diccionari);
            if (mw.getPoints() == 0)
                return mw.getPoints(); // canviar fitxes;
            else {
                int x = mw.getCoordinates().getFirst();
                int y = mw.getCoordinates().getSecond();
                boolean h = mw.getHorizontal();
                List<Fitxa> word = mw.getWord();
                taulell.inserirParaula(x, y, h, word, "", "");
                incrementar_puntuacio_partida(mw.getPoints(), jugador2);
                int fitxes2 = 0;
                // Eliminar fitxes utilitzades
                for (Fitxa f : word) {
                    if (jugador2.eliminarFitxa(f.getLletra()) != null) {
                        ++fitxes2;
                    }
                }
                if (fitxes2 == 7) {
                    incrementar_puntuacio_partida(50, jugador2);
                }

                // Reposar fitxes
                reomplirFitxesJugador(jugador2, bossa);
                return mw.getPoints();
            }
        }
        return 0;
    }

    /**
     * Incrementa la puntuació del jugador actual.
     *
     * @param punts La quantitat de punts a afegir al jugador actual.
     * @param torn  El número del torn actual.
     */
    public void afegirPuntsJugadorActual(int punts) {
        Jugador jugadorActual = getJugadorActual(false);
        jugadorActual.incrementar_puntuacio_actual(punts);
    }

    /**
     * Retorna la llista de fitxes actuals del jugador que té el torn.
     *
     * @param torn El número del torn actual.
     * @return La llista de fitxes del jugador actual.
     */
    public List<Fitxa> getFitxesActuals() {
        Jugador jugadorActual = getJugadorActual(false);
        return jugadorActual.getFitxes_actuals();
    }

    /**
     * Retorna la instància de la bossa.
     *
     * @return La bossa de fitxes.
     */
    public Bossa getBossa() {
        return this.bossa;
    }

    /**
     * Retorna el jugador 1 de la partida.
     *
     * @return El jugador 1.
     */
    public Jugador getJugador1() {
        return this.jugador1;
    }

    /**
     * Retorna el jugador 2 de la partida.
     *
     * @return El jugador 2.
     */
    public Jugador getJugador2() {
        return this.jugador2;
    }

    /**
     * Obté el jugador que està jugant el torn.
     *
     * @return el jugador que està jugant el torn.
     */
    public Jugador getJugadorActual(boolean torn_b) {
        if (torn_b) { // Comprovar si el torn és parell o senar
            return ((torn + 1) % 2 == 1) ? jugador1 : jugador2;
        } else {
            return (torn % 2 == 1) ? jugador1 : jugador2;
        }
    }

    /**
     * Finalitza la partida per torns consecutius passats.
     */
    public void finalitzarPerTornsConsecutius() {
        puntuacio_jugador_1 = calcularPuntuacionsFitxes(jugador1);
        jugador1.incrementar_puntuacio_actual(-puntuacio_jugador_1);
        puntuacio_jugador_2 = calcularPuntuacionsFitxes(jugador2);
        jugador2.incrementar_puntuacio_actual(-puntuacio_jugador_2);

        ((Avatar) jugador1).incrementarPartidesJugades();
        ((Avatar) jugador2).incrementarPartidesJugades();
    }

    /**
     * Calcula la puntuació total de les fitxes restants d'un jugador.
     *
     * @param jugador El jugador del qual es calcularà la puntuació de les fitxes
     *                restants.
     * @return La suma de les puntuacions de les fitxes restants.
     */
    public int calcularPuntuacionsFitxes(Jugador jugador) {
        int suma = 0;
        for (Fitxa fitxa : jugador.getFitxes_actuals()) {
            suma += fitxa.getPuntuacio();
        }
        return suma;
    }
}
package domini.scrabble;

import java.util.*;
import domini.jugadors.*;
import domini.auxiliars.ColorTerminal;

/**
 * Classe que gestiona els rankings globals i locals dels jugadors.
 *
 * Aquesta classe permet mantenir un registre de les puntuacions globals i
 * locals
 * dels jugadors, així com actualitzar-les i mostrar-les.
 */
public class Ranking {

    /**
     * Classe interna que representa un jugador amb la seva puntuació.
     */
    private static class JugadorPuntuacio implements Comparable<JugadorPuntuacio> {
        int idJugador;
        int puntuacio;

        /**
         * Constructor de JugadorPuntuacio.
         * 
         * @param idJugador Identificador del jugador.
         * @param puntuacio Puntuació del jugador.
         */
        public JugadorPuntuacio(int idJugador, int puntuacio) {
            this.idJugador = idJugador;
            this.puntuacio = puntuacio;
        }

        /**
         * Compara aquesta instància amb un altre objecte `JugadorPuntuacio`.
         *
         * @param other L'altre objecte a comparar.
         * @return Un valor negatiu, zero o positiu segons l'ordre de puntuació.
         */
        @Override
        public int compareTo(JugadorPuntuacio other) {
            return Integer.compare(other.puntuacio, this.puntuacio);
        }
    }

    private PriorityQueue<JugadorPuntuacio> rankingGlobal;
    private PriorityQueue<JugadorPuntuacio> rankingLocal;
    private Map<Integer, Jugador> jugadorsRegistrats;

    /**
     * Constructor de la classe Ranking.
     * 
     * @param jugadorsRegistrats Mapa amb tots els jugadors registrats.
     */
    public Ranking(Map<Integer, Jugador> jugadorsRegistrats) {
        this.rankingGlobal = new PriorityQueue<>();
        this.rankingLocal = new PriorityQueue<>();
        this.jugadorsRegistrats = jugadorsRegistrats;
        this.inicialitzarRankingGlobal();
    }

    /**
     * Inicialitza el ranking global amb les puntuacions actuals dels jugadors
     * registrats.
     */
    private void inicialitzarRankingGlobal() {
        rankingGlobal.clear();
        jugadorsRegistrats.forEach((id, jugador) -> {
            rankingGlobal.add(new JugadorPuntuacio(id, jugador.getPuntuacio_actual()));
        });
    }

    /**
     * Inicia una partida amb dos jugadors, afegint-los al ranking local.
     * 
     * @param idJugador1 ID del primer jugador.
     * @param idJugador2 ID del segon jugador.
     */
    public void iniciarPartida(int idJugador1, int idJugador2) {
        rankingLocal.clear();
        if (idJugador1 != -1) rankingLocal.add(new JugadorPuntuacio(idJugador1, 0));
        if (idJugador2 != -1) rankingLocal.add(new JugadorPuntuacio(idJugador2, 0));
    }

    /**
     * Actualitza la puntuació d’un jugador en la partida local.
     * 
     * @param idJugador ID del jugador.
     * @param punts     Puntuació a assignar.
     */
    public void actualitzarPuntsPartidaLocal(int idJugador, int punts) {
        rankingLocal.removeIf(jp -> jp.idJugador == idJugador);
        rankingLocal.add(new JugadorPuntuacio(idJugador, punts));
    }

    /**
     * Actualitza la puntuació d’un jugador en el ranking global.
     * 
     * @param idJugador ID del jugador.
     * @param punts     Puntuació a assignar.
     */
    public void actualitzarPuntsPartidaGlobal(int idJugador, int punts) {
        // Cerca el jugador al ranking global
        JugadorPuntuacio jugadorPuntuacio = null;
        for (JugadorPuntuacio jp : rankingGlobal) {
            if (jp.idJugador == idJugador) {
                jugadorPuntuacio = jp;
                break;
            }
        }

        // Si el jugador ja existeix, actualitza la puntuació
        if (jugadorPuntuacio != null) {
            rankingGlobal.remove(jugadorPuntuacio);
            jugadorPuntuacio.puntuacio += punts;
            rankingGlobal.add(jugadorPuntuacio);
        } else {
            // Si no existeix, afegeix-lo amb la puntuació inicial
            rankingGlobal.add(new JugadorPuntuacio(idJugador, punts));
        }
    }

    /**
     * Comprova si el ranking local és buit.
     * 
     * @return true si no hi ha cap jugador, false altrament.
     */
    public boolean estaBuit() {
        return rankingLocal.isEmpty();
    }

    /**
     * Retorna una llista amb els millors avatars del rànquing global fins a un límit donat.
     *
     * <p>Es fa una còpia de la cua de prioritat del rànquing global i se n'extreuen
     * els {@code limit} primers jugadors ordenats per puntuació. Si no hi ha prou
     * jugadors registrats, es mostra un missatge d'advertència.</p>
     *
     * @param limit Nombre màxim d'avatars a retornar.
     * @return Llista d'avatars ordenats per puntuació (descendent), fins a {@code limit} elements.
     */
    public List<Avatar> getRankingGlobalLimitat(int limit) {
        List<Avatar> ret = new ArrayList<>();
        PriorityQueue<JugadorPuntuacio> copia = new PriorityQueue<>(rankingGlobal);
        int posicio = 1;

        while (!copia.isEmpty() && posicio <= limit) {
            JugadorPuntuacio jp = copia.poll();
            Jugador jugador = jugadorsRegistrats.get(jp.idJugador);

            if (jugador != null) {
                ret.add((Avatar) jugador);
            }
            ++posicio;
        }

        return ret;
    }

    /**
     * Elimina un jugador del ranking global.
     * 
     * @param idJugador ID del jugador a eliminar.
     */
    public void eliminarJugador(Integer idJugador) {
        rankingGlobal.removeIf(jp -> jp.idJugador == idJugador);
    }
}
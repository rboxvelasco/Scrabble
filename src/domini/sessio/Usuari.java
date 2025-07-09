package domini.sessio;

import domini.jugadors.*;
import domini.scrabble.Partida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import persistencia.*;

/**
 * Representa un usuari del joc de Scrabble.
 *
 * Cada usuari té un nom i una contrasenya.
 * També es gestionen els jugadors que té cada usuari
 */
public class Usuari {
    private String nom;
    private String contrasenya;
    private List<Integer> jugadors_usuari;
    private List<Integer> partides_usuari;

    /**
     * Constructor de la classe Usuari.
     *
     * Inicialitza un jugador amb un nom i una contrasenya.
     * 
     * @param nom             El nom de l'usuari.
     * @param contrasenya     La contrasenya del jugador.
     */
    public Usuari(String nom, String contrasenya) {
        this.nom = nom;
        this.contrasenya = contrasenya;
        this.jugadors_usuari = new ArrayList<>();
        this.partides_usuari = new ArrayList<>();
    }

    /**
     * Retorna el nom de l'usuari.
     *
     * @return Torna el nom de l'usuari
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retorna la contrasenya de l'usuari.
     *
     * @return Torna la contrasenya de l'usuari
     */
    public String getContrasenya() {
        return contrasenya;
    }


    /**
     * Retorna els jugadors que té guardats l'usuari.
     *
     *@throws IOException Si hi ha un error en carregar els jugadors.
     * @return Torna els jugadors de l'usuari.
     */
    public List<Jugador> getJugadors() throws IOException {
        List<Jugador> ret = new ArrayList<Jugador>();

        for (int id : jugadors_usuari) {
            ret.add(JugadorIO.loadJugador(id, getNom()));
        }

        return ret;
    }


    /**
     * Retorna les partides jugades de l'usuari.
     *
     * @throws IOException Si hi ha un error en carregar les partides.
     * @return Torna les partides de l'usuari
     */
    public List<Partida> getPartides() throws IOException {
        List<Partida> ret = new ArrayList<Partida>();

        for (int id : partides_usuari) {
            ret.add(PartidaIO.loadPartida(id, getNom()));
        }

        return ret;
    }


    /**
     * Compara la contrasenya proporcionada amb la contrasenya de l'usuari.
     *
     * @param contrasenyaProporcionada La contrasenya a comparar.
     * @return true si les contrasenyes coincideixen, false en cas contrari.
     */
    public boolean compararContrasenya(String contrasenyaProporcionada) {
        return this.contrasenya.equals(contrasenyaProporcionada);
    }

    /**
     * Afegim un jugador a la llista de jugadors de l'usuari
     * 
     * @param jugador El jugador a afegir.
     * @return true si el jugador s'ha afegit correctament, false en cas contrari.
     */
    public boolean afegirJugador(Jugador jugador) {
        if (jugadors_usuari.add(jugador.getIdJugador()))
            return true;
        return false;
    }

    /**
     * Afegim una partida a la llista de partides de l'usuari
     * 
     * @param partida La partida a afegir.
     * @return true si la partida s'ha afegit correctament, false en cas contrari.
     */
    public boolean afegirPartida(Partida partida) {
        if (partides_usuari.add(partida.getIdPartida()))
            return true;
        return false;
    }

    /**
     * Elimina el jugador identificat per id dels de l'usuari.
     * 
     * @param id Id del jugadora a eliminar
     */
    public void eliminarJugadorPerId(int id) {
        jugadors_usuari.removeIf(j -> j == id);
    }

    /**
     * Elimina la partida identificada per id.
     * 
     * @param id Id de la partida a eliminar
     */
    public void eliminarPartidaPerId(int id) {
        partides_usuari.removeIf(p -> p == id);
    }
}
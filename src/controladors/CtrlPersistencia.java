package controladors;

import domini.jugadors.Jugador;
import domini.jugadors.Maquina;
import domini.scrabble.Partida;
import domini.sessio.Usuari;
import persistencia.JugadorIO;
import persistencia.PartidaIO;
import persistencia.UsuariIO;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de persistència.
 * 
 * Proporciona mètodes per gestionar l'emmagatzematge i la recuperació de dades 
 *      relacionades amb els jugadors i les partides.
 */
public class CtrlPersistencia {

    /**
     * Desa un jugador a l'emmagatzematge persistent.
     * 
     * @param jugador El jugador a desar.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static void guardarJugador(Jugador jugador, String idUsuari) throws IOException {
        if (jugador instanceof Maquina) return;
        JugadorIO.saveJugador(jugador, idUsuari);
    }


    /**
     * Elimina un jugador de l'emmagatzematge persistent.
     * 
     * @param idJugador Identificador del jugador que es vol eliminar.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static void eliminarJugador(Integer idJugador, String idUsuari) throws IOException {
        JugadorIO.eliminarJugador(idJugador, idUsuari);
    }


    /**
     * Desa un usuari a l'emmagatzematge persistent.
     * 
     * @param usuari L'usuari a desar.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static void guardarUsuari(Usuari usuari) throws IOException {
        UsuariIO.saveUsuari(usuari);
    }


    /**
     * Elimina un usuari de l'emmagatzematge persistent.
     * 
     * @param nom Nom de l'usuari que es vol eliminar.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static void eliminarUsuari(String nom) throws IOException {
        UsuariIO.eliminarUsuari(nom);
    }

    /**
     * Carrega una partida de l'emmagatzematge persistent.
     * 
     * @param idPartida Identificador de la partida.
     * @return La partida carregada.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static Partida carregarPartida(Integer idPartida, String idUsuari) throws IOException {
        return PartidaIO.loadPartida(idPartida, idUsuari);
    }

    /**
     * Desa una partida a l'emmagatzematge persistent.
     * 
     * @param partida La partida a desar.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static void guardarPartida(Partida partida, String idUsuari) throws IOException {
            PartidaIO.savePartida(partida, idUsuari);
    }

    /**
     * Elimina una partida de l'emmagatzematge persistent.
     * 
     * @param idPartida Identificador de la partida que es vol eliminar.
     * @throws IOException Si hi ha un problema d'entrada/sortida.
     */
    public static void eliminarPartida(Integer idPartida, String idUsuari) throws IOException {
        PartidaIO.eliminarPartida(idPartida, idUsuari);
    }


    /**
     * Carrega els usuaris guardats a la capa de persistència.
     * 
     * @throws IOException Si no es troba el directori on estan guardades les dades de l'usuari.
     * @return             Un Map on el key és l'identificador de l'usuari (nom) i el value és
     *                     el propi usuari.
     */
    public static Map<String, Usuari> carregarUsuarisRegistrats() throws IOException {
        File dir = new File(UsuariIO.BASE_DIRECTORY);
        
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("No s'ha pogut crear el directori: " + UsuariIO.BASE_DIRECTORY);
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("El path especificat no és un directori: " + UsuariIO.BASE_DIRECTORY);
        }

        File[] jsonFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        });

        Map<String, Usuari> usuaris = new HashMap<>();
        if (jsonFiles != null) {
            for (File f : jsonFiles) {
                String fileName = f.getName();
                String nomUsuari = fileName.substring(0, fileName.length() - 5);
                Usuari u = UsuariIO.loadUsuari(nomUsuari);
                if (u != null) {
                    usuaris.put(nomUsuari, u);
                }
            }
        }
        return usuaris;
    }
}
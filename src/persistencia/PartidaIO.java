package persistencia;

import domini.jugadors.Jugador;
import domini.scrabble.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestor de persistència per a partides.
 * 
 * Proporciona funcionalitats per guardar, carregar, afegir, eliminar i
 * modificar partides a l'emmagatzematge persistent en format JSON.
 */
public class PartidaIO {

    // private static final Gson gson = new Gson();
    private static final Gson gson = new Gson();
    public static final String BASE_DIRECTORY = "data/partides";

    /**
     * Constructor de la classe Gestor_Partida.
     */
    private PartidaIO() {
    }

    /**
     * Carrega una partida des d'un fitxer JSON.
     * 
     * @param idPartida Identificador de la partida que es vol carregar.
     * @throws IOException Si hi ha un error d'entrada/sortida.
     *  @return La partida carregada.
     */
    public static Partida loadPartida(Integer idPartida, String idUsuari) throws IOException {
        // String filepath = BASE_DIRECTORY + "/" + idPartida + ".json";
        String filepath = "data/partides/" + idUsuari + "/" + idPartida + ".json";
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Jugador.class, new JugadorDeserializer())
            .registerTypeAdapter(Estat.class, new EstatInstanceCreator())
            .create();

        try (FileReader reader = new FileReader(filepath)) {
            Partida partida = gson.fromJson(reader, Partida.class);

            // Aquí reconstruïm l'índex de la FastDeleteList de fitxes
            partida.getBossa().getFitxes().rebuildIndexMap();

            // Tenim en compte que l'estat no es guarda.
            partida.getEstat().setPartida(partida);

            return partida;
        }
    }

    /**
     * Desa una partida a un fitxer JSON.
     * 
     * @param partida partida que es vol desar.
     * @throws IOException Si hi ha un error d'entrada/sortida.
     */
    public static void savePartida(Partida partida, String idUsuari) throws IOException {
        String directoryPath = "data/partides/" + idUsuari + "/";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // crea todos los directorios necesarios
        }

        String filepath = directoryPath + "/" + partida.getIdPartida() + ".json";
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(partida, writer);
        }
    }

    /**
     * Elimina una partida esborrant el seu fitxer JSON.
     * 
     * @param idPartida Identificador de la partida que es vol eliminar.
     */

    public static void eliminarPartida(Integer idPartida, String idUsuari) throws IOException {
        String directoryPath = "data/partides/" + idUsuari + "/";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            throw new IOException("El directori de partides per a l'usuari " + idUsuari + " no existeix.");
        }

        String filepath = directoryPath + "/" + idPartida + ".json";
        File file = new File(filepath);

        if (!file.exists()) {
            throw new IOException("El fitxer de la partida amb id " + idPartida + " no existeix.");
        }

        boolean deleted = file.delete();
        if (!deleted) {
            throw new IOException("No s'ha pogut eliminar el fitxer de la partida amb id " + idPartida);
        }
    }
}
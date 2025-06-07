package persistencia;

import domini.jugadors.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestor de persistència per a jugadors.
 * 
 * Proporciona funcionalitats per guardar, carregar, afegir, eliminar i
 * modificar jugadors
 * a l'emmagatzematge persistent en format JSON.
 */
public class JugadorIO {

    private static final Gson gson;
    static {
        // Configure Gson with custom serializer and deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Jugador.class, new JugadorSerializer());
        gsonBuilder.registerTypeAdapter(Jugador.class, new JugadorDeserializer());
        gson = gsonBuilder.create();
    }
    public static final String BASE_DIRECTORY = "data/jugadors";

    /**
     * Constructor de la classe Gestor_Jugador.
     */
    private JugadorIO() {
    }

    /**
     * Carrega un jugador des d'un fitxer JSON.
     * 
     * @param idJugador Identificador del jugador que es vol carregar.
     * @return El jugador carregat.
     * @throws IOException Si hi ha un error d'entrada/sortida.
     */
    public static Jugador loadJugador(Integer idJugador, String idUsuari) throws IOException {
        String filepath = "data/jugadors/" + idUsuari + "/" + idJugador + ".json";
        File file = new File(filepath);

        if (!file.exists()) {
            throw new FileNotFoundException("El fitxer del jugador amb id " + idJugador + " no existeix.");
        }

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Jugador.class); // Use Jugador.class, deserializer handles the rest
        }
    }

    /**
     * Desa un jugador a un fitxer JSON.
     * 
     * @param jugador Jugador que es vol desar.
     * @throws IOException Si hi ha un error d'entrada/sortida.
     */
    public static void saveJugador(Jugador jugador, String idUsuari) throws IOException {
        String directoryPath = "data/jugadors/" + idUsuari + "/";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filepath = directoryPath + "/" + jugador.getIdJugador() + ".json";
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(jugador, writer); // Use custom serializer
        }
    }

    /**
     * Elimina un jugador esborrant el seu fitxer JSON.
     * 
     * @param idJugador Identificador del jugador que es vol eliminar.
     */
    public static void eliminarJugador(Integer idJugador, String idUsuari) throws IOException {
        String filepath = "data/jugadors/" + idUsuari + "/" + idJugador + ".json";
        File file = new File(filepath);

        if (!file.exists()) {
            throw new IOException("El fitxer del jugador amb id " + idJugador + " no existeix.");
        }

        boolean deleted = file.delete();
        if (!deleted) {
            throw new IOException("No s'ha pogut eliminar el fitxer del jugador amb id " + idJugador);
        }
    }
}
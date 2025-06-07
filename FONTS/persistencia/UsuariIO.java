package persistencia;

import com.google.gson.Gson;

import domini.sessio.Usuari;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestor de persistència per a usuaris.
 * 
 * Proporciona funcionalitats per guardar, carregar, afegir, eliminar i modificar usuaris
 *      a l'emmagatzematge persistent en format JSON.
 */

public class UsuariIO {

    private static final Gson gson = new Gson();
    public static final String BASE_DIRECTORY = "data/usuaris/";

    /**
     * Constructor de la classe Gestor_Usuari.
     */
    private UsuariIO() {
    }

    /**
     * Carrega un usuari des d'un fitxer JSON.
     * 
     * @param nom Nom de l'usuari que es vol carregar.
     * @return L'usuari carregat.
     * @throws IOException Si hi ha un error d'entrada/sortida.
     */
    public static Usuari loadUsuari(String nom) throws IOException {
        String filepath = BASE_DIRECTORY + "/" + nom + ".json";
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }
        try (FileReader reader = new FileReader(filepath)) {
            return gson.fromJson(reader, Usuari.class);
        }
    }

    /**
     * Desa un usuari a un fitxer JSON.
     * 
     * @param usuari Usuari que es vol desar.
     * @throws IOException Si hi ha un error d'entrada/sortida.
     */
    public static void saveUsuari(Usuari usuari) throws IOException {
        String filepath = BASE_DIRECTORY + "/" + usuari.getNom() + ".json"; // Usar nombre, no ID
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(usuari, writer); // Objeto correcto
        }
    }

    /**
     * Elimina un usuari esborrant el seu fitxer JSON.
     * 
     * @param nom Nom de l'usuari que es vol eliminar.
     */
    public static void eliminarUsuari(String nom) throws IOException {
        String filepath = BASE_DIRECTORY + "/" + nom + ".json";
        File file = new File(filepath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new IOException("No s'ha pogut eliminar el fitxer de l'usuari amb nom " + nom);
            }
        }
    }
}
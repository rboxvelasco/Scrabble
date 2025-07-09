package domini.scrabble;

import domini.auxiliars.ColorTerminal;
import domini.auxiliars.FastDeleteList;
import domini.excepcions.ExcepcioArxiuBossa;
import domini.excepcions.ExcepcioArxiuDiccionari;
import domini.excepcions.ExcepcioDomini;
import java.io.*;
import java.util.*;

/**
 * Representa la bossa de fitxes d'Scrabble.
 *
 * La bossa conté una col·lecció de fitxes que es poden utilitzar durant el joc.
 * També permet gestionar les fitxes, com ara afegir, retirar o inicialitzar-les
 * des d'un fitxer.
 */
public class Bossa {
    private final FastDeleteList<Fitxa> fitxes;
    private Map<String, Integer> mapaPuntuacions;

    /**
     * Constructor de la classe Bossa.
     *
     * Inicialitza la bossa amb una llista buida de fitxes i carrega les fitxes
     * des del fitxer que li passem.
     *
     * @param arxiu El path al fitxer de text que conté les dades de les fitxes.
     */
    public Bossa(String arxiu) {
        this.fitxes = new FastDeleteList<>();
        this.mapaPuntuacions = new HashMap<>();

        try {
            carregarFitxes(arxiu);
        }
        catch (ExcepcioArxiuDiccionari e) {
            e.printStackTrace();
            System.exit(0);
        }
        catch (ExcepcioDomini e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Obté la puntuació associada a una lletra.
     *
     * @param lletra La lletra de la qual es vol obtenir la puntuació.
     * @return La puntuació de la lletra, o 0 si no existeix.
     */
    public int obtenirPuntuacio(String lletra) {
        return mapaPuntuacions.getOrDefault(lletra.toUpperCase(), 0);
    }

    /**
     * Obté l'identificador d'una fitxa segons la seva lletra.
     *
     * @param lletra La lletra de la fitxa.
     * @return L'identificador de la fitxa, o -1 si no es troba.
     */
    public int getIdFitxa(String lletra) {
        for (int i = 0; i < fitxes.size(); ++i) {
            if (fitxes.get(i).getLletra().equalsIgnoreCase(lletra))
                return(fitxes.get(i).getId());
        }
        return -1; // Retorna -1 si no es troba la fitxa
    }

    /**
     * Retira una fitxa aleatòria de la bossa.
     *
     * Selecciona una fitxa de manera aleatòria de la bossa,
     * la treu i la retorna. Si no hi ha fitxes disponibles, imprimeix un missatge
     * indicant que la bossa està buida i retorna null.
     *
     * @return La fitxa agafada o bé null si no hi ha fitxes disponibles.
     */
    public Fitxa agafarFitxa() {
        if (fitxes.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(fitxes.size());
        Fitxa seleccionada = fitxes.get(index);
        fitxes.removeAt(index);
        return seleccionada;
    }

    /**
     * Inicialitza la bossa amb les fitxes des d'un fitxer de text.
     *
     * Llegeix un fitxer de text que conté la informació sobre les fitxes
     * disponibles per al joc.
     * Cada línia del fitxer ha de tenir el format: <lletra> <quantitat>
     * <puntuació>.
     * Per cada línia, es crea una nova fitxa i es guarda a la bossa amb la
     * quantitat indicada.
     *
     * @throws ExcepcioDomini Si hi ha un error en llegir el fitxer o en processar
     * @param Txt El path al fitxer de text que conté la informació de les fitxes a
     *            carregar.
     *            El fitxer ha de tenir el format: <lletra> <quantitat> <puntuació>.
     */
    public void carregarFitxes(String Txt) throws ExcepcioDomini {
        int id = 0;

        try (BufferedReader input = new BufferedReader(new FileReader("resources/" + Txt))) {
            String fila;
            while ((fila = input.readLine()) != null) {
                String[] components = fila.split(" ");
                if (components.length == 3) {
                    String lletra = components[0];
                    int quantitat = Integer.parseInt(components[1]);
                    int puntuacio = Integer.parseInt(components[2]);

                    mapaPuntuacions.put(lletra.toUpperCase(), puntuacio);
                    for (int i = 0; i < quantitat; i++) {
                        fitxes.add(new Fitxa(id++, lletra, puntuacio));
                    }
                }
            }
        }
        catch (FileNotFoundException fnfe) {
            // Encapsulem la causa en la nostra ExcepcioArxiuDiccionari
            throw new ExcepcioArxiuBossa(Txt, fnfe);
        } catch (IOException ioe) {
            // Capturem altres I/O errors que puguin ocórrer
            throw new ExcepcioDomini("Error durant la obtenció del set de fitxes: " + Txt, ioe);
        }
    }

    /**
     * Buida la bossa i l'omple amb les fitxes proporcionades.
     *
     * Borra la bossa actual i li assigna la configuració inicial
     * amb les fitxes que es passin com a paràmetre.
     *
     * @param fitxesIni La nova llista de fitxes per reomplir la bossa.
     */
    public void clearBossa(List<Fitxa> fitxesIni) {
        this.fitxes.clear();
        this.fitxes.addAll(fitxesIni);
    }

    /**
     * Obté el nombre total de fitxes disponibles a la bossa.
     *
     * @return El nombre de fitxes a la bossa.
     */
    public int getMidaBossa() {
        return fitxes.size();
    }

    /**
     * Afegeix una nova fitxa a la bossa.
     *
     * @param id        L'identificador de la fitxa.
     * @param lletra    La lletra de la fitxa.
     * @param puntuacio La puntuació associada a la fitxa.
     */
    public void afegirFitxaBossa(int id, String lletra, int puntuacio) {
        Fitxa newFitxa = new Fitxa(id, lletra, puntuacio);
        fitxes.add(newFitxa);
    }

    /**
     * Obté les fitxes de la bossa.
     *
     * @return Una llista amb les fitxes de la bossa.
     */
    public FastDeleteList<Fitxa> getFitxes() {
        return fitxes;
    }
}
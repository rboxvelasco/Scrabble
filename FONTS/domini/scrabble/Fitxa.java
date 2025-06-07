package domini.scrabble;

/**
 * Representa una fitxa d'Scrabble.
 * Cada fitxa té un identificador, una lletra i una puntuació assignada
 */
public class Fitxa {
    private int id;
    private final String lletra;
    private final int puntuacio;

    /**
     * Constructor de la classe Fitxa.
     *
     * Inicialitza una fitxa del joc de Scrabble amb un identificador, una lletra i
     * una puntuació.
     * 
     * @param id        El identificador de la fitxa afegida.
     * @param lletra    La lletra assignada a la fitxa
     * @param puntuacio La puntuació assignada a cada fitxa.
     */
    public Fitxa(int id, String lletra, int puntuacio) {
        this.id = id;
        this.lletra = lletra;
        this.puntuacio = puntuacio;
    }

    /**
     * Retorna el identificador de la fitxa.
     *
     * @return L'id de la fitxa.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna la lletra associada a la fitxa.
     *
     * @return La lletra de la fitxa.
     */
    public String getLletra() {
        return lletra;
    }

    /**
     * Retorna la puntuació associada a la fitxa.
     *
     * @return La puntuació de la fitxa.
     */
    public int getPuntuacio() {
        return puntuacio;
    }

    /**
     * Compara un objecte amb una instància de Fitxa.
     *
     * @param id Objecte a comparar amb la instància de Fitxa.
     * @return   True si els dos objectes són iguals, False en cas contrari
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Fitxa fitxa = (Fitxa) obj;
        return id == fitxa.id;
    }
}
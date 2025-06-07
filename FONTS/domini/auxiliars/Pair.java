package domini.auxiliars;

/**
 * Representa una parella de valors (first, second).
 *
 * La classe "Pair" és genèrica i permet emmagatzemar dos valors de tipus
 * diferents.
 * És útil per representar relacions o associacions entre dos objectes.
 *
 * @param <T> El tipus del primer element de la parella.
 * @param <U> El tipus del segon element de la parella.
 */
public class Pair<T, U> {
    private final T first;
    private final U second;

    /**
     * Constructor de la classe "Pair".
     *
     * @param first  El primer element de la parella.
     * @param second El segon element de la parella.
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Constructor de còpia de la classe "Pair".
     *
     * Crea una nova instància de "Pair" copiant els valors d'una altra instància.
     *
     * @param original La instància original de la qual es copien els valors.
     */
    public Pair(Pair<T, U> original) {
        this.first = original.getFirst();
        this.second = original.getSecond();
    }

    /**
     * Obté el primer element de la parella.
     *
     * @return El primer element de la parella.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Obté el segon element de la parella.
     *
     * @return El segon element de la parella.
     */
    public U getSecond() {
        return second;
    }

    /**
     * Compara aquesta parella amb un altre objecte.
     *
     * @param o L'objecte amb el qual es vol comparar.
     * @return true si els dos objectes són iguals, false en cas contrari.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    /**
     * Retorna una representació en format String de la parella.
     *
     * @return Una cadena de text que representa la parella.
     */
    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
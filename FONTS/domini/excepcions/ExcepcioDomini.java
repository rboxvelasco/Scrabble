package domini.excepcions;

/**
 * Excepció arrel de tot el paquet domini.
 * Permet agrupar i capturar en bloc error del domini.
 */
public class ExcepcioDomini extends Exception {
    public ExcepcioDomini(String msg) {
        super(msg);
    }
    public ExcepcioDomini(String msg, Throwable causa) {
        super(msg, causa);
    }
}
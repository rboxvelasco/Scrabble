package domini.excepcions;


/**
 * Problemes al carregar el fitxer del set de fitxes.
 */
public class ExcepcioArxiuBossa extends ExcepcioDomini {
    public ExcepcioArxiuBossa(String nomArxiu, Throwable causa) {
        super("No s'ha pogut obtenir el set de fitxes de l'idioma: " + nomArxiu, causa);
    }
}
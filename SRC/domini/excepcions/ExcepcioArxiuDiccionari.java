package domini.excepcions;


/**
 * Problemes al carregar el fitxer de diccionari.
 */
public class ExcepcioArxiuDiccionari extends ExcepcioDomini {
    public ExcepcioArxiuDiccionari(String nomArxiu, Throwable causa) {
        super("No s'ha pogut llegir el diccionari: " + nomArxiu, causa);
    }
}
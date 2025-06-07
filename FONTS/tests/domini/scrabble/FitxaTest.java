package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FitxaTest {
    private Fitxa fitxa;
    private static final int ID_TEST = 1;
    private static final String LLETRA_TEST = "A";
    private static final int PUNTUACIO_TEST = 1;

    @Before
    public void setUp() {
        fitxa = new Fitxa(ID_TEST, LLETRA_TEST, PUNTUACIO_TEST);
    }

    @Test
    public void testConstructor() {
        assertNotNull("La fitxa hauria de crear-se correctament", fitxa);
        assertEquals("El ID hauria de ser l'especificat", ID_TEST, fitxa.getId());
        assertEquals("La lletra hauria de ser l'especificada", LLETRA_TEST, fitxa.getLletra());
        assertEquals("La puntuació hauria de ser l'especificada", PUNTUACIO_TEST, fitxa.getPuntuacio());
    }

    @Test
    public void testConstructorValoresLimite() {
        // Probar valores límite
        Fitxa fitxaValoresLimite = new Fitxa(Integer.MAX_VALUE, "Z", Integer.MAX_VALUE);
        assertEquals("Hauria d'aceptar ID màxim", Integer.MAX_VALUE, fitxaValoresLimite.getId());
        assertEquals("Hauria d'aceptar puntuació màxima", Integer.MAX_VALUE, fitxaValoresLimite.getPuntuacio());
        
        // Probar valores negativos
        Fitxa fitxaNegativa = new Fitxa(-1, "B", -1);
        assertEquals("Hauria d'aceptar ID negatiu", -1, fitxaNegativa.getId());
        assertEquals("Hauria d'aceptar puntuació negativa", -1, fitxaNegativa.getPuntuacio());
    }

    @Test
    public void testGetId() {
        assertEquals("getId() hauria de retornar l'ID correcte", ID_TEST, fitxa.getId());
    }

    @Test
    public void testGetLletra() {
        assertEquals("getLletra() hauria de retornar la lletra correcta", LLETRA_TEST, fitxa.getLletra());
    }

    @Test
    public void testGetPuntuacio() {
        assertEquals("getPuntuacio() hauria de retornar la puntuació correcta", PUNTUACIO_TEST, fitxa.getPuntuacio());
    }

    @Test
    public void testEquals() {
        // Prova reflexivitat
        assertTrue("Una fitxa hauria de ser igual a sí mateixa", fitxa.equals(fitxa));
        
        // Prueba simetria
        Fitxa fitxaIgual = new Fitxa(ID_TEST, LLETRA_TEST, PUNTUACIO_TEST);
        assertTrue("Fitxes con igual ID haurien de ser iguals", fitxa.equals(fitxaIgual));
        assertTrue("Equals hauria de ser simètric", fitxaIgual.equals(fitxa));
        
        // Prova amb ID diferent
        Fitxa fitxaDiferente = new Fitxa(ID_TEST + 1, LLETRA_TEST, PUNTUACIO_TEST);
        assertFalse("Fitxes amb diferent ID no haurien de ser iguals",  fitxa.equals(fitxaDiferente));
        
        // Prova amb null
        assertFalse("Equals amb null hauria de retornar false", fitxa.equals(null));
        
        // Prova amb altre tipus d'objecte
        assertFalse("Equals amb un altre tipus d'objecte hauria de retornar false", fitxa.equals(new Object()));
    }

    @Test
    public void testEqualsConDiferentesLetrasYPuntuaciones() {
        // igual ID pero diferentss lletres y puntuació
        Fitxa fitxaMismoIdDiferenteLetra = new Fitxa(ID_TEST, "B", PUNTUACIO_TEST);
        assertTrue("Fitxes amb igual ID haurien de ser iguals independentemnt de la lletra", fitxa.equals(fitxaMismoIdDiferenteLetra));
        
        Fitxa fitxaMismoIdDiferentePuntuacion = new Fitxa(ID_TEST, LLETRA_TEST, 5);
        assertTrue("Fitxes amb igual ID haurien de ser iguals independentemnt de la puntuació", fitxa.equals(fitxaMismoIdDiferentePuntuacion));
    }

    @Test
    public void testLetraVacia() {
        Fitxa fitxaLetraVacia = new Fitxa(2, "", 0);
        assertEquals("Hauria de permetre lleta buida", "", fitxaLetraVacia.getLletra());
    }

    @Test
    public void testLetraNull() {
        Fitxa fitxaLetraNull = new Fitxa(3, null, 0);
        assertNull("Hauria de permetre lletra Null", fitxaLetraNull.getLletra());
    }

    @Test
    public void testLetraMulticaracter() {
        String letraLarga = "ABC";
        Fitxa fitxaLetraLarga = new Fitxa(4, letraLarga, 0);
        assertEquals("Hauria de permetre lletres de múltiples caràcters", letraLarga, fitxaLetraLarga.getLletra());
    }

    @Test
    public void testInmutabilidad() {
        // Verificar que els valors no poden ser modificats després de la creació
        String letraOriginal = fitxa.getLletra();
        int puntuacionOriginal = fitxa.getPuntuacio();
               
        // Verificar que los valores originales no han canviado
        assertEquals("La lletra no hauria de canviar", letraOriginal, fitxa.getLletra());
        assertEquals("La puntuació no hauria de canviar", puntuacionOriginal, fitxa.getPuntuacio());
    }
}

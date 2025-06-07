package domini.diccionari;

import org.junit.Before;
import org.junit.Test;

import domini.excepcions.ExcepcioArxiuDiccionari;
import domini.excepcions.ExcepcioDomini;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

public class DAWGTest {
    private DAWG dawg;

    @Before
    public void setUp() {
        dawg = new DAWG();
    }

    @Test
    public void testEmptyDAWG() {
        assertFalse(dawg.contains("TEST"));
        assertTrue(dawg.getEdgesFromNode(0).isEmpty());
    }

    @Test
    public void testDictionary2DAWG() {
        try {
            dawg.dictionary2DAWG("english.txt");
            assertTrue(dawg.contains("CAT"));
            assertTrue(dawg.contains("DOG"));
            assertFalse(dawg.contains("XYZ"));
        }
        catch (ExcepcioArxiuDiccionari e) {
            fail("No s'hauria de llençar excepció en carregar diccionari: " + e.getMessage());
        }
        catch (ExcepcioDomini e) {
            System.err.println("Error de domini intern: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testDictionary2DAWGWithInvalidFile() throws IOException {
        try {
            dawg.dictionary2DAWG("nonexistent_file.txt");
            fail("Se esperaba una ExcepcioDomini");
        } catch (ExcepcioDomini e) {
            assertEquals("No s'ha pogut llegir el diccionari: nonexistent_file.txt", e.getMessage());
        }
    }

    @Test
    public void testSearchPrefixes() {
        try {
            dawg.dictionary2DAWG("english.txt");
            Set<String> prefixes = dawg.searchPrefixes("T");
            
            assertTrue(prefixes.contains("A"));
            assertTrue(prefixes.contains("#")); // Ha de contenir # si troba algun prefix
        } catch (ExcepcioDomini e) {
            fail("Error en cargar el diccionari: " + e.getMessage());
        }
    }

    @Test
    public void testSearchSufixes() {
        try {
            dawg.dictionary2DAWG("english.txt");
            Set<String> sufixes = dawg.searchSufixes("A");
            
            assertTrue(sufixes.contains("T"));
            assertTrue(sufixes.contains("#")); // Ha de contenir # si troba algun sufix
        } catch (ExcepcioDomini e) {
            fail("Error al cargar el diccionario: " + e.getMessage());
        }
    }

    @Test
    public void testGetEdgesFromNode() {
        try {
            dawg.dictionary2DAWG("english.txt");
            Set<String> edges = dawg.getEdgesFromNode(0);
            
            assertFalse(edges.isEmpty());
            // Verificar que conte algunes lletres comuns
            assertTrue(edges.contains("A") || edges.contains("T") || edges.contains("S"));
        } catch (ExcepcioDomini e) {
            fail("Error al cargar el diccionario: " + e.getMessage());
        }
    }

    @Test
    public void testGetNodeFromTransition() {
        try {
            dawg.dictionary2DAWG("english.txt");
            int nodeIndex = dawg.getNodeFromTransition(0, "A");
            
            assertTrue(nodeIndex >= -1); // Ha de ser -1 o un índex vàlid
            
            // Probar amb una transició invàlida
            assertEquals(-1, dawg.getNodeFromTransition(0, "$"));
        } catch (ExcepcioDomini e) {
            fail("Error al cargar el diccionario: " + e.getMessage());
        }
    }

    @Test
    public void testContainsWithDigraphs() {
        try {
            // Carregar diccionari espanyol que utilitza dígrafs
            dawg.dictionary2DAWG("castellano.txt");
            
            // Provar paraules amb dígrafs
            assertTrue(dawg.contains("LLUVIA")); // Contié el dígraf LL
            assertTrue(dawg.contains("CARRO")); // Conté el dígraf RR
            
            // Provar paraules sense dígrafs
            assertTrue(dawg.contains("CASA"));
            
            // Probar paraules que no hauríen de ser-hi
            assertFalse(dawg.contains("XYZABC"));
        } catch (ExcepcioDomini e) {
            fail("Error al cargar el diccionario: " + e.getMessage());
        }
    }

    @Test
    public void testContainsWithEmptyString() {
        assertFalse(dawg.contains(""));
    }

    @Test
    public void testContainsWithSpaces() {
        assertFalse(dawg.contains(" "));
        assertFalse(dawg.contains("A B"));
    }

    @Test
    public void testSearchPrefixesEmptyDAWG() {
        Set<String> prefixes = dawg.searchPrefixes("A");
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testSearchSufixesEmptyDAWG() {
        Set<String> sufixes = dawg.searchSufixes("A");
        assertTrue(sufixes.isEmpty());
    }
}
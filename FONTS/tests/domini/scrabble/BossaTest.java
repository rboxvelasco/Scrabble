package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BossaTest {
    private Bossa bossa;
    private static final String ARXIU_TEST = "letrascatalan.txt";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private void crearArxiuTest() throws IOException {
        File resourcesDir = tempFolder.newFolder("resources");
        File arxiuTest = new File(resourcesDir, ARXIU_TEST);
        
        try (FileWriter writer = new FileWriter(arxiuTest)) {
            writer.write("A 9 1\n");   // 9 A's con 1 punto
            writer.write("B 2 3\n");   // 2 B's con 3 puntos
            writer.write("C 2 3\n");   // 2 C's con 3 puntos
            writer.write("# 2 0\n");   // 2 comodines con 0 puntos
        }
    }

    @Before
    public void setUp() throws IOException {
        crearArxiuTest();
        bossa = new Bossa(ARXIU_TEST);
    }

    @Test
    public void testConstructor() {
        assertNotNull("La bossa hauria de crearse correctamente", bossa);
    }

    @Test
    public void testObtenirPuntuacio() {
        assertEquals("La puntuació de 'A' hauria de ser 1", 1, bossa.obtenirPuntuacio("A"));
        assertEquals("La puntuació de 'B' hauria de ser 3", 3, bossa.obtenirPuntuacio("B"));
        assertEquals("La puntuació d'una lletra inexistent hauria de ser 0", 0, bossa.obtenirPuntuacio("Ñ"));
    }

    @Test
    public void testGetIdFitxa() {
        int idA = bossa.getIdFitxa("A");
        assertTrue("L'ID d'una fitxa existent hauria de ser >= 0", idA >= 0);
        assertEquals("L'ID d'una fitxa inexistent hauria de ser -1", -1, bossa.getIdFitxa("Ñ"));
    }

    @Test
    public void testAgafarFitxa() {
        int midaInicial = bossa.getMidaBossa();
        Fitxa fitxa = bossa.agafarFitxa();
        
        assertNotNull("Hauria de retornar una fitxa vàlida", fitxa);
        assertEquals("La miad de la bossa hauria de decrementar", midaInicial - 1, bossa.getMidaBossa());
    }

    @Test
    public void testAgafarFitxaBuidaBossa() {
        // Buidar la bossa
        while (bossa.getMidaBossa() > 0) {
            bossa.agafarFitxa();
        }
        
        assertNull("Hauria de retornar null quan la bossa està buida", bossa.agafarFitxa());
    }

    @Test
    public void testDistribucioFitxes() {
        // Verificar que les quantitats de fitxes son correctes
        int contadorA = 0;
        int contadorB = 0;
        Set<Fitxa> fitxesAgafades = new HashSet<>();
        
        while (bossa.getMidaBossa() > 0) {
            Fitxa f = bossa.agafarFitxa();
            fitxesAgafades.add(f);
            if (f.getLletra().equals("A")) contadorA++;
            if (f.getLletra().equals("B")) contadorB++;
        }
        
        assertEquals("Hauria d'haver 12 fitxes 'A'", 12, contadorA);
        assertEquals("Hauria d'haver 2 fitxes 'B'", 2, contadorB);
    }

    @Test
    public void testClearBossa() {
        List<Fitxa> novesFitxes = new ArrayList<>();
        novesFitxes.add(new Fitxa(1, "X", 8));
        novesFitxes.add(new Fitxa(2, "Y", 4));
        
        bossa.clearBossa(novesFitxes);
        
        assertEquals("La bossa hauria de tenir la nova mida", novesFitxes.size(), bossa.getMidaBossa());
        
        Fitxa primera = bossa.agafarFitxa();
        assertTrue("La bossa hauria de contenir les noves fitxes", primera.getLletra().equals("X") || primera.getLletra().equals("Y"));
    }

    @Test
    public void testGetMidaBossa() {
        int midaInicial = bossa.getMidaBossa();
        assertTrue("La bossa hauria de tenir fitxes inicialment", midaInicial > 0);
        
        bossa.agafarFitxa();
        assertEquals("La mida hauria de decrementar en treure una fitxa", midaInicial - 1, bossa.getMidaBossa());
    }

    @Test
    public void testAfegirFitxaBossa() {
        int midaInicial = bossa.getMidaBossa();
        bossa.afegirFitxaBossa(100, "Z", 10);
        
        assertEquals("La mida hauria d'incrementar en afegir una fitxa", midaInicial + 1, bossa.getMidaBossa());
        
        boolean trobada = false;
        while (bossa.getMidaBossa() > 0) {
            Fitxa f = bossa.agafarFitxa();
            if (f.getLletra().equals("Z") && f.getPuntuacio() == 10) {
                trobada = true;
                break;
            }
        }
        
        assertTrue("La fitxa aefgida hauria de poder recuperar-se", trobada);
    }

    @Test
    public void testPuntuacionsComodins() {
        assertEquals("La puntuació del comodí hauria de ser 0", 0, bossa.obtenirPuntuacio("#"));
    }

    @Test
    public void testCaseSensitive() {
        assertEquals("La puntuació hauria de ser independent de majúscules/minúscules", bossa.obtenirPuntuacio("a"), bossa.obtenirPuntuacio("A"));
    }
}

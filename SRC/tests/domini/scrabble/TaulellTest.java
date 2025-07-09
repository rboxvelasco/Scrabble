package domini.scrabble;

import domini.auxiliars.Pair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TaulellTest {
    private Taulell taulell;
    private static final int MIDA_TAULELL = 15;

    @Before
    public void setUp() {
        taulell = new Taulell(MIDA_TAULELL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMidaNegativa() {
        new Taulell(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMidaCero() {
        new Taulell(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMidaPar() {
        new Taulell(14);
    }

    @Test
    public void testGetMida() {
        assertEquals("La mida del taulell hauria de ser la especificada", MIDA_TAULELL, taulell.getMida());
    }

    @Test
    public void testEstaBuit() {
        assertTrue("El taulell hauria de ser buit inicialment", taulell.estaBuit());

        // Afegir una fitxa i comprovar que ja no està buit
        Fitxa fitxa = new Fitxa(1, "A", 1);
        taulell.getCasella(7, 7).afegirFitxa(fitxa);
        assertFalse("El taulell no hauria de ser buit després d'afegir una fitxa", taulell.estaBuit());
    }

    @Test
    public void testLimits() {
        assertTrue("Posició vàlida, hauria d'estar dins dels límits", taulell.limits(0, 0));
        assertTrue("Posició vàlida, hauria d'estar dins dels límits", taulell.limits(MIDA_TAULELL-1, MIDA_TAULELL-1));
        assertFalse("Posició negativa que no hauria d'estar dins dels límits", taulell.limits(-1, 0));
        assertFalse("Posición major que la mida no hauria d'estar dins dels límits", taulell.limits(MIDA_TAULELL, 0));
    }

    @Test
    public void testGetCasella() {
        Casella casella = taulell.getCasella(0, 0);
        assertNotNull("Hauria de retornar casella buida", casella);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCasellaFueraLimites() {
        taulell.getCasella(-1, 0);
    }

    @Test
    public void testGetAnchors() {
        // En un taulel buit, només el centre és anchor
        Set<Pair<Integer, Integer>> anchors = taulell.getAnchors();
        assertEquals("Només hauria d'haver un anchor en un taulell buit", 1, anchors.size());
        
        Pair<Integer, Integer> centro = taulell.getCentreTaulell();
        boolean correct = false;
        for (Pair<Integer, Integer> a : anchors) {
            if (a.equals(centro)) correct = true;
        }
        assertTrue("L'anchor hauria de ser el centre del taulell", correct);

        // Afegir fitxa i comprovar anchors adjacents
        Fitxa fitxa = new Fitxa(1, "A", 1);
        List<Fitxa> word = new ArrayList<Fitxa>();
        word.add(fitxa);
        taulell.inserirParaula(7, 7, true, word, "", "");

        anchors = taulell.getAnchors();

        assertTrue("Haurien d'existir anchors al voltant de la fitxa col·locada", anchors.size() > 1);
    }

    @Test
    public void testTranspose() {
        // Col·locar fitxa en una posició
        Fitxa fitxa = new Fitxa(1, "A", 1);
        taulell.getCasella(1, 2).afegirFitxa(fitxa);
        
        Taulell transposed = taulell.transpose();
        
        assertTrue("La fitxa hauria d'estar a la posició trasposada", transposed.getCasella(2, 1).EstaOcupat());
        assertEquals("La fitxa trasposada hauria de ser la mateixa", fitxa.getLletra(), transposed.getCasella(2, 1).getFitxa().getLletra());
    }

    @Test
    public void testInserirParaula() {
        List<Fitxa> paraula = new ArrayList<>();
        paraula.add(new Fitxa(1, "C", 1));
        paraula.add(new Fitxa(2, "A", 1));
        paraula.add(new Fitxa(3, "S", 1));
        paraula.add(new Fitxa(4, "A", 1));
        
        int puntuacio = taulell.inserirParaula(7, 7, true, paraula, "", "");
        
        assertTrue("La primera lletra hauria d'estar col·locada", taulell.getCasella(7, 7).EstaOcupat());
        assertTrue("La última lletra hauria d'estar col·locada", taulell.getCasella(7, 10).EstaOcupat());
        assertTrue("La puntuació hauria de ser positiva", puntuacio > 0);
    }

    @Test
    public void testGetCentreTaulell() {
        Pair<Integer, Integer> centro = taulell.getCentreTaulell();
        assertEquals("La coordenada X del centre hauria de ser correcta", MIDA_TAULELL/2, (int)centro.getFirst());
        assertEquals("La coordenada Y del centre hauria de ser correcta", MIDA_TAULELL/2, (int)centro.getSecond());
    }

    @Test
    public void testInserirParaulaConMultiplicadores() {
        List<Fitxa> paraula = new ArrayList<>();
        paraula.add(new Fitxa(1, "C", 2));
        paraula.add(new Fitxa(2, "A", 1));
        paraula.add(new Fitxa(3, "S", 3));
        paraula.add(new Fitxa(4, "A", 1));
        
        // Insertar en una posició amb multiplicadors
        int puntuacioNormal = taulell.inserirParaula(7, 7, true, paraula, "", "");
        taulell = new Taulell(MIDA_TAULELL); // Reset taulell
        int puntuacioMultiplicada = taulell.inserirParaula(0, 0, true, paraula, "", "");
        
        assertNotEquals("Les puntuaciones haurien de ser diferents amb multiplicadors", puntuacioNormal, puntuacioMultiplicada);
    }

    @Test
    public void testInserirParaulaVertical() {
        List<Fitxa> paraula = new ArrayList<>();
        paraula.add(new Fitxa(1, "C", 1));
        paraula.add(new Fitxa(2, "A", 1));
        paraula.add(new Fitxa(3, "S", 1));
        paraula.add(new Fitxa(4, "A", 1));
        
        taulell.inserirParaula(7, 7, false, paraula, "", "");
        
        assertTrue("La primera lletra hauria d'estar col·locada", taulell.getCasella(7, 7).EstaOcupat());
        assertTrue("La última lletra hauria d'estar col·locada", taulell.getCasella(10, 7).EstaOcupat());
    }
}

package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import domini.auxiliars.Pair;

public class CasellaTest {
    private Casella casella;
    private static final int MIDA_TAULELL = 15;

    @Before
    public void setUp() {
        casella = new Casella(7, 7, MIDA_TAULELL);
    }

    @Test
    public void testConstructor() {
        assertEquals(new Pair<>(7, 7), casella.getCoord());
        assertNull(casella.getFitxa());
        assertFalse(casella.isAnchor());
        
        // Verificar que tots els cross_checks estan inicializats a true
        boolean[] crossChecks = casella.getCrossChecks();
        assertEquals(34, crossChecks.length);
        for (boolean check : crossChecks) {
            assertTrue(check);
        }
    }

    @Test
    public void testSetMultiplicadorCasellaTripleParaula() {
        Casella casellaTP = new Casella(0, 0, MIDA_TAULELL);
        assertEquals(Multiplicador.TRIPLE_PARAULA, casellaTP.getMultiplicador());
        
        // Verificar una altra posició de triple paraula
        casellaTP = new Casella(0, MIDA_TAULELL/2, MIDA_TAULELL);
        assertEquals(Multiplicador.TRIPLE_PARAULA, casellaTP.getMultiplicador());
    }

    @Test
    public void testSetMultiplicadorCasellaDobleParaula() {
        Casella casellaDP = new Casella(1, 1, MIDA_TAULELL);
        assertEquals(Multiplicador.DOBLE_PARAULA, casellaDP.getMultiplicador());
    }

    @Test
    public void testSetMultiplicadorCasellaTripleLletra() {
        Casella casellaTL = new Casella(5, 5, MIDA_TAULELL);
        assertEquals(Multiplicador.TRIPLE_LLETRA, casellaTL.getMultiplicador());
    }

    @Test
    public void testSetMultiplicadorCasellaDobleLletra() {
        Casella casellaDL = new Casella(3, 0, MIDA_TAULELL);
        assertEquals(Multiplicador.DOBLE_LLETRA, casellaDL.getMultiplicador());
    }

    @Test
    public void testSetMultiplicadorCasellaNormal() {
        Casella casellaNormal = new Casella(7, 8, MIDA_TAULELL);
        assertEquals(Multiplicador.CAP, casellaNormal.getMultiplicador());
    }

    @Test
    public void testAfegirFitxa() {
        Fitxa fitxa = new Fitxa(0, "A", 1);
        
        // Afegir fitxa a casella buida
        assertTrue(casella.afegirFitxa(fitxa));
        assertEquals(fitxa, casella.getFitxa());
        assertTrue(casella.EstaOcupat());
        
        // Intentem afegir fitxa a casella ocupada
        Fitxa otraFitxa = new Fitxa(1, "B", 2);
        assertFalse(casella.afegirFitxa(otraFitxa));
        assertEquals(fitxa, casella.getFitxa()); // La fitxa original ha de mantenir-se
    }

    @Test
    public void testRetirarFitxa() {
        Fitxa fitxa = new Fitxa(2, "A", 1);
        casella.afegirFitxa(fitxa);
        
        // Retirar fitxa correcta
        assertTrue(casella.retirarFitxa(fitxa));
        assertFalse(casella.EstaOcupat());
        assertNull(casella.getFitxa());
        
        // Intentar retirar fitxa de casella buida
        assertFalse(casella.retirarFitxa(fitxa));
        
        // Intentar retirar fitxa diferent
        casella.afegirFitxa(fitxa);
        Fitxa otraFitxa = new Fitxa(3, "B", 2);
        assertFalse(casella.retirarFitxa(otraFitxa));
        assertTrue(casella.EstaOcupat());
        assertEquals(fitxa, casella.getFitxa());
    }

    @Test
    public void testSetAndIsAnchor() {
        assertFalse(casella.isAnchor());
        
        casella.setAnchor(true);
        assertTrue(casella.isAnchor());
        
        casella.setAnchor(false);
        assertFalse(casella.isAnchor());
    }

    @Test
    public void testActualitzarCrossChecks() {
        boolean[] newCross = new boolean[34];
        // Establir alguns valors específics
        newCross[0] = false; // 'A'
        newCross[1] = true;  // 'B'
        
        casella.actualitzarCrossChecks(newCross);
        
        boolean[] updatedCrossChecks = casella.getCrossChecks();
        assertFalse(updatedCrossChecks[0]);
        assertTrue(updatedCrossChecks[1]);
    }

    @Test
    public void testReiniciarCrossCheck() {
        boolean[] newCross = new boolean[34];
        for (int i = 0; i < newCross.length; i++) {
            newCross[i] = false;
        }
        
        casella.actualitzarCrossChecks(newCross);
        casella.reiniciarCrossCheck();
        
        boolean[] resetCrossChecks = casella.getCrossChecks();
        for (boolean check : resetCrossChecks) {
            assertTrue(check);
        }
    }

    @Test
    public void testEstaOcupat() {
        assertFalse(casella.EstaOcupat());
        
        Fitxa fitxa = new Fitxa(4, "A", 1);
        casella.afegirFitxa(fitxa);
        assertTrue(casella.EstaOcupat());
        
        casella.retirarFitxa(fitxa);
        assertFalse(casella.EstaOcupat());
    }
}

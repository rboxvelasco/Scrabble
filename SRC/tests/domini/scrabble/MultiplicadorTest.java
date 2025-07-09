package domini.scrabble;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiplicadorTest {

    @Test
    public void testValoresMultiplicador() {
        assertEquals("CAP hauria de tenir valor 1", 1, Multiplicador.CAP.getMultiplicador());
        assertEquals("DOBLE_LLETRA hauria de tenir 2", 2, Multiplicador.DOBLE_LLETRA.getMultiplicador());
        assertEquals("TRIPLE_LLETRA hauria de tenir valor 3", 3, Multiplicador.TRIPLE_LLETRA.getMultiplicador());
        assertEquals("DOBLE_PARAULA hauria de tenir valor 2", 2, Multiplicador.DOBLE_PARAULA.getMultiplicador());
        assertEquals("TRIPLE_PARAULA hauria de tenir valor 3", 3, Multiplicador.TRIPLE_PARAULA.getMultiplicador());
    }

    @Test
    public void testEnumValues() {
        Multiplicador[] valores = Multiplicador.values();
        assertEquals("Deberían existir 5 tipos de multiplicadores", 
                    5, valores.length);
        
        // Verificar que tots els valors esperats estan presents
        assertTrue("Hauria d'existir el Multiplicador CAP", containsMultiplicador(valores, Multiplicador.CAP));
        assertTrue("Hauria d'existir el Multiplicador DOBLE_LLETRA", containsMultiplicador(valores, Multiplicador.DOBLE_LLETRA));
        assertTrue("Hauria d'existir el Multiplicador TRIPLE_LLETRA", containsMultiplicador(valores, Multiplicador.TRIPLE_LLETRA));
        assertTrue("Hauria d'existir el Multiplicador DOBLE_PARAULA", containsMultiplicador(valores, Multiplicador.DOBLE_PARAULA));
        assertTrue("Hauria d'existir el Multiplicador TRIPLE_PARAULA", containsMultiplicador(valores, Multiplicador.TRIPLE_PARAULA));
    }

    private boolean containsMultiplicador(Multiplicador[] valores, Multiplicador multiplicador) {
        for (Multiplicador m : valores) {
            if (m == multiplicador) return true;
        }
        return false;
    }

    @Test
    public void testValueOf() {
        assertEquals("valueOf hauria de tornar CAP correctament", Multiplicador.CAP, Multiplicador.valueOf("CAP"));
        assertEquals("valueOf hauria de tornar DOBLE_LLETRA correctament", Multiplicador.DOBLE_LLETRA, Multiplicador.valueOf("DOBLE_LLETRA"));
        assertEquals("valueOf hauria de tornar TRIPLE_LLETRA correctament", Multiplicador.TRIPLE_LLETRA, Multiplicador.valueOf("TRIPLE_LLETRA"));
        assertEquals("valueOf hauria de tornar DOBLE_PARAULA correctament", Multiplicador.DOBLE_PARAULA, Multiplicador.valueOf("DOBLE_PARAULA"));
        assertEquals("valueOf hauria de tornar TRIPLE_PARAULA correctament", Multiplicador.TRIPLE_PARAULA, Multiplicador.valueOf("TRIPLE_PARAULA"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalido() {
        Multiplicador.valueOf("VALOR_INVALID");
    }

    @Test
    public void testComparacion() {
        // Verificar que les constants enum son singleton
        Multiplicador cap1 = Multiplicador.CAP;
        Multiplicador cap2 = Multiplicador.CAP;
        assertTrue("La mateixa constant enum hauria de ser igual utilitzant ==", cap1 == cap2);
        assertTrue("La mateixa constant enum hauria de ser igual utilitzant equals()", cap1.equals(cap2));
    }

    @Test
    public void testOrdinal() {
        assertEquals("CAP hauria de tenir ordinal 0", 0, Multiplicador.CAP.ordinal());
        assertEquals("DOBLE_LLETRA hauria de tenir ordinal 1", 1, Multiplicador.DOBLE_LLETRA.ordinal());
        assertEquals("TRIPLE_LLETRA hauria de tenir ordinal 2", 2, Multiplicador.TRIPLE_LLETRA.ordinal());
        assertEquals("DOBLE_PARAULA hauria de tenir ordinal 3", 3, Multiplicador.DOBLE_PARAULA.ordinal());
        assertEquals("TRIPLE_PARAULA hauria de tenir ordinal 4", 4, Multiplicador.TRIPLE_PARAULA.ordinal());
    }

    @Test
    public void testToString() {
        assertEquals("toString de CAP hauria de ser 'CAP'", "CAP", Multiplicador.CAP.toString());
        assertEquals("toString de DOBLE_LLETRA hauria de ser 'DOBLE_LLETRA'", "DOBLE_LLETRA", Multiplicador.DOBLE_LLETRA.toString());
        assertEquals("toString de TRIPLE_LLETRA hauria de ser  'TRIPLE_LLETRA'", "TRIPLE_LLETRA", Multiplicador.TRIPLE_LLETRA.toString());
        assertEquals("toString de DOBLE_PARAULA hauria de ser  'DOBLE_PARAULA'", "DOBLE_PARAULA", Multiplicador.DOBLE_PARAULA.toString());
        assertEquals("toString de TRIPLE_PARAULA hauria de ser 'TRIPLE_PARAULA'", "TRIPLE_PARAULA", Multiplicador.TRIPLE_PARAULA.toString());
    }

    @Test
    public void testInmutabilidad() {
        // Verificar que los valores no pueden ser modificados
        int valorInicial = Multiplicador.CAP.getMultiplicador();
        // No hay manera de modificar el valor, pero verificamos que sigue siendo el mismo
        assertEquals("El valor del multiplicador no hauria de canviar", valorInicial, Multiplicador.CAP.getMultiplicador());
    }
}
package domini.auxiliars;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import domini.scrabble.Fitxa;
import java.util.ArrayList;
import java.util.List;

public class MaxWordTest {
    private MaxWord maxWord;

    @Before
    public void setUp() {
        maxWord = new MaxWord();
    }

    @Test
    public void testConstructorDefault() {
        assertTrue(maxWord.getWord().isEmpty());
        assertEquals(0, maxWord.getPoints());
        assertEquals(new Pair<>(-1, -1), maxWord.getCoordinates());
        assertTrue(maxWord.getHorizontal());
    }

    @Test
    public void testSetAndGetWord() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(0, "A", 1));
        fitxes.add(new Fitxa(1, "B", 2));
        
        maxWord.setWord(fitxes);
        
        assertEquals(fitxes, maxWord.getWord());
        assertEquals(2, maxWord.getWord().size());
        assertEquals("A", maxWord.getWord().get(0).getLletra());
        assertEquals("B", maxWord.getWord().get(1).getLletra());
    }

    @Test
    public void testSetAndGetPoints() {
        maxWord.setPoints(10);
        assertEquals(10, maxWord.getPoints());
        
        maxWord.setPoints(20);
        assertEquals(20, maxWord.getPoints());
    }

    @Test
    public void testSetAndGetCoordinates() {
        Pair<Integer, Integer> coords = new Pair<>(5, 7);
        maxWord.setCoordinates(coords);
        
        assertEquals(coords, maxWord.getCoordinates());
        assertEquals(Integer.valueOf(5), maxWord.getCoordinates().getFirst());
        assertEquals(Integer.valueOf(7), maxWord.getCoordinates().getSecond());
    }

    @Test
    public void testSetAndGetHorizontal() {
        assertTrue(maxWord.getHorizontal()); // valor por defecte
        
        maxWord.setHorizontal(false);
        assertFalse(maxWord.getHorizontal());
        
        maxWord.setHorizontal(true);
        assertTrue(maxWord.getHorizontal());
    }

    @Test
    public void testCompleteWordUpdate() {
        // Crear una paraula completa amb tots els atributs
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(2, "C", 3));
        fitxes.add(new Fitxa(3, "A", 1));
        fitxes.add(new Fitxa(4, "S", 1));
        fitxes.add(new Fitxa(5, "A", 1));
        
        Pair<Integer, Integer> coords = new Pair<>(2, 3);
        
        maxWord.setWord(fitxes);
        maxWord.setPoints(15);
        maxWord.setCoordinates(coords);
        maxWord.setHorizontal(true);
        
        // Verificar todos los valores
        assertEquals(fitxes, maxWord.getWord());
        assertEquals(15, maxWord.getPoints());
        assertEquals(coords, maxWord.getCoordinates());
        assertTrue(maxWord.getHorizontal());
    }

    @Test
    public void testEmptyWordUpdate() {
        List<Fitxa> fitxesVacias = new ArrayList<>();
        maxWord.setWord(fitxesVacias);
        maxWord.setPoints(0);
        maxWord.setCoordinates(new Pair<>(-1, -1));
        
        assertTrue(maxWord.getWord().isEmpty());
        assertEquals(0, maxWord.getPoints());
        assertEquals(new Pair<>(-1, -1), maxWord.getCoordinates());
    }
}
package domini.auxiliars;

import org.junit.Test;
import static org.junit.Assert.*;

public class PairTest {

    @Test
    public void testConstructorAndGetters() {
        Pair<String, Integer> pair = new Pair<>("test", 42);
        
        assertEquals("test", pair.getFirst());
        assertEquals(Integer.valueOf(42), pair.getSecond());
    }

    @Test
    public void testCopyConstructor() {
        Pair<String, Integer> original = new Pair<>("original", 100);
        Pair<String, Integer> copy = new Pair<>(original);
        
        assertEquals(original.getFirst(), copy.getFirst());
        assertEquals(original.getSecond(), copy.getSecond());
    }

    @Test
    public void testEqualsWithSameValues() {
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, Integer> pair2 = new Pair<>("test", 42);
        
        assertEquals(pair1, pair2);
    }

    @Test
    public void testEqualsWithDifferentValues() {
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, Integer> pair2 = new Pair<>("test", 43);
        
        assertNotEquals(pair1, pair2);
    }

    @Test
    public void testEqualsWithNull() {
        Pair<String, Integer> pair = new Pair<>("test", 42);
        
        assertNotEquals(pair, null);
    }

    @Test
    public void testEqualsWithDifferentTypes() {
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<Integer, String> pair2 = new Pair<>(42, "test");
        
        assertNotEquals(pair1, pair2);
    }

    @Test
    public void testEqualsWithSameObject() {
        Pair<String, Integer> pair = new Pair<>("test", 42);
        
        assertEquals(pair, pair);
    }

    @Test
    public void testToString() {
        Pair<String, Integer> pair = new Pair<>("test", 42);
        String expected = "Pair{first=test, second=42}";
        
        assertEquals(expected, pair.toString());
    }

    @Test
    public void testWithDifferentTypes() {
        Pair<Double, Boolean> pair = new Pair<>(3.14, true);
        
        assertEquals(Double.valueOf(3.14), pair.getFirst());
        assertEquals(true, pair.getSecond());
    }

    @Test
    public void testWithNullValues() {
        Pair<String, String> pair = new Pair<>(null, null);
        
        assertNull(pair.getFirst());
        assertNull(pair.getSecond());
    }

    @Test
    public void testWithComplexObjects() {
        StringBuilder sb1 = new StringBuilder("test1");
        StringBuilder sb2 = new StringBuilder("test2");
        Pair<StringBuilder, StringBuilder> pair = new Pair<>(sb1, sb2);
        
        assertEquals("test1", pair.getFirst().toString());
        assertEquals("test2", pair.getSecond().toString());
    }
}
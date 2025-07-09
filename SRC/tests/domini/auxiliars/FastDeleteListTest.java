package domini.auxiliars;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class FastDeleteListTest {
    private FastDeleteList<String> list;

    @Before
    public void setUp() {
        list = new FastDeleteList<>();
    }

    @Test
    public void testAddAndGet() {
        list.add("uno");
        list.add("dos");
        list.add("tres");
        
        assertEquals("uno", list.get(0));
        assertEquals("dos", list.get(1));
        assertEquals("tres", list.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.add("uno");
        assertEquals(1, list.size());
        list.add("dos");
        assertEquals(2, list.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.add("uno");
        assertFalse(list.isEmpty());
        list.remove("uno");
        assertTrue(list.isEmpty());
    }

    @Test
    public void testContains() {
        list.add("uno");
        assertTrue(list.contains("uno"));
        assertFalse(list.contains("dos"));
    }

    @Test
    public void testRemove() {
        list.add("uno");
        list.add("dos");
        list.add("tres");
        
        assertTrue(list.remove("dos"));
        assertFalse(list.contains("dos"));
        assertEquals(2, list.size());
        
        assertFalse(list.remove("noExiste"));
    }

    @Test
    public void testRemoveAt() {
        list.add("uno");
        list.add("dos");
        list.add("tres");
        
        assertTrue(list.removeAt(1));
        assertEquals(2, list.size());
        assertFalse(list.contains("dos"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtInvalidIndex() {
        list.add("uno");
        list.removeAt(1);
    }

    @Test
    public void testAddAll() {
        List<String> valores = Arrays.asList("uno", "dos", "tres");
        list.addAll(valores);
        
        assertEquals(3, list.size());
        assertTrue(list.contains("uno"));
        assertTrue(list.contains("dos"));
        assertTrue(list.contains("tres"));
    }

    @Test
    public void testClear() {
        list.add("uno");
        list.add("dos");
        assertFalse(list.isEmpty());
        
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    public void testDuplicateElements() {
        list.add("uno");
        list.add("uno");
        list.add("uno");
        
        assertEquals(3, list.size());
        assertTrue(list.contains("uno"));
        
        list.remove("uno");
        assertEquals(2, list.size());
        assertTrue(list.contains("uno"));
    }

    @Test
    public void testToString() {
        list.add("uno");
        list.add("dos");
        
        String str = list.toString();
        assertTrue(str.contains("uno"));
        assertTrue(str.contains("dos"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        list.get(0);
    }

    @Test
    public void testRemoveAndReorganization() {
        list.add("uno");
        list.add("dos");
        list.add("tres");
        
        list.removeAt(0);
        
        assertEquals(2, list.size());
        assertTrue(list.contains("dos"));
        assertTrue(list.contains("tres"));
    }
}
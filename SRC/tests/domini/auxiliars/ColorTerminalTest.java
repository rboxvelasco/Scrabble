package domini.auxiliars;

import org.junit.Test;
import static org.junit.Assert.*;

public class ColorTerminalTest {

    @Test
    public void testColorConstants() {
        // Verificar que les constants de color no son null
        assertNotNull("RESET no hauria de ser ser null", ColorTerminal.RESET);
        assertNotNull("BLACK no hauria de ser ser null", ColorTerminal.BLACK);
        assertNotNull("RED no hauria de ser ser null", ColorTerminal.YELLOW);
        assertNotNull("GREEN no hauria de ser ser null", ColorTerminal.PURPLE);
        
        // Verificar valorss específics d'alguns codis ANSI
        assertEquals("\033[0m", ColorTerminal.RESET);
        assertEquals("\033[0;31m", ColorTerminal.RED);
        assertEquals("\033[0;32m", ColorTerminal.GREEN);
    }

    @Test
    public void testBoldColorConstants() {
        // Verificar constants de colors en negreta
        assertNotNull("BLACK_BOLD no hauria de ser ser null", ColorTerminal.BLACK_BOLD);
        assertEquals("\033[1;31m", ColorTerminal.RED_BOLD);
        assertEquals("\033[1;32m", ColorTerminal.GREEN_BOLD);
    }

    @Test
    public void testBackgroundColorConstants() {
        // Verificar constants de colors de fons
        assertNotNull("BLACK_BACKGROUND no hauria de ser ser null", ColorTerminal.BLACK_BACKGROUND);
        assertEquals("\033[41m", ColorTerminal.RED_BACKGROUND);
        assertEquals("\033[42m", ColorTerminal.GREEN_BACKGROUND);
    }

    @Test
    public void testCenterTextNormal() {
        String text = "Hola";
        int width = 10;
        String centered = ColorTerminal.centerText(text, width);
        assertEquals("   Hola", centered);
    }

    @Test
    public void testCenterTextLargerThanWidth() {
        String text = "Text molt llarg :)";
        int width = 5;
        String centered = ColorTerminal.centerText(text, width);
        assertEquals(text, centered);
    }
}
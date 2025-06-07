package domini.auxiliars;

/**
 * Classe per gestionar els colors de la terminal utilitzant codis ANSI.
 * Inclou mètodes per centrar text i constants per a diferents colors.
 *
 */
public class ColorTerminal {
    // Códigos de color ANSI
    public static final String RESET = "\033[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String MAGENTA = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    public static final String PURPLE = "\033[0;35m";

    // Versiones en negrita
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String MAGENTA_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String WHITE_BOLD = "\033[1;37m";

    // Fondos
    public static final String BLACK_BACKGROUND = "\033[40m";
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String MAGENTA_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String WHITE_BACKGROUND = "\033[47m";

    /**
     * Centra el text en una línia de la terminal.
     *
     * @param text  El text a centrar.
     * @param width L'amplada de la línia.
     * @return El text centrat.
     */
    public static String centerText(String text, int width) {
        int textLength = text.replaceAll("\033\\[[;\\d]*m", "").length(); // Sense codis ANSI
        if (textLength >= width) return text;
        int padding = (width - textLength) / 2;
        return " ".repeat(padding) + text;
    }
}

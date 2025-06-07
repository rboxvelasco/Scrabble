package interficie;

import javax.swing.*;
import java.awt.*;

/**
 * Botó personalitzat amb una imatge de fons.
 * <p>
 * Aquesta classe hereta de {@link JButton} i permet establir una imatge com a fons del botó,
 * mantenint el text i altres elements del botó visibles per sobre de la imatge.
 * </p>
 */
class ImageButton extends JButton {
    /**
     * Imatge de fons del botó.
     */
    private Image imatgeFons;

    /**
     * Constructor que crea un botó amb text i una imatge de fons.
     * 
     * @param text Text que es mostrarà al botó.
     * @param rutaImatge Ruta del fitxer de la imatge que s'utilitzarà com a fons.
     */
    public ImageButton(String text, String rutaImatge) {
        super(text);
        carregarImatge(rutaImatge); // Carrega la imatge inicial
        setOpaque(false); // Evita que el fons per defecte cobreixi la imatge
    }

    /**
     * Carrega la imatge des de la ruta especificada.
     * 
     * @param rutaImatge Ruta del fitxer de la imatge.
     */
    private void carregarImatge(String rutaImatge) {
        try {
            ImageIcon icon = new ImageIcon(rutaImatge);
            imatgeFons = icon.getImage();
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    ImageButton.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
            imatgeFons = null; // Opcional: assignar null si falla la càrrega
        }
    }

    /**
     * Canvia la imatge de fons del botó.
     * 
     * @param rutaImatge Nova ruta de la imatge que es vol posar de fons.
     */
    public void setImage(String rutaImatge) {
        carregarImatge(rutaImatge); // Actualitza la imatge de fons
        repaint(); // Redibuixa el botó amb la nova imatge
    }

    /**
     * Sobrescriu el mètode per pintar el component.
     * <p>
     * Dibuixa la imatge escalada al tamany actual del botó, i després dibuixa el text i altres elements del botó.
     * </p>
     *
     * @param g Context gràfic on es dibuixa el botó.
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (imatgeFons != null) {
            // Dibuixa la imatge escalada al tamany del botó
            g.drawImage(imatgeFons, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g); // Dibuixa el text i altres elements per sobre
    }
}
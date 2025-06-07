package interficie.utils;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;

/**
 * @class Tools
 * @brief Classe d'utilitats gràfiques per a la interfície d'usuari.
 *
 * Proporciona mètodes útils per al tractament d'elements gràfics, com la redimensió
 * d'imatges per adaptar-les a components Swing mantenint la seva proporció original.
 */
public class Tools {
    /**
     * Actualitza la imatge d'un JLabel redimensionant-la per adaptar-se a les mides màximes indicades
     * mantenint la proporció original de la imatge.
     * 
     * @param original  La imatge original encapsulada en un ImageIcon.
     * @param label     El JLabel on es vol posar la imatge redimensionada.
     * @param maxWidth  Amplada màxima per la imatge.
     * @param maxHeight Alçada màxima per la imatge.
     */
    public static void actualitzarImatge(ImageIcon original, JLabel label, int maxWidth, int maxHeight) {
        Image imgOriginal = original.getImage();
        int originalWidth = imgOriginal.getWidth(null);
        int originalHeight = imgOriginal.getHeight(null);

        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth = maxWidth;
        int newHeight = (int) (newWidth / aspectRatio);

        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }

        Image img = imgOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
    }
}

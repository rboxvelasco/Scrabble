package interficie;

import javax.swing.SwingUtilities;

import controladors.CtrlPresentacio;

/**
 * Classe d'entrada a la GUI de l'aplicació.
 * <p>
 * Aquesta classe inicia l'aplicació Swing executant-la dins del fil de l'Event Dispatch Thread (EDT),
 * assegurant que tota la interfície gràfica es carregui de manera segura i eficient.
 * </p>
 */
public class MainGUI {

    /**
     * Punt d'entrada principal del programa.
     * <p>
     * Inicialitza el controlador de presentació i llança la interfície gràfica de l'usuari.
     * S'executa mitjançant {@code SwingUtilities.invokeLater} per garantir que tota
     * la manipulació de la GUI es realitza al fil adequat.
     * </p>
     *
     * @param args Arguments de línia de comandes (no s'utilitzen).
     */    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CtrlPresentacio ctrl = CtrlPresentacio.getInstancia();
            ctrl.launchApp();
        });
    }
}

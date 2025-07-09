package interficie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Diàleg modal Swing per seleccionar una partida guardada d'un usuari concret.
 *  
 * Mostra una llista de partides disponibles per a l'usuari i permet a l'usuari
 * seleccionar-ne una per reprendre-la. Es pot confirmar amb doble clic o amb el
 * botó "Reprendre".
 *  
 */
public class SelectorPartida extends JDialog {
    private int idPartida = -1;

    /**
     * Crea un diàleg modal per seleccionar una partida guardada.
     *
     * @param parent    La finestra principal (parent) sobre la qual es mostra el
     *                  diàleg.
     * @param nomUsuari El nom de l'usuari actual, que s'utilitza per buscar les
     *                  partides guardades
     *                  a la carpeta {@code data/partides/nomUsuari/}.
     *
     *
     *                  Es mostren els arxius JSON amb nom numèric (ex:
     *                  {@code 5.json}) com a "Partida 5", etc.
     *
     *
     *                  Amb doble clic o amb el botó "Reprendre", es retorna
     *                  l’ID seleccionat.
     *                  Amb "Cancel·lar" es tanca el diàleg sense seleccionar
     *                  cap partida.
     *
     */
    public SelectorPartida(Frame parent, String nomUsuari) {
        super(parent, "Selecciona una partida per reprendre", true);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Títol
        JLabel labelTitol = new JLabel("Partides guardades");
        labelTitol.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        labelTitol.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitol.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(labelTitol, BorderLayout.NORTH);

        // Llista de partides trobades
        DefaultListModel<String> model = new DefaultListModel<>();
        java.util.List<Integer> ids = new ArrayList<>();

        File carpeta = new File("data/partides/" + nomUsuari + "/");
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] arxius = carpeta.listFiles((dir, name) -> name.matches("\\d+\\.json"));
            if (arxius != null) {
                for (File arxiu : arxius) {
                    String nom = arxiu.getName();
                    int id = Integer.parseInt(nom.replace(".json", ""));
                    ids.add(id);
                    model.addElement("Partida " + id);
                }
            }
        }

        JList<String> llista = new JList<>(model);
        llista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        llista.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(llista);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scroll, BorderLayout.CENTER);

        JPanel panellBotons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panellBotons.setBackground(Color.WHITE);
        JButton botoReprendre = new JButton("Reprendre");
        JButton botoCancel = new JButton("Cancel·lar");

        botoReprendre.setBackground(new Color(0, 153, 76));
        botoReprendre.setForeground(Color.WHITE);
        botoReprendre.setFocusPainted(false);
        botoReprendre.setFont(new Font("SansSerif", Font.BOLD, 14));

        botoCancel.setBackground(new Color(204, 0, 0));
        botoCancel.setForeground(Color.WHITE);
        botoCancel.setFocusPainted(false);
        botoCancel.setFont(new Font("SansSerif", Font.BOLD, 14));

        panellBotons.add(botoReprendre);
        panellBotons.add(botoCancel);
        add(panellBotons, BorderLayout.SOUTH);

        // Acció doble clic
        llista.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = llista.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        idPartida = ids.get(index);
                        dispose(); // Tancar el diàleg
                    }
                }
            }
        });

        // Acció botó "Reprendre"
        botoReprendre.addActionListener(e -> {
            int index = llista.getSelectedIndex();
            if (index >= 0) {
                idPartida = ids.get(index);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una partida per continuar.", "Advertència",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Acció botó "Cancel·lar"
        botoCancel.addActionListener(e -> dispose());

        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    /**
     * Retorna l'identificador de la partida seleccionada per reprendre.
     *
     * @return L’ID de la partida seleccionada, o -1 si no se n'ha seleccionat cap.
     */
    public int getIdPartida() {
        return idPartida;
    }
}

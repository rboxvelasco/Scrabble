package interficie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import controladors.CtrlPresentacio;
import domini.jugadors.*;
import interficie.utils.*;

/**
 * @class VistaConsultarEstadistiques
 *        Mostra una finestra amb les estadístiques dels avatars de l'usuari
 *        registrat.
 *
 *        Aquesta vista permet seleccionar un avatar d'una llista i veure les
 *        seves estadístiques de joc.
 *        També inclou un botó per tornar al menú anterior.
 */
public class VistaConsultarEstadistiques extends JFrame {
    private JList<Avatar> llistaAvatars;
    private JEditorPane areaEstadistiques;
    private DefaultListModel<Avatar> modelLlista;

    private ImageIcon imatgeLogo;
    private JLabel labelLogo;

    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;

    /**
     * Constructor principal de la vista.
     * 
     * @param menuOriginal Referència al JFrame del menú anterior per copiar mida i
     *                     posició.
     */
    public VistaConsultarEstadistiques(JFrame menuOriginal) {
        setTitle("Scrabble - Consultar Estadístiques");
        setSize(menuOriginal.getSize());
        setLocation(menuOriginal.getLocation());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            ImageIcon icono = new ImageIcon("./resources/icon2.png");
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaConsultarEstadistiques.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        JPanel panellPrincipal = new JPanel(new BorderLayout(20, 20));
        panellPrincipal.setBackground(Colors.GREEN_BACKGROUND);
        panellPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(panellPrincipal);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, LOGO_WIDTH, LOGO_HEIGHT);
            labelLogo.setBounds(10, 10, LOGO_WIDTH, LOGO_HEIGHT);
            panellPrincipal.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaConsultarEstadistiques.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        int w = getWidth();
        int h = getHeight();
        double escalaX = w / (double) WIDTH_BASE;
        double escalaY = h / (double) HEIGHT_BASE;

        int logoW = (int) (LOGO_WIDTH * escalaX);
        int logoH = (int) (LOGO_HEIGHT * escalaY);
        Tools.actualitzarImatge(imatgeLogo, labelLogo, logoW, logoH);
        labelLogo.setBounds((int) (10 * escalaX), (int) (10 * escalaY), logoW, logoH);

        // Títol centrat
        JLabel titol = new JLabel("Estadístiques dels Avatars", SwingConstants.CENTER);
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titol.setForeground(Colors.GREEN_TEXT);
        panellPrincipal.add(titol, BorderLayout.NORTH);

        // Panell
        JPanel panellCentral = new JPanel(new GridLayout(1, 2, 20, 0));
        panellCentral.setBackground(Colors.GREEN_BACKGROUND);

        modelLlista = new DefaultListModel<>();
        llistaAvatars = new JList<>(modelLlista);
        carregarAvatars();
        llistaAvatars.setCellRenderer(new AvatarRenderer());
        llistaAvatars.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollAvatars = new JScrollPane(llistaAvatars);
        scrollAvatars.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panellCentral.add(scrollAvatars);

        // Panell dreta: estadístiques
        JPanel panellDreta = new JPanel();
        panellDreta.setLayout(new GridBagLayout());
        panellDreta.setOpaque(false);

        areaEstadistiques = new JEditorPane();
        areaEstadistiques.setContentType("text/html");
        areaEstadistiques.setEditable(false);
        areaEstadistiques.setOpaque(false);
        areaEstadistiques.setBorder(null);
        areaEstadistiques.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        areaEstadistiques.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollEstadistiques = new JScrollPane(areaEstadistiques);
        scrollEstadistiques.setOpaque(false);
        scrollEstadistiques.getViewport().setOpaque(false);
        scrollEstadistiques.setBorder(null);
        scrollEstadistiques.setPreferredSize(new Dimension(400, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panellDreta.add(scrollEstadistiques, gbc);

        panellCentral.add(panellDreta);

        panellPrincipal.add(panellCentral, BorderLayout.CENTER);

        // Botó tornar
        JPanel panellBotons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panellBotons.setBackground(Colors.GREEN_BACKGROUND);

        JButton botoTornar = new JButton("TORNAR");
        botoTornar.setMnemonic(KeyEvent.VK_T);
        botoTornar.setPreferredSize(new Dimension(120, 40));
        botoTornar.setBackground(Colors.YELLOW_LIGHT);
        botoTornar.setForeground(Color.BLACK);
        botoTornar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 3));

        botoTornar.addActionListener(e -> CtrlPresentacio.tornar());
        botoTornar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botoTornar.setBackground(Colors.YELLOW_DARK);
                botoTornar.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                botoTornar.setBackground(Colors.YELLOW_LIGHT);
                botoTornar.setForeground(Color.BLACK);
            }
        });

        panellBotons.add(botoTornar);
        panellPrincipal.add(panellBotons, BorderLayout.SOUTH);

        afegirListenerLlista();

        SwingUtilities.invokeLater(() -> {
            llistaAvatars.requestFocusInWindow();
            if (!modelLlista.isEmpty()) {
                llistaAvatars.setSelectedIndex(0);
            }
        });

        setVisible(true);
    }

    /**
     * Carrega els avatars dels jugadors registrats al model de la llista.
     */
    private void carregarAvatars() {
        for (Jugador j : CtrlPresentacio.getJugadorsRegistrats().values()) {
            if (j instanceof Avatar avatar)
                modelLlista.addElement(avatar);
        }
    }

    /**
     * Afegeix un listener a la llista per mostrar estadístiques quan es selecciona
     * un avatar.
     */
    private void afegirListenerLlista() {
        llistaAvatars.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Avatar avatar = llistaAvatars.getSelectedValue();
                if (avatar != null)
                    mostrarEstadistiques(avatar);
            }
        });
    }

    /**
     * @brief Mostra les estadístiques d’un avatar seleccionat a l’àrea
     *        d’estadístiques.
     * @param avatar L’avatar del qual es mostraran les estadístiques.
     */
    private void mostrarEstadistiques(Avatar avatar) {
        String html = "<html><head><style>"
                + "body { font-family: 'Comic Sans MS', cursive, sans-serif; font-size: 13px; color: #000; }"
                + "b { color: #33691E; }"
                + "span.bullet { color: #F6BC09; margin-right: 5px; }"
                + "</style></head><body>"
                + "<p style='font-size:14px; font-weight:bold;'>Nom de l'avatar: " + avatar.getNom() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Partides guanyades:</b> " + avatar.getPartides_guanyades()
                + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Guanyades contra màquina:</b> "
                + avatar.getPartides_guanyades_maquina() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Guanyades contra jugador:</b> "
                + avatar.getPartides_guanyades_jugador() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Jugades contra màquina:</b> "
                + avatar.getPartides_jugades_maquina() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Jugades contra jugador:</b> "
                + avatar.getPartides_jugades_jugador() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Puntuació màxima en una paraula:</b> "
                + avatar.getPuntuacio_max_paraula() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Partides abandonades:</b> "
                + avatar.getPartides_abandonades() + "</p>"
                + "<p><span class='bullet'>&#9679;</span> <b>Total jugades:</b> " + avatar.getPartidesJugades() + "</p>"
                + "</body></html>";

        areaEstadistiques.setText(html);
        areaEstadistiques.setCaretPosition(0);
    }

    /**
     * @class AvatarRenderer
     *        Renderer personalitzat per mostrar l’avatar amb imatge i nom.
     */
    private static class AvatarRenderer extends JLabel implements ListCellRenderer<Avatar> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Avatar> list, Avatar avatar, int index,
                boolean isSelected, boolean cellHasFocus) {
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setText("  " + avatar.getNom());

            String path = avatar.getrutaImatge(); // assumeix que existeix aquest mètode
            ImageIcon icona;
            if (path != null && new File(path).exists()) {
                icona = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            } else {
                icona = new ImageIcon(new ImageIcon("./resources/default-avatar.png").getImage().getScaledInstance(40,
                        40, Image.SCALE_SMOOTH));
            }
            setIcon(icona);
            setIconTextGap(10);

            if (isSelected) {
                setBackground(Colors.YELLOW_LIGHT);
                setForeground(Color.BLACK);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.DARK_GRAY);
            }
            return this;
        }
    }
}

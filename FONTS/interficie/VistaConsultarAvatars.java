package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import domini.jugadors.*;
import interficie.utils.*;
import java.awt.geom.Ellipse2D;

/**
 * @class VistaConsultarAvatars
 *        Mostra una finestra llistant els avatars disponibles.
 */
public class VistaConsultarAvatars extends JFrame {
    private JList<String[]> llistaAvatars;
    private DefaultListModel<String[]> modelLlista;
    private ImageIcon imatgeLogo;
    private JLabel labelLogo;

    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;

    /**
     * Constructor de la vista que mostra la llista d'avatares registrats.
     *
     * Inicialitza una nova finestra amb les mateixes dimensions i posició que el
     * menú original.
     * Configura la interfície gràfica, incloent-hi:
     * - Títol i icona de l'aplicació.
     * - Un logo redimensionat i posicionat a la cantonada superior esquerra.
     * - Un títol central.
     * - Un panell amb tots els avatars registrats, mostrats com a icones circulars
     * amb nom.
     * - Un botó "TORNAR" per retornar a la vista anterior.
     *
     * Aquesta vista s'utilitza per consultar de forma visual els avatars que han
     * estat registrats
     * prèviament mitjançant {@link CtrlPresentacio#getJugadorsRegistrats()}.
     *
     * @param menuOriginal Finestra original des de la qual es mostra aquesta vista.
     *                     Es fa servir
     *                     per copiar-ne la mida i la posició.
     */
    public VistaConsultarAvatars(JFrame menuOriginal) {
        setTitle("Scrabble - Consultar Avatars");
        Dimension screenSize = menuOriginal.getSize();
        setSize(screenSize.width, screenSize.height);
        setLocation(menuOriginal.getLocation());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Icone d'aplicació
        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaConsultarAvatars.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        JPanel panellPrincipal = new JPanel(new BorderLayout(15, 15));
        panellPrincipal.setBackground(Colors.GREEN_BACKGROUND);
        panellPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
                    VistaConsultarAvatars.this,
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

        // Escalar logo
        int logoW = (int) (LOGO_WIDTH * escalaX);
        int logoH = (int) (LOGO_HEIGHT * escalaY);
        Tools.actualitzarImatge(imatgeLogo, labelLogo, logoW, logoH);
        labelLogo.setBounds((int) (10 * escalaX), (int) (10 * escalaY), logoW, logoH);

        JLabel titol = new JLabel("Llista d'Avatars Registrats", SwingConstants.CENTER);
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        titol.setForeground(Colors.GREEN_TEXT);
        panellPrincipal.add(titol, BorderLayout.NORTH);

        JPanel panellAvatars = new JPanel(new GridLayout(0, 4, 20, 20));
        panellAvatars.setBackground(Colors.GREEN_BACKGROUND);

        // Panell scrollable
        JScrollPane scrollAvatars = new JScrollPane(panellAvatars);
        scrollAvatars.setBorder(null);
        scrollAvatars.getViewport().setOpaque(false);
        scrollAvatars.setOpaque(false);
        scrollAvatars.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panellPrincipal.add(scrollAvatars, BorderLayout.CENTER);
        carregarAvatars(panellAvatars);

        JPanel panellBotons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panellBotons.setBackground(Colors.GREEN_BACKGROUND);

        JButton botoTornar = new JButton("TORNAR");
        botoTornar.setMnemonic(KeyEvent.VK_T);
        botoTornar.setPreferredSize(new Dimension(100, 40));
        botoTornar.setBackground(Colors.YELLOW_LIGHT);
        botoTornar.setForeground(Color.BLACK);
        botoTornar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        botoTornar.addActionListener(e -> {
            CtrlPresentacio.tornar();
        });

        botoTornar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoTornar.setBackground(Colors.YELLOW_DARK);
                botoTornar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoTornar.setBackground(Colors.YELLOW_LIGHT);
                botoTornar.setForeground(Color.BLACK);
            }
        });
        panellBotons.add(botoTornar);
        panellPrincipal.add(panellBotons, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Carrega i mostra visualment tots els avatars registrats en un panell donat.
     *
     * Aquest mètode elimina tots els components existents del panell passat com a
     * paràmetre
     * i hi afegeix, per a cada objecte {@link Avatar} registrat, un panell amb la
     * seva imatge
     * circular (retallada i redimensionada) i el seu nom.
     *
     * Les imatges es mostren com a icones circulars de mida fixa, i el nom de
     * l'avatar s'afegeix
     * a sota. Cada avatar es mostra dins del seu propi panell amb estil
     * personalitzat.
     *
     * @param panellAvatars Panell de Swing on s'afegiran els avatars.
     */
    private void carregarAvatars(JPanel panellAvatars) {
        panellAvatars.removeAll();
        final int ICON_SIZE = 80;

        for (Jugador jug : CtrlPresentacio.getJugadorsRegistrats().values()) {
            if (jug instanceof Avatar) {
                Avatar avatar = (Avatar) jug;
                String nom = avatar.getNom();
                String ruta = avatar.getrutaImatge();

                // Càrrega i retall circular
                ImageIcon icon = new ImageIcon(ruta);
                Image img = icon.getImage();

                int w = img.getWidth(null);
                int h = img.getHeight(null);
                int size = Math.min(w, h);
                int x = (w - size) / 2;
                int y = (h - size) / 2;
                BufferedImage cropped = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2c = cropped.createGraphics();
                g2c.drawImage(img, 0, 0, size, size, x, y, x + size, y + size, null);
                g2c.dispose();

                BufferedImage circleImg = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Ellipse2D.Double clip = new Ellipse2D.Double(0, 0, ICON_SIZE, ICON_SIZE);
                g2.setClip(clip);
                g2.drawImage(cropped, 0, 0, ICON_SIZE, ICON_SIZE, null);
                g2.dispose();

                // Panell
                JPanel avatarPanel = new JPanel();
                avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
                avatarPanel.setBackground(Colors.GREEN_BACKGROUND);
                avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel imgLabel = new JLabel(new ImageIcon(circleImg));
                imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel nomLabel = new JLabel(nom, SwingConstants.CENTER);
                nomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                nomLabel.setForeground(Colors.GREEN_TEXT);
                nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                avatarPanel.add(imgLabel);
                avatarPanel.add(Box.createVerticalStrut(5));
                avatarPanel.add(nomLabel);

                panellAvatars.add(avatarPanel);
            }
        }

        panellAvatars.revalidate();
        panellAvatars.repaint();
    }
}

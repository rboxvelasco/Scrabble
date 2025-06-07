package interficie;

import javax.swing.*;
import controladors.CtrlPresentacio;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import interficie.utils.*;

/**
 * @class VistaCanviTorn
 *        Mostra una finestra indicant que un jugador ha de passar el dispositiu
 *        a un altre.
 */
public class VistaCanviTorn extends JFrame {
    private JPanel panellPrincipal;
    private JLabel labelTop, labelBottom, labelLogo;
    private JButton btnOkay;
    private ImageIcon imatgeLogo;
    private static CtrlPresentacio ctrlPresentacio = CtrlPresentacio.getInstancia();
    private final int MARGIN = 30;
    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;

    /**
     * Constructor de la classe VistaCanviTorn
     * 
     * @param jugador1     Nom del jugador que passa el torn
     * @param jugador2     Nom del jugador que rep el torn
     * @param vistaPartida Vista de la partida per obtenir mida i posició
     */
    public VistaCanviTorn(String jugador1, String jugador2, JFrame vistaPartida) {
        setTitle("Canvi de Torn");
        setSize(vistaPartida.getSize());
        setLocation(vistaPartida.getLocation());
        setMinimumSize(new Dimension(400, 200));
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ctrlPresentacio.tornarPartida();
                dispose();
            }
        });

        // Icone d'aplicació
        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaCanviTorn.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Panell principal
        panellPrincipal = new JPanel();
        panellPrincipal.setLayout(new BorderLayout());
        panellPrincipal.setBackground(Colors.GREEN_BACKGROUND);
        panellPrincipal.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
        add(panellPrincipal);

        // Subpanell
        JPanel panellContingut = new JPanel();
        panellContingut.setLayout(new BoxLayout(panellContingut, BoxLayout.Y_AXIS));
        panellContingut.setBackground(Colors.GREEN_BACKGROUND);
        panellPrincipal.add(panellContingut, BorderLayout.CENTER);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, LOGO_WIDTH, LOGO_HEIGHT);
            JPanel panellLogo = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panellLogo.setBackground(Colors.GREEN_BACKGROUND);
            panellLogo.add(labelLogo);
            panellPrincipal.add(panellLogo, BorderLayout.NORTH);
        } else {
            JOptionPane.showOptionDialog(
                    VistaCanviTorn.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Imatge central
        String rutaCanvi = "./resources/canvi.png";
        File fitxerCanvi = new File(rutaCanvi);
        if (fitxerCanvi.exists()) {
            ImageIcon iconCanvi = new ImageIcon(rutaCanvi);
            JLabel labelCanvi = new JLabel();
            Tools.actualitzarImatge(iconCanvi, labelCanvi, 400, 200);
            labelCanvi.setAlignmentX(Component.CENTER_ALIGNMENT);
            panellContingut.add(Box.createVerticalStrut(5));
            panellContingut.add(labelCanvi);
            panellContingut.add(Box.createVerticalStrut(5));
        } else {
            JOptionPane.showOptionDialog(
                    VistaCanviTorn.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        String textTop = "<html><center><span style='color:red'>" + jugador1
                + "</span> ha de passar el dispositiu a <span style='color:blue'>" + jugador2
                + "</span>.</center></html>";
        labelTop = new JLabel(textTop, SwingConstants.CENTER);
        labelTop.setForeground(Colors.GREEN_TEXT);
        labelTop.setAlignmentX(Component.CENTER_ALIGNMENT);
        panellContingut.add(Box.createVerticalStrut(10));
        panellContingut.add(labelTop);
        panellContingut.add(Box.createVerticalStrut(10));

        String textBottom = "<html><center>Quan <span style='color:blue'>" + jugador2
                + "</span> estigui llest, prem D'acord.</center></html>";
        labelBottom = new JLabel(textBottom, SwingConstants.CENTER);
        labelBottom.setForeground(Colors.GREEN_TEXT);
        labelBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        panellContingut.add(labelBottom);
        panellContingut.add(Box.createVerticalStrut(10));

        btnOkay = new JButton("D'acord");
        btnOkay.setBackground(Colors.YELLOW_LIGHT);
        btnOkay.setForeground(Color.BLACK);
        btnOkay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.YELLOW_DARK, 3, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnOkay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOkay.setFocusPainted(false);
        btnOkay.addActionListener(e -> {
            dispose();
            ctrlPresentacio.tornarPartida();
        });
        btnOkay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnOkay.setBackground(Colors.YELLOW_DARK);
                btnOkay.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnOkay.setBackground(Colors.YELLOW_LIGHT);
                btnOkay.setForeground(Color.BLACK);
            }
        });
        panellContingut.add(Box.createVerticalStrut(20));
        panellContingut.add(btnOkay);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnOkay.doClick();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                escalarComponents();
            }
        });

        escalarComponents();
    }

    /**
     * Escala i posiciona els components segons la mida actual.
     */
    private void escalarComponents() {
        int w = getWidth();
        int h = getHeight();
        double escalaX = w / (double) WIDTH_BASE;
        double escalaY = h / (double) HEIGHT_BASE;

        int logoW = (int) (LOGO_WIDTH * escalaX);
        int logoH = (int) (LOGO_HEIGHT * escalaY);
        Tools.actualitzarImatge(imatgeLogo, labelLogo, logoW, logoH);

        int fontSize = (int) (20 * Math.min(escalaX, escalaY));
        Font font = new Font("Comic Sans MS", Font.BOLD, Math.max(fontSize, 18));
        labelTop.setFont(font);
        labelBottom.setFont(font);
        btnOkay.setFont(font);

        int btnWidth = (int) (120 * escalaX);
        int btnHeight = (int) (40 * escalaY);
        btnOkay.setPreferredSize(new Dimension(btnWidth, btnHeight));
        btnOkay.setMaximumSize(new Dimension(btnWidth, btnHeight));
    }
}
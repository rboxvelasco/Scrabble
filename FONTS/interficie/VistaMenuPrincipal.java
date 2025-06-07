package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import interficie.utils.Colors;

/**
 * @class VistaMenuPrincipal
 *        Classe que representa la finestra principal del menú de l'aplicació
 *        Scrabble.
 * 
 *        Aquesta classe mostra el menú principal de l'aplicació, que ofereix
 *        les opcions:
 *        1) Iniciar Sessió
 *        2) Crear Perfil
 *        3) Eliminar Perfil
 */
public class VistaMenuPrincipal extends JFrame {
    private JPanel panellPrincipal;
    private JLabel logo;
    private JButton botoIniciar, botoCrear, botoNormes, botoSortir;

    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;
    private final int WIDTH_SORTIR = 80, HEIGHT_SORTIR = 40;
    private final int OPT_X = 425, OPT_Y = 340;
    private final int IMG_WIDTH = 600, IMG_HEIGTH = 200;

    private ImageIcon icon;

    /**
     * Actualitza la imatge del logotip amb una mida determinada.
     * 
     * @param original Imatge original.
     * @param width    Amplada desitjada.
     * @param height   Alçada desitjada.
     */
    private void actualitzarImatge(ImageIcon original, int width, int height) {
        int posX = (getWidth() - (width / 2)) / 2;
        int posY = 60;

        Image img = original.getImage().getScaledInstance(width / 2, height, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(img));
        logo.setBounds(posX, posY, width / 2, height);
    }

    /**
     * Escala tots els components gràfics en funció de la mida de la
     * finestra.
     */
    private void escalarComponentes() {
        int w = getWidth();
        int h = getHeight();
        double escalaX = w / (double) WIDTH_BASE;
        double escalaY = h / (double) HEIGHT_BASE;
        int imgW = (int) (IMG_WIDTH * escalaX);
        int imgH = (int) (IMG_HEIGTH * escalaY * 0.8);
        actualitzarImatge(icon, imgW, imgH);
        botoIniciar.setBounds((int) ((OPT_X - 225) * escalaX), (int) (OPT_Y * escalaY), (int) (150 * escalaX),
                (int) (40 * escalaY));
        botoCrear.setBounds((int) (OPT_X * escalaX), (int) (OPT_Y * escalaY), (int) (150 * escalaX),
                (int) (40 * escalaY));
        botoNormes.setBounds((int) ((OPT_X + 225) * escalaX), (int) (OPT_Y * escalaY), (int) (150 * escalaX),
                (int) (40 * escalaY));
        botoSortir.setBounds((int) (875 * escalaX), (int) (500 * escalaY), (int) (WIDTH_SORTIR * escalaX),
                (int) (HEIGHT_SORTIR * escalaY));

        int fontSize_gran = (int) (16 * Math.min(escalaX, escalaY));
        int fontSize_petit = (int) (12 * Math.min(escalaX, escalaY));
        Font font_gran = new Font("Comic Sans MS", Font.BOLD, Math.max(fontSize_gran, 10));
        Font font_petita = new Font("Comic Sans MS", Font.BOLD, Math.max(fontSize_petit, 10));
        botoIniciar.setFont(font_gran);
        botoCrear.setFont(font_gran);
        botoNormes.setFont(font_gran);
        botoSortir.setFont(font_petita);
    }

    /**
     * Constructor de la classe VistaMenuPrincipal.
     * 
     * Inicialitza i configura tots els elements de la finestra.
     */
    public VistaMenuPrincipal() {
        setTitle("Scrabble - Menú Principal");
        setSize(WIDTH_BASE, HEIGHT_BASE);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaMenuPrincipal.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        panellPrincipal = new JPanel(null);
        panellPrincipal.setBackground(Colors.GREEN_BACKGROUND);
        add(panellPrincipal);

        String ruta = "./resources/logo2.png";
        File archivoImagen = new File(ruta);
        if (archivoImagen.exists()) {
            icon = new ImageIcon(ruta);
            logo = new JLabel();
            actualitzarImatge(icon, IMG_WIDTH, IMG_HEIGTH);
        } else {
            JOptionPane.showOptionDialog(
                    VistaMenuPrincipal.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
            logo = new JLabel("Imatge no disponible");
        }

        int posX = (getWidth() - (IMG_WIDTH * getWidth() / (int) WIDTH_BASE / 2)) / 2;
        int posY = 40;
        logo.setBounds(posX, posY, IMG_WIDTH, IMG_HEIGTH);
        panellPrincipal.add(logo);

        // Botó Iniciar Sessió
        botoIniciar = new JButton("Iniciar Sessió");
        botoIniciar.setBackground(Colors.YELLOW_LIGHT);
        botoIniciar.setBounds(OPT_X - 225, OPT_Y, 150, 40);
        botoIniciar.setForeground(Colors.GREEN_TEXT);
        botoIniciar.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));

        botoIniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoIniciar.setBackground(Colors.GREEN_BORDER);
                botoIniciar.setForeground(Colors.YELLOW_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoIniciar.setBackground(Colors.YELLOW_LIGHT);
                botoIniciar.setForeground(Colors.GREEN_TEXT);
            }
        });

        // Botó Crear Perfil
        botoCrear = new JButton("Crear Perfil");
        botoCrear.setBackground(Colors.YELLOW_MID);
        botoCrear.setBounds(OPT_X, OPT_Y, 150, 40);
        botoCrear.setForeground(Colors.GREEN_TEXT);
        botoCrear.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));

        botoCrear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoCrear.setBackground(Colors.GREEN_BORDER);
                botoCrear.setForeground(Colors.YELLOW_MID);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoCrear.setBackground(Colors.YELLOW_MID);
                botoCrear.setForeground(Colors.GREEN_TEXT);
            }
        });

        // Botó Eliminar Perfil
        botoNormes = new JButton("Consultar Normes");
        botoNormes.setBackground(Colors.YELLOW_DARK);
        botoNormes.setBounds(OPT_X + 225, OPT_Y, 150, 40);
        botoNormes.setForeground(Colors.GREEN_TEXT);
        botoNormes.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));

        botoNormes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoNormes.setBackground(Colors.GREEN_BORDER);
                botoNormes.setForeground(Colors.YELLOW_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoNormes.setBackground(Colors.YELLOW_DARK);
                botoNormes.setForeground(Colors.GREEN_TEXT);
            }
        });

        // Botó Sortir
        botoSortir = new JButton("SORTIR");
        botoSortir.setMnemonic(KeyEvent.VK_S);
        botoSortir.setBackground(Colors.RED);
        botoSortir.setForeground(Color.BLACK);
        botoSortir.setBorder(BorderFactory.createLineBorder(Colors.RED_BORDER, 4));
        botoSortir.setBounds(875, 500, WIDTH_SORTIR, HEIGHT_SORTIR);

        botoSortir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoSortir.setBackground(Colors.RED_BORDER);
                botoSortir.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoSortir.setBackground(Colors.RED);
                botoSortir.setForeground(Color.BLACK);
            }
        });

        panellPrincipal.add(botoIniciar);
        panellPrincipal.add(botoCrear);
        panellPrincipal.add(botoNormes);

        botoCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.canviarCrearPerfil();
            }
        });

        botoIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.canviarIniciarSessio();
            }
        });

        botoNormes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.consultarNormes();
            }
        });

        botoSortir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.sortir();
            }
        });

        panellPrincipal.add(botoSortir);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                escalarComponentes();
            }
        });

        Font fontGran = new Font("Comic Sans MS", Font.BOLD, 16);
        Font fontPetita = new Font("Comic Sans MS", Font.BOLD, 12);

        botoIniciar.setFont(fontGran);
        botoCrear.setFont(fontGran);
        botoNormes.setFont(fontGran);
        botoSortir.setFont(fontPetita);

        setVisible(true);
    }
}

package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import interficie.utils.*;

/**
 * @class VistaMenuJoc
 *        Classe que representa la finestra principal del menú de l'aplicació
 *        Scrabble un cop s'ha iniciat sessió.
 */
public class VistaMenuJoc extends JFrame {
    private JPanel panellPrincipal;
    private JLabel labelLogo, labelSignOut;
    private ImageIcon imatgeLogo, imatgeSignOut;
    private JButton botoEliminarAvatar, botoCrearAvatar, botoNormes, botoSortir, botoTancarSessio, botoEstadistiques,
            botoRanking, botoCrearPartida, botoReprendrePartida, botoEliminarPerfil, botoConsultarAvatars;

    // Constants generals
    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;
    private final int IMG_WIDTH = 50, IMG_HEIGHT = 50;

    // Constants pel logo
    private final int LOGO_X = 10, LOGO_Y = 10;

    // Constants pel grup d'avatars
    private final int AVATAR_X = 125, AVATAR_Y_INICIAL = 55;
    private final int AVATAR_WIDTH = 150, AVATAR_HEIGHT = 40, AVATAR_ESPACIO_VERTICAL = 50;

    // Constants pel grup de consultes
    private final int CONSULTAS_X = 125, CONSULTAS_Y_INICIAL = 255;
    private final int CONSULTAS_WIDTH = 150, CONSULTAS_HEIGHT = 40, CONSULTAS_ESPACIO_VERTICAL = 50;

    // Constantes pels botons de partida
    private final int CREAR_PARTIDA_X = 400, CREAR_PARTIDA_Y = 110;
    private final int CREAR_PARTIDA_WIDTH = 462, CREAR_PARTIDA_HEIGHT = 158;

    private final int REPRENDRE_PARTIDA_X = 400, REPRENDRE_PARTIDA_Y = 285;
    private final int REPRENDRE_PARTIDA_WIDTH = 462, REPRENDRE_PARTIDA_HEIGHT = 158;

    // Constants pel grup de perfil/sessió
    private final int ELIMINAR_PERFIL_X = 125, ELIMINAR_PERFIL_Y = 455;
    private final int TANCAR_SESSIO_X = 805, TANCAR_SESSIO_Y = 20;
    private final int PERFIL_WIDTH = 150, PERFIL_HEIGHT = 40;
    private final int X_REDUIT = 30;

    // Constants pel botó sortir
    private final int SORTIR_X = 875, SORTIR_Y = 500;
    private final int SORTIR_WIDTH = 80, SORTIR_HEIGHT = 40;

    // Constants per a la imatge de sign_out.png
    private final int SIGN_OUT_WIDTH = 30, SIGN_OUT_HEIGHT = 30;
    private final int SIGN_OUT_X = TANCAR_SESSIO_X + (PERFIL_WIDTH - X_REDUIT) - SIGN_OUT_WIDTH;
    private final int SIGN_OUT_Y = TANCAR_SESSIO_Y + 5;

    /**
     * Escala tots els components gràfics en funció de la mida de la
     * finestra.
     */
    private void escalarComponentes() {
        int w = getWidth();
        int h = getHeight();
        double escalaX = w / (double) WIDTH_BASE;
        double escalaY = h / (double) HEIGHT_BASE;
        int logoW = (int) (IMG_WIDTH * escalaX);
        int logoH = (int) (IMG_HEIGHT * escalaY);
        Tools.actualitzarImatge(imatgeLogo, labelLogo, logoW, logoH);
        labelLogo.setBounds((int) (LOGO_X * escalaX), (int) (LOGO_Y * escalaY), logoW, logoH);

        // Imatge Tancar Sessió
        if (labelSignOut != null) {
            int signOutW = (int) (SIGN_OUT_WIDTH * escalaX);
            int signOutH = (int) (SIGN_OUT_HEIGHT * escalaY);
            Tools.actualitzarImatge(imatgeSignOut, labelSignOut, signOutW, signOutH);
            labelSignOut.setBounds((int) ((TANCAR_SESSIO_X + (PERFIL_WIDTH - X_REDUIT) - SIGN_OUT_WIDTH) * escalaX),
                    (int) (SIGN_OUT_Y * escalaY), signOutW, signOutH);
        }

        botoCrearAvatar.setBounds((int) (AVATAR_X * escalaX), (int) (AVATAR_Y_INICIAL * escalaY),
                (int) (AVATAR_WIDTH * escalaX), (int) (AVATAR_HEIGHT * escalaY));
        botoConsultarAvatars.setBounds((int) (AVATAR_X * escalaX),
                (int) ((AVATAR_Y_INICIAL + AVATAR_ESPACIO_VERTICAL) * escalaY), (int) (AVATAR_WIDTH * escalaX),
                (int) (AVATAR_HEIGHT * escalaY));
        botoEliminarAvatar.setBounds((int) (AVATAR_X * escalaX),
                (int) ((AVATAR_Y_INICIAL + 2 * AVATAR_ESPACIO_VERTICAL) * escalaY), (int) (AVATAR_WIDTH * escalaX),
                (int) (AVATAR_HEIGHT * escalaY));

        botoNormes.setBounds((int) (CONSULTAS_X * escalaX), (int) (CONSULTAS_Y_INICIAL * escalaY),
                (int) (CONSULTAS_WIDTH * escalaX), (int) (CONSULTAS_HEIGHT * escalaY));
        botoEstadistiques.setBounds((int) (CONSULTAS_X * escalaX),
                (int) ((CONSULTAS_Y_INICIAL + CONSULTAS_ESPACIO_VERTICAL) * escalaY), (int) (CONSULTAS_WIDTH * escalaX),
                (int) (CONSULTAS_HEIGHT * escalaY));
        botoRanking.setBounds((int) (CONSULTAS_X * escalaX),
                (int) ((CONSULTAS_Y_INICIAL + 2 * CONSULTAS_ESPACIO_VERTICAL) * escalaY),
                (int) (CONSULTAS_WIDTH * escalaX), (int) (CONSULTAS_HEIGHT * escalaY));

        botoCrearPartida.setBounds((int) (CREAR_PARTIDA_X * escalaX), (int) (CREAR_PARTIDA_Y * escalaY),
                (int) (CREAR_PARTIDA_WIDTH * escalaX), (int) (CREAR_PARTIDA_HEIGHT * escalaY));
        botoReprendrePartida.setBounds((int) (REPRENDRE_PARTIDA_X * escalaX), (int) (REPRENDRE_PARTIDA_Y * escalaY),
                (int) (REPRENDRE_PARTIDA_WIDTH * escalaX), (int) (REPRENDRE_PARTIDA_HEIGHT * escalaY));

        botoEliminarPerfil.setBounds((int) (ELIMINAR_PERFIL_X * escalaX), (int) (ELIMINAR_PERFIL_Y * escalaY),
                (int) (PERFIL_WIDTH * escalaX), (int) (PERFIL_HEIGHT * escalaY));
        botoTancarSessio.setBounds((int) ((TANCAR_SESSIO_X + (PERFIL_WIDTH - X_REDUIT)) * escalaX),
                (int) (TANCAR_SESSIO_Y * escalaY), (int) (X_REDUIT * escalaX), (int) (PERFIL_HEIGHT * escalaY));

        botoSortir.setBounds((int) (SORTIR_X * escalaX), (int) (SORTIR_Y * escalaY), (int) (SORTIR_WIDTH * escalaX),
                (int) (SORTIR_HEIGHT * escalaY));

        int fontSize_gran = (int) (16 * Math.min(escalaX, escalaY));
        int fontSize_petit = (int) (12 * Math.min(escalaX, escalaY));
        int fontSize_destacado = (int) (30 * Math.min(escalaX, escalaY));
        Font font_gran = new Font("Comic Sans MS", Font.BOLD, Math.max(fontSize_gran, 10));
        Font font_petita = new Font("Comic Sans MS", Font.BOLD, Math.max(fontSize_petit, 10));
        Font font_partida = new Font("Comic Sans MS", Font.BOLD, Math.max(fontSize_destacado, 14));

        botoEliminarAvatar.setFont(font_gran);
        botoCrearAvatar.setFont(font_gran);
        botoNormes.setFont(font_gran);
        botoSortir.setFont(font_petita);
        botoTancarSessio.setFont(font_gran);
        botoEstadistiques.setFont(font_gran);
        botoRanking.setFont(font_gran);
        botoConsultarAvatars.setFont(font_gran);
        botoEliminarPerfil.setFont(font_gran);
        botoCrearPartida.setFont(font_partida);
        botoReprendrePartida.setFont(font_partida);
    }

    /**
     * Constructor de la classe VistaMenuJoc.
     * Inicialitza i configura tots els elements de la finestra.
     */
    public VistaMenuJoc(VistaIniciarSessio iniciSessio, VistaCrearPerfil crearPerfil) {
        setTitle("Scrabble - Menú Joc");
        if (iniciSessio != null) {
            Dimension screenSize = iniciSessio.getSize();
            setSize(screenSize.width, screenSize.height);
            setLocation(iniciSessio.getLocation());
        } else {
            Dimension screenSize = crearPerfil.getSize();
            setSize(screenSize.width, screenSize.height);
            setLocation(crearPerfil.getLocation());
        }

        // Icone d'aplicació
        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaMenuJoc.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panellPrincipal = new JPanel(null);
        panellPrincipal.setBackground(Colors.GREEN_BACKGROUND);
        add(panellPrincipal);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, IMG_WIDTH, IMG_HEIGHT);
            labelLogo.setBounds(LOGO_X, LOGO_Y, IMG_WIDTH, IMG_HEIGHT);
            panellPrincipal.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaMenuJoc.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Imatge sign_out al costat de Tancar Sessió
        String rutaSignOut = "./resources/sign_out.png";
        File archivoSignOut = new File(rutaSignOut);
        if (archivoSignOut.exists()) {
            imatgeSignOut = new ImageIcon(rutaSignOut);
            labelSignOut = new JLabel();
            Tools.actualitzarImatge(imatgeSignOut, labelSignOut, SIGN_OUT_WIDTH, SIGN_OUT_HEIGHT);
            labelSignOut.setBounds(SIGN_OUT_X, SIGN_OUT_Y, SIGN_OUT_WIDTH, SIGN_OUT_HEIGHT);
            panellPrincipal.add(labelSignOut);
        } else {
            JOptionPane.showOptionDialog(
                    VistaMenuJoc.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Botó Crear Avatar
        botoCrearAvatar = new JButton("Crear Avatar");
        botoCrearAvatar.setBackground(Colors.YELLOW_LIGHT);
        botoCrearAvatar.setForeground(Colors.GREEN_TEXT);
        botoCrearAvatar.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoCrearAvatar.setBounds(AVATAR_X, AVATAR_Y_INICIAL, AVATAR_WIDTH, AVATAR_HEIGHT);
        configurarMouseListener(botoCrearAvatar, Colors.YELLOW_LIGHT, Colors.GREEN_TEXT);

        // Botó Consultar Avatars
        botoConsultarAvatars = new JButton("Consultar Avatar");
        botoConsultarAvatars.setBackground(Colors.YELLOW_MID);
        botoConsultarAvatars.setForeground(Colors.GREEN_TEXT);
        botoConsultarAvatars.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoConsultarAvatars.setBounds(AVATAR_X, AVATAR_Y_INICIAL + AVATAR_ESPACIO_VERTICAL, AVATAR_WIDTH,
                AVATAR_HEIGHT);
        configurarMouseListener(botoConsultarAvatars, Colors.YELLOW_MID, Colors.GREEN_TEXT);

        // Botó Eliminar Avatar
        botoEliminarAvatar = new JButton("Eliminar Avatar");
        botoEliminarAvatar.setBackground(Colors.YELLOW_DARK);
        botoEliminarAvatar.setForeground(Colors.GREEN_TEXT);
        botoEliminarAvatar.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoEliminarAvatar.setBounds(AVATAR_X, AVATAR_Y_INICIAL + 2 * AVATAR_ESPACIO_VERTICAL, AVATAR_WIDTH,
                AVATAR_HEIGHT);
        configurarMouseListener(botoEliminarAvatar, Colors.YELLOW_DARK, Colors.GREEN_TEXT);

        // Botó Normes
        botoNormes = new JButton("Consultar Normes");
        botoNormes.setBackground(Colors.YELLOW_LIGHT);
        botoNormes.setForeground(Colors.GREEN_TEXT);
        botoNormes.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoNormes.setBounds(CONSULTAS_X, CONSULTAS_Y_INICIAL, CONSULTAS_WIDTH, CONSULTAS_HEIGHT);
        configurarMouseListener(botoNormes, Colors.YELLOW_LIGHT, Colors.GREEN_TEXT);

        // Botó Estadístiques
        botoEstadistiques = new JButton("Estadístiques");
        botoEstadistiques.setBackground(Colors.YELLOW_MID);
        botoEstadistiques.setForeground(Colors.GREEN_TEXT);
        botoEstadistiques.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoEstadistiques.setBounds(CONSULTAS_X, CONSULTAS_Y_INICIAL + CONSULTAS_ESPACIO_VERTICAL, CONSULTAS_WIDTH,
                CONSULTAS_HEIGHT);
        configurarMouseListener(botoEstadistiques, Colors.YELLOW_MID, Colors.GREEN_TEXT);

        // Botó Ranking
        botoRanking = new JButton("Consultar Ranking");
        botoRanking.setBackground(Colors.YELLOW_DARK);
        botoRanking.setForeground(Colors.GREEN_TEXT);
        botoRanking.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoRanking.setBounds(CONSULTAS_X, CONSULTAS_Y_INICIAL + 2 * CONSULTAS_ESPACIO_VERTICAL, CONSULTAS_WIDTH,
                CONSULTAS_HEIGHT);
        configurarMouseListener(botoRanking, Colors.YELLOW_DARK, Colors.GREEN_TEXT);

        // Botó Crear Partida
        String rutaImagenCrear = "./resources/scrabble_up_color.png";
        botoCrearPartida = new ImageButton("Crear Partida", rutaImagenCrear);
        botoCrearPartida.setBackground(Colors.YELLOW_LIGHT);
        botoCrearPartida.setForeground(Color.WHITE);
        botoCrearPartida.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 6));
        botoCrearPartida.setBounds(CREAR_PARTIDA_X, CREAR_PARTIDA_Y, CREAR_PARTIDA_WIDTH, CREAR_PARTIDA_HEIGHT);
        botoCrearPartida.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((ImageButton) botoCrearPartida).setImage("./resources/scrabble_up_bn.png");
                botoCrearPartida.setForeground(Colors.YELLOW_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((ImageButton) botoCrearPartida).setImage("./resources/scrabble_up_color.png");
                botoCrearPartida.setForeground(Color.WHITE);
            }
        });

        // Botó Reprendre Partida
        String rutaImagenReprendre = "./resources/scrabble_down_color.png";
        botoReprendrePartida = new ImageButton("Reprendre Partida", rutaImagenReprendre);
        botoReprendrePartida.setBackground(Colors.YELLOW_LIGHT);
        botoReprendrePartida.setForeground(Color.WHITE);
        botoReprendrePartida.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 6));
        botoReprendrePartida.setBounds(REPRENDRE_PARTIDA_X, REPRENDRE_PARTIDA_Y, REPRENDRE_PARTIDA_WIDTH,
                REPRENDRE_PARTIDA_HEIGHT);
        botoReprendrePartida.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((ImageButton) botoReprendrePartida).setImage("./resources/scrabble_down_bn.png");
                botoReprendrePartida.setForeground(Colors.YELLOW_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((ImageButton) botoReprendrePartida).setImage("./resources/scrabble_down_color.png");
                botoReprendrePartida.setForeground(Color.WHITE);
            }
        });

        // Botó Eliminar Perfil
        botoEliminarPerfil = new JButton("Eliminar Perfil");
        botoEliminarPerfil.setBackground(Colors.YELLOW_DARK);
        botoEliminarPerfil.setForeground(Colors.GREEN_TEXT);
        botoEliminarPerfil.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoEliminarPerfil.setBounds(ELIMINAR_PERFIL_X, ELIMINAR_PERFIL_Y, PERFIL_WIDTH, PERFIL_HEIGHT);
        configurarMouseListener(botoEliminarPerfil, Colors.YELLOW_DARK, Colors.GREEN_TEXT);

        // Botó Tancar Sessió
        botoTancarSessio = new JButton("");
        botoTancarSessio.setBackground(Colors.YELLOW_MID);
        botoTancarSessio.setForeground(Colors.GREEN_TEXT);
        botoTancarSessio.setBorder(BorderFactory.createLineBorder(Colors.GREEN_BORDER, 4));
        botoTancarSessio.setText("");
        botoTancarSessio.setBounds(TANCAR_SESSIO_X + (PERFIL_WIDTH - X_REDUIT), TANCAR_SESSIO_Y, X_REDUIT,
                PERFIL_HEIGHT);
        botoTancarSessio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                double escalaX = getWidth() / (double) WIDTH_BASE;
                double escalaY = getHeight() / (double) HEIGHT_BASE;
                botoTancarSessio.setBounds((int) (TANCAR_SESSIO_X * escalaX),
                        (int) (TANCAR_SESSIO_Y * escalaY),
                        (int) (PERFIL_WIDTH * escalaX),
                        (int) (PERFIL_HEIGHT * escalaY));
                botoTancarSessio.setText("Tancar Sessió");
                panellPrincipal.revalidate();
                panellPrincipal.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                double escalaX = getWidth() / (double) WIDTH_BASE;
                double escalaY = getHeight() / (double) HEIGHT_BASE;
                botoTancarSessio.setBounds((int) ((TANCAR_SESSIO_X + (PERFIL_WIDTH - X_REDUIT)) * escalaX),
                        (int) (TANCAR_SESSIO_Y * escalaY),
                        (int) (X_REDUIT * escalaX),
                        (int) (PERFIL_HEIGHT * escalaY));
                botoTancarSessio.setText("");
                panellPrincipal.revalidate();
                panellPrincipal.repaint();
            }
        });

        // Botó Sortir
        botoSortir = new JButton("SORTIR");
        botoSortir.setMnemonic(KeyEvent.VK_S);
        botoSortir.setBackground(Colors.RED);
        botoSortir.setForeground(Color.BLACK);
        botoSortir.setBorder(BorderFactory.createLineBorder(Colors.RED_BORDER, 4));
        botoSortir.setBounds(SORTIR_X, SORTIR_Y, SORTIR_WIDTH, SORTIR_HEIGHT);
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

        panellPrincipal.add(botoEliminarAvatar);
        panellPrincipal.add(botoCrearAvatar);
        panellPrincipal.add(botoNormes);
        panellPrincipal.add(botoTancarSessio);
        panellPrincipal.add(botoEstadistiques);
        panellPrincipal.add(botoRanking);
        panellPrincipal.add(botoCrearPartida);
        panellPrincipal.add(botoReprendrePartida);
        panellPrincipal.add(botoEliminarPerfil);
        panellPrincipal.add(botoConsultarAvatars);
        panellPrincipal.add(botoSortir);
        panellPrincipal.setComponentZOrder(botoTancarSessio, 0);
        if (labelSignOut != null)
            panellPrincipal.setComponentZOrder(labelSignOut, 1);

        botoCrearAvatar.addActionListener(e -> CtrlPresentacio.canviarCrearAvatar());
        botoEliminarAvatar.addActionListener(e -> eliminarAvatar());
        botoNormes.addActionListener(e -> CtrlPresentacio.consultarNormes());
        botoEstadistiques.addActionListener(e -> CtrlPresentacio.canviarConsultarEstadistiques());
        botoRanking.addActionListener(e -> CtrlPresentacio.canviarConsultarRanking());
        botoConsultarAvatars.addActionListener(e -> CtrlPresentacio.canviarConsultarAvatars());
        botoCrearPartida.addActionListener(e -> CtrlPresentacio.canviarCrearPartida());
        botoReprendrePartida.addActionListener(e -> reprendrePartida());
        botoTancarSessio.addActionListener(e -> tancarSessio());
        botoSortir.addActionListener(e -> CtrlPresentacio.sortir());
        botoEliminarPerfil.addActionListener(e -> eliminarPerfil());

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                escalarComponentes();
            }
        });

        setVisible(true);
    }

    /**
     * Configura el MouseListener per canviar colors al passar el ratolí.
     */
    private void configurarMouseListener(JButton boton, Color colorNormal, Color textNormal) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(Colors.GREEN_BORDER);
                boton.setForeground(Colors.YELLOW_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorNormal);
                boton.setForeground(textNormal);
            }
        });
    }

    /**
     * Lògica per eliminar un avatar.
     */
    private void eliminarAvatar() {
        Object[] opcions = { "Eliminar", "Cancel·lar" };
        String avatar_a_eliminar;
        int resposta;

        do {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel panelNom = new JPanel();
            panelNom.add(new JLabel("Nom de l'avatar a eliminar:"));
            JTextField textFieldNom = new JTextField(15);
            panelNom.add(textFieldNom);
            panel.add(panelNom);

            resposta = JOptionPane.showOptionDialog(this, panel, "Scrabble - Eliminació d'avatar",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcions, opcions[1]);

            if (resposta == 0) {
                avatar_a_eliminar = textFieldNom.getText().trim();
                if (avatar_a_eliminar.isEmpty()) {
                    JOptionPane.showOptionDialog(
                            this,
                            "Has d'introduir un nom vàlid.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else {
                    Object[] opcionsConfirmacio = { "Si", "No" };
                    int confirmacio = JOptionPane.showOptionDialog(this, "Estàs segur que vols eliminar l'avatar?",
                            "Confirmació", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, opcionsConfirmacio, opcionsConfirmacio[1]);
                    if (confirmacio == JOptionPane.YES_OPTION) {
                        boolean eliminat = CtrlPresentacio.eliminarAvatar(avatar_a_eliminar);
                        JOptionPane.showOptionDialog(
                                this,
                                eliminat ? "Avatar eliminat correctament!" : "Aquest avatar no existeix",
                                "Scrabble - Eliminar Avatar",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new Object[] { "D'acord" },
                                "D'acord");
                    }
                    break;
                }
            }
        } while (resposta == 0);
    }

    /**
     * Lògica per reprendre una partida.
     */
    private void reprendrePartida() {
        String nomUsuari = CtrlPresentacio.getNomUsuariActiu();
        SelectorPartida selector = new SelectorPartida(null, nomUsuari);
        selector.setVisible(true);
        int idSeleccionat = selector.getIdPartida();
        if (idSeleccionat != -1) {
            CtrlPresentacio.reprendrePartida(idSeleccionat);

        } else {
            JOptionPane.showOptionDialog(
                    null,
                    "No s'ha seleccionat cap partida.",
                    "Scrabble - Reprendre Partida",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }
    }

    /**
     * Lògica per tancar la sessió.
     */
    private void tancarSessio() {
        Object[] opcions = { "Si", "No" };
        int respuesta = JOptionPane.showOptionDialog(this, "Estàs segur que vols tancar la sessió?",
                "Scrabble - Confirmació de tancament de sessió", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opcions, opcions[1]);
        if (respuesta == 0) {
            CtrlPresentacio.tancarSessio();
        }
    }

    /**
     * Lògica per eliminar el perfil.
     */
    private void eliminarPerfil() {
        Object[] opcions = { "Si", "No" };
        int respuesta = JOptionPane.showOptionDialog(this, "Estàs segur que vols eliminar aquest perfil?",
                "Scrabble - Confirmació d'eliminació de perfil", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opcions, opcions[1]);
        if (respuesta == 0) {
            boolean eliminat = false;
            try {
                eliminat = CtrlPresentacio.eliminarPerfil();
            } catch (IOException exc) {
                JOptionPane.showOptionDialog(
                    VistaMenuJoc.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
                exc.printStackTrace();
            }
            JOptionPane.showOptionDialog(
                    this,
                    eliminat ? "Perfil eliminat!"
                            : "Sembla que no has iniciat cap sessió, tanca l'aplicació per evitar malmetre l'aplicació!",
                    "Scrabble - Eliminar Perfil",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }
    }
}
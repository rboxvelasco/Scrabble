package interficie;

import javax.swing.*;
import controladors.CtrlPresentacio;
import domini.scrabble.*;
import domini.jugadors.*;
import interficie.utils.Colors;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;

/**
 * @class VistaPartida
 *        Classe que representa la interfície gràfica d'una partida de Scrabble.
 *
 *        Aquesta classe gestiona la visualització del taulell, els racks dels
 *        jugadors, la informació de la partida i els botons d'interacció.
 *        S'encarrega de mostrar i actualitzar els elements visuals durant el
 *        transcurs del joc, com el taulell, les fitxes, les puntuacions
 *        i els torns, així com de gestionar les accions dels jugadors
 *        (col·locar paraules, canviar fitxes, abandonar, etc.).
 *
 */
public class VistaPartida extends JFrame {
    private Taulell t;
    private JPanel boardContainer, racSuperior, racInferior, puntuacionsPanel, racRack;
    private JLabel lblTorn;
    private int idioma;
    private String id;

    /**
     * Constructor de la classe VistaPartida.
     *
     * Inicialitza la finestra de la partida amb la mateixa mida i posició que el
     * menú principal,
     * configura l'ícona de l'aplicació, inicialitza el taulell i l'idioma, i crida
     * els mètodes per
     * configurar la interfície i actualitzar els elements visuals.
     *
     * @param menuJoc Finestra del menú principal per obtenir mida i posició.
     * @param id      Identificador de l'idioma de la partida.
     */
    public VistaPartida(JFrame menuJoc, String id) {
        super("Scrabble - Partida");
        Dimension screenSize = menuJoc.getSize();
        setSize(screenSize.width, screenSize.height);
        setSize(menuJoc.getSize());
        setLocation(menuJoc.getLocation());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Reproduir so
        try {
            File audioFile = new File("resources/sounds/countdown.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
        
        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        this.t = CtrlPresentacio.getPartida().getTaulell();
        this.idioma = Integer.parseInt(id);
        this.id = id;
        initUI();

        refrescarTaulell();
        refrescarInformacio();
        refrescarRack();
        refrescarRackJugadors();
    }

    /**
     * Inicialitza els components de la interfície gràfica.
     *
     * Configura el layout de la finestra, crea els panells per al taulell, els
     * racks, la informació de la partida
     * i els botons d'interacció. També afegeix una llegenda per als multiplicadors
     * del taulell i associa els listeners
     * als botons per gestionar les accions del joc.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        racRack = new JPanel();
        topPanel.add(racRack, BorderLayout.CENTER);
        racSuperior = new JPanel();
        topPanel.add(racSuperior, BorderLayout.CENTER);
        topPanel.setBackground(Colors.GREEN_BACKGROUND);
        racSuperior.setBackground(Colors.GREEN_BACKGROUND);

        JPanel infoPartida = new JPanel(new GridLayout(2, 1));
        lblTorn = new JLabel("Torn: ");
        lblTorn.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        puntuacionsPanel = new JPanel();
        puntuacionsPanel.setLayout(new BoxLayout(puntuacionsPanel, BoxLayout.X_AXIS));
        puntuacionsPanel.setBackground(Colors.GREEN_BACKGROUND);
        infoPartida.add(lblTorn);
        infoPartida.add(puntuacionsPanel);
        infoPartida.setBackground(Colors.GREEN_BACKGROUND);

        JPanel infoWrapper = new JPanel();
        infoWrapper.setLayout(new BoxLayout(infoWrapper, BoxLayout.X_AXIS));
        infoWrapper.add(Box.createHorizontalGlue());
        infoWrapper.add(infoPartida);
        infoWrapper.add(Box.createHorizontalStrut(20));
        infoWrapper.setBackground(Colors.GREEN_BACKGROUND);
        topPanel.add(infoWrapper, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        boardContainer = new JPanel();
        boardContainer.setBackground(Colors.GREEN_BACKGROUND);

        JPanel boardWrapper = new JPanel(new BorderLayout());
        boardWrapper.setBackground(Colors.GREEN_BACKGROUND);
        boardWrapper.add(boardContainer, BorderLayout.CENTER);
        centerPanel.add(boardWrapper, BorderLayout.CENTER);
        centerPanel.setBackground(Colors.GREEN_BACKGROUND);

        JPanel botoneraLateral = new JPanel(null);
        botoneraLateral.setPreferredSize(new Dimension(300, 800));
        botoneraLateral.setBackground(Colors.GREEN_BACKGROUND);

        JButton btnAbandonar = new JButton("Abandonar Partida");
        JButton btnPausar = new JButton("Pausar Partida");
        JButton btnGuardar = new JButton("Guardar Partida");
        JButton btnPassarTorn = new JButton("Passar Torn");
        JButton btnColocarParaula = new JButton("Col·locar Paraula");
        JButton btnCanviarFitxes = new JButton("Canviar Fitxes");

        btnAbandonar.setBackground(Colors.RED);
        btnAbandonar.setForeground(Color.BLACK);
        btnAbandonar.setBorder(BorderFactory.createLineBorder(Colors.RED_BORDER, 4));
        btnAbandonar.setBounds(60, 50, 180, 40);

        btnAbandonar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAbandonar.setBackground(Colors.RED_BORDER);
                btnAbandonar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAbandonar.setBackground(Colors.RED);
                btnAbandonar.setForeground(Color.BLACK);
            }
        });

        btnPausar.setBackground(Colors.YELLOW_LIGHT);
        btnPausar.setForeground(Color.BLACK);
        btnPausar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        btnPausar.setBounds(60, 110, 180, 40);

        btnPausar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPausar.setBackground(Colors.YELLOW_DARK);
                btnPausar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPausar.setBackground(Colors.YELLOW_LIGHT);
                btnPausar.setForeground(Color.BLACK);
            }
        });

        btnGuardar.setBackground(Colors.GREEN_LIGHT);
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        btnGuardar.setBounds(60, 170, 180, 40);

        btnGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(Colors.GREEN_TEXT);
                btnGuardar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(Colors.GREEN_LIGHT);
                btnGuardar.setForeground(Color.BLACK);
            }
        });

        btnPassarTorn.setBackground(Colors.YELLOW_LIGHT);
        btnPassarTorn.setForeground(Color.BLACK);
        btnPassarTorn.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        btnPassarTorn.setBounds(60, 230, 180, 40);

        btnPassarTorn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPassarTorn.setBackground(Colors.YELLOW_DARK);
                btnPassarTorn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPassarTorn.setBackground(Colors.YELLOW_LIGHT);
                btnPassarTorn.setForeground(Color.BLACK);
            }
        });

        btnColocarParaula.setBackground(Colors.GREEN_LIGHT);
        btnColocarParaula.setForeground(Color.BLACK);
        btnColocarParaula.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        btnColocarParaula.setBounds(60, 290, 180, 40);
        btnColocarParaula.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnColocarParaula.setBackground(Colors.GREEN_TEXT);
                btnColocarParaula.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnColocarParaula.setBackground(Colors.GREEN_LIGHT);
                btnColocarParaula.setForeground(Color.BLACK);
            }
        });

        btnCanviarFitxes.setBackground(Colors.YELLOW_LIGHT);
        btnCanviarFitxes.setForeground(Color.BLACK);
        btnCanviarFitxes.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        btnCanviarFitxes.setBounds(60, 350, 180, 40);

        btnCanviarFitxes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCanviarFitxes.setBackground(Colors.YELLOW_DARK);
                btnCanviarFitxes.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCanviarFitxes.setBackground(Colors.YELLOW_LIGHT);
                btnCanviarFitxes.setForeground(Color.BLACK);
            }
        });

        botoneraLateral.add(btnAbandonar);
        botoneraLateral.add(btnPausar);
        botoneraLateral.add(btnGuardar);
        botoneraLateral.add(btnPassarTorn);
        botoneraLateral.add(btnColocarParaula);
        botoneraLateral.add(btnCanviarFitxes);

        JPanel leyendaPanel = new JPanel();
        leyendaPanel.setBackground(Colors.GREEN_BACKGROUND);
        leyendaPanel.setLayout(new GridLayout(5, 1, 5, 5));

        leyendaPanel.add(crearEtiquetaColor(new Color(135, 206, 250), "Doble Lletra"));
        leyendaPanel.add(crearEtiquetaColor(new Color(0, 128, 192), "Triple Lletra"));
        leyendaPanel.add(crearEtiquetaColor(new Color(255, 182, 193), "Doble Paraula"));
        leyendaPanel.add(crearEtiquetaColor(new Color(220, 20, 60), "Triple Paraula"));
        leyendaPanel.add(crearEtiquetaColor(new Color(245, 222, 179), "Normal"));

        leyendaPanel.setBounds(60, 410, 180, 150);
        botoneraLateral.add(leyendaPanel);

        btnPassarTorn.addActionListener(e -> {
            try {
                File audioFile = new File("resources/sounds/boo.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            }
            if (CtrlPresentacio.passarTornPartida()) {
                JOptionPane.showOptionDialog(
                        VistaPartida.this,
                        "S'ha acabat la partida. \n" + "El jugador " + CtrlPresentacio.getNomUsuariActiu()
                                + " ha guanyat la partida perquè l'oponent ha passat 3 torns consecutius.",
                        "Partida Acabada",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");
                CtrlPresentacio.canviRankingPartida();

            }

            else if (!"Maquina".equals(CtrlPresentacio.getPartida().getJugadorActual(false).getNom()))
                CtrlPresentacio.canviarCanviTorn();

            refrescarTaulell();
            refrescarInformacio();
            refrescarRack();
            refrescarRackJugadors();
        });

        btnCanviarFitxes.addActionListener(e -> {
            JTextField numeroField = new JTextField(5);
            JPanel panelNumero = new JPanel();
            panelNumero.setLayout(new BoxLayout(panelNumero, BoxLayout.Y_AXIS));
            panelNumero.add(new JLabel("Nombre de fitxes a canviar (1-7):"));
            panelNumero.add(numeroField);

            Object[] options = { "D'acord", "Cancel·lar" };

            int resultNumero = JOptionPane.showOptionDialog(this, panelNumero, "Canviar fitxes",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (resultNumero == JOptionPane.OK_OPTION) {
                try {
                    int numero = Integer.parseInt(numeroField.getText().trim());

                    if (numero < 1 || numero > 7) {
                        JOptionPane.showOptionDialog(
                                VistaPartida.this,
                                "Has d'introduir un nombre entre 1 i 7.",
                                "Error",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                new Object[] { "D'acord" },
                                "D'acord");
                        return;
                    }
                    List<Fitxa> fitxesDisponibles = CtrlPresentacio.getLletresJugadorTornPartida();
                    if (numero == 7) {
                        List<String> totesFitxes = new ArrayList<>();
                        for (Fitxa f : fitxesDisponibles) {
                            totesFitxes.add(f.getLletra());
                        }
                        int codi = CtrlPresentacio.canviarFitxesPartida(totesFitxes, 7, true, "", "");

                        if (codi == 8) {
                            JOptionPane.showOptionDialog(
                                    VistaPartida.this,
                                    "La bossa està buida. No pots canviar fitxes.",
                                    "Error",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE,
                                    null,
                                    new Object[] { "D'acord" },
                                    "D'acord");

                        } else if (codi == 0) {
                            Jugador jugadorActual = CtrlPresentacio.getPartida().getJugadorActual(false);
                            if (!"Maquina".equals(jugadorActual.getNom()))
                                CtrlPresentacio.canviarCanviTorn();

                            refrescarTaulell();
                            refrescarInformacio();
                            refrescarRack();
                            refrescarRackJugadors();
                        }

                    } else {
                        JTextField fichasField = new JTextField(20);
                        JPanel panelFitxes = new JPanel();
                        panelFitxes.setLayout(new BoxLayout(panelFitxes, BoxLayout.Y_AXIS));
                        panelFitxes.add(new JLabel("Fitxes a canviar (separades per espais):"));
                        panelFitxes.add(fichasField);
                        panelFitxes.setBackground(Colors.GREEN_BACKGROUND);

                        int resultFitxes = JOptionPane.showOptionDialog(null, panelFitxes, "Indica les fitxes",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                        if (resultFitxes == JOptionPane.OK_OPTION) {
                            String fichasInput = fichasField.getText().trim();

                            if (fichasInput.isEmpty()) {
                                JOptionPane.showOptionDialog(
                                        VistaPartida.this,
                                        "Falta introduir les fitxes a canviar.",
                                        "Error",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE,
                                        null,
                                        new Object[] { "D'acord" },
                                        "D'acord");
                                return;
                            }

                            String[] fichas = fichasInput.toUpperCase().split("\\s+");

                            if (fichas.length != numero) {
                                JOptionPane.showOptionDialog(
                                        VistaPartida.this,
                                        "El nombre de fitxes no coincideix amb la xifra introduïda.",
                                        "Error",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE,
                                        null,
                                        new Object[] { "D'acord" },
                                        "D'acord");
                                return;
                            }

                            List<String> fichasList = Arrays.asList(fichas);

                            int codi = CtrlPresentacio.canviarFitxesPartida(fichasList, numero, true, "", "");

                            if (codi == 7) {
                                JOptionPane.showOptionDialog(
                                        VistaPartida.this,
                                        "La fitxa introduïda no es troba al rack.",
                                        "Error",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE,
                                        null,
                                        new Object[] { "D'acord" },
                                        "D'acord");
                            } else if (codi == 8) {
                                JOptionPane.showOptionDialog(
                                        VistaPartida.this,
                                        "La bossa està buida. No pots canviar fitxes.",
                                        "Error",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.ERROR_MESSAGE,
                                        null,
                                        new Object[] { "D'acord" },
                                        "D'acord");
                            } else if (codi == 0) {
                                if (CtrlPresentacio.finalitzarPerLletresEsgotadesPartida()) {
                                    JOptionPane.showOptionDialog(
                                            VistaPartida.this,
                                            "La partida ha finalitzat per lletres esgotades tant en els jugadors com a la bossa.",
                                            "Partida Acabada",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            new Object[] { "D'acord" },
                                            "D'acord");
                                    CtrlPresentacio.canviRankingPartida();
                                }

                                Jugador jugadorActual = CtrlPresentacio.getPartida().getJugadorActual(false);
                                if (!"Maquina".equals(jugadorActual.getNom()))
                                    CtrlPresentacio.canviarCanviTorn();

                                refrescarTaulell();
                                refrescarInformacio();
                                refrescarRack();
                                refrescarRackJugadors();
                            }
                        }
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "Introdueix una xifra vàlida.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                }
            }
        });

        btnPausar.addActionListener(e -> {
            Object[] opcions = { "Abandonar Partida", "Reprendre Partida", "Guardar Partida" };
            int resposta = JOptionPane.showOptionDialog(
                    this,
                    "La partida s'ha pausat. Què vols fer a continuació?",
                    "Scrabble - Pausa de Partida",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcions,
                    opcions[1]);
            if (resposta == 0)
                confirmarAbandonar();
            else if (resposta == 2)
                confirmarGuardar();
        });

        btnColocarParaula.addActionListener(e -> {
            int size = t.getMida();
            JTextField tfPalabra = new JTextField(10);
            JSpinner spinnerFila = new JSpinner(new SpinnerNumberModel(size / 2, 0, size - 1, 1));
            JSpinner spinnerColumna = new JSpinner(new SpinnerNumberModel(size / 2, 0, size - 1, 1));
            JRadioButton rbHorizontal = new JRadioButton("Horizontal", true);
            JRadioButton rbVertical = new JRadioButton("Vertical");
            ButtonGroup grupoOrientacion = new ButtonGroup();
            grupoOrientacion.add(rbHorizontal);
            grupoOrientacion.add(rbVertical);

            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            panel.add(new JLabel("Introdueix la paraula:"));
            panel.add(tfPalabra);
            panel.add(new JLabel("Introdueix la fila (0 a " + (size - 1) + "):"));
            panel.add(spinnerFila);
            panel.add(new JLabel("Introdueix la columna (0 a " + (size - 1) + "):"));
            panel.add(spinnerColumna);
            panel.add(new JLabel("Orientació:"));
            JPanel orientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            orientPanel.add(rbHorizontal);
            orientPanel.add(rbVertical);
            panel.add(orientPanel);

            int codi = -1;
            while (codi != 0 && codi != 8) {
                Object[] options = { "D'acord", "Cancel·lar" };

                int result = JOptionPane.showOptionDialog(this, panel, "Col·locar Paraula",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (result != JOptionPane.OK_OPTION) {
                    break;
                }

                String palabra = tfPalabra.getText().trim().toUpperCase();
                int fila = (Integer) spinnerFila.getValue();
                int columna = (Integer) spinnerColumna.getValue();
                boolean horizontal = rbHorizontal.isSelected();

                codi = CtrlPresentacio.colocarParaulaPartida(palabra, fila, columna, horizontal);

                if (codi != 0) {
                    try {
                        File audioFile = new File("resources/sounds/wrong.wav");
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
                    }
                }

                if (codi == 1) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La paraula que has introduït se surt del taulell.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 2) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La paraula que has introduït no es troba al diccionari.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 3) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "Alguna fitxa de la paraula no es troba al rack ni al taulell.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 4) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La primera paraula ha de passar pel centre del taulell.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 5) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La paraula introduïda no toca cap fitxa del taulell.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 6) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "Alguna de les paraules creuades no es troba al diccionari.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 7) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La fitxa introduida no es troba al rack.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 8) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La bossa està buida. Ja no pots reposar fitxes, però la partida pot seguir.",
                            "Bossa Buida",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 9) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "Estàs intentant sobreescriure una paraula del taulell.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else if (codi == 10) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La paraula inserida està al diccionari, però estàs formant una que no hi és.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                }
            }

            if (codi == 0 || codi == 8) {
                try {
                    File audioFile = new File("resources/sounds/ok_word.wav");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
                }

                if (CtrlPresentacio.finalitzarPerLletresEsgotadesPartida()) {
                    JOptionPane.showOptionDialog(
                            VistaPartida.this,
                            "La partida ha finalitzat perquè la bossa és buida i un jugador ha col·locat totes les seves fitxes.",
                            "Partida Acabada",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    CtrlPresentacio.canviRankingPartida();
                }

                if (codi == 8) CtrlPresentacio.getPartida().incrementarTorn();
                Jugador jugadorActual = CtrlPresentacio.getPartida().getJugadorActual(false);
                if (!"Maquina".equals(jugadorActual.getNom()))
                    CtrlPresentacio.canviarCanviTorn();

                refrescarTaulell();
                refrescarInformacio();
                refrescarRackJugadors();
            }
        });

        btnAbandonar.addActionListener(e -> confirmarAbandonar());
        btnGuardar.addActionListener(e -> confirmarGuardar());

        centerPanel.add(botoneraLateral, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        racInferior = new JPanel();
        add(racInferior, BorderLayout.SOUTH);
        racInferior.setBackground(Colors.GREEN_BACKGROUND);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
    }

    /**
     * Crea un component visual amb un quadrat de color i un text descriptiu.
     *
     * Aquest mètode és utilitzat per generar les etiquetes de la llegenda dels
     * multiplicadors del taulell.
     *
     * @param color Color del quadrat.
     * @param text  Text descriptiu del multiplicador.
     * @return Un JPanel amb un quadrat de color i una etiqueta de text.
     */
    public static JPanel crearEtiquetaColor(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        JLabel square = new JLabel();
        square.setOpaque(true);
        square.setBackground(color);
        square.setPreferredSize(new Dimension(20, 20));
        square.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);

        panel.add(square);
        panel.add(label);

        return panel;
    }

    /**
     * Actualitza la visualització del rack del jugador actual.
     *
     * Carrega les imatges de les fitxes del jugador actual segons l'idioma de la
     * partida i les mostra
     * al panell `racRack`. Si no es pot carregar una imatge, es mostra una
     * representació de text com a alternativa.
     */
    public void refrescarRack() {
        Partida partida = CtrlPresentacio.getPartida();
        Jugador jugadorActual = partida.getJugadorActual(false);
        racRack.removeAll();
        racRack.setLayout(new FlowLayout());
        String idiomaFitxes = id.equals("2") ? "esp" : id.equals("3") ? "eng" : "cat";
        for (Fitxa f : jugadorActual.getFitxes_actuals()) {
            try {
                String lletra = f.getLletra();
                String ruta = "resources/" + idiomaFitxes + "_" + lletra.toUpperCase() + ".png";
                BufferedImage img = ImageIO.read(new File(ruta));
                ImageIcon icon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                JLabel lbl = new JLabel(icon);
                lbl.setPreferredSize(new Dimension(40, 40));
                racRack.add(lbl);
            } catch (Exception e) {
                JLabel fallback = new JLabel(f.getLletra());
                fallback.setFont(new Font("Arial", Font.BOLD, 18));
                fallback.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                fallback.setOpaque(true);
                fallback.setBackground(Color.LIGHT_GRAY);
                fallback.setPreferredSize(new Dimension(40, 40));
                fallback.setHorizontalAlignment(SwingConstants.CENTER);
                racRack.add(fallback);
            }
        }
        racRack.revalidate();
        racRack.repaint();
    }

    /**
     * Confirma l'acció d'abandonar la partida.
     *
     * Mostra un diàleg de confirmació i, si l'usuari accepta, crida el controlador
     * per abandonar la partida
     * i actualitzar el rànquing.
     */
    private void confirmarAbandonar() {
        Object[] opcions = { "Sí", "No" };
        int resposta = JOptionPane.showOptionDialog(
                this,
                "Vols abandonar i tornar al menú principal?\n Les dades no es guardaràn i la partida no es podrà reprendre.",
                "Scrabble - Confirmació",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcions,
                opcions[0]);
        if (resposta == 0) {
            CtrlPresentacio.abandonarPartida();
            CtrlPresentacio.canviRankingPartida();
        }
    }

    /**
     * Confirma l'acció de guardar la partida.
     *
     * Mostra un diàleg de confirmació i, si l'usuari accepta, crida el controlador
     * per guardar l'estat
     * de la partida i tornar al menú principal.
     */
    private void confirmarGuardar() {
        Object[] opcions = { "Sí", "No" };
        int resposta = JOptionPane.showOptionDialog(this,
                "La partida es guardarà i podràs reanudar-la en qualsevol moment. Vols continuar?",
                "Scrabble - Guardar Partida", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcions,
                opcions[0]);

        if (resposta == 0) {
            try {
                CtrlPresentacio.guardarPartida();
            } catch (IOException exc) {
                JOptionPane.showOptionDialog(
                        VistaPartida.this,
                        "Error de connexió amb el servidor.",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");
                exc.printStackTrace();
            }
            CtrlPresentacio.tornar();
        }
    }

    /**
     * Actualitza la visualització del taulell de joc.
     *
     * Reconstrueix el taulell amb les caselles i les seves fitxes, aplicant els
     * colors corresponents
     * als multiplicadors i mostrant les imatges de les fitxes o una representació
     * de text si no es poden carregar.
     * També afegeix etiquetes de files i columnes per facilitar la navegació.
     */
    public void refrescarTaulell() {
        int size = t.getMida();
        boardContainer.removeAll();
        boardContainer.setLayout(new GridLayout(size, size, 2, 2));

        String prefixIdioma;
        switch (idioma) {
            case 1:
                prefixIdioma = "cat";
                break;
            case 2:
                prefixIdioma = "esp";
                break;
            case 3:
                prefixIdioma = "eng";
                break;
            default:
                prefixIdioma = "cat";
                break;
        }

        JPanel colLabels = new JPanel(new GridLayout(1, size, 2, 2));
        colLabels.setBackground(Colors.GREEN_BACKGROUND);
        for (int j = 0; j < size; j++) {
            JLabel colLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER);
            colLabel.setFont(new Font("Arial", Font.BOLD, 12));
            colLabel.setForeground(Color.BLACK);
            colLabel.setPreferredSize(new Dimension(50, 20));
            colLabels.add(colLabel);
        }

        JPanel rowLabels = new JPanel(new GridLayout(size, 1, 2, 2));
        rowLabels.setBackground(Colors.GREEN_BACKGROUND);

        for (int i = 0; i < size; i++) {
            JLabel rowLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
            rowLabel.setForeground(Color.BLACK);
            rowLabel.setPreferredSize(new Dimension(20, 50));
            rowLabels.add(rowLabel);

            for (int j = 0; j < size; j++) {
                Casella c = t.getCasella(i, j);
                JLabel lbl = new JLabel("", SwingConstants.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        BorderFactory.createEmptyBorder(2, 2, 2, 2)));
                lbl.setPreferredSize(new Dimension(50, 50));
                lbl.setMinimumSize(new Dimension(50, 50));
                lbl.setMaximumSize(new Dimension(50, 50));

                Color bgColor;
                switch (c.getMultiplicador()) {
                    case DOBLE_LLETRA:
                        bgColor = new Color(135, 206, 250);
                        break;
                    case TRIPLE_LLETRA:
                        bgColor = new Color(0, 128, 192);
                        break;
                    case DOBLE_PARAULA:
                        bgColor = new Color(255, 182, 193);
                        break;
                    case TRIPLE_PARAULA:
                        bgColor = new Color(220, 20, 60);
                        break;
                    default:
                        bgColor = new Color(245, 222, 179);
                        break;
                }
                lbl.setBackground(bgColor);

                if (i == size / 2 && j == size / 2 && c.getFitxa() == null) {
                    try {
                        BufferedImage star = ImageIO.read(new File("./resources/star.png"));
                        ImageIcon icon = new ImageIcon(star.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                        lbl.setIcon(icon);
                    } catch (IOException e) {
                        lbl.setText("★");
                        lbl.setFont(new Font("Arial", Font.BOLD, 16));
                        lbl.setForeground(Color.BLACK);
                    }
                }

                if (c.getFitxa() != null) {
                    try {
                        String lletra = c.getFitxa().getLletra();
                        String ruta = "./resources/" + prefixIdioma + "_" + lletra.toUpperCase() + ".png";
                        BufferedImage img = ImageIO.read(new File(ruta));
                        ImageIcon icon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                        lbl.setIcon(icon);
                        lbl.setBackground(bgColor);
                    } catch (Exception e) {
                        lbl.setText(c.getFitxa().getLletra());
                        lbl.setFont(new Font("Arial", Font.BOLD, 16));
                        lbl.setForeground(Color.BLACK);
                        lbl.setBackground(bgColor);
                    }
                }

                boardContainer.add(lbl);
            }
        }

        JPanel boardWrapper = (JPanel) boardContainer.getParent();
        boardWrapper.removeAll();
        boardWrapper.add(colLabels, BorderLayout.NORTH);
        boardWrapper.add(rowLabels, BorderLayout.WEST);
        boardWrapper.add(boardContainer, BorderLayout.CENTER);

        boardContainer.revalidate();
        boardContainer.repaint();
        boardWrapper.revalidate();
        boardWrapper.repaint();
    }

    /**
     * Crea un panell amb la informació d'un jugador.
     *
     * Genera un panell amb l'avatar i el nom del jugador, així com la seva
     * puntuació actual.
     *
     * @param jugador    Jugador del qual es mostra la informació.
     * @param puntuacion Puntuació actual del jugador.
     * @return Un JPanel amb l'avatar i la informació del jugador.
     */
    private JPanel crearPanelJugador(Jugador jugador, int puntuacion) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Colors.GREEN_BACKGROUND);

        String avatarPath = jugador.getrutaImatge().toString();
        try {
            BufferedImage img = ImageIO.read(new File(avatarPath));
            ImageIcon icon = new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            JLabel lblAvatar = new JLabel(icon);
            panel.add(lblAvatar);
        } catch (IOException e) {
            JOptionPane.showOptionDialog(
                    VistaPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        JLabel lblInfo = new JLabel(jugador.getNom() + " (" + puntuacion + ")");
        lblInfo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        panel.add(lblInfo);

        return panel;
    }

    /**
     * Actualitza la informació de la partida.
     *
     * Actualitza el text del torn actual i les puntuacions dels jugadors. Si el
     * torn és de la màquina,
     * executa la jugada de la màquina i actualitza el taulell, la informació i els
     * racks.
     */
    public void refrescarInformacio() {
        Jugador jugadorActual = CtrlPresentacio.getPartida().getJugadorActual(false);

        if ("Maquina".equals(jugadorActual.getNom())) {
            CtrlPresentacio.ferJugadaMaquinaPartida();
            refrescarTaulell();
            refrescarInformacio();
            refrescarRack();
            refrescarRackJugadors();
        }

        Partida partida = CtrlPresentacio.getPartida();
        lblTorn.setText("Torn actual: " + partida.getJugadorActual(false).getNom());
        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();

        puntuacionsPanel.removeAll();
        puntuacionsPanel.add(crearPanelJugador(j1, partida.getPuntuacioJugador1()));
        puntuacionsPanel.add(Box.createHorizontalStrut(10));
        puntuacionsPanel.add(crearPanelJugador(j2, partida.getPuntuacioJugador2()));
        puntuacionsPanel.add(Box.createHorizontalStrut(50));
        puntuacionsPanel.revalidate();
        puntuacionsPanel.repaint();
    }

    /**
     * Actualitza els racks dels jugadors.
     *
     * Mostra les fitxes del jugador actual i maquetes per a les fitxes de
     * l'oponent, alternant els panells
     * superior i inferior segons el torn.
     */
    public void refrescarRackJugadors() {
        Partida partida = CtrlPresentacio.getPartida();
        Jugador jugadorActual = partida.getJugadorActual(false);
        Jugador jugadorOponent = (jugadorActual == partida.getJugador1()) ? partida.getJugador2()
                : partida.getJugador1();

        JPanel racActual, racOponent;
        if (partida.getTorn() % 2 == 0) {
            racActual = racSuperior;
            racOponent = racInferior;
        } else {
            racActual = racInferior;
            racOponent = racSuperior;
        }

        racActual.removeAll();
        racOponent.removeAll();

        afegirFitxesARack(racActual, jugadorActual);
        afegirMaquetesARack(racOponent, jugadorOponent);

        racActual.revalidate();
        racOponent.revalidate();
        racActual.repaint();
        racOponent.repaint();
    }

    /**
     * Afegeix les fitxes d'un jugador al seu rack.
     *
     * Carrega les imatges de les fitxes del jugador segons l'idioma i les afegeix
     * al panell corresponent,
     * juntament amb l'avatar del jugador. Si no es pot carregar una imatge, es
     * mostra una representació de text.
     *
     * @param rac     Panell on es mostren les fitxes.
     * @param jugador Jugador del qual es mostren les fitxes.
     */
    private void afegirFitxesARack(JPanel rac, Jugador jugador) {
        rac.setLayout(new FlowLayout());
        String idioma = "cat";
        if (id.equals("2")) {
            idioma = "esp";
        } else if (id.equals("3")) {
            idioma = "eng";
        }

        String avatarPath = jugador.getrutaImatge().toString();
        try {
            BufferedImage img = ImageIO.read(new File(avatarPath));
            ImageIcon icon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            JLabel lblAvatar = new JLabel(icon);
            rac.add(lblAvatar);
        } catch (IOException e) {
            JOptionPane.showOptionDialog(
                    VistaPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        for (Fitxa f : jugador.getFitxes_actuals()) {
            try {
                String lletra = f.getLletra();
                String ruta = "resources/" + idioma + "_" + lletra.toUpperCase() + ".png";
                BufferedImage imgFitxa = ImageIO.read(new File(ruta));
                ImageIcon iconFitxa = new ImageIcon(imgFitxa.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                JLabel lbl = new JLabel(iconFitxa);
                lbl.setPreferredSize(new Dimension(40, 40));
                rac.add(lbl);
            } catch (Exception e) {
                JLabel fallback = new JLabel(f.getLletra());
                fallback.setFont(new Font("Arial", Font.BOLD, 18));
                fallback.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                fallback.setOpaque(true);
                fallback.setBackground(Color.LIGHT_GRAY);
                fallback.setPreferredSize(new Dimension(40, 40));
                fallback.setHorizontalAlignment(SwingConstants.CENTER);
                rac.add(fallback);
            }
        }
    }

    /**
     * Afegeix maquetes al rack de l'oponent.
     *
     * Afegeix imatges de maquetes per representar les fitxes de l'oponent,
     * juntament amb el seu avatar,
     * sense revelar les lletres reals.
     *
     * @param rac     Panell on es mostren les maquetes.
     * @param jugador Jugador del qual es mostren les maquetes.
     */
    private void afegirMaquetesARack(JPanel rac, Jugador jugador) {
        rac.setLayout(new FlowLayout());

        String avatarPath = jugador.getrutaImatge().toString();
        try {
            BufferedImage img = ImageIO.read(new File(avatarPath));
            ImageIcon icon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            JLabel lblAvatar = new JLabel(icon);
            rac.add(lblAvatar);
        } catch (IOException e) {
            JOptionPane.showOptionDialog(
                    VistaPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        int quantitat = jugador.getFitxes_actuals().size();
        for (int i = 0; i < quantitat; i++) {
            try {
                String ruta = "resources/maqueta_ficha.png";
                BufferedImage img = ImageIO.read(new File(ruta));
                ImageIcon icon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                JLabel lbl = new JLabel(icon);
                lbl.setPreferredSize(new Dimension(40, 40));
                rac.add(lbl);
            } catch (Exception e) {
                JLabel fallback = new JLabel("?");
                fallback.setFont(new Font("Arial", Font.BOLD, 18));
                fallback.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                fallback.setOpaque(true);
                fallback.setBackground(Color.LIGHT_GRAY);
                fallback.setPreferredSize(new Dimension(40, 40));
                fallback.setHorizontalAlignment(SwingConstants.CENTER);
                rac.add(fallback);
            }
        }
    }
}
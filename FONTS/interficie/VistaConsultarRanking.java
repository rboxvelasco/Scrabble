package interficie;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import domini.jugadors.Avatar;
import interficie.utils.*;
import javax.sound.sampled.*;


/**
 * @class VistaConsultarRanking
 *        Vista gràfica que mostra el rànquing d'avatars dels jugadors, ordenats
 *        per puntuació.
 * 
 *        Aquesta finestra permet visualitzar les puntuacions totals i partides
 *        guanyades dels jugadors
 *        en format de rànquing. Mostra medalles per als tres primers
 *        classificats, icones d'avatars i
 *        altres elements visuals. També proporciona un botó per tornar al menú
 *        anterior.
 */
public class VistaConsultarRanking extends JFrame {
    private JPanel panell;
    private JTextArea areaRanking;
    private JButton botoTornar;
    private JScrollPane scrollPane;
    private final int WIDTH_BOTONS = 100, HEIGHT_BOTONS = 40;
    private ImageIcon imatgeLogo;
    private JLabel labelLogo;

    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;

    /**
     * Constructor de la classe. Crea i inicialitza la finestra de rànquing.
     *
     * @param menuOriginal Finestra anterior per obtenir la mida i posició.
     */
    public VistaConsultarRanking(JFrame menuOriginal) {
        setTitle("Scrabble - Rànquing d'Avatars");
        Dimension screenSize = menuOriginal.getSize();
        setSize(screenSize.width, screenSize.height);
        setLocation(menuOriginal.getLocation());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Icone d'aplicació
        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaConsultarRanking.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        panell = new JPanel(new BorderLayout(15, 15));
        panell.setBackground(Colors.GREEN_BACKGROUND);
        panell.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panell);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, LOGO_WIDTH, LOGO_HEIGHT);
            labelLogo.setBounds(10, 10, LOGO_WIDTH, LOGO_HEIGHT);
            panell.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaConsultarRanking.this,
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

        JLabel titol = new JLabel("Rànquing d'Avatars", SwingConstants.CENTER);
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        titol.setForeground(Colors.GREEN_TEXT);
        panell.add(titol, BorderLayout.NORTH);

        areaRanking = new JTextArea();
        areaRanking.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        areaRanking.setEditable(false);
        areaRanking.setOpaque(false);

        scrollPane = new JScrollPane(areaRanking);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        panell.add(scrollPane, BorderLayout.CENTER);

        JPanel panellBotons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panellBotons.setBackground(Colors.GREEN_BACKGROUND);

        botoTornar = new JButton("TORNAR");
        botoTornar.setMnemonic(KeyEvent.VK_T); // Perquè s'activi també amb Alt. + T
        botoTornar.setPreferredSize(new Dimension(WIDTH_BOTONS, HEIGHT_BOTONS));
        botoTornar.setBackground(Colors.YELLOW_LIGHT);
        botoTornar.setForeground(Color.BLACK);
        botoTornar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        botoTornar.addActionListener(e -> CtrlPresentacio.tornar());

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
        panell.add(panellBotons, BorderLayout.SOUTH);

        int limit = -1;
        while (limit < 0) {
            Object[] options = { "Acceptar", "Cancel·lar" };

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("Introdueix quantes posicions vols que es mostrin al ranking:");
            JTextField textField = new JTextField(10);
            panel.add(label);
            panel.add(Box.createVerticalStrut(10));
            panel.add(textField);

            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Entrada de límit",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (result == 1 || result == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showOptionDialog(
                        this,
                        "Entrada cancel·lada.",
                        "Cancel·lar",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.CANCEL_OPTION,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");
                return;
            } else if (result == 0) {
                String input = textField.getText();
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showOptionDialog(
                            this,
                            "Has d'introduir un valor.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    continue;
                }

                try {
                    limit = Integer.parseInt(input);
                    if (limit < 0) {
                        JOptionPane.showOptionDialog(
                                this,
                                "Introdueix un nombre enter positiu.",
                                "Error",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                new Object[] { "D'acord" },
                                "D'acord");
                        limit = -1;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showOptionDialog(
                            this,
                            "Introdueix un nombre enter vàlid.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                }
            }
        }

        boolean sound = carregarRanking(limit);
        setVisible(true);
        if (sound) {
            try {
                File audioFile = new File("resources/sounds/wow.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException exc) {
            }
        }
    }

    /**
     * Carrega i mostra el rànquing a partir del límit indicat.
     * Si no hi ha dades, mostra un missatge d'error amb una icona.
     * Si hi ha dades, les organitza en un panell amb format gràfic.
     *
     * @param limit Nombre màxim de posicions que es volen mostrar.
     */
    private boolean carregarRanking(int limit) {
        List<Avatar> ranking = CtrlPresentacio.getRanking(limit);
        if (ranking == null || ranking.isEmpty()) {
            areaRanking.setVisible(false);

            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
            errorPanel.setBackground(Colors.GREEN_BACKGROUND);

            errorPanel.add(Box.createVerticalStrut(175));

            JLabel missatge = new JLabel("No hi ha cap avatar que hagi jugat una partida jugador vs jugador.");
            missatge.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            missatge.setForeground(Color.RED);
            missatge.setAlignmentX(Component.CENTER_ALIGNMENT);
            missatge.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel imagenTriste = new JLabel();
            try {
                ImageIcon iconoTriste = new ImageIcon("./resources/triste.png");
                Image imagenEscalada = iconoTriste.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                imagenTriste.setIcon(new ImageIcon(imagenEscalada));
                imagenTriste.setAlignmentX(Component.CENTER_ALIGNMENT);
            } catch (Exception e) {
                JOptionPane.showOptionDialog(
                    VistaConsultarRanking.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
            }

            errorPanel.add(missatge);
            errorPanel.add(Box.createVerticalStrut(15));
            errorPanel.add(imagenTriste);

            panell.add(errorPanel, BorderLayout.CENTER);
            return false;
        }

        if (limit > ranking.size()) {
            JOptionPane.showOptionDialog(
                    this,
                    "Només hi ha " + ranking.size() + " jugadors que han jugat partides jugador vs jugador.",
                    "Informació",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        JPanel rankingPanel = new JPanel();
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));
        rankingPanel.setOpaque(false);
        rankingPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel header = new JPanel(new GridLayout(1, 4, 10, 10));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblPosicio = new JLabel("Posició", SwingConstants.CENTER);
        JLabel lblNom = new JLabel("Jugador", SwingConstants.CENTER);
        JLabel lblPuntuacio = new JLabel("Puntuació", SwingConstants.CENTER);
        JLabel lblGuanyades = new JLabel("Partides Guanyades", SwingConstants.CENTER);

        Font titolFont = new Font("Comic Sans MS", Font.BOLD, 18);
        Color colorTitol = new Color(0, 100, 0);

        lblPosicio.setForeground(colorTitol);
        lblNom.setForeground(colorTitol);
        lblPuntuacio.setForeground(colorTitol);
        lblGuanyades.setForeground(colorTitol);

        lblPosicio.setFont(titolFont);
        lblNom.setFont(titolFont);
        lblPuntuacio.setFont(titolFont);
        lblGuanyades.setFont(titolFont);

        header.add(lblPosicio);
        header.add(lblNom);
        header.add(lblPuntuacio);
        header.add(lblGuanyades);

        rankingPanel.add(header);
        rankingPanel.add(Box.createVerticalStrut(15));

        String[] medallaRutes = {
                "./resources/medalla1.png",
                "./resources/medalla2.png",
                "./resources/medalla3.png"
        };

        Color[] coloresFondo = {
                new Color(255, 215, 0, 50),
                new Color(192, 192, 192, 50),
                new Color(205, 127, 50, 50)
        };

        int pos = 1;
        for (Avatar avatar : ranking) {
            JPanel fila = new JPanel(new GridLayout(1, 4, 10, 10));
            fila.setPreferredSize(new Dimension(800, 70));
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

            if (pos <= 3) {
                fila.setBackground(coloresFondo[pos - 1]);
                fila.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloresFondo[pos - 1].darker(), 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            } else {
                fila.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                fila.setBackground(new Color(255, 255, 255, 100));
            }
            fila.setOpaque(true);

            JPanel posPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            posPanel.setOpaque(false);
            if (pos <= 3) {
                ImageIcon icon = new ImageIcon(medallaRutes[pos - 1]);
                Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                posPanel.add(new JLabel(new ImageIcon(scaled)));
            } else {
                JLabel posLabel = new JLabel(String.valueOf(pos), JLabel.CENTER);
                posLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
                posLabel.setForeground(Colors.GREEN_TEXT);
                posPanel.add(posLabel);
            }
            fila.add(posPanel);

            JPanel nomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            nomPanel.setOpaque(false);
            JLabel nomLabel = new JLabel(avatar.getNom(), JLabel.CENTER);
            nomLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            nomLabel.setForeground(Color.BLACK);

            try {
                ImageIcon avatarIcon = new ImageIcon("./resources/avatars/" + avatar.getNom() + ".png");
                if (avatarIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image scaledAvatar = avatarIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    nomPanel.add(new JLabel(new ImageIcon(scaledAvatar)));
                }
            } catch (Exception e) {
                JOptionPane.showOptionDialog(
                    VistaConsultarRanking.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
            }

            nomPanel.add(nomLabel);
            fila.add(nomPanel);

            JPanel puntuacioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            puntuacioPanel.setOpaque(false);
            ImageIcon trofeuIcon = new ImageIcon("./resources/trofeu.png");
            Image trofeuScaled = trofeuIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            puntuacioPanel.add(new JLabel(new ImageIcon(trofeuScaled)));
            JLabel puntuacioLabel = new JLabel(String.format("%,d", avatar.getPuntuacio_total()) + " punts");
            puntuacioLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            puntuacioLabel.setForeground(Color.BLACK);
            puntuacioPanel.add(puntuacioLabel);
            fila.add(puntuacioPanel);

            JPanel guanyadesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            guanyadesPanel.setOpaque(false);
            ImageIcon ptIcon = new ImageIcon("./resources/pg.png");
            Image ptScaled = ptIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            guanyadesPanel.add(new JLabel(new ImageIcon(ptScaled)));
            JLabel guanyadesLabel = new JLabel(String.format("%,d", avatar.getPartides_guanyades()) + " guanyades");
            guanyadesLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            guanyadesLabel.setForeground(Color.BLACK);
            guanyadesPanel.add(guanyadesLabel);
            fila.add(guanyadesPanel);

            rankingPanel.add(fila);
            rankingPanel.add(Box.createVerticalStrut(5));
            pos++;
        }

        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NORTH;

        containerPanel.add(rankingPanel, gbc);
        scrollPane.setViewportView(containerPanel);
        scrollPane.getVerticalScrollBar().setValue(0);
    
        return true;
    }
}

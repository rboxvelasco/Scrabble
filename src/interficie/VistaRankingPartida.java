package interficie;

import javax.swing.*;
import controladors.CtrlPresentacio;
import java.awt.*;
import interficie.utils.Colors;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * @class VistaRankingPartida
 * Finestra que mostra el rànquing i el resultat final d'una partida de Scrabble.
 * 
 * Aquesta classe crea una interfície gràfica que mostra els noms dels dos jugadors,
 * els seus punts finals, i indica clarament qui ha guanyat amb un símbol d'estrella.
 * També permet tornar a la pantalla anterior mitjançant un botó.
 * 
 * La finestra disposa d'una estructura amb:
 * - Títol a la part superior.
 * - Missatge que indica el guanyador o si hi ha hagut empat.
 * - Visualització de punts de cada jugador amb colors diferents.
 * - Estrella a la dreta del guanyador.
 * - Botó "TORNAR" per tornar a la vista anterior.
 */
public class VistaRankingPartida extends JFrame {
    
    /**
     * Constructor que inicialitza i mostra la finestra de rànquing.
     * 
     * @param nomj1 Nom del primer jugador.
     * @param nomj2 Nom del segon jugador.
     * @param punts1 Punts obtinguts pel primer jugador.
     * @param punts2 Punts obtinguts pel segon jugador.
     * 
     * Configura la finestra, crea els components gràfics i mostra el resultat final
     * destacant el guanyador amb una estrella i un missatge personalitzat.
     * Inclou un botó per tornar a la pantalla anterior y reproduce un sonido al instanciarse.
     */
    public VistaRankingPartida(String nomj1, String nomj2, int punts1, int punts2) {
        setTitle("Ranking de la Partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Reproduir so
        try {
            File audioFile = new File("resources/sounds/end_of_game.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }

        // Icona d'aplicació
        try {
            ImageIcon icono = new ImageIcon("./resources/icon2.png");
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaRankingPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Colors.GREEN_BACKGROUND);
        add(mainPanel);

        // Títol
        JLabel titolLabel = new JLabel("Ranking de la Partida", SwingConstants.CENTER);
        titolLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        titolLabel.setForeground(Colors.GREEN_TEXT);
        titolLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        mainPanel.add(titolLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Colors.GREEN_BACKGROUND);
        centerPanel.add(Box.createVerticalStrut(100));

        // Construim la frase
        String frase;
        if (punts1 > punts2) {
            frase = "<html>El jugador <span style='color:blue;'>" + nomj1
                    + "</span> ha guanyat a <span style='color:red;'>" + nomj2
                    + "</span>, la puntuació final ha estat:</html>";
        } else if (punts2 > punts1) {
            frase = "<html>El jugador <span style='color:red;'>" + nomj2
                    + "</span> ha guanyat a <span style='color:blue;'>" + nomj1
                    + "</span>, la puntuació final ha estat:</html>";
        } else {
            frase = "<html>Heu empatat, la vostra puntuació ha estat:</html>";
        }

        JLabel fraseLabel = new JLabel(frase, SwingConstants.CENTER);
        fraseLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        fraseLabel.setForeground(Color.BLACK);
        fraseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fraseLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        centerPanel.add(fraseLabel);

        // Panel puntuacions
        JPanel scoresPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        scoresPanel.setBackground(Colors.GREEN_BACKGROUND);

        // Jugador 1
        JLabel puntsLabel1 = new JLabel(nomj1 + ": " + punts1);
        puntsLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        puntsLabel1.setForeground(Color.BLUE);
        JPanel j1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        j1Panel.setBackground(Colors.GREEN_BACKGROUND);
        j1Panel.add(puntsLabel1);

        // Jugador 2
        JLabel puntsLabel2 = new JLabel(nomj2 + ": " + punts2);
        puntsLabel2.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        puntsLabel2.setForeground(Color.RED);
        JPanel j2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        j2Panel.setBackground(Colors.GREEN_BACKGROUND);
        j2Panel.add(puntsLabel2);

        // Afegir estrella
        try {
            ImageIcon estrella = new ImageIcon("./resources/estrella.png");
            Image scaledImage = estrella.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel estrellaLabel = new JLabel(new ImageIcon(scaledImage));

            if (punts1 > punts2) {
                j1Panel.add(estrellaLabel);
            } else if (punts2 > punts1) {
                j2Panel.add(estrellaLabel);
            }
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaRankingPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        scoresPanel.add(j1Panel);
        scoresPanel.add(j2Panel);
        centerPanel.add(scoresPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Botó Tornar
        JButton tornarButton = new JButton("TORNAR");
        tornarButton.setMnemonic(KeyEvent.VK_T);
        tornarButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        tornarButton.setBackground(Colors.YELLOW_LIGHT);
        tornarButton.setForeground(Color.BLACK);
        tornarButton.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        tornarButton.setPreferredSize(new Dimension(80, 40));
        tornarButton.setFocusPainted(false);

        tornarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tornarButton.setBackground(Colors.YELLOW_DARK);
                tornarButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tornarButton.setBackground(Colors.YELLOW_LIGHT);
                tornarButton.setForeground(Color.BLACK);
            }
        });

        tornarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.tornar();
            }
        });

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Colors.GREEN_BACKGROUND);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        footerPanel.add(tornarButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
package interficie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import interficie.utils.*;
import controladors.CtrlPresentacio;

/**
 * @class VistaCrearPartida
 *
 *        Mostra una finestra preguntant els paràmetres amb què l'usuari vol
 *        jugar la partida.
 */
public class VistaCrearPartida extends JFrame {
    private JPanel panellCrearAvatar;
    private JButton botoCrearPartida, botoTornar;
    private JPanel formPanel;
    private JCheckBox toggleJugador2;

    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;
    private final int WIDTH_TORNAR = 80, HEIGHT_TORNAR = 40;
    private final int MAX_IMAGE_WIDTH = 200;
    private ImageIcon imatgeLogo;
    private JLabel labelLogo;

    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;

    /**
     * JTextField personalitzat que mostra un text "placeholder" quan el camp està
     * buit i no té el focus, ajudant a indicar què s'ha d'introduir.
     *
     * El placeholder es mostra en color gris i en cursiva, posicionat lleugerament
     * desplaçat respecte a l'esquerra i verticalment centrat.
     */
    class PlaceholderTextField extends JTextField {
        private String placeholder;

        /**
         * Estableix el text del placeholder.
         * 
         * @param placeholder el text que es mostrarà com a placeholder
         */
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        /**
         * Repinta el component i, si cal, dibuixa el placeholder.
         * 
         * @param g l'objecte Graphics per fer el dibuix
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder != null && getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                g2.drawString(placeholder, 5, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }

    /**
     * Crea una nova finestra per a la creació d'una partida de Scrabble.
     * 
     * Aquesta vista permet a l'usuari introduir els noms dels jugadors, triar si
     * vol jugar contra la màquina,
     * seleccionar l'idioma de la partida, i ajustar la mida del tauler.
     * També inclou controls visuals com un logo, una imatge superior i botons
     * estilitzats.
     * 
     * @param menu la finestra principal (JFrame) des de la qual es crea aquesta
     *             vista,
     *             s'utilitza per ajustar la mida i posició de la finestra actual.
     * 
     *             Funcionalitats destacades:
     *             - Escalat de la finestra i dels elements gràfics segons la mida
     *             de la finestra principal.
     *             - Control de visibilitat del segon jugador segons si es juga
     *             contra la màquina o no.
     *             - Selector d'idioma amb botons toggle estilitzats i canvis
     *             visuals al passar el ratolí.
     *             - Selector de mida del tauler amb botons + i - que canvien la
     *             mida entre 5 i 29 en passos de 2.
     *             - Validació dels camps abans de crear la partida amb missatges
     *             d'error en cas d'entrada invàlida.
     *             - Botó "TORNAR" per tornar a la vista anterior.
     *
     *             Recursos utilitzats:
     *             - Icones i imatges carregades des de la carpeta
     *             "./resources/".
     *             - Fonts i colors personalitzats definits a la classe
     *             {@code Colors}.
     */
    public VistaCrearPartida(JFrame menu) {
        setTitle("Scrabble - Crear Partida");
        Dimension screenSize = menu.getSize();
        setSize(screenSize.width, screenSize.height);
        setLocation(menu.getLocation());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaCrearPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        panellCrearAvatar = new JPanel(new BorderLayout());
        panellCrearAvatar.setBackground(Colors.GREEN_BACKGROUND);
        add(panellCrearAvatar);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, LOGO_WIDTH, LOGO_HEIGHT);
            labelLogo.setBounds(10, 10, LOGO_WIDTH, LOGO_HEIGHT);
            panellCrearAvatar.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaCrearPartida.this,
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

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Colors.GREEN_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String rutaFoto = "./resources/foto.png";
        try {
            ImageIcon foto = new ImageIcon(rutaFoto);
            Image img = foto.getImage();
            int originalWidth = foto.getIconWidth();
            int originalHeight = foto.getIconHeight();
            int newWidth = Math.min(originalWidth, MAX_IMAGE_WIDTH);
            int newHeight = (int) ((double) newWidth / originalWidth * originalHeight);
            Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            JLabel labelFoto = new JLabel(new ImageIcon(scaledImg));
            labelFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(labelFoto);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space below image
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaCrearPartida.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Colors.GREEN_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Colors.GREEN_BACKGROUND);
        leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panellJ1 = new JPanel();
        panellJ1.setLayout(new BoxLayout(panellJ1, BoxLayout.Y_AXIS));
        panellJ1.setBackground(Colors.GREEN_BACKGROUND);
        panellJ1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textUsuari = new JLabel("Nom Jugador 1:");
        textUsuari.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        textUsuari.setAlignmentX(Component.CENTER_ALIGNMENT);
        panellJ1.add(textUsuari);

        PlaceholderTextField campUsuari = new PlaceholderTextField();
        campUsuari.setPlaceholder("Nom Jugador 1");
        campUsuari.setMaximumSize(new Dimension(300, 30));
        campUsuari.setBackground(Color.WHITE);
        campUsuari.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        panellJ1.add(campUsuari);
        panellJ1.add(Box.createRigidArea(new Dimension(0, 10)));

        toggleJugador2 = new JCheckBox("Jugar contra Màquina");
        toggleJugador2.setSelected(false);
        toggleJugador2.setBackground(Colors.GREEN_BACKGROUND);
        toggleJugador2.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        toggleJugador2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panellJ1.add(toggleJugador2);

        JPanel panellJ2 = new JPanel();
        panellJ2.setLayout(new BoxLayout(panellJ2, BoxLayout.Y_AXIS));
        panellJ2.setBackground(Colors.GREEN_BACKGROUND);
        panellJ2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelNomJ2 = new JLabel("Nom Jugador 2:");
        labelNomJ2.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        labelNomJ2.setAlignmentX(Component.CENTER_ALIGNMENT);
        PlaceholderTextField campNomJ2 = new PlaceholderTextField();
        campNomJ2.setPlaceholder("Nom Jugador 2");
        campNomJ2.setMaximumSize(new Dimension(300, 30));
        campNomJ2.setBackground(Color.WHITE);
        campNomJ2.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        panellJ2.add(labelNomJ2);
        panellJ2.add(campNomJ2);

        toggleJugador2.addActionListener(e -> panellJ2.setVisible(!toggleJugador2.isSelected()));

        leftPanel.add(panellJ1);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(panellJ2);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Colors.GREEN_BACKGROUND);
        rightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(5, 5, 5, 5);
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.fill = GridBagConstraints.NONE;

        JPanel panellIdiomes = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panellIdiomes.setBackground(Colors.GREEN_BACKGROUND);

        JLabel labelIdioma = new JLabel("Selecciona Idioma:");
        labelIdioma.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        JToggleButton botoCatala = new JToggleButton("Català");
        JToggleButton botoCastella = new JToggleButton("Castellano");
        JToggleButton botoAngles = new JToggleButton("English");

        ButtonGroup grupIdiomes = new ButtonGroup();
        grupIdiomes.add(botoCatala);
        grupIdiomes.add(botoCastella);
        grupIdiomes.add(botoAngles);
        botoCatala.setSelected(true);

        for (JToggleButton b : new JToggleButton[] { botoCatala, botoCastella, botoAngles }) {
            b.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
            b.setPreferredSize(new Dimension(120, 40));
            panellIdiomes.add(b);
        }

        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        rightPanel.add(labelIdioma, gbcRight);

        gbcRight.gridx = 1;
        rightPanel.add(panellIdiomes, gbcRight);

        JPanel panellTamany = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panellTamany.setBackground(Colors.GREEN_BACKGROUND);

        JLabel labelTamany = new JLabel("Mida del tauler:");
        labelTamany.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        JButton botoMenys = new JButton("-");
        JButton botoMes = new JButton("+");
        JLabel labelTamanyValor = new JLabel("15");
        labelTamanyValor.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

        botoMenys.setPreferredSize(new Dimension(50, 30));
        botoMes.setPreferredSize(new Dimension(50, 30));

        botoMenys.setBackground(Colors.RED);
        botoMenys.setForeground(Color.WHITE);
        botoMenys.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        botoMenys.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));

        botoMes.setBackground(Colors.GREEN_LIGHT);
        botoMes.setForeground(Color.WHITE);
        botoMes.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        botoMes.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));

        panellTamany.add(botoMenys);
        panellTamany.add(labelTamanyValor);
        panellTamany.add(botoMes);

        final int[] midaTauler = { 15 };
        botoMenys.addActionListener(e -> {
            if (midaTauler[0] > 5) {
                midaTauler[0] -= 2;
                labelTamanyValor.setText(String.valueOf(midaTauler[0]));
            }
        });
        botoMes.addActionListener(e -> {
            if (midaTauler[0] < 29) {
                midaTauler[0] += 2;
                labelTamanyValor.setText(String.valueOf(midaTauler[0]));
            }
        });

        gbcRight.gridx = 0;
        gbcRight.gridy = 1;
        rightPanel.add(labelTamany, gbcRight);

        gbcRight.gridx = 1;
        rightPanel.add(panellTamany, gbcRight);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        formPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        formPanel.add(rightPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Colors.GREEN_BACKGROUND);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        botoCrearPartida = new JButton("Crear Partida");
        botoCrearPartida.setBackground(Colors.GREEN_LIGHT);
        botoCrearPartida.setForeground(Colors.GREEN_TEXT);
        botoCrearPartida.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        botoCrearPartida.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        botoCrearPartida.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(botoCrearPartida);

        botoCrearPartida.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoCrearPartida.setBackground(Colors.GREEN_TEXT);
                botoCrearPartida.setForeground(Colors.GREEN_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoCrearPartida.setBackground(Colors.GREEN_LIGHT);
                botoCrearPartida.setForeground(Colors.GREEN_TEXT);
            }
        });

        getRootPane().setDefaultButton(botoCrearPartida);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(formPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalGlue());

        panellCrearAvatar.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Colors.GREEN_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));

        botoTornar = new JButton("TORNAR");
        botoTornar.setMnemonic(KeyEvent.VK_T);
        botoTornar.setBackground(Colors.YELLOW_LIGHT);
        botoTornar.setForeground(Color.BLACK);
        botoTornar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        botoTornar.setPreferredSize(new Dimension(WIDTH_TORNAR, HEIGHT_TORNAR));
        bottomPanel.add(botoTornar);

        panellCrearAvatar.add(bottomPanel, BorderLayout.SOUTH);

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

        botoCrearPartida.addActionListener(e -> {
            String nomJ1 = campUsuari.getText().trim();
            String nomJ2 = campNomJ2.getText().trim();
            String idioma;
            if (botoCatala.isSelected())
                idioma = "1";
            else if (botoCastella.isSelected())
                idioma = "2";
            else
                idioma = "3";

            int mida = midaTauler[0];

            if (nomJ1.isEmpty()) {
                JOptionPane.showOptionDialog(
                        VistaCrearPartida.this,
                        "No t'oblidis de posar el nom del Jugador 1.",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");

                return;
            }

            if (!toggleJugador2.isSelected()) {
                if (nomJ2.isEmpty()) {
                    JOptionPane.showOptionDialog(
                        VistaCrearPartida.this,
                        "No t'oblidis de posar el nom del Jugador 2 o de marcar la casella per jugar contra la maquina.",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");
                    return;
                }
                if (nomJ1.equals(nomJ2)) {
                    JOptionPane.showOptionDialog(
                        VistaCrearPartida.this,
                        "No pots jugar contra tu mateix!",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");
                    return;
                }
            } else {
                nomJ2 = "MAQUINA";
            }

            boolean creada = CtrlPresentacio.crearPartida(nomJ1, nomJ2, idioma, mida);
            if (!creada) {
                JOptionPane.showOptionDialog(
                        VistaCrearPartida.this,
                        "Algun dels avatars introduïts no existeix.",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "D'acord" },
                        "D'acord");
            }
        });

        for (JToggleButton b : new JToggleButton[] { botoCatala, botoCastella, botoAngles }) {
            b.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
            b.setPreferredSize(new Dimension(120, 40));
            b.setBackground(Colors.GREEN_LIGHT);
            b.setForeground(Colors.GREEN_TEXT);
            b.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));

            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!b.isSelected()) {
                        b.setBackground(Colors.GREEN_TEXT);
                        b.setForeground(Colors.GREEN_LIGHT);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!b.isSelected()) {
                        b.setBackground(Colors.GREEN_LIGHT);
                        b.setForeground(Colors.GREEN_TEXT);
                    }
                }
            });

            b.addActionListener(e -> {
                for (JToggleButton other : new JToggleButton[] { botoCatala, botoCastella, botoAngles }) {
                    if (other == b) {
                        other.setBackground(Colors.GREEN_TEXT);
                        other.setForeground(Colors.GREEN_LIGHT);
                    } else {
                        other.setBackground(Colors.GREEN_LIGHT);
                        other.setForeground(Colors.GREEN_TEXT);
                    }
                }
            });
        }

        setVisible(true);
    }
}
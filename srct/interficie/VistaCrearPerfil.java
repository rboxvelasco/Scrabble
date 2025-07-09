package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.event.*;
import interficie.utils.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @class VistaCanviTorn
 *        Vista per crear un nou perfil d'usuari a l'aplicació Scrabble.
 *
 *        Aquesta finestra permet a l'usuari introduir un nom d'usuari i una
 *        contrasenya
 *        per crear un perfil nou. Mostra imatges, placeholders en els camps de
 *        text,
 *        i gestiona la interacció amb el controlador de presentació.
 *
 *        Inclou:
 *        - Imatge de logo i animal.
 *        - Formulari amb placeholders.
 *        - Botons per crear perfil i tornar a la pantalla anterior.
 *        - Validació bàsica d'entrada.
 *        - Adaptació dinàmica al redimensionament de la finestra.
 */
public class VistaCrearPerfil extends JFrame {
    private JPanel panellCrearPerfil;
    private JLabel labelLogo, labelAnimal;
    private PlaceholderTextField campUsuari;
    private JPasswordField campContrasenya;
    private JButton botoCrear, botoTornar;

    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;
    private final int WIDTH_TORNAR = 80, HEIGHT_TORNAR = 40;
    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private final int ANIMAL_WIDTH = 150, ANIMAL_HEIGHT = 150;

    private ImageIcon imatgeLogo, imatgeAnimal;
    private JPanel formPanel;

    /**
     * Camp de text personalitzat que mostra un placeholder quan està buit i no té
     * focus.
     */
    class PlaceholderTextField extends JTextField {
        private String placeholder;

        /**
         * Estableix el text placeholder.
         * 
         * @param placeholder Text que es mostrarà quan el camp estigui buit i sense
         *                    focus.
         */
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        /**
         * Repinta el component i dibuixa el placeholder si cal.
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
     * Constructor que crea la finestra per crear un perfil.
     *
     * @param menu Vista del menú principal, per posicionar i dimensionar aquesta
     *             finestra de forma relativa.
     */
    public VistaCrearPerfil(VistaMenuPrincipal menu) {
        setTitle("Scrabble - Crear Perfil");
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
                    VistaCrearPerfil.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Panell principal
        panellCrearPerfil = new JPanel(null);
        panellCrearPerfil.setBackground(Colors.GREEN_BACKGROUND);
        add(panellCrearPerfil);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, LOGO_WIDTH, LOGO_HEIGHT);
            labelLogo.setBounds(10, 10, LOGO_WIDTH, LOGO_HEIGHT);
            panellCrearPerfil.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaCrearPerfil.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Imatge animal
        String rutaAnimal = "./resources/animal.png";
        File archivoAnimal = new File(rutaAnimal);
        if (archivoAnimal.exists()) {
            imatgeAnimal = new ImageIcon(rutaAnimal);
            labelAnimal = new JLabel();
            Tools.actualitzarImatge(imatgeAnimal, labelAnimal, ANIMAL_WIDTH, ANIMAL_HEIGHT);
            int animalX = (WIDTH_BASE - ANIMAL_WIDTH) / 2;
            labelAnimal.setBounds(animalX, 70, ANIMAL_WIDTH, ANIMAL_HEIGHT);
            panellCrearPerfil.add(labelAnimal);
        } else {
            JOptionPane.showOptionDialog(
                    VistaCrearPerfil.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Formulari
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Colors.GREEN_BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Títol
        JLabel titol = new JLabel("Crear Perfil");
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titol.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titol);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel filaUsuari = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filaUsuari.setBackground(Colors.GREEN_BACKGROUND);
        JLabel textUsuari = new JLabel("Introdueix el nom d'usuari desitjat:");
        textUsuari.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        campUsuari = new PlaceholderTextField();
        campUsuari.setPlaceholder("Nom d'usuari");
        campUsuari.setPreferredSize(new Dimension(250, 30));
        campUsuari.setBackground(Color.WHITE);
        campUsuari.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        filaUsuari.add(textUsuari);
        filaUsuari.add(campUsuari);
        formPanel.add(filaUsuari);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel filaContrasenya = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filaContrasenya.setBackground(Colors.GREEN_BACKGROUND);
        JLabel textContrasenya = new JLabel("Introdueix la contrasenya desitjada:");
        textContrasenya.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        campContrasenya = new JPasswordField("*************");
        campContrasenya.setPreferredSize(new Dimension(250, 30));
        campContrasenya.setBackground(Color.WHITE);
        campContrasenya.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        campContrasenya.setForeground(Color.GRAY);
        campContrasenya.setEchoChar((char) 0);

        campContrasenya.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(campContrasenya.getPassword()).equals("*************")) {
                    campContrasenya.setText("");
                    campContrasenya.setEchoChar('●');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campContrasenya.getPassword().length == 0) {
                    campContrasenya.setText("*************");
                    campContrasenya.setEchoChar((char) 0);
                }
            }
        });

        filaContrasenya.add(textContrasenya);
        filaContrasenya.add(campContrasenya);
        formPanel.add(filaContrasenya);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botó crear perfil
        botoCrear = new JButton("Crear Perfil");
        botoCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        botoCrear.setBackground(Colors.GREEN_LIGHT);
        botoCrear.setForeground(Colors.GREEN_TEXT);
        botoCrear.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        botoCrear.setPreferredSize(new Dimension(200, 40));
        botoCrear.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        botoCrear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoCrear.setBackground(Colors.GREEN_TEXT);
                botoCrear.setForeground(Colors.GREEN_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoCrear.setBackground(Colors.GREEN_LIGHT);
                botoCrear.setForeground(Colors.GREEN_TEXT);
            }
        });

        formPanel.add(botoCrear);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel);

        centerPanel.setBounds(0, 150, WIDTH_BASE, 400);
        panellCrearPerfil.add(centerPanel);

        // Botó tornar
        botoTornar = new JButton("TORNAR");
        botoTornar.setMnemonic(KeyEvent.VK_T);
        botoTornar.setBackground(Colors.YELLOW_LIGHT);
        botoTornar.setForeground(Color.BLACK);
        botoTornar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        botoTornar.setBounds(WIDTH_BASE - WIDTH_TORNAR - 20, HEIGHT_BASE - 80, WIDTH_TORNAR, HEIGHT_TORNAR);

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

        botoTornar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.tornar();
            }
        });

        panellCrearPerfil.add(botoTornar);
        getRootPane().setDefaultButton(botoCrear);

        botoCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = campUsuari.getText().trim();
                String contrasenya = new String(campContrasenya.getPassword()).trim();

                if (nom.isEmpty()) {
                    JOptionPane.showOptionDialog(
                            VistaCrearPerfil.this,
                            "Sembla que has oblidat posar un nom d'usuari.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");

                    return;
                } else if (!nom.matches("^[\\p{L}0-9._]+$")) {
                    JOptionPane.showOptionDialog(
                            VistaCrearPerfil.this,
                            "El nom conté caràcters no vàlids. Els vàlids són: A-Z (amb o sense accent) 0-9 . _",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    return;
                } else if (contrasenya.equals("*************") || contrasenya.isEmpty()) {
                    JOptionPane.showOptionDialog(
                            VistaCrearPerfil.this,
                            "Sembla que has oblidat introduir una contrasenya.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    return;
                }

                boolean creat = CtrlPresentacio.crearPerfil(nom, contrasenya);

                if (!creat) {
                    JOptionPane.showOptionDialog(
                            VistaCrearPerfil.this,
                            "Sembla que ja existeix algú amb aquest nom d'usuari.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    return;
                }

                Object[] opcions = { "Si", "No" };

                int respuesta = JOptionPane.showOptionDialog(
                        VistaCrearPerfil.this,
                        "S'ha creat correctament el teu perfil, vols iniciar sessió amb ell?",
                        "Srabble - Confirmació d'inici de sessió.",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcions,
                        opcions[0]);

                if (respuesta == 1) {
                    CtrlPresentacio.tornar();
                } else {
                    try {
                        CtrlPresentacio.iniciarSessio(nom, contrasenya);
                    } catch (IOException exc) {
                        JOptionPane.showOptionDialog(
                                VistaCrearPerfil.this,
                                "Error carregant els usuaris: " + exc.getMessage(),
                                "Error",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                new Object[] { "D'acord" },
                                "D'acord");
                    }
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                escalarComponentes();
            }
        });

        setVisible(true);
    }

    /**
     * Escala i reubica els components segons la mida actual de la finestra,
     * mantenint proporcions relatives.
     */
    private void escalarComponentes() {
        int w = getWidth();
        int h = getHeight();
        double escalaX = w / (double) WIDTH_BASE;
        double escalaY = h / (double) HEIGHT_BASE;

        int logoW = (int) (LOGO_WIDTH * escalaX);
        int logoH = (int) (LOGO_HEIGHT * escalaY);
        Tools.actualitzarImatge(imatgeLogo, labelLogo, logoW, logoH);
        labelLogo.setBounds((int) (10 * escalaX), (int) (10 * escalaY), logoW, logoH);

        int animalW = (int) (ANIMAL_WIDTH * escalaX);
        int animalH = (int) (ANIMAL_HEIGHT * escalaY);
        Tools.actualitzarImatge(imatgeAnimal, labelAnimal, animalW, animalH);
        int animalX = (w - animalW) / 2;
        labelAnimal.setBounds(animalX, (int) (70 * escalaY), animalW, animalH);

        JPanel centerPanel = (JPanel) panellCrearPerfil.getComponent(2);
        centerPanel.setBounds(0, (int) (150 * escalaY), w, (int) (400 * escalaY));

        botoTornar.setBounds(
                (int) (875 * escalaX), (int) (500 * escalaY),
                (int) (WIDTH_TORNAR * escalaX),
                (int) (HEIGHT_TORNAR * escalaY));
    }
}
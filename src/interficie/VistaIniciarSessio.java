package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.event.*;
import interficie.utils.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @class VistaIniciarSessio
 *        Classe que representa la finestra principal del menú de l'aplicació
 *        Scrabble un cop s'ha iniciat sessió.
 */
public class VistaIniciarSessio extends JFrame {
    private JPanel panellIniciarSessio;
    private JPanel formWrapper;
    private JLabel labelLogo, labelAnimal;
    private JPasswordField campContrasenya;
    private JButton botoIniciar, botoTornar;

    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;
    private final int WIDTH_TORNAR = 80, HEIGHT_TORNAR = 40;
    private final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private final int ANIMAL_WIDTH = 150, ANIMAL_HEIGHT = 150;

    private ImageIcon imatgeLogo, imatgeAnimal;
    private JPanel formPanel;

    /**
     * Classe interna que permet mostrar un placeholder (text predefinit i en gris)
     * dins d'un JTextField quan aquest està buit i sense focus.
     */
    class PlaceholderTextField extends JTextField {
        private String placeholder;

        /**
         * Estableix el text placeholder que es mostrarà quan el camp estigui buit.
         * 
         * @param placeholder Text del placeholder
         */
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        /**
         * Pinta el component JTextField, incloent el placeholder quan és necessari.
         * 
         * @param g Context gràfic per pintar el component.
         * 
         *          Aquest mètode sobreescrit dibuixa el placeholder en color gris i en
         *          cursiva quan el JTextField
         *          està buit i no té el focus.
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
     * Constructor que crea la vista d'iniciar sessió.
     * 
     * Mostra un formulari amb camps per introduir nom d'usuari i contrasenya, i
     * permet iniciar sessió o anar a la pantalla de crear compte.
     * 
     * El disseny és escalable, amb imatges i components que s'ajusten a la mida de
     * la finestra.
     * 
     * @param menu Vista del menú principal per posicionar la finestra i adaptar la
     *             mida
     */
    public VistaIniciarSessio(VistaMenuPrincipal menu) {
        setTitle("Scrabble - Iniciar Sessió");
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
                    VistaIniciarSessio.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Panell principal
        panellIniciarSessio = new JPanel(null);
        panellIniciarSessio.setBackground(Colors.GREEN_BACKGROUND);
        add(panellIniciarSessio);

        // Logo petit
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, LOGO_WIDTH, LOGO_HEIGHT);
            labelLogo.setBounds(10, 10, LOGO_WIDTH, LOGO_HEIGHT);
            panellIniciarSessio.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaIniciarSessio.this,
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
            panellIniciarSessio.add(labelAnimal);
        } else {
            JOptionPane.showOptionDialog(
                    VistaIniciarSessio.this,
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
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Menys padding superior

        // Títol
        JLabel titol = new JLabel("Iniciar Sessió");
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titol.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titol);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Menys espai després del títol

        // Fila usuari
        JPanel filaUsuari = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filaUsuari.setBackground(Colors.GREEN_BACKGROUND);
        JLabel textUsuari = new JLabel("Introdueix el nom d'usuari desitjat:");
        textUsuari.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        PlaceholderTextField campUsuari = new PlaceholderTextField();
        campUsuari.setPlaceholder("Nom d'usuari");
        campUsuari.setPreferredSize(new Dimension(250, 30));
        campUsuari.setBackground(Color.WHITE);
        campUsuari.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        filaUsuari.add(textUsuari);
        filaUsuari.add(campUsuari);
        formPanel.add(filaUsuari);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Menys espai entre camps

        // Fila contrasenya
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
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botó iniciar sessió
        botoIniciar = new JButton("Iniciar Sessió");
        botoIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botoIniciar.setBackground(Colors.GREEN_LIGHT);
        botoIniciar.setForeground(Colors.GREEN_TEXT);
        botoIniciar.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        botoIniciar.setPreferredSize(new Dimension(200, 40));
        botoIniciar.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        // Afegim l'enllaç
        JPanel panelRegistre = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelRegistre.setBackground(Colors.GREEN_BACKGROUND);

        JLabel labelPregunta = new JLabel("No tens un compte?");
        labelPregunta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        JButton botoRegistre = new JButton("Crea un compte");
        botoRegistre.setBorderPainted(false);
        botoRegistre.setContentAreaFilled(false);
        botoRegistre.setForeground(Colors.GREEN_BORDER);
        botoRegistre.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        botoRegistre.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botoRegistre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CtrlPresentacio.canviarCrearPerfilDesdeIniciarSessio();
            }
        });

        panelRegistre.add(labelPregunta);
        panelRegistre.add(botoRegistre);

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(panelRegistre);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(botoIniciar);

        botoIniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoIniciar.setBackground(Colors.GREEN_TEXT);
                botoIniciar.setForeground(Colors.GREEN_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoIniciar.setBackground(Colors.GREEN_LIGHT);
                botoIniciar.setForeground(Colors.GREEN_TEXT);
            }
        });

        formPanel.add(botoIniciar);

        formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formPanel);
        int formY = 200;
        formWrapper.setBounds(0, formY, WIDTH_BASE, 250);
        panellIniciarSessio.add(formWrapper);

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

        panellIniciarSessio.add(botoTornar);
        getRootPane().setDefaultButton(botoIniciar);

        botoIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = campUsuari.getText().trim();
                String contrasenya = new String(campContrasenya.getPassword()).trim();

                if (nom.isEmpty() || contrasenya.isEmpty() || contrasenya.equals("*************")) {
                    JOptionPane.showOptionDialog(
                            VistaIniciarSessio.this,
                            "Els camps d'usuari i contrasenya no poden estar buits",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    return;
                }

                try {
                    int codi = CtrlPresentacio.iniciarSessio(nom, contrasenya);

                    if (codi == 1) {
                        JOptionPane.showOptionDialog(
                                VistaIniciarSessio.this,
                                "L'usuari \"" + nom + "\" no existeix",
                                "Error",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                new Object[] { "D'acord" },
                                "D'acord");
                    } else if (codi == 2) {
                        JOptionPane.showOptionDialog(
                                VistaIniciarSessio.this,
                                "Contrasenya incorrecta!",
                                "Error",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                new Object[] { "D'acord" },
                                "D'acord");
                    }

                } catch (IOException exc) {
                    JOptionPane.showOptionDialog(
                            VistaIniciarSessio.this,
                            "Error de connexió amb el servidor.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
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
     * Escala i recol·loca els components en funció de la mida actual de la
     * finestra.
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

        int formY = (int) (200 * escalaY);
        formWrapper.setBounds(0, formY, w, (int) (250 * escalaY));

        botoTornar.setBounds((int) (875 * escalaX), (int) (500 * escalaY), (int) (WIDTH_TORNAR * escalaX),
                (int) (HEIGHT_TORNAR * escalaY));
    }
}
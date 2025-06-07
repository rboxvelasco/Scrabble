package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import interficie.utils.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import java.awt.event.*;

/**
 * @class VistaCrearAvatars
 *        Mostra una finestra que demana les dades necessaries per a crear un
 *        Avatar.
 */
public class VistaCrearAvatar extends JFrame {
    private JPanel panellCrearAvatar;
    private JLabel labelLogo;
    private PlaceholderTextField campUsuari;
    private JButton botoCrear, botoTornar, botoEscollirImatge, botoEliminarImatge;
    private JLabel labelImagenSeleccionada;
    private JPanel formPanel;

    private String rutaImatge = "./resources/usuari.png";
    private final String[] IMATGES_PREDEFINIDES = {
            "./resources/avatar 1.png",
            "./resources/avatar 2.png",
            "./resources/avatar 3.png",
            "./resources/avatar 4.png",
            "./resources/avatar 5.png",
            "./resources/avatar 6.png"
    };

    private final int WIDTH_BASE = 1000, HEIGHT_BASE = 600;
    private final int WIDTH_TORNAR = 80, HEIGHT_TORNAR = 40;
    private final int WIDTH_BOTO = 200, HEIGHT_BOTO = 40;
    private final int WIDTH_LOGO = 50, HEIGHT_LOGO = 50;
    private final int IMG_WIDTH = 150, IMG_HEIGTH = 150;

    private ImageIcon imatgeUsuari, imatgeLogo;

    /**
     * @class PlaceholderTextField
     *        JTextField amb suport per a un text placeholder.
     * 
     *        Aquesta classe extèn JTextField i mostra un text de placeholder quan
     *        el camp està buit
     *        i no té el focus, facilitant la indicació a l'usuari del contingut
     *        esperat.
     */
    class PlaceholderTextField extends JTextField {
        private String placeholder;

        /**
         * Estableix el text placeholder.
         * 
         * @param placeholder Text que es mostrarà quan el camp estigui buit.
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
     * Constructor de la finestra VistaCrearAvatar.
     * 
     * Crea una interfície gràfica per permetre a l'usuari crear un avatar
     * personalitzat,
     * seleccionant una imatge o eliminant-la, i introduint un nom per l'avatar.
     * 
     * @param menu La finestra principal (JFrame) des de la qual s'inicia aquesta
     *             vista.
     *             S'utilitza per obtenir la mida i la posició inicials de la
     *             finestra.
     * 
     *             La finestra inclou:
     *
     *             - Icona d'aplicació i títol específic.
     *             - Panell principal amb un fons verd.
     *             - Logo petit a la cantonada superior esquerra.
     *             - Imatge per defecte o seleccionada de l'avatar, amb botons per
     *             canviar o eliminar la imatge.
     *             - Formulari per introduir el nom del nou avatar amb validacions.
     *             - Botó per crear l'avatar, que invoca el controlador corresponent
     *             i mostra missatges d'èxit o error.
     *             - Botó per tornar a la pantalla anterior.
     *             - Efectes visuals per als botons quan el ratolí entra o surt.
     *             - Redimensionament automàtic dels components quan canvia la mida
     *             de la finestra.
     *
     * @throws Exception Si hi ha problemes carregant les imatges o els recursos
     *                   gràfics,
     *                   es captura i mostra un missatge d'error per consola.
     */
    public VistaCrearAvatar(JFrame menu) {
        // Icone d'aplicació
        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaCrearAvatar.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }
        setTitle("Scrabble - Crear Avatar");
        Dimension screenSize = menu.getSize();
        setSize(screenSize.width, screenSize.height);
        setLocation(menu.getLocation());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panellCrearAvatar = new JPanel(null);
        panellCrearAvatar.setBackground(Colors.GREEN_BACKGROUND);
        add(panellCrearAvatar);

        // Logo petit a la cantonada superior esquerra
        String rutaLogo = "./resources/icon.png";
        File archivoLogo = new File(rutaLogo);
        if (archivoLogo.exists()) {
            imatgeLogo = new ImageIcon(rutaLogo);
            labelLogo = new JLabel();
            Tools.actualitzarImatge(imatgeLogo, labelLogo, WIDTH_LOGO, HEIGHT_LOGO);
            labelLogo.setBounds(10, 10, WIDTH_LOGO, HEIGHT_LOGO);
            panellCrearAvatar.add(labelLogo);
        } else {
            JOptionPane.showOptionDialog(
                    VistaCrearAvatar.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        imatgeUsuari = new ImageIcon(rutaImatge);
        labelImagenSeleccionada = new JLabel();
        Tools.actualitzarImatge(imatgeUsuari, labelImagenSeleccionada, IMG_WIDTH, IMG_HEIGTH);
        labelImagenSeleccionada.setHorizontalAlignment(JLabel.CENTER);
        labelImagenSeleccionada.setBounds(200, 150, IMG_WIDTH, IMG_HEIGTH);
        panellCrearAvatar.add(labelImagenSeleccionada);

        // Botó per seleccionar imatge
        botoEscollirImatge = new JButton("Seleccionar imatge");
        botoEscollirImatge.setBackground(Colors.GREEN_LIGHT);
        botoEscollirImatge.setForeground(Colors.GREEN_TEXT);
        botoEscollirImatge.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        botoEscollirImatge.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        botoEscollirImatge.setBounds(labelImagenSeleccionada.getX() + (IMG_WIDTH - WIDTH_BOTO) / 2,
                labelImagenSeleccionada.getY() + IMG_HEIGTH + 10, WIDTH_BOTO, HEIGHT_BOTO);

        botoEscollirImatge.addActionListener(e -> mostrarSelectorImagenes());

        botoEscollirImatge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoEscollirImatge.setBackground(Colors.GREEN_TEXT);
                botoEscollirImatge.setForeground(Colors.GREEN_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoEscollirImatge.setBackground(Colors.GREEN_LIGHT);
                botoEscollirImatge.setForeground(Colors.GREEN_TEXT);
            }
        });
        panellCrearAvatar.add(botoEscollirImatge);

        botoEliminarImatge = new JButton("Eliminar imatge");
        botoEliminarImatge.setBackground(Colors.RED);
        botoEliminarImatge.setForeground(Color.BLACK);
        botoEliminarImatge.setBorder(BorderFactory.createLineBorder(Colors.RED_BORDER, 4));
        botoEliminarImatge.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        botoEliminarImatge.setBounds(labelImagenSeleccionada.getX() + (IMG_WIDTH - WIDTH_BOTO) / 2,
                botoEscollirImatge.getY() + HEIGHT_BOTO + 10, WIDTH_BOTO, HEIGHT_BOTO);

        botoEliminarImatge.addActionListener(e -> {
            rutaImatge = "./resources/usuari.png";
            imatgeUsuari = new ImageIcon(rutaImatge);
            Tools.actualitzarImatge(imatgeUsuari, labelImagenSeleccionada, IMG_WIDTH, IMG_HEIGTH);
        });

        botoEliminarImatge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoEliminarImatge.setBackground(Colors.RED_BORDER);
                botoEliminarImatge.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoEliminarImatge.setBackground(Colors.RED);
                botoEliminarImatge.setForeground(Color.BLACK);
            }
        });
        panellCrearAvatar.add(botoEliminarImatge);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Colors.GREEN_BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Títol centrat
        JLabel titol = new JLabel("Crear Avatar");
        titol.setForeground(Colors.GREEN_TEXT);
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titol.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titol);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel filaUsuari = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        filaUsuari.setBackground(Colors.GREEN_BACKGROUND);
        JLabel labelUsuari = new JLabel("Introdueix el nom del avatar:");
        labelUsuari.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        campUsuari = new PlaceholderTextField();
        campUsuari.setPlaceholder("Nom del avatar");
        campUsuari.setPreferredSize(new Dimension(250, 30));
        campUsuari.setBackground(Color.WHITE);
        campUsuari.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        filaUsuari.add(labelUsuari);
        filaUsuari.add(campUsuari);
        formPanel.add(filaUsuari);

        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botó Crear Avatar
        botoCrear = new JButton("Crear Avatar");
        botoCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        botoCrear.setBackground(Colors.GREEN_LIGHT);
        botoCrear.setForeground(Colors.GREEN_TEXT);
        botoCrear.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        botoCrear.setPreferredSize(new Dimension(WIDTH_BOTO, HEIGHT_BOTO));
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

        // Posicionar el formulari
        int formX = (int) (WIDTH_BASE * 0.55);
        int formY = 160;
        formPanel.setBounds(formX, formY, 300, 250);
        panellCrearAvatar.add(formPanel);

        // Botó Tornar
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

        panellCrearAvatar.add(botoTornar);
        getRootPane().setDefaultButton(botoCrear);

        botoCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = campUsuari.getText().trim();

                if (nom.matches("")) {
                    JOptionPane.showOptionDialog(
                            VistaCrearAvatar.this,
                            "Sembla que has oblidat posar un nom a l'avatar.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");

                    return;
                } else if (!nom.matches("^[\\p{L}0-9._]+$")) {
                    JOptionPane.showOptionDialog(
                            VistaCrearAvatar.this,
                            "El nom conté caràcters no vàlids. Els vàlids són: A-Z (amb o sense accent) 0-9 . _",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    return;
                } else if (nom.matches("MAQUINA") || nom.matches("Maquina") || nom.matches("maquina")) {
                    JOptionPane.showOptionDialog(
                            VistaCrearAvatar.this,
                            "No pots crear un avatar amb aquest nom.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    return;
                }

                if (rutaImatge.equals("./resources/usuari.png")) {
                    Random rand = new Random();
                    int idx = rand.nextInt(IMATGES_PREDEFINIDES.length);
                    rutaImatge = IMATGES_PREDEFINIDES[idx];
                    imatgeUsuari = new ImageIcon(rutaImatge);
                    Tools.actualitzarImatge(imatgeUsuari, labelImagenSeleccionada, IMG_WIDTH, IMG_HEIGTH);
                }

                boolean creat = false;
                try {
                    creat = CtrlPresentacio.crearAvatar(nom, rutaImatge);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (creat) {
                    JOptionPane.showOptionDialog(
                            VistaCrearAvatar.this,
                            "Avatar creat correctament!",
                            "Scrabble - Crear Avatar",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                } else {
                    JOptionPane.showOptionDialog(
                            VistaCrearAvatar.this,
                            "Sembla que ja tens un avatar amb aquest nom.",
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
            @Override
            public void componentResized(ComponentEvent e) {
                escalarComponentes();
            }
        });

        setVisible(true);
    }

    /**
     * Escala i posiciona els components segons la mida actual.
     */
    private void escalarComponentes() {
        int w = getWidth();
        int h = getHeight();
        double escalaX = w / (double) WIDTH_BASE;
        double escalaY = h / (double) HEIGHT_BASE;

        // Escalar logo
        int logoW = (int) (WIDTH_LOGO * escalaX);
        int logoH = (int) (HEIGHT_LOGO * escalaY);
        Tools.actualitzarImatge(imatgeLogo, labelLogo, logoW, logoH);
        labelLogo.setBounds((int) (10 * escalaX), (int) (10 * escalaY), logoW, logoH);

        // Escalar y posicionar imagen de usuario (izquierda, más arriba)
        int imgW = (int) (IMG_WIDTH * escalaX);
        int imgH = (int) (IMG_HEIGTH * escalaY);
        Tools.actualitzarImatge(imatgeUsuari, labelImagenSeleccionada, imgW, imgH);
        // Mantenemos la posición relativa más arriba
        labelImagenSeleccionada.setBounds(
                (int) (200 * escalaX),
                (int) (150 * escalaY),
                imgW,
                imgH);

        // Escalar y posicionar botó seleccionar imatge (también más arriba)
        botoEscollirImatge.setBounds(
                labelImagenSeleccionada.getX() + (imgW - (int) (WIDTH_BOTO * escalaX)) / 2,
                labelImagenSeleccionada.getY() + imgH + (int) (10 * escalaY),
                (int) (WIDTH_BOTO * escalaX),
                (int) (HEIGHT_BOTO * escalaY));

        // Escalar formulario (derecha, más arriba)
        int formW = (int) (300 * escalaX);
        int formH = (int) (250 * escalaY);
        formPanel.setBounds(
                (int) (w * 0.55),
                (int) (160 * escalaY),
                formW,
                formH);

        // Escalar botó tornar
        botoTornar.setBounds((int) (875 * escalaX), (int) (500 * escalaY), (int) (WIDTH_TORNAR * escalaX),
                (int) (HEIGHT_TORNAR * escalaY));

        botoEliminarImatge.setBounds(
                labelImagenSeleccionada.getX() + (imgW - (int) (WIDTH_BOTO * escalaX)) / 2,
                botoEscollirImatge.getY() + (int) (HEIGHT_BOTO * escalaY) + (int) (10 * escalaY),
                (int) (WIDTH_BOTO * escalaX),
                (int) (HEIGHT_BOTO * escalaY));
    }

    /**
     * Mostra un diàleg modal per seleccionar la imatge de perfil de l'usuari.
     * 
     * Aquest mètode crea un JDialog amb una interfície gràfica que permet a
     * l'usuari:
     * - Seleccionar una imatge predefinida d'un conjunt d'imatges.
     * - Pujar una imatge des del seu PC mitjançant un JFileChooser.
     * - Veure una vista prèvia de la imatge seleccionada.
     * - Acceptar o cancel·lar la selecció.
     * 
     * La finestra està estructurada en tres zones principals:
     * - Panell superior amb el botó per pujar imatges i la vista prèvia.
     * - Panell central amb una graella d'imatges predefinides per triar.
     * - Panell inferior amb els botons "Acceptar" i "Tornar".
     * 
     * Al seleccionar una imatge (predefinida o pujada), s'actualitza la vista
     * prèvia i s'activa el botó "Acceptar".
     * En acceptar, es guarda la imatge seleccionada per a ús posterior i es tanca
     * el diàleg.
     *
     */
    private void mostrarSelectorImagenes() {
        JDialog selectorDialog = new JDialog(this, "Selecciona la teva imatge de perfil", true);
        selectorDialog.setLayout(new BorderLayout());
        selectorDialog.setSize(850, 650);
        selectorDialog.setLocationRelativeTo(this);

        // Panell principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Colors.GREEN_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panell superior amb opcions
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(Colors.GREEN_BACKGROUND);

        // Botó per pujar imatge
        JButton botoPujarImatge = new JButton("Puja una foto des del teu PC");
        botoPujarImatge.setBackground(Colors.GREEN_LIGHT);
        botoPujarImatge.setForeground(Colors.GREEN_TEXT);
        botoPujarImatge.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 2));
        botoPujarImatge.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        botoPujarImatge.setPreferredSize(new Dimension(250, 40));

        // Vista prèvia
        JLabel vistaPrevia = new JLabel();
        vistaPrevia.setHorizontalAlignment(JLabel.CENTER);
        vistaPrevia.setBorder(BorderFactory.createTitledBorder("Vista prèvia"));
        vistaPrevia.setPreferredSize(new Dimension(200, 200));

        topPanel.add(botoPujarImatge);
        topPanel.add(vistaPrevia);

        JPanel gridPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(Colors.GREEN_BACKGROUND);

        // Panell inferior amb botons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Colors.GREEN_BACKGROUND);

        JButton botoCancelar = new JButton("TORNAR");
        botoCancelar.setMnemonic(KeyEvent.VK_T);
        botoCancelar.setBackground(Colors.YELLOW_LIGHT);
        botoCancelar.setForeground(Color.BLACK);
        botoCancelar.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        botoCancelar.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        botoCancelar.setPreferredSize(new Dimension(100, 40));

        botoCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoCancelar.setBackground(Colors.YELLOW_DARK);
                botoCancelar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoCancelar.setBackground(Colors.YELLOW_LIGHT);
                botoCancelar.setForeground(Color.BLACK);
            }
        });

        JButton botoAcceptar = new JButton("ACCEPTAR");
        botoAcceptar.setBackground(Colors.GREEN_LIGHT);
        botoAcceptar.setForeground(Colors.GREEN_TEXT);
        botoAcceptar.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 4));
        botoAcceptar.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        botoAcceptar.setPreferredSize(new Dimension(100, 40));
        botoAcceptar.setEnabled(false);

        botoAcceptar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botoAcceptar.setBackground(Colors.GREEN_TEXT);
                botoAcceptar.setForeground(Colors.GREEN_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botoAcceptar.setBackground(Colors.GREEN_LIGHT);
                botoAcceptar.setForeground(Colors.GREEN_TEXT);
            }
        });

        // Variables per guardar la selecció
        final ImageIcon[] iconaSeleccionada = { null };
        final String[] rutaSeleccionada = { null };

        // Afegir imatges predefinides
        for (String rutaImatge : IMATGES_PREDEFINIDES) {
            ImageIcon iconaOriginal = new ImageIcon(rutaImatge);
            ImageIcon iconaEscalada = new ImageIcon(
                    iconaOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));

            JPanel panelImatge = new JPanel(new BorderLayout());
            panelImatge.setBorder(BorderFactory.createEmptyBorder());
            panelImatge.setOpaque(false);

            JLabel labelImatge = new JLabel(iconaEscalada);
            panelImatge.add(labelImatge, BorderLayout.CENTER);

            panelImatge.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Component comp : gridPanel.getComponents()) {
                        if (comp instanceof JPanel) {
                            ((JPanel) comp).setBorder(BorderFactory.createEmptyBorder());
                            comp.setBackground(Color.WHITE);
                        }
                    }

                    panelImatge.setBorder(BorderFactory.createLineBorder(Colors.GREEN_TEXT, 3));
                    panelImatge.setBackground(Colors.GREEN_LIGHT);

                    iconaSeleccionada[0] = iconaOriginal;
                    rutaSeleccionada[0] = rutaImatge;

                    ImageIcon preview = new ImageIcon(iconaOriginal.getImage()
                            .getScaledInstance(180, 180, Image.SCALE_SMOOTH));
                    vistaPrevia.setIcon(preview);

                    botoAcceptar.setEnabled(true);
                }
            });

            gridPanel.add(panelImatge);
        }

        botoPujarImatge.addActionListener(e -> {
            LookAndFeel lafOriginal = UIManager.getLookAndFeel();
            
            try {
                // Establim LookAndFeel del SO
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(selectorDialog);

                JFileChooser selectorFitxers = new JFileChooser();
                selectorFitxers.setDialogTitle("Selecciona una imatge");
                selectorFitxers.setFileSelectionMode(JFileChooser.FILES_ONLY);

                selectorFitxers.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) {
                        String nom = f.getName().toLowerCase();
                        return f.isDirectory() ||
                                nom.endsWith(".jpg") ||
                                nom.endsWith(".jpeg") ||
                                nom.endsWith(".png");
                    }

                    public String getDescription() {
                        return "Imatges (*.jpg, *.jpeg, *.png)";
                    }
                });

                int resultat = selectorFitxers.showOpenDialog(selectorDialog);

                if (resultat == JFileChooser.APPROVE_OPTION) {
                    File fitxerSeleccionat = selectorFitxers.getSelectedFile();
                    try {
                        for (Component comp : gridPanel.getComponents()) {
                            if (comp instanceof JPanel) {
                                ((JPanel) comp).setBorder(BorderFactory.createEmptyBorder());
                                comp.setBackground(Color.WHITE);
                            }
                        }

                        ImageIcon novaIcona = new ImageIcon(fitxerSeleccionat.getAbsolutePath());
                        iconaSeleccionada[0] = novaIcona;
                        rutaSeleccionada[0] = fitxerSeleccionat.getAbsolutePath();

                        ImageIcon preview = new ImageIcon(novaIcona.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
                        vistaPrevia.setIcon(preview);

                        botoAcceptar.setEnabled(true);
                    } catch (Exception ex) {
                        JOptionPane.showOptionDialog(
                            VistaCrearAvatar.this,
                            "Error de connexió amb el servidor.",
                            "Error",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[] { "D'acord" },
                            "D'acord");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    // Restaurem LookAndFeel original
                    UIManager.setLookAndFeel(lafOriginal);
                    SwingUtilities.updateComponentTreeUI(selectorDialog);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        botoCancelar.addActionListener(e -> {
            selectorDialog.dispose();
        });

        botoAcceptar.addActionListener(e -> {
            if (iconaSeleccionada[0] != null) {
                rutaImatge = rutaSeleccionada[0];
                imatgeUsuari = iconaSeleccionada[0];

                Tools.actualitzarImatge(imatgeUsuari, labelImagenSeleccionada, IMG_WIDTH, IMG_HEIGTH);
                selectorDialog.dispose();
            }
        });

        bottomPanel.add(botoAcceptar);
        bottomPanel.add(botoCancelar);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        selectorDialog.add(mainPanel);
        selectorDialog.setVisible(true);
    }
}
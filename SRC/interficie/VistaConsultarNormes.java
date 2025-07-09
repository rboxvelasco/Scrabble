package interficie;

import javax.swing.*;

import controladors.CtrlPresentacio;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import interficie.utils.*;

/**
 * @class VistaConsultarNormes
 * Mostra una finestra amb les normes del joc Scrabble en format HTML.
 * 
 * Aquesta vista permet a l'usuari consultar les normes llegides des d'un fitxer.
 * També conté un botó per tornar enrere i un petit logotip.
 */
public class VistaConsultarNormes extends JFrame {
    private JPanel panell;
    private JTextPane areaNormes;
    private JScrollPane scrollPane;
    private JButton botoTornar;
    private JLabel labelLogo;
    private static final int WIDTH_BOTONS = 100, HEIGHT_BOTONS = 40;
    private static final int LOGO_WIDTH = 50, LOGO_HEIGHT = 50;
    private ImageIcon imatgeLogo;

    /**
     * Constructora. Inicialitza i mostra la finestra amb les normes del joc.
     *
     * @param menuPrincipal Finestra del menú principal.
     * @param menuJoc Finestra del menú del joc.
     */
    public VistaConsultarNormes(JFrame menuPrincipal, JFrame menuJoc) {
        setTitle("Scrabble - Normes del Joc");
        if (menuPrincipal != null) {
            Dimension screenSize = menuPrincipal.getSize();
            setSize(screenSize.width, screenSize.height);
            setLocation(menuPrincipal.getLocation());
        } else {
            Dimension screenSize = menuJoc.getSize();
            setSize(screenSize.width, screenSize.height);
            setLocation(menuJoc.getLocation());
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String rutaIcono = "./resources/icon2.png";
        try {
            ImageIcon icono = new ImageIcon(rutaIcono);
            setIconImage(icono.getImage());
        } catch (Exception e) {
            JOptionPane.showOptionDialog(
                    VistaConsultarNormes.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        initComponents();
        carregarNormes();
        setVisible(true);
    }

    /**
     * Inicialitza els components gràfics de la vista.
     */
    private void initComponents() {
        // Panell principal
        panell = new JPanel(new BorderLayout());
        panell.setBackground(Colors.GREEN_BACKGROUND);
        panell.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
                    VistaConsultarNormes.this,
                    "Error de connexió amb el servidor.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { "D'acord" },
                    "D'acord");
        }

        // Panell central
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Colors.GREEN_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Títol
        JLabel titol = new JLabel("Normes del Joc Scrabble", SwingConstants.CENTER);
        titol.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titol.setForeground(Colors.GREEN_TEXT);
        centerPanel.add(titol, BorderLayout.NORTH);

        // Àrea de text
        areaNormes = new JTextPane();
        areaNormes.setContentType("text/html");
        areaNormes.setEditable(false);
        areaNormes.setBackground(Colors.GREEN_BACKGROUND);
        areaNormes.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        scrollPane = new JScrollPane(areaNormes);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Colors.GREEN_BACKGROUND);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Botó Tornar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Colors.GREEN_BACKGROUND);

        botoTornar = createStyledButton("TORNAR");
        botoTornar.setMnemonic(KeyEvent.VK_T);
        botoTornar.addActionListener(e -> CtrlPresentacio.tornar());
        bottomPanel.add(botoTornar);

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        panell.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Crea un botó estilitzat amb comportament visual quan el ratolí entra i surt.
     *
     * @param text Text que mostrarà el botó.
     * @return JButton creat amb l'estil aplicat.
     */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(WIDTH_BOTONS, HEIGHT_BOTONS));
        btn.setBackground(Colors.YELLOW_LIGHT);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Colors.YELLOW_DARK, 4));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Colors.YELLOW_DARK);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Colors.YELLOW_LIGHT);
                btn.setForeground(Color.BLACK);
            }
        });
        return btn;
    }

    /** 
     * Llegeix les normes des del fitxer de text i les mostra amb estil HTML.
     * 
     * El fitxer esperat és `resources/normes.txt`. Si no existeix, es mostra un missatge de reserva.
     */
    private void carregarNormes() {
        String ruta = "resources/normes.txt";
        File fitxer = new File(ruta);

        if (!fitxer.exists()) {
            showFallbackMessage();
            return;
        }

        StringBuilder contingut = new StringBuilder();
        contingut.append("<html><body style='font-family:Comic Sans MS; font-size:14px; color:#000000;'>");

        try (BufferedReader reader = new BufferedReader(new FileReader(fitxer))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("===")) {
                    contingut.append("<h2 style='color:#4A784A;'>").append(linea.substring(3)).append("</h2>");
                } else if (linea.startsWith("---")) {
                    contingut.append("<hr style='border:1px solid #4A784A; width:80%;'>");
                } else if (linea.startsWith("- ")) {
                    contingut.append("<p style='margin-left:20px;'><span style='color:#F6BC09;'>-</span> ")
                            .append(linea.substring(2)).append("</p>");
                } else if (linea.matches("^\\d+\\..*")) {
                    int index = linea.indexOf('.');
                    String numero = linea.substring(0, index + 1);
                    String text = linea.substring(index + 1).trim();
                    contingut.append("<p style='margin-left:20px;'><span style='color:#F6BC09;'>")
                            .append(numero)
                            .append("</span> ")
                            .append(text)
                            .append("</p>");
                } else if (!linea.trim().isEmpty()) {
                    String lineaModificada = linea.replaceAll("\\[(.*?)\\]",
                            "<span style='color:#F6BC09;'>[$1]</span>");
                    contingut.append("<p>").append(lineaModificada).append("</p>");
                }
            }

            contingut.append("<p style='text-align:center; margin-top:30px;'>"
                    + "Podeu consultar les normes oficials a:<br>"
                    + "<a href='https://service.mattel.com/instruction_sheets/51280.pdf'>"
                    + "https://service.mattel.com/instruction_sheets/51280.pdf</a></p>");

            contingut.append("</body></html>");
            areaNormes.setText(contingut.toString());
            areaNormes.setCaretPosition(0);
        } catch (IOException e) {
            showFallbackMessage();
        }
    }

    /**
     * Mostra un missatge HTML alternatiu quan no es troba l'arxiu de normes.
     */
    private void showFallbackMessage() {
        String html = "<html><body style='font-family:Comic Sans MS; font-size:14px; color:#2E5E2E; text-align:center;'>"
                +
                "<h2 style='color:#4A784A;'>⚠ No s'ha trobat l'arxiu amb les normes</h2>" +
                "<p>Podeu consultar les normes oficials a:</p>" +
                "<p><a href='https://service.mattel.com/instruction_sheets/51280.pdf'>https://service.mattel.com/instruction_sheets/51280.pdf</a></p>"
                +
                "</body></html>";
        areaNormes.setText(html);
    }
}
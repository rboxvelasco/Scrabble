package drivers;

import controladors.CtrlDomini;
import controladors.CtrlPartida;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import domini.auxiliars.ColorTerminal;
import domini.scrabble.Fitxa;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Driver de l'aplicació
 */
public class Driver {
    private static CtrlDomini ctrl_dom = null;
    private static CtrlPartida ctrl_partida = null;
    private static Scanner sc = null;
    private static final int num_opts = 8;
    private static int formatEntrada;
    private static final Set<String> DIGRAFS = Set.of("RR", "LL", "NY", "CH", "L·L");
    private static boolean enPartida;

    /**
     * Inicialitza el driver.
     */
    private static void init() {
        ctrl_dom = CtrlDomini.getInstancia();
        ctrl_partida = CtrlPartida.getInstancia(ctrl_dom);
        enPartida = true;
    }

    /**
     * Mostrar les opcions disponibles al menú principal.
     */
    private static void mostrarOpcions() {
        clearScreen();
        System.out.println(ColorTerminal.CYAN + "\n******* OPCIONS DEL CONTROLADOR DE DOMINI *******" + ColorTerminal.RESET);
        System.out.println("[0] Sortir\n");
        System.out.println(ColorTerminal.CYAN + "******* LOG IN *******" + ColorTerminal.RESET);
        System.out.println("[1] Crear Jugador");
        System.out.println("[2] Iniciar Sessió\n");
        System.out.println(ColorTerminal.CYAN + "******* Consultar Jugador *******" + ColorTerminal.RESET);
        System.out.println("[3] Mostrar Estadístiques");
        System.out.println("[4] Eliminar Jugador\n");
        System.out.println(ColorTerminal.CYAN + "******* Informació Partida *******" + ColorTerminal.RESET);
        System.out.println("[5] Crear Partida");
        System.out.println("[6] Reprendre Partida");
        System.out.println("[7] Mostrar Rànquing\n");
        System.out.println(ColorTerminal.CYAN + "******* Normes *******" + ColorTerminal.RESET);
        System.out.println("[8] Consultar Normes\n");
        System.out.println(ColorTerminal.CYAN + "***********************\n" + ColorTerminal.RESET);
        System.out.println("Introdueixi la opció que desitja: \n");
    }

    /**
     * Serveix per comparar dos arxius.
     * S'utilitza en cas de voler executar el driver amb un Joc de Proves, per comparar
     * l'output obtingut amb el fitxer d'output esperat.
     */
    private static void CompararArxius() {
        try {
            // 1. Ruta fixa del primer fitxer
            File fitxer1 = new File("drivers/jocs de prova/output.txt");
    
            // 2. Obrir selecció d’arxiu per al segon fitxer
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Selecciona el fitxer amb què vols comparar");
            File directoriInicial = new File("drivers/jocs de prova");
            selector.setCurrentDirectory(directoriInicial);
    
            int resultat = selector.showOpenDialog(null);
            if (resultat != JFileChooser.APPROVE_OPTION) {
                System.out.println(ColorTerminal.RED + "Comparació cancel·lada per l'usuari." + ColorTerminal.RESET);
                return;
            }
    
            File fitxer2 = selector.getSelectedFile();
    
            // 3. Començar comparació
            try (
                BufferedReader lector1 = new BufferedReader(new FileReader(fitxer1));
                BufferedReader lector2 = new BufferedReader(new FileReader(fitxer2))
            ) {
                String linia1, linia2;
                int numLinia = 1;
                boolean iguals = true;
    
                while ((linia1 = lector1.readLine()) != null | (linia2 = lector2.readLine()) != null) {
                    if (linia1 == null || linia2 == null || !linia1.equals(linia2)) {
                        iguals = false;
                    }
                    numLinia++;
                }
    
                if (iguals) System.out.println("El resultat és idèntic a l'esperat!.");
                else System.out.println("El resultat és diferent de l'esperat.");
            }
    
        } catch (IOException e) {
            System.out.println("Error durant la comparació de resultats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inici del driver.
     * Permet seleccionar el format d'entrada de les dades (terminal o fitxer).
     *
     * @param args Arguments de la línia de comandes.
     */
    public static void main(String[] args) {
        init();
        PrintStream consolaOriginal = System.out; // necessari per lectura de fitxers
        boolean formatValid = false;
        while (!formatValid) {
            Scanner lectorInicial = new Scanner(System.in);

            clearScreen();
            System.out.println(ColorTerminal.CYAN + "**********************************************" + ColorTerminal.RESET);
            System.out.println("Seleccioneu el format d'entrada de les dades:");
            System.out.println("[0] Sortir");
            System.out.println("[1] Terminal (Entrada Manual)");
            System.out.println("[2] Fitxer   (Jocs de Prova)");
            System.out.println(ColorTerminal.CYAN + "**********************************************" + ColorTerminal.RESET);
            System.out.println(" ");

            String entrada = lectorInicial.nextLine().trim();
            try {
                if (entrada.isEmpty()) {
                    System.out.println(ColorTerminal.RED + "Opció no vàlida!! Seleccioneu 0, 1 o 2.\n"+ ColorTerminal.RESET);
                    continue;
                }

                formatEntrada = Integer.parseInt(entrada);

                if (formatEntrada == 0) {
                    System.out.println(ColorTerminal.MAGENTA + "\nSortint... Fins aviat!\n\nDriver Tancat." + ColorTerminal.RESET);
                    formatValid = true;
                    return;
                } else if (formatEntrada == 1) {
                    formatValid = true;
                    sc = new Scanner(System.in);
                    clearScreen();
                } else if (formatEntrada == 2) {
                    formatValid = true;

                    // L'usuari escull l'arxiu amb el Joc de Proves
                    try {
                        // Establim aparença segons SO de l'usuari
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
                        JFileChooser selector = new JFileChooser();
                        selector.setDialogTitle("Selecciona un Joc de Proves .txt");
                        File directorioInicial = new File("./drivers/jocs de prova");
                        selector.setCurrentDirectory(directorioInicial);
                        // selector.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            
                        int resultat = selector.showOpenDialog(null); // null = finestra centrada
            
                        if (resultat == JFileChooser.APPROVE_OPTION) {
                            File fitxerSeleccionat = selector.getSelectedFile();
                            sc = new Scanner(fitxerSeleccionat);
                            // System.out.println("Fitxer seleccionat: " + fitxerSeleccionat.getAbsolutePath());
                            System.out.println("\nExecutant Joc de proves...\n");
                        } else {
                            System.out.println(ColorTerminal.PURPLE + "Selecció cancel·lada." + ColorTerminal.RESET);
                            System.out.println(ColorTerminal.PURPLE + "\nTancant Driver... Fins aviat!" + ColorTerminal.RESET);
                            return;
                        }
            
                    } catch (FileNotFoundException e) {
                        System.out.println("Error: Fitxer no trobat.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        // Creeem (o sobreescribim) l'arxiu output.txt
                        PrintStream salida = new PrintStream(new FileOutputStream("drivers/jocs de prova/output.txt"));
                        System.setOut(salida);  // Redirigim System.out a l'arxiu
                    } catch (FileNotFoundException e) {
                        System.err.println("No s'ha pogut crear el fitxer de sortida.");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(ColorTerminal.RED + "\nOpció no vàlida!! Seleccioneu 1 o 2.\n" + ColorTerminal.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ColorTerminal.RED + "\nOpció no vàlida!! Seleccioneu 1 o 2.\n" + ColorTerminal.RESET);
            }
        }

        int opcio = -1;
        boolean validG = false;
        while (true) {
            if (formatEntrada == 1) {
                mostrarOpcions();
                System.out.print("Tria una opció: ");
                while (!validG) {
                    if (sc.hasNextInt()) {
                        opcio = sc.nextInt();
                        validG = true;
                    } else {
                        System.out.println(ColorTerminal.RED + "Error: No has introduït una xifra." + ColorTerminal.RESET);
                        System.out.print("Tria una opció: ");
                        sc.next();
                    }
                }
            }
            else {
                if (sc.hasNextInt()) {
                    opcio = sc.nextInt();
                    validG = true;
                } else {
                    // Final de l'arxiu, tancar driver
                    System.out.println(ColorTerminal.MAGENTA + "\nFi del fitxer");
                    System.setOut(consolaOriginal);

                    CompararArxius();

                    // File archivo = new File("drivers/jocs de prova/output.txt");

                    // if (archivo.delete()) System.out.println("Archivo eliminado correctamente.");
                    // else System.out.println("No se pudo eliminar el archivo (puede que no exista).");

                    System.out.println(ColorTerminal.PURPLE + "\nSortint... Fins aviat!" + ColorTerminal.RESET);
                    break;
                }
            }
            validG = false;
            sc.nextLine();
            if (opcio == 0) {
                System.out.println(ColorTerminal.MAGENTA + "\nSortint... Fins aviat!" + ColorTerminal.RESET);
                break;
            }
            if (opcio < 1 || opcio > num_opts) {
                System.out.println(ColorTerminal.RED + "\nOpció no vàlida!! Torna-ho a intentar.\n" + ColorTerminal.RESET);
                continue;
            }
            switch (opcio) {
                case 1:
                    crearJugador();
                    break;
                case 2:
                    iniciarSessio();
                    break;
                case 3:
                    mostrarEstadistiques();
                    break;
                case 4:
                    eliminarJugador();
                    break;
                case 5:
                    if (crearPartida()) {
                        jugarPartida();
                    }
                    break;
                case 6:
                    IniciReprendrePartida();
                    break;
                case 7:
                    mostrarRankings();
                    break;
                case 8:
                    consultarNormes();
                    break;

                default:
                    System.out.println(ColorTerminal.RED + "Opció no vàlida. Introdueixi una opció vàlida:" + ColorTerminal.RESET);
            }
        }
        System.out.println(ColorTerminal.MAGENTA + "\nDriver tancat." + ColorTerminal.RESET);
    }

    /**
     * Reprén una partida que s'ha deixat a mitges (no implementada).
     */
    public static void IniciReprendrePartida() {
        if (formatEntrada == 1) {
            System.out.println(ColorTerminal.YELLOW
                    + "La funcionalitat de reprendre partida no està disponible per la Entrega 1, la implementarem per la Entrega 2."
                    + ColorTerminal.RESET);

            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
            sc.nextLine();
        } else {
            System.out.println(ColorTerminal.YELLOW
                    + "La funcionalitat de reprendre partida no està disponible per la Entrega 1, la implementarem per la Entrega 2.\n"
                    + ColorTerminal.RESET);
        }
    }

    /**
     * Elimina un jugador del sistema.
     * Permet eliminar un jugador introduint el seu nom i contrasenya, sigui des
     * de la terminal o des d'un fitxer.
     */
    private static void eliminarJugador() {
        String nom;
        String contrasenya;

        if (formatEntrada == 1) {
            // Mode terminal (interactiu)
            System.out.println("Introdueix el nom del jugador a eliminar (o escriu 'exit' per sortir): ");
            nom = sc.nextLine().trim().toUpperCase();

            if (nom.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "Sortint de l'eliminació del jugador..." + ColorTerminal.RESET);
                return;
            }

            System.out.println("Introdueix la contrasenya per confirmar (o escriu 'exit' per sortir): ");
            contrasenya = sc.nextLine();

            if (contrasenya.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "\nSortint de l'eliminació del jugador...\n" + ColorTerminal.RESET);
                return;
            }

            // Confirmació abans d’eliminar
            System.out.println("Estàs segur que vols eliminar el jugador " + nom + "? (s/n)");
            String confirmacio = sc.nextLine().trim().toLowerCase();

            while (!confirmacio.equals("s") && !confirmacio.equals("n")) {
                System.out.println(ColorTerminal.RED + "\nOpció no vàlida !! Introdueix 's' per sí o 'n' per no.\n" + ColorTerminal.RESET);
                confirmacio = sc.nextLine().trim().toLowerCase();
            }

            if (!confirmacio.equals("s")) {
                System.out.println(ColorTerminal.MAGENTA_BOLD + "\nOperació cancel·lada.\n" + ColorTerminal.RESET);
                return;
            }

            try {
                if (ctrl_dom.iniciarSessio(nom, contrasenya)) {
                    boolean eliminat = ctrl_dom.eliminarJugador(nom, contrasenya);
                    if (eliminat) {
                        System.out.println(ColorTerminal.GREEN + "\nJugador " + nom + " eliminat correctament." + ColorTerminal.GREEN);
                        System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                        sc.nextLine();
                    } else {
                        System.out.println(ColorTerminal.RED + "\nNo s'ha pogut eliminar el jugador." + ColorTerminal.RESET);
                        System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                        sc.nextLine();
                    }
                } else {
                    System.out.println(ColorTerminal.RED + "\nError: usuari no existeix o contrasenya incorrecta.\n" + ColorTerminal.RESET);

                    System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar a intentar-ho..." + ColorTerminal.RESET);
                    sc.nextLine();
                }
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error eliminant jugador: " + e.getMessage() + ColorTerminal.RESET);
                throw e;
            }
        } else {
            // Mode fitxer (no modificat)
            try {
                nom = sc.nextLine().trim().toUpperCase();
                contrasenya = sc.nextLine().trim();
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint dades des del fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return;
            }

            try {
                if (ctrl_dom.iniciarSessio(nom, contrasenya)) {
                    boolean eliminat = ctrl_dom.eliminarJugador(nom, contrasenya);
                    System.out.println(ColorTerminal.GREEN + "Jugador " + nom + (eliminat ? " eliminat correctament." : " no s'ha pogut eliminar.") + ColorTerminal.RESET);
                } else {
                    System.out.println(ColorTerminal.RED + "Error: Usuari o contrasenya incorrectes." + ColorTerminal.RESET);
                }
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error eliminant jugador: " + e.getMessage() + ColorTerminal.RESET);
            }
        }

        System.out.println("");
    }

    /**
     * Crea un nou jugador al sistema.
     * Permet crear un jugador introduint un nom d'usuari i una contrasenya,
     * sigui des de la terminal o des d'un fitxer.
     */
    private static void crearJugador() {
        String nom;
        String contrasenya;

        if (formatEntrada == 1) {
            while (true) {
                System.out.println(ColorTerminal.YELLOW + "\nNo es té en compte la distinció entre majúscules i minúscules." + ColorTerminal.RESET);
                System.out.println("Introdueix un nom d'usuari (o escriu 'exit' per sortir): ");
                nom = sc.nextLine().trim().toUpperCase();

                if (nom.equalsIgnoreCase("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "\nSortint de la creació del jugador...\n" + ColorTerminal.RESET);
                    return;
                }

                if (nom.isEmpty()) {
                    System.out.println(ColorTerminal.RED + "\nError: El nom no pot estar buit." + ColorTerminal.RESET);
                    continue;
                }
                
                if (!nom.matches("^[A-ZÀ-Ú ]+$")) { // Permet lletres (incloent accents) i espais
                    System.out.println(ColorTerminal.RED + "\nError: El nom només pot contenir lletres i espais.\n" + ColorTerminal.RESET);
                    continue;
                }

                if (ctrl_dom.existeixJugador(nom)) {
                    System.out.println(ColorTerminal.RED + "\nError: Ja existeix un jugador amb aquest nom. Introdueix un altre nom." + ColorTerminal.RESET);
                } else {
                    break;
                }
            }

            while (true) {
                System.out.println("Introdueix la contrasenya per confirmar (o escriu 'exit' per sortir): ");
                contrasenya = sc.nextLine();

                if (contrasenya.equalsIgnoreCase("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "\nSortint de la creació del jugador...\n" + ColorTerminal.RESET);
                    continue;
                }

                if (contrasenya.isEmpty()) {
                    System.out.println(ColorTerminal.RED + "\nError: La contrasenya no pot estar buida." + ColorTerminal.RESET);
                    continue;
                } else {
                    break;
                }
            }

        } else {
            try {
                nom = sc.nextLine().trim().toUpperCase();
                contrasenya = sc.nextLine().trim();
                if (nom.isEmpty() || contrasenya.isEmpty()) {
                    System.out.println(ColorTerminal.RED + "Error: El nom o la contrasenya del fitxer no poden estar buits." + ColorTerminal.RESET);
                    return;
                }
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint dades des del fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return;
            }
        }

        try {
            ctrl_dom.crearJugador(nom, contrasenya);
            System.out.println(ColorTerminal.GREEN + "\nJugador creat correctament: " + nom + ColorTerminal.RESET);
            
            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
            sc.nextLine();
        } catch (Exception e) {
            System.out.println(ColorTerminal.RED + "Error: " + e.getMessage() + ColorTerminal.RESET);
            if (formatEntrada == 1) throw e;
        }

        System.out.println("");
    }

    /**
     * Inicia sessió amb un jugador existent.
     * Comprova les credencials del jugador i inicia sessió si són correctes.
     */
    private static void iniciarSessio() {
        String nom;
        String contrasenya;

        if (formatEntrada == 1) {
            System.out.println("Introdueix un nom d'usuari (o escriu 'exit' per sortir):");
            nom = sc.nextLine().trim().toUpperCase();

            if (nom.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "Sortint de l'inici de sessió..." + ColorTerminal.RESET);
                return;
            }

            System.out.println("Introdueix la contrasenya per confirmar (o escriu 'exit' per sortir):");
            contrasenya = sc.nextLine();

            if (contrasenya.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "Sortint de l'inici de sessió..." + ColorTerminal.RESET);
                return;
            }
        } else {
            try {
                nom = sc.nextLine().trim().toUpperCase();
                contrasenya = sc.nextLine().trim();

                if (nom.equalsIgnoreCase("exit") || contrasenya.equalsIgnoreCase("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "Sortint de l'inici de sessió..." + ColorTerminal.RESET);
                    return;
                }
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint les usuari o contrasenya des del fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return;
            }
        }

        // Comprovar credencials
        try {
            boolean sessioIniciada = ctrl_dom.iniciarSessio(nom, contrasenya);
            if (sessioIniciada) {
                System.out.println(ColorTerminal.GREEN + "\nSessió iniciada per l'usuari: " + nom + ColorTerminal.RESET);

                System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                sc.nextLine();
            } else {
                System.out.println(ColorTerminal.RED + "\nError: Usuari o contrasenya incorrectes.\n"+ ColorTerminal.RESET);

                System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar a intentar-ho..." + ColorTerminal.RESET);
                sc.nextLine();
            }
        } catch (Exception e) {
            System.out.println(ColorTerminal.RED + "Error: " + e.getMessage() + ColorTerminal.RESET);
            if (formatEntrada == 1)
                throw e;
        }

        System.out.println("");
    }

    /**
     * Mostra les estadístiques d'un jugador.
     * Permet consultar les estadístiques d'un jugador existent introduint el seu
     * nom.
     */
    private static void mostrarEstadistiques() {
        String nom;

        if (formatEntrada == 1) {
            // Mode terminal (interactiu)
            System.out.println("Introdueix el nom del jugador (o escriu 'exit' per sortir):");
            nom = sc.nextLine().trim().toUpperCase();

            if (nom.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "Sortint de la consulta d'estadístiques..." + ColorTerminal.RESET);
                return;
            }
        } else {
            // Mode fitxer
            try {
                nom = sc.nextLine().trim().toUpperCase();
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint el fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return;
            }
        }

        if (nom.isEmpty()) {
            System.out.println(ColorTerminal.RED + "Error: El nom no pot estar buit." + ColorTerminal.RESET);
            return;
        }

        try {
            if (!ctrl_dom.existeixJugador(nom)) {
                System.out.println(ColorTerminal.RED + "Error: El jugador '" + nom + "' no existeix." + ColorTerminal.RESET);

                System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                sc.nextLine();
                return;
            }

            clearScreen();
            ctrl_dom.mostrarEstadistiques(nom);
            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per sortir..." + ColorTerminal.RESET);
            sc.nextLine();
            // System.out.println("Estadístiques de " + nom);
        } catch (Exception e) {
            System.out.println(ColorTerminal.RED + "Error: " + e.getMessage() + ColorTerminal.RESET);
        }

        System.out.println("");
    }

    /**
     * Crea una nova partida.
     * Permet configurar i iniciar una partida nova, sigui contra un altre
     * jugador o contra la màquina.
     *
     * @return True si la partida s'ha creat correctament, False en cas contrari.
     */
    private static boolean crearPartida() {
        enPartida = true;
        if (formatEntrada == 1) {
            String resposta = "";
            while (true) {
                System.out.println("Vols jugar contra la màquina? [s/n]");
                resposta = sc.nextLine().trim().toLowerCase();

                if (resposta.equals("s") || resposta.equals("n"))
                    break;

                System.out.println(ColorTerminal.RED + "\nError: resposta no vàlida. Si us plau, escriu 's' o 'n'.\n" + ColorTerminal.RESET);
            }
            String nomJugador1, contrasenya1;

            while (true) {
                System.out.println("\nIntrodueix el nom del jugador 1 (o escriu 'exit' per sortir):");
                nomJugador1 = sc.nextLine().toUpperCase();
                if (nomJugador1.equalsIgnoreCase("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "\nSortint del procés de creació de partida..." + ColorTerminal.RESET);
                    return false;
                }

                System.out.println("Introdueix la contrasenya del jugador1 (o escriu 'exit' per sortir):");
                contrasenya1 = sc.nextLine();

                if (contrasenya1.equalsIgnoreCase("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "\nSortint de l'inici de sessió..." + ColorTerminal.RESET);
                    return false;
                }

                if (ctrl_dom.obtenirIdJugador(nomJugador1, contrasenya1) != null) break;

                System.out.println(ColorTerminal.RED + "\nError: Les credencials del jugador 1 són incorrectes. Torna-ho a intentar.\n" + ColorTerminal.RESET);
            }

            String nomJugador2;
            String contrasenya2;
            if (resposta.equals("s")) {
                nomJugador2 = "MAQUINA";
                contrasenya2 = "*";
            } else {
                while (true) {
                    System.out.println("\nIntrodueix el nom del jugador 2 (o escriu 'exit' per sortir):");
                    nomJugador2 = sc.nextLine().toUpperCase();
                    if (nomJugador2.equalsIgnoreCase("exit")) {
                        System.out.println(ColorTerminal.MAGENTA + "\nSortint del procés de creació de partida..." + ColorTerminal.RESET);
                        return false;
                    }

                    System.out.println("Introdueix la contrasenya del jugador 2 (o escriu 'exit' per sortir):");
                    contrasenya2 = sc.nextLine();

                    if (contrasenya2.equalsIgnoreCase("exit")) {
                        System.out.println(ColorTerminal.MAGENTA + "\nSortint de l'inici de sessió..." + ColorTerminal.RESET);
                        return false;
                    }

                    if (ctrl_dom.obtenirIdJugador(nomJugador2, contrasenya2) != null) break;

                    System.out.println(ColorTerminal.RED + "\nError: Les credencials del jugador 2 són incorrectes. Torna-ho a intentar.\n" + ColorTerminal.RESET);
                }
            }

            String idioma = "";
            while (true) {
                System.out.println("\nIntrodueix el número corresponent a l'idioma de la partida (o escriu 'exit' per sortir):");
                System.out.println("[1] Català");
                System.out.println("[2] Castellà");
                System.out.println("[3] Anglès");
                idioma = sc.nextLine().trim().toLowerCase();

                if (idioma.equals("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "Sortint del procés de creació de partida..." + ColorTerminal.RESET);
                    return false;
                }

                if (idioma.equals("1") || idioma.equals("2") || idioma.equals("3"))
                    break;
                System.out.println(ColorTerminal.RED + "Error: L'idioma introduït no és vàlid. Tria una opció de la llista.\n" + ColorTerminal.RESET);
            }

            int midaTaulell = -1;
            while (true) {
                System.out.println("\nIntrodueix la mida del taulell (ha de ser senar entre 5 i 29) o escriu 'exit' per sortir:");
                String entrada = sc.nextLine().trim().toLowerCase();

                if (entrada.equals("exit")) {
                    System.out.println(ColorTerminal.MAGENTA + "Sortint del procés de creació de partida..." + ColorTerminal.RESET);
                    return false;
                }

                try {
                    midaTaulell = Integer.parseInt(entrada);
                    if (midaTaulell >= 5 && midaTaulell % 2 == 1 && midaTaulell <= 29) break;
                    else System.out.println(ColorTerminal.RED + "Opció no vàlida!! La mida ha de un nombre senar entre 5 i 29. Torna-ho a intentar\n" + ColorTerminal.RESET);
                } catch (NumberFormatException e) {
                    System.out.println(ColorTerminal.RED + "Opció no vàlida!! Has d'introduir un número enter. Torna-ho a intentar.\n" + ColorTerminal.RESET);
                }
            }

            try {
                ctrl_dom.CrearPartida(nomJugador1, contrasenya1, nomJugador2, contrasenya2, idioma, midaTaulell);
            } catch (IllegalArgumentException e) {
                System.out.println(ColorTerminal.RED + "Error: " + e.getMessage() + ColorTerminal.RESET);
            }
        } else {
            try {
                String maquina = sc.nextLine().trim();
                boolean maq = false;
                if (maquina.equals("n")) maq = false;
                else if (maquina.equals("s")) maq = true;

                String nom1 = sc.nextLine().trim();
                String contra1 = sc.nextLine().trim();

                String nom2 = "MAQUINA";
                String contra2 = "*";
                if (!maq) {
                    nom2 = sc.nextLine().trim();
                    contra2 = sc.nextLine().trim();
                }

                String idioma = sc.nextLine().trim();
                int mida = sc.nextInt();

                ctrl_dom.CrearPartida(nom1, contra1, nom2, contra2, idioma, mida);
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint dades des del fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return false;
            }
        }
        System.out.println("");
        return true;
    }

    /**
     * Reanuda una partida pausada.
     * Permet continuar una partida que havia estat pausada prèviament.
     */
    private static void reprendrePartida() {
        if (formatEntrada == 1) {
            try {
                ctrl_dom.ReprendrePartida();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Funcionalitat no implementada encara per fitxer.");
        }
        System.out.println("");
    }

    /**
     * Mostra el rànquing global de jugadors.
     * Permet consultar les millors posicions del rànquing global, amb l'opció de
     * personalitzar el nombre de posicions a mostrar.
     */
    public static void mostrarRankings() {
        int mostrarFins = 5;

        System.out.println("Es mostraran per defecte les 5 primeres posicions del ranking global.");
        System.out.println("Prem 'E' si en vols veure més (o qualsevol altra tecla per continuar):");
        String entrada = sc.nextLine().trim();

        if (entrada.equalsIgnoreCase("E")) {
            System.out.print("Introdueix el número de posicions que vols mostrar: ");
            String numero = sc.nextLine().trim();
            try {
                mostrarFins = Integer.parseInt(numero);
            } catch (NumberFormatException e) {
                System.out.println(ColorTerminal.RED + "Entrada no vàlida. Es mostraran només les 5 primeres posicions." + ColorTerminal.RESET);
                mostrarFins = 5;
            }
        }

        try {
            clearScreen();
            ctrl_dom.mostrarRankingGlobalLimitat(mostrarFins);

            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar al menú..." + ColorTerminal.RESET);
            sc.nextLine();
        } catch (Exception e) {
            System.out.println(ColorTerminal.RED + "Error: " + e.getMessage() + ColorTerminal.RESET);
        }

        System.out.println("");
    }

    /**
     * Consulta les normes del joc.
     * Mostra les normes del joc des d'un fitxer local o proporciona un enllaç a les
     * normes oficials.
     */
    private static void consultarNormes() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        try {
            System.out.println(ColorTerminal.CYAN_BOLD + "╔═══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║" + ColorTerminal.centerText(ColorTerminal.CYAN_BACKGROUND + ColorTerminal.WHITE_BOLD +
                    "  NORMES DEL JOC SCRABBLE  " + ColorTerminal.RESET, 74) + ColorTerminal.CYAN_BOLD + "                     ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════════════╝\n" + ColorTerminal.RESET);

            BufferedReader reader = new BufferedReader(new FileReader("resources/normes.txt"));
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("===")) {
                    System.out.println(ColorTerminal.GREEN_BOLD + linea.substring(3).trim() + ColorTerminal.RESET);
                } else if (linea.startsWith("---")) {
                    System.out.println(ColorTerminal.YELLOW + "─".repeat(60) + ColorTerminal.RESET);
                } else if (linea.startsWith("- ")) {
                    System.out.println(ColorTerminal.YELLOW + "• " + ColorTerminal.WHITE + linea.substring(2) + ColorTerminal.RESET);
                } else if (linea.matches(".*\\[.*\\].*")) {
                    System.out.println(linea.replaceAll("\\[(.*?)\\]", ColorTerminal.YELLOW_BOLD + "[$1]" + ColorTerminal.WHITE) + ColorTerminal.RESET);
                } else {
                    System.out.println(ColorTerminal.WHITE + linea + ColorTerminal.RESET);
                }
            }
            reader.close();

            System.out.println();
            System.out.println(ColorTerminal.CYAN_BOLD + "Per a més informació, visiteu les normes oficials:");
            System.out.println(ColorTerminal.CYAN + "https://service.mattel.com/instruction_sheets/51280.pdf" + ColorTerminal.RESET);
            System.out.println();
            System.out.println(ColorTerminal.MAGENTA + "Prem 'Enter' per tornar al menú..." + ColorTerminal.RESET);
            sc.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println(ColorTerminal.RED_BOLD + "No s'ha trobat l'arxiu local amb les normes." + ColorTerminal.RESET);
            System.out.println(ColorTerminal.CYAN_BOLD + "Podeu consultar les normes oficials a:");
            System.out.println(ColorTerminal.CYAN + "https://service.mattel.com/instruction_sheets/51280.pdf" + ColorTerminal.RESET);
            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar al menú..." + ColorTerminal.RESET);
            sc.nextLine();
        } catch (IOException e) {
            System.out.println(ColorTerminal.RED_BOLD + "Error en llegir l'arxiu de normes local." + ColorTerminal.RESET);
            System.out.println(ColorTerminal.CYAN_BOLD + "Podeu consultar les normes oficials a:");
            System.out.println(ColorTerminal.CYAN + "https://service.mattel.com/instruction_sheets/51280.pdf" + ColorTerminal.RESET);
            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar al menú..." + ColorTerminal.RESET);
            sc.nextLine();
        }
    }

    /**
     * Mostra per pantalla les opcions de les que disposa un jugador
     * durant la partida.
     */
    private static void mostrarOpcionsPartida() {
        System.out.println(ColorTerminal.CYAN + "\n******* OPCIONS DEL CONTROLADOR DE PARTIDA *******" + ColorTerminal.RESET);
        System.out.println("[1] Col·locar paraula");
        System.out.println("[2] Canviar fitxes");
        System.out.println("[3] Passar torn");
        System.out.println("[4] Pausar partida");
        System.out.println("[5] Guardar partida");
        System.out.println("[6] Abandonar partida");
        System.out.println("[7] Consultar normes");
        System.out.println(ColorTerminal.CYAN + "***************************************\n" + ColorTerminal.RESET);
        System.out.println("Introdueixi la opció que desitja: \n");
    }

    /**
     * Donada una seqüència de caràcters identifica qualsevol submot "LxL" on x no pertany
     * al conjunt {A-Z} i ho substitueix per "L.L".
     * 
     * @param text  Seqüència de caràcters a modificar.
     * @return      La seqüència amb les "L·L" corregides.
     */
    private static String substituirLxL(String text) {
        return text.replaceAll("L[^A-Z\\s]L", "L·L");
    }

    /**
     * Donada una seqüència de caràcters identifica qualsevol submot "[VOCAL]?" o 
     * "?[VOCAL]" i ho substitueix per "[VOCAL]Ç" o "C[VOCAL]" pertinentment.
     * 
     * @param text  Seqüència de caràcters a modificar.
     * @return      La seqüència amb les "Ç" corregides.
     */
    private static String substituirCtrencada(String text) {
        text = text.replaceAll("A([^A-ZÀ-ÿ\\s])", "AÇ");
        text = text.replaceAll("E([^A-ZÀ-ÿ\\s])", "EÇ");
        text = text.replaceAll("I([^A-ZÀ-ÿ\\s])", "IÇ");
        text = text.replaceAll("O([^A-ZÀ-ÿ\\s])", "OÇ");
        text = text.replaceAll("U([^A-ZÀ-ÿ\\s])", "UÇ");

        text = text.replaceAll("([^A-ZÀ-ÿ\\s])A", "ÇA");
        text = text.replaceAll("([^A-ZÀ-ÿ\\s])E", "ÇE");
        text = text.replaceAll("([^A-ZÀ-ÿ\\s])I", "ÇI");
        text = text.replaceAll("([^A-ZÀ-ÿ\\s])O", "ÇO");
        text = text.replaceAll("([^A-ZÀ-ÿ\\s])U", "ÇU");

        return text;
    }

    /**
     * Donat un text entrat per terminal, és possible que el charSet de la consola no
     * reconegui correctament les 'Ç' o "L·L", així doncs identifiquem aquests caràcters
     * mal reconeguts i els corregim.
     * 
     * @param text  Un String amb potencials errors.
     * @return      Un String amb els caràcters sensibles corregits.
     */
    private static String corregir_caracters_terminal_windows(String text) {
        String corrected = text;
        corrected = substituirLxL(corrected);
        corrected = substituirCtrencada(corrected);
        return corrected;
    }

    /**
     * Genera una llista amb les lletres i dígrafs del paràmetre.
     *
     * Recorre l'string d'entrada segmentant tots els seus elements identificant 
     * correctament els dígrafs.
     *
     * @param word Paraula a segmentar.
     *
     * @return Una llista amb els elements ja segmentats.
     */
    private static List<String> tokenize(String word) {
        word = corregir_caracters_terminal_windows(word);
        List<String> tokens = new ArrayList<>();

        int i = 0;
        while (i < word.length()) {
            if (i + 2 <= word.length() && DIGRAFS.contains(word.substring(i, i + 2))) {
                tokens.add(word.substring(i, i + 2));
                i += 2;
            } else if (i + 3 <= word.length() && DIGRAFS.contains(word.substring(i, i + 3))) {
                tokens.add(word.substring(i, i + 3));
                i += 3;
            } else {
                tokens.add(String.valueOf(word.charAt(i)));
                i++;
            }
        }
        return tokens;
    }

    /**
     * Col·loca una paraula al taulell.
     * Permet col·locar una paraula al taulell de joc introduint la seva posició
     * inicial i orientació.
     *
     * @return True si la paraula s'ha col·locat correctament, False en cas
     *         contrari.
     */
    private static boolean colocarParaula() {
        System.out.println(ColorTerminal.YELLOW + "Si vols utilitzar un comodí escriu la lletra per la que vulguis canviar-lo, no el '#'" + ColorTerminal.RESET);
        System.out.println("Introdueix la paraula COMPLETA que vulguis formar:");
        String paraulaStr = sc.nextLine().toUpperCase();
        int fila;
        System.out.println("Introdueixi la fila inicial: ");
        while (!sc.hasNextInt()) {
            System.out.println(ColorTerminal.RED + "Error: Només es permeten xifres enteres positives. Torna-ho a intentar." + ColorTerminal.RESET);
            sc.next();
            System.out.println("Introdueixi la fila inicial: ");
        }
        fila = sc.nextInt();

        System.out.println("Introdueixi la columna inicial: ");
        int columna;
        while (!sc.hasNextInt()) {
            System.out.println(ColorTerminal.RED + "Error: Només es permeten xifres enteres positives. Torna-ho a intentar." + ColorTerminal.RESET);
            sc.next();
            System.out.println("Introdueixi la columna inicial: ");
        }

        columna = sc.nextInt();
        sc.nextLine();

        int horitzontal = -1;
        while (true) {
            System.out.println("La paraula a introduir és: ");
            System.out.println("[1] Horitzontal");
            System.out.println("[2] Vertical");

            String h = sc.nextLine().trim();

            try {
                horitzontal = Integer.parseInt(h);
            } catch (NumberFormatException e) {
                System.out.println(ColorTerminal.RED + "Opció no vàlida!! Seleccioneu 1 o 2.\n" + ColorTerminal.RESET);
                continue;
            }

            if (horitzontal == 1 || horitzontal == 2) break;
            else System.out.println(ColorTerminal.RED + "Opció no vàlida!! Seleccioneu 1 o 2.\n" + ColorTerminal.RESET);
        }

        
        boolean hor = false;
        if (horitzontal == 1) hor = true;
        else if (horitzontal == 2) hor = false;

        System.out.println("\n");
        List<String> tokensParaula = tokenize(paraulaStr);
        List<Fitxa> fitxesParaula = new ArrayList<>();

        for (int i = 0; i < tokensParaula.size(); i++) {
            String lletra = tokensParaula.get(i);
            int puntuacio = ctrl_partida.getBossa1().obtenirPuntuacio(lletra);
            int idFitxa = ctrl_partida.getBossa1().getIdFitxa(lletra);

            fitxesParaula.add(new Fitxa(idFitxa, lletra, puntuacio));
        }

        if (ctrl_partida.colocarParaula(fitxesParaula, fila, columna, hor)) {
            clearScreen();
            ctrl_partida.imprimirTaulell();
            System.out.println(ColorTerminal.GREEN + "Paraula col·locada correctament!" + ColorTerminal.RESET);

            if (!ctrl_partida.contraMaquina()) System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per passar a la pantalla de canvi de Torn..." + ColorTerminal.RESET);
            else System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
            sc.nextLine();

            if (!ctrl_partida.contraMaquina()) PantallaCanviDeTorn(ctrl_partida.jugador_controladorNOM(), ctrl_partida.jugadorNoActual_controladorNOM());

            return true;
        } else {
            System.out.println(ColorTerminal.RED + "Error al col·locar la paraula.\n" + ColorTerminal.RESET);

            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar a intentar-ho..." + ColorTerminal.RESET);
            sc.nextLine();

            return false;
        }
    }

    /**
     * Mostra la pantalla de canvi de torn donant temps per a que el jugador
     * es retiri i no vegi la pantalla del contrincant.
     */
    private static void PantallaCanviDeTorn(String nom_j1, String nom_j2) {
        clearScreen();

        System.out.println(ColorTerminal.CYAN_BOLD + "╔═══════════════════════════════════════════════════╗");
        System.out.println("║" + ColorTerminal.centerText(ColorTerminal.CYAN_BACKGROUND + ColorTerminal.WHITE_BOLD +
                "  CANVI DE TORN  " + ColorTerminal.RESET, 51) + ColorTerminal.CYAN_BOLD + "                 ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n" + ColorTerminal.RESET);

        System.out.println(" " + ColorTerminal.RED + nom_j1 + ColorTerminal.RESET +
             " ha de passar el dispositiu a " + ColorTerminal.GREEN + nom_j2 + ColorTerminal.RESET + ".\n");
        System.out.println(" Quan " + ColorTerminal.GREEN + nom_j2 + ColorTerminal.RESET + " estigui llest, prem" +
            ColorTerminal.MAGENTA + " 'Enter'" + ColorTerminal.RESET + " per a veure la teva pantalla.");

        sc.nextLine();
    }

    /**
     * Identifica els caràcters del paràmetre d'entrada que hauríen de correspondre's
     * amb 'Ç' i els substitueix. Funció auxiliar de canviarFitxes()
     * 
     * @param text  Text a corregir
     * @return      Un String amb amb els caràcters pertinents canviats per 'Ç'
     */
    private static String identificarCTrenada(String text) {
        StringBuilder corregit = new StringBuilder(text);
    
        boolean trobada = false;
        List<Fitxa> fitxesDisponibles = ctrl_partida.getLletresJugadorTorn();
        for (Fitxa f : fitxesDisponibles) {
            if (f.getLletra().equals("Ç")) {
                trobada = true;
                break;
            }
        }
    
        if (trobada) {
            for (int i = 0; i < corregit.length(); i++) {
                char c = corregit.charAt(i);
                String charAsString = String.valueOf(c);
    
                if (!charAsString.matches("[A-ZÀ-ÿÇ\\s#·]")) {
                    corregit.setCharAt(i, 'Ç');
                }
            }
        }
    
        return corregit.toString();
    }

    /**
     * Canvia fitxes del jugador actual.
     * Permet al jugador canviar algunes o totes les seves fitxes per altres de la
     * bossa.
     *
     * @return true si les fitxes s'han canviat correctament, false en cas contrari.
     */
    private static boolean canviarFitxes() {
        int numFitxes;
        while (true) {
            System.out.println("Quantes fitxes vols canviar? o escriu 'exit' per a sortir: ");
            String inputNum = sc.nextLine().trim();
            if (inputNum.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "Sortint de canviar fitxes..." + ColorTerminal.RESET);
                return false; // CANVIAR NO S'HA DE PASSAR TURNO
            }
            if (!inputNum.matches("\\d+")) {
                System.out.println(ColorTerminal.RED + "Error: Només es permeten xifres enteres positives. Torna-ho a intentar." + ColorTerminal.RESET);
                continue;
            }
            numFitxes = Integer.parseInt(inputNum);
            if (numFitxes <= 0) {
                System.out.println(ColorTerminal.RED + "Error: El número ha de ser més gran que zero. Torna-ho a intentar." + ColorTerminal.RESET);
                continue;
            }
            if (numFitxes > ctrl_partida.jugador_controladorNUM_FITXES()) {
                System.out.println(ColorTerminal.RED + "Error: No pots canviar més fitxes de les que tens. Torna-ho a intentar." + ColorTerminal.RESET);
                continue;
            }
            else break;
        }

        List<Fitxa> fitxesDisponibles = ctrl_partida.getLletresJugadorTorn();
        if (numFitxes == 7) {
            System.out.println("Vols canviar totes les teves fitxes? (s/n)");
            String confirmacio = sc.nextLine().trim().toLowerCase();

            if (confirmacio.equals("s")) {
                List<String> totesFitxes = new ArrayList<>();
                for (Fitxa f : fitxesDisponibles) {
                    totesFitxes.add(f.getLletra());
                }

                if (ctrl_partida.canviarFitxes(totesFitxes, totesFitxes.size(), true, "", "")) {
                    clearScreen();
                    
                    System.out.print("\nFitxes disponibles: [ ");
                    for (Fitxa f : ctrl_partida.getLletresJugadorTorn()) {
                        System.out.print(f.getLletra() + "(" + f.getPuntuacio() + ") ");
                    }
                    System.out.println("] - El format que segueix és: lletra(puntuació).\n");

                    System.out.println(ColorTerminal.GREEN + "Totes les fitxes han estat canviades!" + ColorTerminal.RESET);
                    System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                    sc.nextLine();

                    return true;
                } else {
                    System.out.println(ColorTerminal.RED + "Error al canviar les fitxes" + ColorTerminal.RESET);
                    System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                    sc.nextLine();

                    return false;
                }
            } else {
                System.out.println(ColorTerminal.MAGENTA_BOLD + "Operació cancel·lada" + ColorTerminal.RESET);
                System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                sc.nextLine();

                return false;
            }
        }

        while (true) {
            System.out.println("Introdueix les lletres que vols canviar (separades per espais) o escriu 'exit' per a sortir:");
            String input = sc.nextLine().trim().toUpperCase();
            input = substituirLxL(input); // Per a identificar bé les L·L des de la terminal de windows
            input = identificarCTrenada(input); // Per a identificar bé les Ç des de la terminal de windows


            if (input.equalsIgnoreCase("exit")) {
                System.out.println(ColorTerminal.MAGENTA + "Sortint de canviar fitxes..." + ColorTerminal.RESET);

                System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                sc.nextLine();
                return false;
            }

            if (input.isEmpty()) {
                System.out.println(ColorTerminal.RED + "No has introduït cap lletra. Torna-ho a intentar." + ColorTerminal.RESET);
                continue;
            }

            if (!input.matches("^[A-ZÀ-ÿÇ\\s#·]+$")) {
                System.out.println(ColorTerminal.RED + "Error: Només es permeten lletres. No pots introduir números ni símbols. Torna-ho a intentar." + ColorTerminal.RESET);
                continue;
            }

            String[] lletresSeleccionades = input.split("\\s+");
            List<String> lletresACanviar = new ArrayList<>();
            boolean totesTrobades = true;

            for (String lletra : lletresSeleccionades) {
                boolean trobada = false;
                for (Fitxa f : fitxesDisponibles) {
                    if (f.getLletra().equals(lletra)) {
                        lletresACanviar.add(f.getLletra());
                        trobada = true;
                        break;
                    }
                }
                if (!trobada) {
                    System.out.println(ColorTerminal.RED + "La lletra '" + lletra + "' no està a les teves fitxes disponibles. Torna-ho a intentar." + ColorTerminal.RESET);
                    totesTrobades = false;
                    continue;
                }
            }

            if (!totesTrobades) {
                continue;
            }

            if (ctrl_partida.canviarFitxes(lletresACanviar, numFitxes, true, "", "")) {
                clearScreen();

                System.out.print("\nFitxes disponibles: [ ");
                for (Fitxa f : ctrl_partida.getLletresJugadorTorn()) {
                    System.out.print(f.getLletra() + "(" + f.getPuntuacio() + ") ");
                }
                System.out.println("] - El format que segueix és: lletra(puntuació).\n");
    
                System.out.println(ColorTerminal.GREEN + "Fitxes canviades!" + ColorTerminal.RESET);

                System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
                sc.nextLine();

                // if (!ctrl_partida.contraMaquina()) PantallaCanviDeTorn();
                break;
            } else {
                continue;
            }
        }
        return true;
    }

    /**
     * Passa el torn del jugador actual.
     * Permet al jugador actual passar el seu torn sense fer cap acció.
     *
     * @return true si el torn s'ha passat correctament, false si la partida ha
     *         finalitzat.
     */
    private static boolean passarTorn() {
        if (ctrl_partida.passarTorn()) {
            System.out.println(ColorTerminal.GREEN + "\nLa partida ha finalitzat per torns inactius consecutius." + ColorTerminal.RESET);

            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per tornar al menú..." + ColorTerminal.RESET);
            sc.nextLine();

            return false;
        } else {
            System.out.println(ColorTerminal.GREEN + "Torn passat!" + ColorTerminal.RESET);

            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
            sc.nextLine();

            if (!ctrl_partida.contraMaquina()) PantallaCanviDeTorn(ctrl_partida.jugadorNoActual_controladorNOM(), ctrl_partida.jugador_controladorNOM());
            return true;
        }
    }

    /**
     * Pausa la partida actual.
     * Permet pausar la partida i mostrar un menú amb opcions per continuar o
     * finalitzar la partida.
     *
     * @return true si la partida es reanuda, false si es finalitza.
     */
    private static boolean pausarPartida() {
        ctrl_partida.pausarCt();
        System.out.println(ColorTerminal.GREEN + "\nLa partida s'ha pausat." + ColorTerminal.RESET);

        return mostrarMenuPausa();
    }

    
    /**
     * Permet guardar la partida i torna a la pantalla d'inici.
     * 
     * @return True si s'ha guardat correctament, False en cas contrari.
     */
    private static boolean guardarPartida() {
        String confirmacio;
        if (formatEntrada == 1) {
            System.out.println("\nEstàs a punt de guardar la partida.");
            System.out.println(ColorTerminal.YELLOW + "\nTots els canvis es guardaran per poder reprendre la partida més tard." + ColorTerminal.RESET);
            System.out.println("\nEstàs segur que vols continuar? (s/n)");
            confirmacio = sc.nextLine().trim().toLowerCase();

            while (!confirmacio.equals("s") && !confirmacio.equals("n")) {
                System.out.println(ColorTerminal.RED + "\nSi us plau, introdueix 's' per sí o 'n' per no:" + ColorTerminal.RESET);
                confirmacio = sc.nextLine().trim().toLowerCase();
            }
        } else {
            try {
                confirmacio = sc.nextLine().trim().toLowerCase();
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint confirmació des del fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return false;
            }
        }

        if (confirmacio.equals("s")) {
            System.out.println(ColorTerminal.GREEN + "\nLa partida s'ha guardat correctament.\n" + ColorTerminal.RESET);
            return true;
        } else {
            System.out.println(
                    ColorTerminal.MAGENTA_BOLD + "\nHas cancel·lat guardar la partida. La partida continua."
                            + ColorTerminal.RESET);
            return false;
        }
    }

    /**
     * Abandona la partida actual.
     * Permet al jugador abandonar la partida, finalitzant-la immediatament.
     *
     * @return true si el jugador abandona la partida, false si decideix continuar.
     */
    private static boolean abandonarPartida() {
        String confirmacio;

        if (formatEntrada == 1) {
            System.out.println("\nEstàs a punt d'abandonar la partida.");
            System.out.println(ColorTerminal.YELLOW + "\nTots els canvis que no hagin estat guardats es perdran i no es podran recuperar." + ColorTerminal.RESET);
            System.out.println("\nEstàs segur que vols continuar? (s/n)");

            confirmacio = sc.nextLine().trim().toLowerCase();

            while (!confirmacio.equals("s") && !confirmacio.equals("n")) {
                System.out.println(ColorTerminal.RED + "\nSi us plau, introdueix 's' per sí o 'n' per no:" + ColorTerminal.RESET);
                confirmacio = sc.nextLine().trim().toLowerCase();
            }
        } else {
            try {
                confirmacio = sc.nextLine().trim().toLowerCase();
            } catch (Exception e) {
                System.out.println(ColorTerminal.RED + "Error llegint confirmació des del fitxer: " + e.getMessage() + ColorTerminal.RESET);
                return false;
            }
        }

        if (confirmacio.equals("s")) {
            clearScreen();
            ctrl_partida.abandonarPartida();
            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
            sc.nextLine();

            return true;
        } else {
            System.out.println(ColorTerminal.MAGENTA_BOLD + "\nHas cancel·lat l'abandonament. La partida continua." + ColorTerminal.RESET);

            System.out.println(ColorTerminal.MAGENTA + "\nPrem 'Enter' per continuar..." + ColorTerminal.RESET);
            sc.nextLine();
            return false;
        }
    }

    /**
     * Juga una partida.
     * Controla el flux principal d'una partida demanant accions al Jugador.
     *
     * @return false si la partida finalitza, true si continua.
     */
    private static boolean jugarPartida() {
        while (enPartida) {
            if (ctrl_partida.jugador_controladorID() != -1) {
                clearScreen();
                System.out.println(ColorTerminal.CYAN + "\n*********************************************************************************" + ColorTerminal.RESET);
                System.out.println(" És el torn del jugador: " + ctrl_partida.jugador_controladorNOM() + ".  Torn nº:" + ctrl_partida.controladorTORN() + "   " +
                        ctrl_partida.getPartida().getJugador1().getNom() + ": " + ctrl_partida.getPartida().getPuntuacioJugador1() + " punts.   " +
                        ctrl_partida.getPartida().getJugador2().getNom() + ": " + ctrl_partida.getPartida().getPuntuacioJugador2() + " punts.");
                System.out.println(ColorTerminal.CYAN + "*********************************************************************************\n" + ColorTerminal.RESET);
                ctrl_partida.imprimirTaulell();
                System.out.print("\n\nFitxes disponibles: [ ");
                for (Fitxa f : ctrl_partida.getLletresJugadorTorn()) {
                    System.out.print(f.getLletra() + "(" + f.getPuntuacio() + ") ");
                }
                System.out.println("] - El format que segueix és: lletra(puntuació).\n");
                mostrarOpcionsPartida();

                boolean validG = false;
                int opcio = -1;
                System.out.println("Tria una opció: ");
                while (!validG) {
                    if (sc.hasNextInt()) {
                        opcio = sc.nextInt();
                        validG = true;
                    } else {
                        System.out.println(ColorTerminal.RED + "\nError: No has introduït una xifra entera.\n" + ColorTerminal.RESET);
                        System.out.println("Tria una opció: ");
                        sc.next();
                    }
                }
                validG = false;
                sc.nextLine();

                switch (opcio) {
                    case 1:
                        if (colocarParaula()) {
                            ctrl_partida.incrementarTorn();
                            if (ctrl_partida.finalitzarPerLletresEsgotades()) {
                                System.out.println(ColorTerminal.GREEN + "La partida ha finalitzat per lletres esgotades tant en els jugadors com a la bossa." + ColorTerminal.RESET);
                                return false;
                            }
                        }
                        break;

                    case 2:
                        if (canviarFitxes()) {
                            if (ctrl_partida.finalitzarPerLletresEsgotades()) {
                                System.out.println(ColorTerminal.GREEN + "La partida ha finalitzat per lletres esgotades tant en els jugadors com a la bossa." + ColorTerminal.RESET);
                                return false;
                            }
                        }
                        else break;

                    case 3:
                        if (!passarTorn()) {
                            return false;
                        }
                        break;

                    case 4:
                        pausarPartida();
                        break;

                        case 5:
                        boolean haGuardat = guardarPartida();
                        if (haGuardat) {
                            enPartida = false;
                            return false;
                        } else {
                            break;
                        }

                    case 6:
                        boolean haAbandonat = abandonarPartida();
                        if (haAbandonat) {
                            enPartida = false;
                            return false;
                        } else {
                            break;
                        }

                    case 7:
                        consultarNormes();
                        break;

                    default:
                        System.out.println(ColorTerminal.RED + "Opció no vàlida. Introdueixi una opció vàlida:" + ColorTerminal.RESET);
                }
            } else {
                ctrl_partida.ferJugadaMaquina();
                ctrl_partida.incrementarTorn();
            }
        }

        return false;
    }

    /**
     * Mostra el menú de pausa.
     * Permet al jugador accedir a opcions mentre la partida està pausada.
     *
     * @return true si la partida es reanuda, false si es finalitza.
     */
    private static boolean mostrarMenuPausa() {
        boolean sortit = false;

        while (!sortit) {

            System.out.println(ColorTerminal.CYAN + "***********************************************" + ColorTerminal.RESET);
            System.out.println("Opcions disponibles per a reprendre la partida:");
            System.out.println(" ");
            System.out.println("[1] Reprendre Partida");
            System.out.println("[2] Guardar Partida");
            System.out.println("[3] Abandonar Partida");
            System.out.println(ColorTerminal.CYAN + "***********************************************" + ColorTerminal.RESET);

            int opcioPausa = -1;
            boolean valid = false;

            while (!valid) {
                if (sc.hasNextInt()) {
                    opcioPausa = sc.nextInt();
                    valid = true;
                } else {
                    System.out.println(ColorTerminal.RED + "\nOpció no vàlida. Introdueixi una opció vàlida: \n" + ColorTerminal.RESET);
                    sc.next();
                }
            }

            sc.nextLine();

            switch (opcioPausa) {
                case 1:
                    System.out.println(ColorTerminal.GREEN + "\nReprendre la partida...\n" + ColorTerminal.RESET);
                    reprendrePartida();
                    sortit = true;
                    break;

                case 2:
                boolean haGuardat2 = guardarPartida();
                System.out.println(" ");

                if (haGuardat2) {
                    enPartida = false;
                    return false;
                } else {
                    System.out
                            .println(ColorTerminal.GREEN + "\nTornant al menú de pausa...\n" + ColorTerminal.RESET);
                    break;
                }

            case 3:
                    boolean haAbandonat2 = abandonarPartida();
                    System.out.println(" ");

                    if (haAbandonat2) {
                        enPartida = false;
                        return false;
                    } else {
                        System.out.println(ColorTerminal.GREEN + "\nTornant al menú de pausa...\n" + ColorTerminal.RESET);
                        break;
                    }
                default:

                    System.out.println(ColorTerminal.RED + "\nOpció no vàlida. Introdueixi una opció vàlida: \n" + ColorTerminal.RESET);
            }
        }

        return true;
    }

    /**
     * Neteja la pantalla del terminal.
     * Depenent del sistema operatiu, executa l'ordre corresponent per netejar la
     * pantalla.
     */
    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println(ColorTerminal.RED + "Error al limpiar pantalla: " + e.getMessage() + ColorTerminal.RESET);
        }
    }
}
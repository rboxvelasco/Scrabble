package controladors;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import domini.jugadors.Avatar;
import domini.jugadors.Jugador;
import domini.scrabble.Fitxa;
import domini.scrabble.Partida;
import interficie.*;

/**
 * Representa el controlador de presentació.
 *
 * El controlador de presentació fa d'intermediari entres la GUI (Grafic User
 * Interface) amb la resta de controladors de l'aplicació.
 */
public class CtrlPresentacio {
    private static CtrlPresentacio instancia;
    private static CtrlDomini ctrlDomini;
    private static CtrlPartida ctrlPartida;
    private static JFrame vistaPrevia;
    private static JFrame vistaActual;

    /**
     * Retorna la instancia del controlador de domini.
     *
     * @return Ctrl_Domini
     */
    public static CtrlPresentacio getInstancia() {
        if (instancia == null)
            instancia = new CtrlPresentacio();
        return instancia;
    }

    /**
     * Inicialitza l'aplicació creant les instàncies del controlador de domini i de
     * partida,
     * i mostrant la vista principal del menú.
     */
    public void launchApp() {
        ctrlDomini = CtrlDomini.getInstancia();
        ctrlPartida = CtrlPartida.getInstancia(ctrlDomini);
        vistaActual = new VistaMenuPrincipal();
    }

    // === Gestió d'usuaris ===

    /**
     * Inicia una sessió amb l'usuari especificat.
     * Si l'autenticació és correcta, canvia la vista al menú del joc.
     *
     * @param nom         El nom d'usuari.
     * @param contrasenya La contrasenya de l'usuari.
     * @throws IOException Si hi ha un error durant l'accés a dades.
     * @return Un codi d'estat: 0 si l'inici de sessió ha tingut èxit, altre valor
     *         en cas d'error.
     */
    public static int iniciarSessio(String nom, String contrasenya) throws IOException {
        int codi = ctrlDomini.iniciarSessioUser(nom, contrasenya);

        // Cas d'èxit
        if (codi == 0) {
            vistaPrevia = vistaActual;

            if (vistaPrevia instanceof VistaIniciarSessio)
                vistaActual = new VistaMenuJoc((VistaIniciarSessio) vistaPrevia, null);
            else
                vistaActual = new VistaMenuJoc(null, (VistaCrearPerfil) vistaPrevia);
            vistaActual.setVisible(true);

            vistaPrevia.dispose();
        }

        return codi;
    }

    /**
     * Crea un nou perfil d'usuari amb el nom i la contrasenya proporcionats.
     *
     * @param nom         El nom d'usuari a crear.
     * @param contrasenya La contrasenya associada al nou usuari.
     * @return true si la creació ha estat satisfactòria; false en cas contrari.
     */
    public static boolean crearPerfil(String nom, String contrasenya) {
        return ctrlDomini.crearUsuari(nom, contrasenya);
    }

    /**
     * Elimina el perfil de l'usuari actiu.
     *
     * @return Cert si l'eliminació ha estat satisfactòria, fals en cas contrari.
     * @throws IOException Si es produeix un error d'entrada/sortida.
     */
    public static boolean eliminarPerfil() throws IOException {
        boolean eliminat = ctrlDomini.eliminarUsuari();

        if (eliminat) {
            vistaActual.dispose();
            vistaActual = new VistaMenuPrincipal();
            vistaActual.setVisible(true);
        }

        return eliminat;
    }

    /**
     * Tanca la sessió de l'usuari actual i retorna a la vista principal.
     */
    public static void tancarSessio() {
        try {
            ctrlDomini.tancarSessio();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vistaActual.dispose();
        if (vistaPrevia != null)
            vistaPrevia.dispose();
        vistaActual = new VistaMenuPrincipal();
        vistaActual.setVisible(true);
    }

    // === Gestió d'avatars ===
    /**
     * Crea un nou avatar amb el nom i la imatge especificats.
     *
     * @param nom        El nom de l'avatar.
     * @param rutaImatge La ruta de la imatge associada.
     * @return Cert si l'avatar s'ha creat correctament.
     * @throws IOException Si es produeix un error d'entrada/sortida.
     */
    public static boolean crearAvatar(String nom, String rutaImatge) throws IOException {
        boolean creat = ctrlDomini.crearJugador(nom, rutaImatge);
        if (creat) {
            vistaActual.dispose();
            vistaPrevia.setVisible(true);
            vistaActual = vistaPrevia;
        }
        return creat;
    }

    /**
     * Elimina un avatar del sistema.
     *
     * @param avatar_a_eliminar El nom de l'avatar a eliminar.
     * @return Cert si s'ha eliminat correctament, fals en cas contrari.
     */
    public static boolean eliminarAvatar(String avatar_a_eliminar) {
        return ctrlDomini.eliminarJugador(avatar_a_eliminar);
    }

    /**
     * Retorna el conjunt de jugadors registrats al sistema.
     *
     * @return Un mapa amb els identificadors i els jugadors corresponents.
     */
    public static HashMap<Integer, Jugador> getJugadorsRegistrats() {
        return ctrlDomini.getJugadorsRegistrats();
    }

    /**
     * Retorna el nom de l'usuari que té la sessió iniciada.
     *
     * @return El nom de l'usuari actiu.
     */
    public static String getNomUsuariActiu() {
        return ctrlDomini.getNomUsuariActiu();
    }

    // === Gestió de partides ===

    /**
     * Crea una nova partida amb els paràmetres indicats.
     *
     * @param nomJ1  Nom del jugador 1.
     * @param nomJ2  Nom del jugador 2.
     * @param idioma Idioma de la partida.
     * @param mida   Mida del tauler.
     * @return Cert si la partida s'ha creat correctament.
     */
    public static boolean crearPartida(String nomJ1, String nomJ2, String idioma, int mida) {
        boolean creada = ctrlDomini.CrearPartida(nomJ1, nomJ2, idioma, mida);

        if (creada) {
            vistaActual.dispose();
            vistaActual = new VistaPartida((VistaMenuJoc) vistaPrevia, idioma);
            vistaActual.setVisible(true);
        }

        return creada;
    }

    /**
     * Reprèn una partida existent identificada pel seu ID.
     *
     * @param idSeleccionat L'identificador de la partida a reprendre.
     */
    public static void reprendrePartida(int idSeleccionat) {
        ctrlDomini.reprendrePartida(idSeleccionat);

        vistaActual.dispose();
        String idioma = getPartida().getIdioma();

        vistaActual = new VistaPartida(vistaPrevia, idioma);
        vistaActual.setVisible(true);
    }

    /**
     * Passa el torn a l'altre jugador de la partida actual.
     *
     * @return Cert si el canvi de torn s'ha fet correctament.
     */
    public static boolean passarTornPartida() {
        return ctrlPartida.passarTorn();
    }

    /**
     * Canvia fitxes del jugador segons les especificacions donades.
     *
     * @param totesFitxes Llista de totes les fitxes disponibles.
     * @param num         Nombre de fitxes a canviar.
     * @param b           Booleà auxiliar per alguna lògica interna.
     * @param lletra1     Primera lletra seleccionada.
     * @param lletra2     Segona lletra seleccionada.
     * 
     * @return Codi que indica el resultat de l'operació.
     */
    public static int canviarFitxesPartida(List<String> totesFitxes, int num, boolean b, String lletra1, String lletra2) {
        return ctrlPartida.canviarFitxes(totesFitxes, num, b, lletra1, lletra2);
    }

    /**
     * Col·loca una paraula al tauler de la partida actual.
     *
     * @param palabra    Paraula a col·locar.
     * @param fila       Fila on comença la paraula.
     * @param columna    Folumna on comença la paraula.
     * @param horizontal Cert si la paraula s'ha de col·locar en horitzontal; fals
     *                   per vertical.
     * 
     * @return Codi de resultat de la jugada (0 si és vàlida, altres valors per
     *         errors).
     */
    public static int colocarParaulaPartida(String palabra, int fila, int columna, boolean horizontal) {
        return ctrlPartida.colocarParaula(palabra, fila, columna, horizontal);
    }

    /**
     * Abandona la partida actual en curs.
     */
    public static void abandonarPartida() {
        ctrlPartida.abandonarPartida();
    }

    /**
     * Finalitza la partida si ja no queden fitxes disponibles per continuar.
     *
     * @return Cert si la partida ha estat finalitzada per falta de fitxes.
     */
    public static boolean finalitzarPerLletresEsgotadesPartida() {
        return ctrlPartida.finalitzarPerLletresEsgotades();
    }

    /**
     * Desa l'estat actual de la partida en curs.
     *
     * @throws IOException Si es produeix un error d'entrada/sortida durant el
     *                     procés de desament.
     */
    public static void guardarPartida() throws IOException {
        ctrlDomini.guardarPartida();
    }

    /**
     * Executa una jugada automàtica per part de la màquina.
     */
    public static void ferJugadaMaquinaPartida() {
        ctrlPartida.ferJugadaMaquina();
    }

    /**
     * Canvia la vista a la pantalla de rànquing final un cop acabada la partida.
     * Es mostren els noms i puntuacions dels dos jugadors.
     */
    public static void canviRankingPartida() {
        vistaActual.dispose();

        String nomj1 = getPartida().getJugador1().getNom();
        String nomj2 = getPartida().getJugador2().getNom();
        int punts1 = getPartida().getPuntuacioJugador1();
        int punts2 = getPartida().getPuntuacioJugador2();

        vistaActual = new VistaRankingPartida(nomj1, nomj2, punts1, punts2);
        vistaActual.setVisible(true);
    }

    // === Gestió de vistes ===

    /**
     * Canvia la vista actual a la vista de creació de perfil.
     * Oculta la vista actual però no la tanca, i crea una nova instància de
     * {@link VistaCrearPerfil}.
     */
    public static void canviarCrearPerfil() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaCrearPerfil((VistaMenuPrincipal) vistaPrevia);
        vistaActual.setVisible(true); // Creem i mostrem Crear Perfil
    }

    /**
     * Canvia la vista actual a la vista d'inici de sessió.
     * Oculta la vista actual però no la tanca, i crea una nova instància de
     * {@link VistaIniciarSessio}.
     */
    public static void canviarIniciarSessio() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaIniciarSessio((VistaMenuPrincipal) vistaActual);
        vistaActual.setVisible(true); // Creem i mostrem Iniciar Sessió
    }

    /**
     * Canvia la vista actual a {@link VistaCrearAvatar}.
     * Oculta la vista actual i en mostra una nova per a crear l'avatar.
     */
    public static void canviarCrearAvatar() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaCrearAvatar((VistaMenuJoc) vistaPrevia);
        vistaActual.setVisible(true); // Creem i mostrem Crear Avatar
    }

    /**
     * Canvia la vista actual a {@link VistaConsultarAvatars}.
     * Oculta la vista actual i mostra una nova finestra amb els avatars
     * disponibles.
     */
    public static void canviarConsultarAvatars() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaConsultarAvatars((VistaMenuJoc) vistaPrevia);
        vistaActual.setVisible(true); // Creem i mostrem Crear Avatar
    }

    /**
     * Canvia la vista actual a {@link VistaConsultarEstadistiques}.
     * Oculta la vista actual i mostra una nova finestra amb les estadístiques de
     * l'usuari.
     */
    public static void canviarConsultarEstadistiques() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaConsultarEstadistiques((VistaMenuJoc) vistaPrevia);
        vistaActual.setVisible(true); // Creem i mostrem Crear Avatar
    }

    /**
     * Canvia la vista actual a {@link VistaConsultarRanking}.
     * Oculta la vista actual i mostra una nova finestra amb el rànquing de
     * jugadors.
     */
    public static void canviarConsultarRanking() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaConsultarRanking((VistaMenuJoc) vistaPrevia);
        vistaActual.setVisible(true); // Creem i mostrem Crear Avatar
    }

    /**
     * Canvia la vista actual a {@link VistaCrearPartida}.
     * Oculta la vista actual i mostra una nova finestra per a configurar una nova
     * partida.
     */
    public static void canviarCrearPartida() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        vistaActual = new VistaCrearPartida((VistaMenuJoc) vistaPrevia);
        vistaActual.setVisible(true); // Creem i mostrem Crear Avatar
    }

    /**
     * Canvia la vista des de {@link VistaIniciarSessio} a {@link VistaCrearPerfil}.
     * Elimina la vista actual i crea una nova instància de VistaCrearPerfil amb la
     * vista prèvia com a paràmetre.
     */
    public static void canviarCrearPerfilDesdeIniciarSessio() {
        vistaActual.dispose();
        vistaActual = new VistaCrearPerfil((VistaMenuPrincipal) vistaPrevia);
        vistaActual.setVisible(true);
    }

    /**
     * Canvia a la vista de canvi de torn entre jugadors.
     * Es mostra una finestra que indica quin jugador ha de jugar a continuació.
     */
    public static void canviarCanviTorn() {
        vistaActual.setVisible(false);
        Partida p = getPartida();
        new VistaCanviTorn(p.getJugadorActual(true).getNom(), p.getJugadorActual(false).getNom(), vistaActual);
    }

    /**
     * Tanca la sessió de l'usuari, allibera els recursos gràfics i finalitza
     * l'execució de l'aplicació.
     * Si no hi ha sessió iniciada o hi ha un error d'entrada/sortida, s'escriu
     * l'error a la consola.
     */
    public static void sortir() {
        try {
            ctrlDomini.tancarSessio();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vistaActual.dispose(); // Tanquem la finestra
        if (vistaPrevia != null)
            vistaPrevia.dispose();
        System.exit(0);
    }

    /**
     * Torna a la vista anterior a l'actual.
     * Es descarta la vista actual i es mostra la vista prèvia.
     */
    public static void tornar() {
        vistaActual.dispose();
        vistaActual = vistaPrevia;
        vistaActual.setVisible(true);
    }

    // === Auxiliars de dades de partida ===

    /**
     * Mostra la vista de consulta de normes del joc.
     * Oculta la vista actual però no la tanca. Depenent del context (menú principal
     * o joc),
     * s'instancia la vista corresponent.
     */
    public static void consultarNormes() {
        vistaPrevia = vistaActual;
        vistaPrevia.setVisible(false); // Ocultem la finestra, no la tanquem

        if (vistaPrevia instanceof VistaMenuPrincipal)
            vistaActual = new VistaConsultarNormes((VistaMenuPrincipal) vistaActual, null);
        else
            vistaActual = new VistaConsultarNormes(null, (VistaMenuJoc) vistaActual);
        vistaActual.setVisible(true); // Creem i mostrem normes
    }

    /**
     * Obté el rànquing global d'avatares limitat a un cert nombre.
     *
     * @param limit El nombre màxim d'elements a retornar.
     * @return Llista d'avatares ordenats pel rànquing.
     */
    public static List<Avatar> getRanking(int limit) {
        return CtrlDomini.getInstancia().getRankingGlobalLimitat(limit);
    }

    /**
     * Obté les fitxes del jugador al qual li toca el torn actual.
     *
     * @return Llista de fitxes del jugador en torn.
     */
    public static List<Fitxa> getLletresJugadorTornPartida() {
        return ctrlPartida.getLletresJugadorTorn();
    }

    /**
     * Torna a mostrar la vista actual de la partida després d’una acció temporal.
     */
    public static void tornarPartida() {
        vistaActual.setVisible(true);
    }

    /**
     * Retorna la instància de la partida actual en curs.
     *
     * @return L'objecte Partida actiu.
     */
    public static Partida getPartida() {
        return ctrlPartida.getPartida();
    }
}
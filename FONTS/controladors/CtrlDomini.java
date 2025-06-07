package controladors;

import domini.jugadors.*;
import domini.scrabble.Partida;
import domini.scrabble.Ranking;
import domini.sessio.Usuari;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Representa el controlador de domini.
 *
 * El controlador de domini té un conjunt de partides i ranquings.
 * També s'encarrega de controlar les classes i gestionar la lògica general del
 * joc.
 */
public class CtrlDomini {
    private static CtrlDomini instancia;
    private HashMap<Integer, Partida> partides; // la ultima sera la partida actual
    private HashMap<Integer, Jugador> jugadors_registrats;
    private CtrlPartida CtrlPartida;
    private Ranking ranking;
    private HashMap<String, Usuari> usuarisRegistrats;
    private Usuari usuariActiu;

    /**
     * Constructor de la classe Ctrl_Domini.
     * 
     * @throws IOException Si hi ha un error en inicialitzar els usuaris.
     */
    private CtrlDomini() throws IOException {
        partides = new HashMap<>();
        jugadors_registrats = new HashMap<>();
        this.ranking = new Ranking(this.jugadors_registrats);
        this.CtrlPartida = CtrlPartida.getInstancia(this);
        this.usuarisRegistrats = new HashMap<>();
        inicialitzarUsuaris();
    }

    /**
     * Retorna la instancia del controlador de domini.
     *
     * @return Ctrl_Domini
     */
    public static CtrlDomini getInstancia() {
        if (instancia == null) {
            try {
                instancia = new CtrlDomini();
            } catch (IOException e) {
                e.printStackTrace(); // O gestionar l'excepció d'una altra manera
            }
        }
        return instancia;
    }

    /**
     * Crear Partida.
     *
     * Crea una partida a partir del jugadors i el idioma seleccionat.
     *
     * @param nomJugador1  Identificador del jugador 1.
     * @param nomJugador2  Identificador del jugador 2.
     * @param idioma       Idioma de la partida.
     * @param midaTaulell  Mida del taulell.
     *
     * @return true si la partida s'ha creat correctament, false altrament.
     */
    public boolean CrearPartida(String nomJugador1, String nomJugador2, String idioma, int midaTaulell) {
        Integer idJugador1;
        Integer idJugador2;

        boolean contraMaquina = nomJugador2.equals("MAQUINA");

        idJugador1 = obtenirIdJugador(nomJugador1);

        if (contraMaquina) {
            idJugador2 = -1;
        } else {
            idJugador2 = obtenirIdJugador(nomJugador2);
        }

        if (idJugador1 == null || idJugador2 == null) return false;

        Jugador jugador1;
        Jugador jugador2;

        if (contraMaquina) {
            jugador1 = jugadors_registrats.get(idJugador1);
            jugador2 = new Maquina();
            if (Math.random() < 0.5) {
                Jugador temp = jugador1;
                jugador1 = jugador2;
                jugador2 = temp;
            }
        } else {
            Jugador j1 = jugadors_registrats.get(idJugador1);
            Jugador j2 = jugadors_registrats.get(idJugador2);
            if (Math.random() < 0.5) {
                jugador1 = j1;
                jugador2 = j2;
            } else {
                jugador1 = j2;
                jugador2 = j1;
            }
        }

        int idPartida = obtenirNouIdPartida();

        try {
            Partida novaPartida = CtrlPartida.crearPartida(jugador1, jugador2, idioma, midaTaulell, idPartida);
            partides.put(idPartida, novaPartida);
            ranking.iniciarPartida(idJugador1, idJugador2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Obté una partida per ID
     *
     * @param idPartida Identificador de la partida
     * @return Partida sol·licitada o null si no existeix
     */
    public Partida getPartida(int idPartida) {
        return partides.get(idPartida);
    }

    /**
     * Obté tots els IDs de partides existents
     *
     * @return Conjunt immutable d'IDs
     */
    public Set<Integer> getIdsPartides() {
        return Collections.unmodifiableSet(partides.keySet());
    }

    /**
     * Actualitza el rànquing després d'una partida.
     *
     * @param idPartida     ID de la partida acabada
     * @param jugador1      Jugador1 de la partida
     * @param punt_jugador1 Punts assolits per Jugador1
     * @param jugador2      Jugador2 de la partida
     * @param punt_jugador2 Punts assolits per Jugador2
     */
    public void actualitzarRanking(int idPartida, Jugador jugador1, int punt_jugador1, Jugador jugador2, int punt_jugador2) {
        Partida partida = partides.get(idPartida);
        if (partida == null) {
            return;
        }

        if (ranking.estaBuit()) {
            int idJ1 = jugador1.getIdJugador();
            int idJ2 = jugador2.getIdJugador();
            ranking.iniciarPartida(idJ1, idJ2);
        }

        ranking.actualitzarPuntsPartidaLocal(jugador1.getIdJugador(), punt_jugador1);
        ranking.actualitzarPuntsPartidaLocal(jugador2.getIdJugador(), punt_jugador2);

        if (esPartidaContraMaquina(jugador1, jugador2)) {
            processarPartidaMaquina(jugador1, jugador2);
            return;
        }

        if (!jugadorsRegistratsValids(jugador1, jugador2))
            return;

        actualitzarDadesGlobals(jugador1, jugador2, punt_jugador1, punt_jugador2);
    }

    /**
     * Comprova si la partida és contra la màquina.
     *
     * @param guanyador Jugador guanyador.
     * @param perdedor  Jugador perdedor.
     * @return true si és una partida contra la màquina, false altrament.
     */
    private boolean esPartidaContraMaquina(Jugador guanyador, Jugador perdedor) {
        return guanyador.getIdJugador() == -1 || perdedor.getIdJugador() == -1;
    }

    /**
     * Processa els resultats d'una partida contra la màquina.
     *
     * @param guanyador Jugador guanyador.
     * @param perdedor  Jugador perdedor.
     */
    private void processarPartidaMaquina(Jugador guanyador, Jugador perdedor) {
        Jugador jugadorHumà = (guanyador.getIdJugador() != -1) ? guanyador : perdedor;
        Jugador maquina = (guanyador.getIdJugador() == -1) ? guanyador : perdedor;

        if (jugadorHumà instanceof Avatar) {
            Avatar avatar = (Avatar) jugadorHumà;
            avatar.incrementarPartidesJugadesMaquina();
            if (jugadorHumà.getPuntuacio_actual() > maquina.getPuntuacio_actual()) {
                avatar.incrementarPartidesGuanyadesMaquina();
            }
        }
    }

    /**
     * Comprova que els jugadors registrats siguin valids.
     * 
     * @param guanyador Jugador guanyador.
     * @param perdedor  Jugador perdedor.
     * @return true si són valids, false altrament.
     */
    private boolean jugadorsRegistratsValids(Jugador guanyador, Jugador perdedor) {
        return jugadors_registrats.containsKey(guanyador.getIdJugador()) &&
                jugadors_registrats.containsKey(perdedor.getIdJugador());
    }

    /**
     * Actualitza les dades globals dels jugadors després d'una partida.
     *
     * @param jugador1      Jugador1 de la partida
     * @param jugador2      Jugador2 de la partida
     * @param punt_jugador1 Punts assolits per Jugador1
     * @param punt_jugador2 Punts assolits per Jugador2
     */
    private void actualitzarDadesGlobals(Jugador jugador1, Jugador jugador2, int punt_jugador1, int punt_jugador2) {
        actualitzarJugadorGlobal(jugador1, punt_jugador1);
        actualitzarJugadorGlobal(jugador2, punt_jugador2);

        Jugador jugadorReg1 = jugadors_registrats.get(jugador1.getIdJugador());
        Jugador jugadorReg2 = jugadors_registrats.get(jugador2.getIdJugador());

        if (jugadorReg1 != null && jugadorReg2 != null) {
            jugadorReg1.incrementar_puntuacio_actual(punt_jugador1);
            jugadorReg2.incrementar_puntuacio_actual(punt_jugador2);
        }
    }

    /**
     * Actualitza les dades globals d'un jugador.
     *
     * @param jugador El jugador a actualitzar.
     * @param punt_jugador Punts assolits pel jugador.
     */
    private void actualitzarJugadorGlobal(Jugador jugador, int punt_jugador) {
        if (jugador.getIdJugador() != -1) {
            ranking.actualitzarPuntsPartidaGlobal(jugador.getIdJugador(), punt_jugador);
        }
    }


    /**
     * Obté tots els jugadors registrats.
     *
     * @return Un HashMap emb els jugadors registrats.
     */
    public HashMap<Integer, Jugador> getJugadorsRegistrats() {
        return jugadors_registrats;
    }

    /**
     * Obté el ranking del numero de jugadors indicat
     *
     * @param limit Número de jugadors que vols veure al ranking
     * @return Llista amb el ranking dels jugadors
     */
    public List<Avatar> getRankingGlobalLimitat(int limit) {
        return ranking.getRankingGlobalLimitat(limit);
    }

    /**
     * Obté l'identificador d'un jugador registrat.
     *
     * @param nom         Nom del jugador.
     * @return L'ID del jugador si es troba, o null si no coincideix cap jugador.
     */
    public Integer obtenirIdJugador(String nom) {
        for (Jugador jugador : jugadors_registrats.values()) {
            if (jugador.getNom().equals(nom)) {
                return jugador.getIdJugador();
            }
        }
        return null;
    }


    /**
     * Comprova si existeix jugador.
     *
     * Comprova que existeixi un jugador registrat al sistema amb el nom del
     * paràmetre.
     *
     * @param nom Nom del jugador a comprovar.
     * @return true si el jugador existeix al sistema, false altrament.
     */
    public boolean existeixJugador(String nom) {
        for (Jugador jugador : jugadors_registrats.values()) {
            if (jugador.getNom().equalsIgnoreCase(nom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea un usuari.
     *
     * @param nom Nom de l'usuari a crear.
     * @param contrasenya Contrasenya de l'usuari a crear.
     * @return true si l'usuari s'ha creat correctament, false altrament.
     */
    public boolean crearUsuari(String nom, String contrasenya) {
        if (usuarisRegistrats.containsKey(nom)) {
            return false;
        }

        Usuari nouUsuari = new Usuari(nom, contrasenya);
        usuarisRegistrats.put(nom, nouUsuari);

        try {
            CtrlPersistencia.guardarUsuari(nouUsuari);
        } catch (IOException e) {
        }

        return true;
    }

    /**
     * Carrega els usuaris cada cop que executem el joc.
     *
     * @throws IOException Si hi ha un error en carregar els usuaris.
     */
    public void inicialitzarUsuaris() throws IOException {
        // Obtenim el map des de CtrlPersistencia
        Map<String, Usuari> carregats = CtrlPersistencia.carregarUsuarisRegistrats();
        // Reemplaçar el contingut del HashMap global
        usuarisRegistrats.clear();
        usuarisRegistrats.putAll(carregats);
    }


    /**
     * Inicia la sessio d'un usuari.
     *
     * @param nomUsuari Nom de l'usuari a iniciar sessio.
     * @param contrasenya Contrasenya de l'usuari a iniciar sessio.
     * 
     * @throws IOException Si hi ha un error en iniciar la sessió de l'usuari.
     * @return 0 si s'ha iniciat correctament, 1 si l'usuari no existeix, 2 si la contrasenya 
     * es incorrecta.
     */
    public int iniciarSessioUser(String nomUsuari, String contrasenya) throws IOException {
        Usuari usuari = usuarisRegistrats.get(nomUsuari);
        if (usuari == null)
            return 1;
        else if (!usuari.getContrasenya().equals(contrasenya))
            return 2;
        else {
            usuariActiu = usuari;

            jugadors_registrats.clear();
            for (Jugador j : usuari.getJugadors()) {
                jugadors_registrats.put(j.getIdJugador(), j);
            }

            partides.clear();
            for (Partida p : usuari.getPartides()) {
                partides.put(p.getIdPartida(), p);
            }

            ranking = new Ranking(jugadors_registrats);

            return 0;
        }
    }

    /**
     * Elimina un usuari.
     *
     *@throws IOException Si hi ha un error en eliminar l'usuari.
     * @return true si l'usuari s'ha eliminat correctament, false altrament.
     */
    public boolean eliminarUsuari() throws IOException {
        if (usuariActiu == null) {
            return false;
        }

        String nomUsuari = usuariActiu.getNom();

        try {
            CtrlPersistencia.eliminarUsuari(nomUsuari);
        } catch (IOException e) {
        }

        jugadors_registrats.clear();

        for (Jugador j : usuariActiu.getJugadors()) {
            int id = j.getIdJugador();
            ranking.eliminarJugador(id);

            try {
                CtrlPersistencia.eliminarJugador(id, usuariActiu.getNom());
            } catch (IOException e) {
            }
        }

        usuarisRegistrats.remove(nomUsuari);
        usuariActiu = null;
        return true;
    }


    /**
     * Assigna un id a un jugador.
     * 
     * @return Torna l'Id del jugador.
     */
    private int obtenirNouIdJugador() {
        int maxId = -1;
        for (int id : jugadors_registrats.keySet()) {
            if (id > maxId)
                maxId = id;
        }
        return maxId + 1;
    }

    /**
     * Assigna un id a una partida.
     * 
     * @return Torna l'Id de la partida.
     */
    private int obtenirNouIdPartida() {
        int maxId = -1;
        for (int id : partides.keySet()) {
            if (id > maxId)
                maxId = id;
        }
        return maxId + 1;
    }

    /**
     * Crea un jugador associat a l'usuari actiu.
     *
     * @param nomJugador Nom del jugador a crear.
     * @param rutaImatge Ruta on es troba la imatge del jugador a crear.
     *
     * @throws IOException Si hi ha un error en guardar el jugador.
     * @return true si s'ha creat correctament, false en cas contrari.
     */
    public boolean crearJugador(String nomJugador, String rutaImatge) throws IOException {
        for (Jugador j : usuariActiu.getJugadors()) {
            if (j.getNom().equals(nomJugador)) {
                return false;
            }
        }

        int nouId = obtenirNouIdJugador();
        Jugador nouJugador = new Avatar(nouId, nomJugador, rutaImatge);
        usuariActiu.afegirJugador(nouJugador);
        jugadors_registrats.put(nouId, nouJugador);

        try {
            CtrlPersistencia.guardarUsuari(usuariActiu);
        } catch (IOException e) {
        }
        try {
            CtrlPersistencia.guardarJugador(nouJugador, usuariActiu.getNom());
        } catch (IOException e) {
        }

        return true;
    }

    /**
     * Elimina un jugador registrat del sistema.
     *
     * Elimina el jugador tant del registre com del rànquing.
     *
     * @param nom         Nom del jugador a eliminar.
     * @return true si el jugador ha estat eliminat correctament, false altrament.
     */
    public boolean eliminarJugador(String nom) {
        Integer id = obtenirIdJugador(nom);
        if (id != null) {
            jugadors_registrats.remove(id);
            ranking.eliminarJugador(id);

            try {
                CtrlPersistencia.eliminarJugador(id, usuariActiu.getNom()); // eliminar el fitxer JSON
            } catch (IOException e) {
                return false;
            }

            if (usuariActiu != null) {
                usuariActiu.eliminarJugadorPerId(id);
                try {
                    CtrlPersistencia.guardarUsuari(usuariActiu);
                } catch (IOException e) {
                }
            }

            return true;
        }

        return false;
    }
    
    /**
     * Es tanca la sessió de l'usuari.
     *
     * @throws IOException Si hi ha un error en tancar la sessió.
     */
    public void tancarSessio() throws IOException {
        for (Jugador j : jugadors_registrats.values()) {
            CtrlPersistencia.guardarJugador(j, usuariActiu.getNom());
        }
        usuariActiu = null;
        jugadors_registrats.clear();
    }

    /**
     * Es guarda la partida actual.
     *
     * @throws IOException Si hi ha un error en guardar la partida.
     */
    public void guardarPartida() throws IOException {
        CtrlPersistencia.guardarPartida(CtrlPartida.getPartida(), usuariActiu.getNom());
        usuariActiu.afegirPartida(CtrlPartida.getPartida());
        CtrlPersistencia.guardarUsuari(usuariActiu);
    }

    /**
     * Continuar partida.
     *
     * Continua la partida que s'havia aturat.
     *
     * @param idPartida Id de la partida a reprendre.
     */
    public void reprendrePartida(int idPartida) {
        try {
            Partida partida = partides.get(idPartida);
            if (partida == null) {
                partida = CtrlPersistencia.carregarPartida(idPartida, usuariActiu.getNom());
                if (partida == null) {
                    return;
                }
            }

            usuariActiu.eliminarPartidaPerId(idPartida);
            partides.remove(idPartida);
            partida.encurs();
            CtrlPartida.carregarPartida(partida);

            try {
                CtrlPersistencia.eliminarPartida(idPartida, usuariActiu.getNom()); // eliminar el fitxer JSON
                CtrlPersistencia.guardarUsuari(usuariActiu);

            } catch (IOException e) {
            }
        } catch (IOException e) {
        }
    }

    /**
     * Elimina el .json de la partida indicada al paràmetre.
     *
     * @param idPartida L'id de la partida a eliminar
     */
    public void eliminarPartida(int idPartida) {
        try {
            CtrlPersistencia.eliminarPartida(idPartida, usuariActiu.getNom());
            partides.remove(idPartida);
        } catch (IOException e) {
        }
    }

    /**
     * Obté el nom de l'usuari que ha iniciat sessió a l'aplicació.
     * 
     * @return  El nom d l'usuari actiu o null si no n'hi ha cap.
     */
    public String getNomUsuariActiu() {
        if (usuariActiu != null) {
            return usuariActiu.getNom();
        }
        return null;
    }
}
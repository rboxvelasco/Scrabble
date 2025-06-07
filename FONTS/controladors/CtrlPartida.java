package controladors;

import domini.scrabble.*;
import domini.diccionari.*;
import domini.excepcions.ExcepcioArxiuDiccionari;
import domini.excepcions.ExcepcioDomini;
import domini.jugadors.*;
import domini.auxiliars.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Classe que representa el controlador d'una partida de Scrabble.
 */
public class CtrlPartida {
    private Partida partida;
    private DAWG diccionari;
    Jugador jugador1;
    Jugador jugador2;
    int mida;
    String idioma;
    private static CtrlDomini CtrlDomini;
    private static CtrlPartida instancia = null;
    private static final Set<String> DIGRAFS = Set.of("RR", "LL", "NY", "CH", "L·L");
    String lletra;
    String lletra2;
    List<String> fitxesRack;

    /**
     * Constructor del controlador de la partida.
     * El relaciona amb la instància de Controlador de Domini que l'ha creat
     *
     * @param CtrlDomini Instància de Controlador de Domini que fa la crida.
     * @throws IOException Si hi ha un error en carregar el diccionari.
     */
    public CtrlPartida(CtrlDomini CtrlDomini) throws IOException {
        this.CtrlDomini = CtrlDomini;
    }

    /**
     * Obté la instància única del controlador de partida.
     *
     * @param CtrlDomini Instància del controlador de domini.
     * @return La instància única de CtrlPartida.
     */
    public static CtrlPartida getInstancia(CtrlDomini CtrlDomini) {
        if (instancia == null) {
            try {
                instancia = new CtrlPartida(CtrlDomini);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instancia;
    }

    /**
     * Crea una nova partida.
     *
     * @param jugador1  El primer jugador.
     * @param jugador2  El segon jugador.
     * @param idioma    L'idioma del diccionari.
     * @param mida      La mida del taulell.
     * @param idPartida L'identificador de la partida.
     *
     * @throws IOException Si hi ha un error en carregar el diccionari.
     * @return La partida creada.
     */
    public Partida crearPartida(Jugador jugador1, Jugador jugador2, String idioma, int mida, int idPartida)
            throws IOException {
        this.partida = new Partida(jugador1, jugador2, mida, idioma, idPartida);
        this.diccionari = new DAWG();

        try {
            String fitxer = getDictionaryFileName(idioma);
            this.diccionari.dictionary2DAWG(fitxer);
        } catch (ExcepcioArxiuDiccionari e) {
            e.printStackTrace();
            System.exit(0);
        } catch (ExcepcioDomini e) {
            e.printStackTrace();
        }

        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.lletra = "";
        this.lletra2 = "";
        fitxesRack = new ArrayList<>();
        partida.inicialitzarJugadors();
        return partida;
    }

    /**
     * Carrega una partida existent.
     *
     * @param partida La partida a carregar.
     */
    public void carregarPartida(Partida partida) {
        this.partida = partida;
        this.diccionari = new DAWG();
        String idioma = partida.getIdioma();
        try {
            String fitxer = getDictionaryFileName(idioma);
            this.diccionari.dictionary2DAWG(fitxer);
        } catch (ExcepcioArxiuDiccionari e) {
            e.printStackTrace();
            System.exit(0);
        } catch (ExcepcioDomini e) {
            e.printStackTrace();
        }
        this.jugador1 = partida.getJugador1();
        this.jugador2 = partida.getJugador2();
    }

    /**
     * Obté el nom del fitxer del diccionari segons l'idioma.
     *
     * @param idioma L'idioma seleccionat.
     * @return El nom del fitxer del diccionari.
     */
    private String getDictionaryFileName(String idioma) {
        switch (idioma) {
            case "1":
                return "catalan.txt";
            case "2":
                return "castellano.txt";
            case "3":
                return "english.txt";
            default:
                throw new IllegalArgumentException("Idioma no suportat: " + idioma);
        }
    }

    /**
     * Gestiona el desenvolupament de la jugada de la màquina.
     */
    public void ferJugadaMaquina() {
        Jugador j_actual = partida.getJugadorActual(false);

        if (j_actual.getIdJugador() == -1) {

            int canviar_fitxes = partida.ferJugadaMaquina(diccionari);
            if (canviar_fitxes == 0) {
                List<Integer> rands = new ArrayList<Integer>();
                for (int i = 0; i < j_actual.getFitxes_actuals().size() && rands.size() < 3; ++i) {
                    Random rand = new Random();
                    int numero = rand.nextInt(7);
                    if (!rands.contains(rand))
                        rands.add(numero);
                }
                List<String> fitxes_a_canviar = new ArrayList<String>();
                for (int r : rands) {
                    fitxes_a_canviar.add(j_actual.getFitxes_actuals().get(r).getLletra());
                }

                if (canviarFitxes(fitxes_a_canviar, fitxes_a_canviar.size(), false, "", "") != 0) {
                    passarTorn();
                } else {
                    partida.incrementarTorn();
                }
            } else {
                partida.incrementarTorn();
            }
        }
    }

    /**
     * Finalitza la partida actual.
     */
    public void finalitzarCt() {
        partida.finalitzar();
    }

    /**
     * Pausa la partida actual.
     */
    public void pausarCt() {
        partida.pausar();
    }

    /**
     * Reprén la partida actual.
     */
    public void encursCt() {
        partida.encurs();
    }

    /**
     * Col·loca una paraula al taulell.
     *
     * Aquesta funció s'encarrega de validar i inserir una paraula al taulell,
     * comprovant que les fitxes utilitzades estiguin disponibles al rack del
     * jugador o al taulell, que la paraula sigui vàlida segons el diccionari i les
     * regles del joc, i actualitza la puntuació del jugador. També gestiona l'ús de
     * comodins i l'actualització de les estadístiques del jugador.
     *
     * Codis de retorn:
     * - 0: Paraula col·locada correctament.
     * - 1: La paraula se surt del taulell.
     * - 2: La paraula no existeix al diccionari.
     * - 3: Alguna lletra no està ni al rack ni al taulell.
     * - 4: La paraula no passa pel centre en el primer torn.
     * - 5: La paraula no toca cap fitxa existent.
     * - 6: Es forma una paraula transversal invàlida.
     * - 7: Error en eliminar fitxes del rack.
     * - 8: No hi ha prou fitxes a la bossa.
     * - 9: Hi ha conflicte amb una fitxa ja col·locada al taulell.
     * - 10: Es forma una paraula principal invàlida.
     *
     * @param paraulaStr  La paraula a col·locar (com a string).
     * @param fila        La fila inicial on es col·locarà la paraula.
     * @param columna     La columna inicial on es col·locarà la paraula.
     * @param horitzontal Indica si la paraula es col·loca horitzontalment (true) o
     *                    verticalment (false).
     * @return Un codi d'error segons el resultat de la col·locació.
     */
    public int colocarParaula(String paraulaStr, int fila, int columna, boolean horitzontal) {
        int mida_paraula;

        List<String> tokensParaula = tokenize(paraulaStr);
        List<Fitxa> paraulaCompleta = new ArrayList<>();

        for (int i = 0; i < tokensParaula.size(); i++) {
            String lletra = tokensParaula.get(i);
            int puntuacio = getBossa1().obtenirPuntuacio(lletra);
            int idFitxa = getBossa1().getIdFitxa(lletra);

            paraulaCompleta.add(new Fitxa(idFitxa, lletra, puntuacio));
        }
        Jugador jugadorActual = partida.getJugadorActual(false);
        Taulell taulell_actual = partida.getTaulell();
        List<String> fitxesRack = new ArrayList<>();

        // Contindrà les fitxes de la paraula formada que agafem del rack
        List<Fitxa> fitxes_jugador = jugadorActual.getFitxes_actuals();
        String lletra = "";
        String lletra2 = "";

        // Bucle per inicialitzar fitxes_jugador
        for (int i = 0; i < paraulaCompleta.size(); ++i) {
            // comprovem si la lletra està al rack
            int j = 0;
            boolean esta_al_rack = false;

            while (!esta_al_rack && j < fitxes_jugador.size()) {
                if (paraulaCompleta.get(i).getLletra().equals(fitxes_jugador.get(j).getLletra())) {
                    esta_al_rack = true;
                } else if (fitxes_jugador.get(j).getLletra().equals("#")) {
                    esta_al_rack = true;
                    lletra = paraulaCompleta.get(i).getLletra();
                    if (!lletra.equals( ""))
                        lletra2 = fitxes_jugador.get(j).getLletra();
                } else
                    ++j;
            }

            // Comprovem si la lletra està al taulell
            boolean esta_al_taulell = false;
            if (comprova_paraula(paraulaCompleta, fila, columna, horitzontal) != 1) {
                if (horitzontal) {
                    boolean casella_ocupada = taulell_actual.getCasella(fila, columna + i).EstaOcupat();
                    if (casella_ocupada)
                        esta_al_taulell = paraulaCompleta.get(i).getLletra()
                                .equals(taulell_actual.getCasella(fila, columna + i).getFitxa().getLletra());

                } else {
                    boolean casella_ocupada = taulell_actual.getCasella(fila + i,
                            columna).EstaOcupat();
                    if (casella_ocupada)
                        esta_al_taulell = paraulaCompleta.get(i).getLletra()
                                .equals(taulell_actual.getCasella(fila + i, columna).getFitxa().getLletra());
                }

                if (esta_al_rack && !esta_al_taulell) {
                    fitxesRack.add(paraulaCompleta.get(i).getLletra());
                } else if (!esta_al_taulell && !esta_al_rack)
                    return 3;
            }
        }

        int codi = comprova_paraula(paraulaCompleta, fila, columna, horitzontal);
        if (codi == 0) {
            jugadorActual = partida.getJugadorActual(false);
            taulell_actual = partida.getTaulell();
            fitxesRack = new ArrayList<>();

            // Contindrà les fitxes de la paraula formada que agafem del rack
            fitxes_jugador = jugadorActual.getFitxes_actuals();
            lletra = "";
            lletra2 = "";

            // Bucle per inicialitzar fitxes_jugador
            for (int i = 0; i < paraulaCompleta.size(); ++i) {
                // comprovem si la lletra està al rack
                int j = 0;
                boolean esta_al_rack = false;

                while (!esta_al_rack && j < fitxes_jugador.size()) {
                    if (paraulaCompleta.get(i).getLletra().equals(fitxes_jugador.get(j).getLletra())) {
                        esta_al_rack = true;
                    } else if (fitxes_jugador.get(j).getLletra().equals("#")) {
                        esta_al_rack = true;
                        lletra = paraulaCompleta.get(i).getLletra();
                        if (!lletra.equals(""))
                            lletra2 = fitxes_jugador.get(j).getLletra();
                    } else
                        ++j;
                }

                // Comprovem si la lletra està al taulell
                boolean esta_al_taulell = false;

                if (horitzontal) {
                    boolean casella_ocupada = taulell_actual.getCasella(fila, columna +
                            i).EstaOcupat();
                    if (casella_ocupada)
                        esta_al_taulell = paraulaCompleta.get(i).getLletra()
                                .equals(taulell_actual.getCasella(fila, columna + i).getFitxa().getLletra());

                } else {
                    boolean casella_ocupada = taulell_actual.getCasella(fila + i,
                            columna).EstaOcupat();
                    if (casella_ocupada)
                        esta_al_taulell = paraulaCompleta.get(i).getLletra()
                                .equals(taulell_actual.getCasella(fila + i, columna).getFitxa().getLletra());
                }

                if (esta_al_rack && !esta_al_taulell) {
                    fitxesRack.add(paraulaCompleta.get(i).getLletra());
                } else if (!esta_al_taulell && !esta_al_rack)
                    return 3;
            }

            int puntuacio = partida.getTaulell().inserirParaula(fila, columna, horitzontal, paraulaCompleta, lletra,
                    lletra2);
            partida.incrementar_puntuacio_partida(puntuacio, jugadorActual);

            String paraula = "";
            for (int i = 0; i < paraulaCompleta.size(); ++i) {
                paraula = paraula + paraulaCompleta.get(i).getLletra();
            }

            Jugador j = partida.getJugadorActual(false);
            if (j.getIdJugador() != -1) {
                Avatar avatar = (Avatar) j;
                avatar.actualitzarPuntuacioMaxParaula(puntuacio);
                avatar.setParaulaMesLlarga(paraula);
            }
            mida_paraula = fitxesRack.size();

            // 6. Eliminar fitxes del jugador
            int codi_canviar = canviarFitxes(fitxesRack, fitxesRack.size(), false, lletra, lletra2);
            if (codi_canviar != 0)
                return codi_canviar;
        }

        else
            return codi;

        if (mida_paraula == 7) {
            partida.incrementar_puntuacio_partida(50, jugadorActual);
        }

        jugadorActual.reiniciarTornsPassatsConsecutius();
        return 0;
    }

    /**
     * Comprova si una paraula és vàlida.
     *
     * Codis de retorn:
     * - 0: Paraula vàlida.
     * - 1: La paraula se surt del taulell.
     * - 2: La paraula no existeix al diccionari.
     * - 3: Alguna lletra no està ni al rack ni al taulell.
     * - 4: La paraula no passa pel centre en el primer torn.
     * - 5: La paraula no toca cap fitxa existent.
     * - 6: Es forma una paraula transversal invàlida.
     * - 9: Hi ha conflicte amb una fitxa ja col·locada al taulell.
     * - 10: Es forma una paraula principal invàlida.
     *
     * @param paraulaCompleta Les fitxes que formen la paraula.
     * @param fila            La fila inicial.
     * @param columna         La columna inicial.
     * @param horitzontal     Indica si la paraula es col·loca horitzontalment
     *                        (true) o verticalment (false).
     * @return Un codi d'error segons el resultat de la comprovació.
     */
    private int comprova_paraula(List<Fitxa> paraulaCompleta, int fila, int columna, boolean horitzontal) {

        Jugador jugadorActual = partida.getJugadorActual(false);
        Taulell taulell_actual = partida.getTaulell();

        String paraula = "";
        for (int i = 0; i < paraulaCompleta.size(); ++i) {
            paraula = paraula + paraulaCompleta.get(i).getLletra();
        }

        List<Fitxa> fitxes_jugador = new ArrayList<Fitxa>();
        fitxes_jugador = jugadorActual.getFitxes_actuals();

        // 1. Paraula no se surt del taulell
        if (!posicioCorrecta(paraulaCompleta, fila, columna, horitzontal, taulell_actual)) {
            return 1;
        }

        // 2. Paraula està al diccionari?
        if (!diccionari.contains(paraula)) {
            return 2;
        }

        List<String> fitxesRack = new ArrayList<>();

        // Bucle per inicialitzar fitxes_jugador
        for (int i = 0; i < paraulaCompleta.size(); ++i) {
            // comprovem si la lletra està al rack
            int j = 0;
            boolean esta_al_rack = false;

            while (!esta_al_rack && j < fitxes_jugador.size()) {
                if (paraulaCompleta.get(i).getLletra().equals(fitxes_jugador.get(j).getLletra())) {
                    esta_al_rack = true;
                } else if (fitxes_jugador.get(j).getLletra().equals("#")) {
                    esta_al_rack = true;
                    lletra = paraulaCompleta.get(i).getLletra();
                    if (!lletra.equals(""))
                        lletra2 = fitxes_jugador.get(j).getLletra();
                } else
                    ++j;
            }

            // Comprovem si la lletra està al taulell
            boolean esta_al_taulell = false;

            if (horitzontal) {
                boolean casella_ocupada = taulell_actual.getCasella(fila, columna + i).EstaOcupat();
                if (casella_ocupada)
                    esta_al_taulell = paraulaCompleta.get(i).getLletra()
                            .equals(taulell_actual.getCasella(fila, columna + i).getFitxa().getLletra());

            } else {
                boolean casella_ocupada = taulell_actual.getCasella(fila + i, columna).EstaOcupat();
                if (casella_ocupada)
                    esta_al_taulell = paraulaCompleta.get(i).getLletra()
                            .equals(taulell_actual.getCasella(fila + i, columna).getFitxa().getLletra());
            }

            if (esta_al_rack && !esta_al_taulell) {
                fitxesRack.add(paraulaCompleta.get(i).getLletra());
            }
        }

        // 3. Paraula ha d'estar (taulell || rack) + guardar fitxesRack
        for (int i = 0; i < paraulaCompleta.size(); ++i) {
            int j = 0;
            boolean esta_al_rack = false;
            boolean esta_al_taulell = false;

            if (horitzontal) {
                // Comprovem si la fitxa està en la casella
                boolean casella_ocupada = taulell_actual.getCasella(fila, columna + i).EstaOcupat();
                if (casella_ocupada) {
                    esta_al_taulell = paraulaCompleta.get(i).getLletra()
                            .equals(taulell_actual.getCasella(fila, columna + i).getFitxa().getLletra());

                    // En cas que la casella estigui ocupada i la lletra sigui diferent, retornem
                    // error.
                    if (!esta_al_taulell)
                        return 9;
                }
                boolean trobada = false;

                // Comprovem si fitxa està al rack
                while (!esta_al_rack && j < fitxes_jugador.size()) {
                    if (paraulaCompleta.get(i).getLletra().equals(fitxes_jugador.get(j).getLletra())) {
                        esta_al_rack = true;
                        if (!esta_al_taulell)
                            fitxes_jugador.remove(j);
                        trobada = true;
                    } else
                        ++j;
                }

                if (!trobada) {
                    for (int x = 0; x < fitxes_jugador.size(); ++x) {
                        if (fitxes_jugador.get(x).getLletra().equals("#")) {
                            esta_al_rack = true;
                            if (!esta_al_taulell)
                                fitxes_jugador.remove(x);
                            break;
                        }
                    }
                }
            } else {
                // comprovem que la fitxa està en la casella
                boolean casella_ocupada = taulell_actual.getCasella(fila + i, columna).EstaOcupat();
                if (casella_ocupada) {
                    esta_al_taulell = paraulaCompleta.get(i).getLletra()
                            .equals(taulell_actual.getCasella(fila + i, columna).getFitxa().getLletra());
                }

                boolean trobada = false;
                // comprovem si fitxa està al rack
                while (!esta_al_rack && j < fitxes_jugador.size()) {
                    if (paraulaCompleta.get(i).getLletra().equals(fitxes_jugador.get(j).getLletra())) {
                        esta_al_rack = true;
                        if (!esta_al_taulell)
                            fitxes_jugador.remove(j);
                        trobada = true;
                    } else
                        ++j;
                }

                if (!trobada) {
                    for (int x = 0; x < fitxes_jugador.size(); ++x) {
                        if (fitxes_jugador.get(x).getLletra().equals("#")) {
                            esta_al_rack = true;
                            if (!esta_al_taulell)
                                fitxes_jugador.remove(x);
                            break;
                        }
                    }
                }
            }

            if (!esta_al_rack && !esta_al_taulell) {
                return 3;
            }
        }

        // Comprovacions primer torn
        if (taulell_actual.estaBuit()) {
            if (!paraula_centre(paraulaCompleta, fila, columna, horitzontal, taulell_actual)) {
                return 4;
            }
        }

        // Comprovacions resta de torns
        else {
            // Està en contacte amb cap fitxa del taulell?
            if (!tocaLletra(paraulaCompleta, fila, columna, horitzontal, taulell_actual)) {
                return 5;
            }

            // Forma alguna paraula invàlida?
            else {

                // Estem allargant alguna paraula formant una que no existeixi? p.ex. DRACS +
                // SUR -> DRACSUR
                String prefix = "", sufix = "";

                if (horitzontal) {
                    for (int i = 1; taulell_actual.limits(fila, columna - i)
                            && taulell_actual.getCasella(fila, columna - i).EstaOcupat(); i++) {
                        prefix = taulell_actual.getCasella(fila, columna - i).getFitxa().getLletra() + prefix;
                    }

                    for (int i = 1; taulell_actual.limits(fila, columna + paraula.length() + i)
                            && taulell_actual.getCasella(fila, columna + paraula.length() + i).EstaOcupat(); i++) {
                        sufix = sufix + taulell_actual.getCasella(fila, columna + i).getFitxa().getLletra();
                    }
                } else {
                    for (int i = 1; taulell_actual.limits(fila - i, columna)
                            && taulell_actual.getCasella(fila - i, columna).EstaOcupat(); i++) {
                        prefix = taulell_actual.getCasella(fila - i, columna).getFitxa().getLletra() + prefix;
                    }

                    for (int i = 1; taulell_actual.limits(fila + paraula.length() + i, columna)
                            && taulell_actual.getCasella(fila + paraula.length() + i, columna).EstaOcupat(); i++) {
                        sufix = sufix + taulell_actual.getCasella(fila + i, columna).getFitxa().getLletra();
                    }
                }

                if (!diccionari.contains(prefix + paraula + sufix))
                    return 10;

                // Formem paraules transversals invàlides?
                Pair<Integer, Integer> coords;
                if (!horitzontal)
                    taulell_actual = taulell_actual.transpose();

                for (int i = 0; i < paraulaCompleta.size(); i++) {
                    if (horitzontal)
                        coords = new Pair<Integer, Integer>(fila, columna + i);
                    else
                        coords = new Pair<Integer, Integer>(columna, fila + i);

                    if (!comprovar_noves_paraules(taulell_actual, paraulaCompleta.get(i), coords)) {
                        return 6;
                    }
                }
            }
        }

        return 0;
    }

    /**
     * Comprova si la posició d'una paraula és vàlida dins dels límits del taulell.
     *
     * @param paraulaCompleta Les fitxes que formen la paraula.
     * @param fila            La fila inicial de la paraula.
     * @param columna         La columna inicial de la paraula.
     * @param horitzontal     Indica si la paraula es col·loca horitzontalment.
     * @param taulell_actual  El taulell actual de la partida.
     * @return true si la posició és vàlida, false en cas contrari.
     */
    private boolean posicioCorrecta(List<Fitxa> paraulaCompleta, int fila, int columna, boolean horitzontal,
            Taulell taulell_actual) {
        if (!taulell_actual.limits(fila, columna)) {
            return false;
        }

        int midaP = paraulaCompleta.size();
        if (horitzontal) {
            if (!taulell_actual.limits(fila, columna + midaP - 1)) {
                return false;
            }
        } else {
            if (!taulell_actual.limits(fila + midaP - 1, columna)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Comprova si una paraula col·locada passa pel centre del taulell.
     *
     * @param paraulaCompleta Les fitxes que formen la paraula.
     * @param fila            La fila inicial de la paraula.
     * @param columna         La columna inicial de la paraula.
     * @param horitzontal     Indica si la paraula es col·loca horitzontalment.
     * @param taulell_actual  El taulell actual de la partida.
     * @return true si la paraula passa pel centre del taulell, false en cas
     *         contrari.
     */
    private boolean paraula_centre(List<Fitxa> paraulaCompleta, int fila, int columna, boolean horitzontal,
            Taulell taulell_actual) {
        Pair<Integer, Integer> centre = taulell_actual.getCentreTaulell();
        int midaP = paraulaCompleta.size();
        int fila_aux = fila;
        int columna_aux = columna;
        boolean mig = false;

        while (midaP > 0 && !mig) {
            if (fila_aux == centre.getFirst() && columna_aux == centre.getSecond()) {
                mig = true;
            } else {
                if (horitzontal) {
                    ++columna_aux;
                } else {
                    ++fila_aux;
                }
                --midaP;
            }
        }
        return mig;
    }

    /**
     * Comprova si una paraula col·locada toca alguna lletra existent al taulell.
     *
     * @param paraulaCompleta Les fitxes que formen la paraula.
     * @param fila            La fila inicial de la paraula.
     * @param columna         La columna inicial de la paraula.
     * @param horitzontal     Indica si la paraula es col·loca horitzontalment.
     * @param taulell_actual  El taulell actual de la partida.
     * @return true si la paraula toca alguna lletra existent al taulell, False en
     *         cas contrari.
     */
    private boolean tocaLletra(List<Fitxa> paraulaCompleta, int fila, int columna, boolean horitzontal,
            Taulell taulell_actual) {

        int midaT = taulell_actual.getMida();
        int fila_aux = fila;
        int columna_aux = columna;
        int midaP = paraulaCompleta.size();

        while (midaP > 0) {
            // dreta
            if (columna_aux + 1 < midaT) {
                if (taulell_actual.getCasella(fila_aux, columna_aux + 1).EstaOcupat())
                    return true;
            }

            // esquerra
            if (columna_aux - 1 >= 0) {
                if (taulell_actual.getCasella(fila_aux, columna_aux - 1).EstaOcupat())
                    return true;
            }

            // dalt
            if (fila_aux - 1 >= 0) {
                if (taulell_actual.getCasella(fila_aux - 1, columna_aux).EstaOcupat())
                    return true;
            }

            // baix
            if (fila_aux + 1 < midaT) {
                if (taulell_actual.getCasella(fila_aux + 1, columna_aux).EstaOcupat())
                    return true;
            }
            --midaP;

            if (horitzontal)
                ++columna_aux;
            else
                ++fila_aux;
        }
        return false;
    }

    /**
     * Comprova si totes les lletres d'una paraula col·locada són vàlides segons els
     * crosschecks
     * de les caselles corresponents del taulell.
     *
     * @param paraulaCompleta Les fitxes que formen la paraula.
     * @param fila            La fila inicial de la paraula.
     * @param columna         La columna inicial de la paraula.
     * @param horitzontal     Indica si la paraula es col·loca horitzontalment.
     * @param taulell_actual  El taulell actual de la partida.
     * @return true si totes les lletres són vàlides segons els crosschecks, false
     *         en cas contrari.
     */
    public boolean comprova_crosschecks(List<Fitxa> paraulaCompleta, int fila, int columna, boolean horitzontal,
            Taulell taulell_actual) {

        for (int i = 0; i < paraulaCompleta.size(); ++i) {
            String lletra = paraulaCompleta.get(i).getLletra();
            if (horitzontal) {
                if (!letterIsInCrosscheks(lletra, partida.getTaulell().getCasella(fila, columna + i).getCrossChecks()))
                    return false;
            } else {
                if (!letterIsInCrosscheks(lletra, partida.getTaulell().getCasella(fila + i, columna).getCrossChecks()))
                    return false;
            }
        }
        return true;
    }

    /**
     * Genera una llista amb les lletres i dígrafs del paràmetre.
     * Recorre l'string d'entrada segmentant tots els seus elements identificant
     * correctament els dígrafs.
     *
     * @param word Paraula a segmentar.
     *
     * @return Una llista amb els elements ja segmentats.
     */
    private static List<String> tokenize(String word) {
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
     * Comprova si una lletra és vàlida per a una casella que formi paraula
     * transversal vertical.
     *
     * @param t      El taulell actual de la partida.
     * @param f      La fitxa a comprovar.
     * @param coords Les coordenades de la casella.
     * 
     * @return true si la lletra és vàlida, false en cas contrari.
     */
    private boolean comprovar_noves_paraules(Taulell t, Fitxa f, Pair<Integer, Integer> coords) {

        int row = coords.getFirst();
        int col = coords.getSecond();

        boolean transversal = false;
        if (t.limits(row + 1, col) && t.getCasella(row + 1, col).EstaOcupat())
            transversal = true;
        if (t.limits(row - 1, col) && t.getCasella(row - 1, col).EstaOcupat())
            transversal = true;

        // En cas que formi una paraula transversal
        if (transversal) {
            // Generem la paraula transversal
            String prefix = "";
            String sufix = "";

            int row_aux = row - 1;
            while (t.limits(row_aux, col) && t.getCasella(row_aux, col).EstaOcupat()) {
                prefix = t.getCasella(row_aux, col).getFitxa().getLletra() + prefix;
                --row_aux;
            }

            row_aux = row + 1;
            while (t.limits(row_aux, col) && t.getCasella(row_aux, col).EstaOcupat()) {
                sufix = sufix + t.getCasella(row_aux, col).getFitxa().getLletra();
                ++row_aux;
            }

            String traversalWord = prefix + f.getLletra() + sufix;

            if (!diccionari.contains(traversalWord))
                return false;
        }
        return true;
    }

    /**
     * Comprova si una lletra està present en els crosschecks d'una casella.
     *
     * @param letter      La lletra que es vol comprovar.
     * @param crosschecks Un array de valors booleans que indica les lletres vàlides
     *                    per a la casella.
     * @return true si la lletra està present en els crosschecks, false en cas
     *         contrari.
     */
    private boolean letterIsInCrosscheks(String letter, boolean[] crosschecks) {
        int length = letter.length();

        if (length == 1) {
            char l = letter.charAt(0);
            if (l >= 'A' && l <= 'Z')
                return crosschecks[l - 'A'];
            else if (l == 'Ç')
                return crosschecks[26];
            else if (l == 'Ñ')
                return crosschecks[27];
            else if (l == '#')
                return crosschecks[33];
            else
                return false; // mai s'hauria de complir
        } else if (length == 2) {
            if (letter == "CH")
                return crosschecks[28];
            else if (letter == "NY")
                return crosschecks[29];
            else if (letter == "RR")
                return crosschecks[30];
            else if (letter == "LL")
                return crosschecks[31];
            else
                return false; // mai s'hauria de complir
        } else
            return crosschecks[32];
    }

    /**
     * Obté les fitxes actuals del jugador del torn.
     *
     * @return Una llista de fitxes del jugador actual.
     */
    public List<Fitxa> getLletresJugadorTorn() {
        return partida.getFitxesActuals();
    }

    /**
     * Mostra el taulell actual de la partida.
     */
    public void imprimirTaulell() {
        partida.imprimirtaulell(); // taulell és un atribut del tipus Taulell
    }

    /**
     * Obté la bossa de fitxes de la partida.
     *
     * @return L'objecte Bossa de la partida.
     */
    public Bossa getBossa1() {
        return partida.getBossa();
    }

    /**
     * Canvia un cert nombre de fitxes de la mà del jugador actual.
     *
     * Aquesta funció elimina les fitxes especificades de la mà del jugador actual i
     * les retorna a la bossa,
     * després agafa el mateix nombre de fitxes noves de la bossa. També gestiona el
     * cas dels comodins.
     *
     * Codis de retorn:
     * - 0: Canvi realitzat correctament.
     * - 7: Error en eliminar fitxes del rack del jugador.
     * - 8: No hi ha prou fitxes a la bossa per completar el canvi.
     *
     * @param fitxesACanviar       Les fitxes a canviar.
     * @param numFitxes            El nombre de fitxes a canviar.
     * @param ve_de_canviar_fitxes Indica si prové d'una acció de canvi de fitxes.
     * @param lletra1              En cas que s'utilitzi un comodí, lletra a
     *                             substituir.
     * @param lletra2              En cas que s'utilitzin dos comodins, 2a lletra a
     *                             substituir.
     * @return Un codi d'error que indica l'estat de l'operació (veure descripció).
     */
    public int canviarFitxes(List<String> fitxesACanviar, int numFitxes, boolean ve_de_canviar_fitxes, String lletra1, String lletra2) {
        Jugador jugadorActual = partida.getJugadorActual(false);
        Bossa bossa = partida.getBossa();

        if (bossa.getMidaBossa() == 0) return 8;

        for (String lletra : fitxesACanviar) {
            Fitxa f_elim;
            // Si la lletra que volem canviar és la representació d’un comodí
            f_elim = jugadorActual.eliminarFitxa(lletra);

            if (f_elim == null && (!lletra1.equals("") || !lletra2.equals(""))) {
                // Eliminar la fitxa real que és el comodí però representava lletra1/lletra2
                f_elim = jugadorActual.eliminarFitxa("#");
                if (lletra1.equals("#"))
                    lletra1 = "";
                else if (lletra2.equals("#"))
                    lletra2 = "";
                else
                    return 7;
            }

            if (f_elim == null)
                return 7;

            // Fitxa f_elim = jugadorActual.eliminarFitxa(lletra);
            if (ve_de_canviar_fitxes) {
                bossa.afegirFitxaBossa(f_elim.getId(), f_elim.getLletra(), f_elim.getPuntuacio());
            }
        }

        // Agafar noves fitxes
        for (int i = 0; i < numFitxes; i++) {
            Fitxa novaFitxa = bossa.agafarFitxa();
            if (novaFitxa != null) {
                jugadorActual.afegirFitxa(novaFitxa);
            } else {
                return 8;
            }
        }

        partida.incrementarTorn();
        jugadorActual.reiniciarTornsPassatsConsecutius();
        return 0;
    }

    /**
     * Passa el torn al següent jugador.
     *
     * @return true si la partida ha finalitzat per torns consecutius, false en cas
     *         contrari.
     */
    public boolean passarTorn() {
        partida.getJugadorActual(false).incrementarTornsPassatsConsecutius();
        partida.incrementarTorn();
        return finalitzarPerTornsConsecutius();
    }

    /**
     * Indica si la partida és contra màquina
     *
     * @return true si es juga contra maquina, false d'altra manera.
     */
    public boolean contraMaquina() {
        return (jugador1.getIdJugador() == -1) || (jugador2.getIdJugador() == -1);
    }

    /**
     * Obté el nom del jugador actual.
     *
     * @return El nom del jugador actual.
     */
    public String jugador_controladorNOM() {
        Jugador jugadorActual = partida.getJugadorActual(false);
        return jugadorActual.getNom();
    }

    /**
     * Obté el nom del jugador no actual.
     *
     * @return El nom del jugador no actual.
     */
    public String jugadorNoActual_controladorNOM() {
        Jugador jugadorActual = partida.getJugadorActual(true);
        return jugadorActual.getNom();
    }

    /**
     * Obté l'identificador del jugador actual.
     *
     * @return L'identificador del jugador actual.
     */
    public int jugador_controladorID() {
        Jugador jugadorActual = partida.getJugadorActual(false);
        return jugadorActual.getIdJugador();
    }

    /**
     * Obté el nombre de fitxes que té el jugador actual.
     *
     * @return El nombre de fitxes del jugador actual.
     */
    public int jugador_controladorNUM_FITXES() {
        Jugador jugadorActual = partida.getJugadorActual(false);
        return jugadorActual.getFitxes_actuals().size();
    }

    /**
     * Obté el torn actual de la partida.
     *
     * @return El número del torn actual.
     */
    public int controladorTORN() {
        return partida.getTorn();
    }

    /**
     * Finalitza la partida si les fitxes s'han esgotat.
     *
     * @return true si la partida ha finalitzat, false en cas contrari.
     */
    public boolean finalitzarPerLletresEsgotades() {
        if (partida.getJugadorActual(false).getFitxes_actuals().isEmpty() && partida.getBossa().getMidaBossa() == 0) {
            Jugador guanyador = (jugador1.getFitxes_actuals().isEmpty()) ? jugador1 : jugador2;
            Jugador perdedor = (guanyador == jugador1) ? jugador2 : jugador1;

            int puntsRestants = partida.calcularPuntuacionsFitxes(perdedor);
            partida.incrementar_puntuacio_partida(puntsRestants, guanyador);
            partida.incrementar_puntuacio_partida(-puntsRestants, perdedor);

            int punt_jugador1 = partida.getPuntuacioJugador1();
            int punt_jugador2 = partida.getPuntuacioJugador2();
            jugador1.incrementar_puntuacio_actual(punt_jugador1);
            jugador2.incrementar_puntuacio_actual(punt_jugador2);

            Avatar guany = (Avatar) guanyador;
            guany.incrementarPartidesGuanyades();
            if (perdedor.getIdJugador() == -1) {
                guany.incrementarPartidesGuanyadesMaquina();
            } else {
                guany.incrementarPartidesGuanyades();
            }

            CtrlDomini.actualitzarRanking(partida.getIdPartida(), jugador1, punt_jugador1, jugador2, punt_jugador2);
            return true;
        }
        return false;
    }

    /**
     * Finalitza la partida si s'han passat torns consecutius sense acció.
     *
     * @return true si la partida ha finalitzat, false en cas contrari.
     */
    public boolean finalitzarPerTornsConsecutius() {
        if (jugador1.getTornsPassatsConsecutius() >= 3 || jugador2.getTornsPassatsConsecutius() >= 3
                || (jugador1.getTornsPassatsConsecutius() + jugador2.getTornsPassatsConsecutius()) >= 6) {
            int puntuacio_jugador_1 = partida.calcularPuntuacionsFitxes(jugador1);
            partida.incrementar_puntuacio_partida(-puntuacio_jugador_1, jugador1);
            int puntuacio_jugador_2 = partida.calcularPuntuacionsFitxes(jugador2);
            partida.incrementar_puntuacio_partida(-puntuacio_jugador_2, jugador2);

            int punt_jugador1 = partida.getPuntuacioJugador1();
            int punt_jugador2 = partida.getPuntuacioJugador2();
            jugador1.incrementar_puntuacio_actual(punt_jugador1);
            jugador2.incrementar_puntuacio_actual(punt_jugador2);

            if (partida.getPuntuacioJugador1() > partida.getPuntuacioJugador2()) {
                if (jugador1.getIdJugador() != -1) {
                    Avatar guanyador = (Avatar) jugador1;
                    guanyador.incrementarPartidesGuanyades();
                    if (jugador2.getIdJugador() != -1) {
                        guanyador.incrementarPartidesGuanyadesJugador();
                    } else {
                        guanyador.incrementarPartidesGuanyadesMaquina();
                    }
                }
            } else if (partida.getPuntuacioJugador2() > partida.getPuntuacioJugador1()) {
                if (jugador2.getIdJugador() != -1) {
                    Avatar guanyador = (Avatar) jugador2;
                    guanyador.incrementarPartidesGuanyades();
                    if (jugador1.getIdJugador() != -1) {
                        guanyador.incrementarPartidesGuanyadesJugador();
                    } else {
                        guanyador.incrementarPartidesGuanyadesMaquina();
                    }
                }
            }
            CtrlDomini.actualitzarRanking(partida.getIdPartida(), jugador1, punt_jugador1, jugador2, punt_jugador2);
            return true;
        }
        return false;
    }

    /**
     * El jugador actual abandona la partida.
     */
    public void abandonarPartida() {
        Jugador jugadorAbandona = partida.getJugadorActual(false);
        Jugador oponent = (jugadorAbandona == jugador1) ? jugador2 : jugador1;

        if (jugadorAbandona.getIdJugador() != -1) {
            if (jugadorAbandona.getIdJugador() == jugador1.getIdJugador()) {
                if (partida.getPuntuacioJugador1() > partida.getPuntuacioJugador2()) {
                    // Si el jugador que abandona va guanyant, se li assignen 50 punts menys que
                    // l'oponent
                    partida.reiniciar_puntuacio_partida(jugador1);
                    partida.incrementar_puntuacio_partida((partida.getPuntuacioJugador2() - 50), jugador1);
                }
            } else if (jugadorAbandona.getIdJugador() == jugador2.getIdJugador()) {
                if (partida.getPuntuacioJugador2() > partida.getPuntuacioJugador1()) {
                    // Si el jugador que abandona va guanyant, se li assignen 50 punts menys que
                    // l'oponent
                    partida.reiniciar_puntuacio_partida(jugador2);
                    partida.incrementar_puntuacio_partida((partida.getPuntuacioJugador1() - 50), jugador2);
                }
            }
        }

        int punt_jugador1 = partida.getPuntuacioJugador1();
        int punt_jugador2 = partida.getPuntuacioJugador2();
        jugador1.incrementar_puntuacio_actual(punt_jugador1);
        jugador2.incrementar_puntuacio_actual(punt_jugador2);

        // Actualitzar estadístiques d'abandonament
        if (jugadorAbandona.getIdJugador() != -1) {
            Avatar avatar = (Avatar) jugadorAbandona;
            avatar.incrementarPartidesAbandonades();
            if (oponent.getIdJugador() != -1) {
                Avatar avatar2 = (Avatar) oponent;
                avatar2.incrementarPartidesGuanyades();
                avatar2.incrementarPartidesGuanyadesJugador();
            }
        }

        else if (oponent.getIdJugador() != -1) {
            Avatar avatar2 = (Avatar) oponent;
            avatar2.incrementarPartidesGuanyades();
            avatar2.incrementarPartidesGuanyadesMaquina();
        }

        CtrlDomini.actualitzarRanking(partida.getIdPartida(), jugador1, punt_jugador1, jugador2, punt_jugador2);
    }

    /**
     * Obté l'objecte Partida associat al controlador.
     *
     * @return L'objecte Partida actual.
     */
    public Partida getPartida() {
        return partida;
    }
}
package domini.jugadors;

import java.util.*;
import domini.auxiliars.Pair;
import domini.auxiliars.MaxWord;

import domini.scrabble.Taulell;
import domini.scrabble.Fitxa;
import domini.scrabble.Casella;
import domini.scrabble.Multiplicador;

import domini.diccionari.*;


/**
 * Representa un jugador controlat per la màquina.
 *
 * La classe "Maquina" implementa la lògica necessària perquè la màquina pugui
 * jugar de manera autònoma, incloent la cerca de paraules amb la puntuació més
 * alta
 * i la validació de moviments.
 */
public class Maquina extends Jugador {
    /**
     * Constructor de la classe Maquina.
     *
     * Inicialitza la màquina amb un identificador predeterminat (-1), el nom
     * "Maquina" i la contrasenya "*".
     */
    public Maquina() {
        super(-1, "Maquina", "./resources/maquina.png");
    }

    /**
     * Converteix una llista de fitxes en una cadena de text.
     *
     * @param l La llista de fitxes a convertir.
     * @return La paraula en format String.
     */
    private String fitxes2String(List<Fitxa> l) {
        String ret = "";
        for (int i = 0; i < l.size(); i++) {
            ret += l.get(i).getLletra();
        }
        return ret;
    }

    /**
     * Busca la posició d'una lletra en el rack de la màquina.
     *
     * @param rack El faristol de la màquina.
     * @param let  La lletra a buscar.
     * @return La posició de la lletra en el rack, o -1 si no es troba.
     */
    private int rackContainsLetter(List<Fitxa> rack, String let) {
        for (int i = 0; i < rack.size(); ++i) {
            if (rack.get(i).getLletra().equals(let)) return i;
        }
        return -1;
    }

    /**
     * Calcula la puntuació d'una paraula col·locada en el taulell.
     *
     * @param t      El taulell on es col·loca la paraula.
     * @param word   La paraula de la qual es calcula la puntuació.
     * @param coords Les coordenades inicials de la paraula.
     * @param h      Indica si la paraula és horitzontal (true) o vertical (false).
     * @return La puntuació de la paraula.
     */
    private int calcularPuntatge(Taulell t, List<Fitxa> word, Pair<Integer, Integer> coords, boolean h) {
        int points = 0;
        int doubleWord = 0;
        int tripleWord = 0;

        int x, y;
        if (h) {
            x = coords.getFirst();
            y = coords.getSecond();
        } else {
            x = coords.getSecond();
            y = coords.getFirst();
        }

        for (int i = 0; i < word.size(); ++i) {
            Multiplicador multi = t.getCasella(x, y).getMultiplicador();

            if (multi == Multiplicador.CAP) points += word.get(i).getPuntuacio();
            else if (multi == Multiplicador.DOBLE_LLETRA) points += 2*word.get(i).getPuntuacio();
            else if (multi == Multiplicador.TRIPLE_LLETRA) points += 3*word.get(i).getPuntuacio();
            else if (multi == Multiplicador.DOBLE_PARAULA) {++doubleWord; points += word.get(i).getPuntuacio();}
            else if (multi == Multiplicador.TRIPLE_PARAULA) {++tripleWord; points += word.get(i).getPuntuacio();}

            if (h) ++y; else ++x;
        }

        for (int i = 0; i < doubleWord; ++i) points *= 2;
        for (int i = 0; i < tripleWord; ++i) points *= 3;

        return points;
    }

    /**
     * Valora una paraula i actualitza la millor paraula trobada si és millor.
     *
     * @param t           El taulell on es col·loca la paraula.
     * @param d           El diccionari de l'idioma.
     * @param maxWord     Contenidor amb la informació de la millor paraula trobada.
     * @param partialWord La paraula parcial a valorar.
     * @param coords      Les coordenades inicials de la paraula.
     * @param h           Indica si la paraula és horitzontal (true) o vertical
     *                    (false).
     */
    private void valorarParaula(Taulell t, DAWG d, MaxWord maxWord, List<Fitxa> partialWord, Pair<Integer, Integer> coords, boolean h) {
        String word = fitxes2String(partialWord);
        Pair<Integer, Integer> wCoords = new Pair<Integer, Integer>(coords.getFirst(), coords.getSecond() - partialWord.size());

        if (d.contains(word) && t.limits(wCoords.getFirst(), wCoords.getSecond())) {
            int points = calcularPuntatge(t, partialWord, wCoords, h);
            if (points > maxWord.getPoints()) {
                maxWord.setPoints(points);
                maxWord.setWord(partialWord);
                maxWord.setHorizontal(h);
                if (h) maxWord.setCoordinates(wCoords);
                else maxWord.setCoordinates(new Pair<Integer, Integer>(wCoords.getSecond(), wCoords.getFirst()));
            }
        }
    }

    /**
     * Busca la paraula amb la puntuació més alta donats un taulell i un diccionari.
     *
     * @param t El taulell on es busquen paraules.
     * @param d El diccionari de l'idioma.
     * @return La paraula de màxima puntuació amb els seus punts, coordenades i
     *         direcció.
     */
    public MaxWord ferJugada(Taulell t, DAWG d) {
        MaxWord maxWord = new MaxWord();

        searchBestWord(t, d, true, maxWord); // Cerquem paraules horitzontals
        searchBestWord(t.transpose(), d, false, maxWord); // Cerquem paraules verticals

        return maxWord;
    }

    /**
     * Busca la millor paraula en una direcció específica (horitzontal o vertical).
     *
     * @param t       El taulell on es busquen paraules.
     * @param d       El diccionari de l'idioma.
     * @param h       Indica si es busquen paraules horitzontals (true) o verticals
     *                (false).
     * @param maxWord Contenidor amb la informació de la millor paraula trobada.
     */
    private void searchBestWord(Taulell t, DAWG d, boolean h, MaxWord maxWord) {
        // Identifiquem les caselles anchor del tauell
        Set<Pair<Integer, Integer>> anchors = t.getAnchors();

        // Generem una Llista amb les lletres del rack
        List<Fitxa> rack = new ArrayList<Fitxa>(fitxes_actuals);

        // Per a cada anchor trobat
        for (Pair<Integer, Integer> coords : anchors) {

            // Establim el límit cap a l'esquerra
            int limit = 0;
            for (int i = 1; i <= 7 && t.limits(coords.getFirst(), coords.getSecond()-i); ++i) {
                Casella c = t.getCasella(coords.getFirst(), coords.getSecond()-i);
                if (!c.isAnchor() && !c.EstaOcupat()) ++limit;
                else break;
            }

            // Generem totes les paraules possibles, emmagatzemant la millor a maxWord
            List<Fitxa> partialWord = new ArrayList<Fitxa>();
            generateLeft(t, d, maxWord, rack, partialWord, 0, limit, coords, h);
        }
    }

    /**
     * Genera totes les subparaules prefix possibles cap a l'esquerra d'una casella
     * anchor.
     *
     * @param t           El taulell on es busquen paraules.
     * @param d           El diccionari de l'idioma.
     * @param maxWord     Contenidor amb la informació de la millor paraula trobada.
     * @param rack        Les fitxes disponibles al faristol.
     * @param partialWord La subparaula parcial generada.
     * @param node        El node del DAWG des del qual es generen paraules.
     * @param limit       La longitud màxima de la subparaula generada.
     * @param coords      Les coordenades inicials de la subparaula.
     * @param h           Indica si es busquen paraules horitzontals (true) o
     *                    verticals (false).
     */
    private void generateLeft(Taulell t, DAWG d, MaxWord maxWord, List<Fitxa> rack, List<Fitxa> partialWord, int node, int limit, Pair<Integer, Integer> anchor, boolean h) {

        // Podem trobar 3 casuístiques que provoquin límit 0:
        // - la casella adjacent al anchor està ocupada:     (tractat aquí, extenem la paraula amb les lletres que ja es troben al taulell)
        // - la casella adjacent al anchor també és anchor:  (no s'ha de tractar res, es generaran en altres crides)
        // - ens trobem a la vora del taulell:               (no s'ha de tractar res)
        if (limit == 0) {
            if (t.limits(anchor.getFirst(), anchor.getSecond() - 1)) {
                Casella cas = t.getCasella(anchor.getFirst(), anchor.getSecond() - 1);
                if (cas.EstaOcupat()) {
                    int j = 1;
                    while (t.limits(anchor.getFirst(), anchor.getSecond() - j) && t.getCasella(anchor.getFirst(), anchor.getSecond() - j).EstaOcupat()) {
                        partialWord.add(0, t.getCasella(anchor.getFirst(), anchor.getSecond() - j).getFitxa());
                        ++j;
                    }
                }
            }
        }

        else if (limit > 0) {
            // Per a cada lletra que surti del node actual del DAWG
            for (String let : d.getEdgesFromNode(node)) {
                // Comprovem si la tenim al rack
                int pos_let = rackContainsLetter(rack, let);

                // En cas de no tenir la lletra, comprovem si tenim algun comodin
                boolean comodin = false;
                if (pos_let == -1) {
                    pos_let = rackContainsLetter(rack, "#");
                    if (pos_let != -1) comodin = true;
                }

                // Si tenim la lletra al rack
                if (pos_let != -1) {
                    Fitxa f = rack.get(pos_let);
                    int nextNode = d.getNodeFromTransition(node, let);

                    List<Fitxa> newPartialWord = new ArrayList<Fitxa>(partialWord);
                    if (comodin) newPartialWord.add(new Fitxa(120, let, 0));
                    else newPartialWord.add(f);

                    rack.remove(pos_let);
                    generateLeft(t, d, maxWord, rack, newPartialWord, nextNode, limit - 1, anchor, h);
                    rack.add(f);
                }
            }
        }

        extendRight(t, d, maxWord, rack, partialWord, node, anchor, true, h);
    }

    /**
     * Genera totes les paraules possibles a la dreta d'una subparaula prefix.
     *
     * @param t             El taulell on es busquen paraules.
     * @param d             El diccionari de l'idioma.
     * @param maxWord       Contenidor amb la informació de la millor paraula
     *                      trobada.
     * @param rack          Les fitxes disponibles al faristol.
     * @param partialWord   La subparaula prefix generada.
     * @param node          El node del DAWG des del qual es generen paraules.
     * @param coords        Les coordenades inicials de la subparaula.
     * @param is_the_anchor Indica si la casella actual és una casella anchor.
     * @param h             Indica si es busquen paraules horitzontals (true) o
     *                      verticals (false).
     */
    private void extendRight(Taulell t, DAWG d, MaxWord maxWord, List<Fitxa> rack, List<Fitxa> partialWord, int node, Pair<Integer, Integer> coords, boolean is_the_anchor, boolean h) {
        boolean valid_coords = t.limits(coords.getFirst(), coords.getSecond());

        if (!valid_coords) {
            valorarParaula(t, d, maxWord, partialWord, coords, h);
        }
        else {
            boolean empty_square = !t.getCasella(coords.getFirst(), coords.getSecond()).EstaOcupat();

            if (is_the_anchor) {
                // Per a cada lletra que surti del node actual del DAWG
                for (String let : d.getEdgesFromNode(node)) {
                    // Comprovem si tenim la lletra al rack
                    int pos_let = rackContainsLetter(rack, let);
                    
                    // En cas de no tenir la lletra, comprovem si tenim algun comodin
                    boolean comodin = false;
                    if (pos_let == -1) {
                        pos_let = rackContainsLetter(rack, "#");
                        if (pos_let != -1) comodin = true;
                    }

                    Fitxa f = null;
                    if (comodin) f = new Fitxa(120, let, 0);
                    else if (pos_let != -1) f = rack.get(pos_let);

                    // Si tenim la lletra al rack i està als crosschecks
                    if (pos_let != -1 && valid_traversals(t, d, f, coords)) {
                        int nextNode = d.getNodeFromTransition(node, let);
                        Pair<Integer, Integer> newCoords = new Pair<Integer,Integer>(coords.getFirst(), coords.getSecond() + 1);

                        List<Fitxa> newPartialWord = new ArrayList<Fitxa>(partialWord);
                        newPartialWord.add(f);

                        rack.remove(pos_let);
                        extendRight(t, d, maxWord, rack, newPartialWord, nextNode, newCoords, false, h);
                        rack.add(f);
                    }
                }
            }

            else if (empty_square) {
                valorarParaula(t, d, maxWord, partialWord, coords, h);
                // Per a cada lletra que surti del node actual del DAWG
                for (String let : d.getEdgesFromNode(node)) {
                    // Comprovem si tenim la lletra al rack
                    int pos_let = rackContainsLetter(rack, let);
                    
                    // En cas de no tenir la lletra, comprovem si tenim algun comodin
                    boolean comodin = false;
                    if (pos_let == -1) {
                        pos_let = rackContainsLetter(rack, "#");
                        if (pos_let != -1) comodin = true;
                    }

                    Fitxa f = null;
                    if (comodin) f = new Fitxa(120, let, 0);
                    else if (pos_let != -1) f = rack.get(pos_let);

                    // Si tenim la lletra al rack i està als crosschecks
                    if (pos_let != -1 && valid_traversals(t, d, f, coords)) {
                        int nextNode = d.getNodeFromTransition(node, let);
                        Pair<Integer, Integer> newCoords = new Pair<Integer,Integer>(coords.getFirst(), coords.getSecond() + 1);

                        List<Fitxa> newPartialWord = new ArrayList<Fitxa>(partialWord);
                        newPartialWord.add(f);

                        rack.remove(pos_let);
                        extendRight(t, d, maxWord, rack, newPartialWord, nextNode, newCoords, false, h);
                        rack.add(f);
                    }
                }
            }

            // En cas que la casella ja estigui ocupada
            else {
                Fitxa f = t.getCasella(coords.getFirst(), coords.getSecond()).getFitxa();
                String let = f.getLletra();

                // Si existeix una aresta del node actual amb la lletra, ampliem la paraula
                if (d.getEdgesFromNode(node).contains(let)) {
                    int nextNode = d.getNodeFromTransition(node, let);
                    Pair<Integer, Integer> newCoords = new Pair<Integer,Integer>(coords.getFirst(), coords.getSecond() + 1);

                    List<Fitxa> newPartialWord = new ArrayList<Fitxa>(partialWord);
                    newPartialWord.add(f);

                    extendRight(t, d, maxWord, rack, newPartialWord, nextNode, newCoords, false, h);
                }
            }
        }
    }

    /**
     * Comprova si una lletra és vàlida per a una casella que formi paraula transversal vertical.
     *
     * @param t      El taulell.
     * @param d      El diccionari de l'idioma.
     * @param f      La fitxa a comprovar.
     * @param coords Les coordenades de la casella.

     * @return true si la lletra és vàlida, false en cas contrari.
     */
    private boolean valid_traversals(Taulell t, DAWG d, Fitxa f, Pair<Integer, Integer> coords) {

        int row = coords.getFirst();
        int col = coords.getSecond();

        boolean transversal = false;
        if (t.limits(row + 1, col) && t.getCasella(row + 1, col).EstaOcupat()) transversal = true;
        if (t.limits(row - 1, col) && t.getCasella(row - 1, col).EstaOcupat()) transversal = true;

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

            // Comprovem si hi és al diccionari
            if (!d.contains(traversalWord)) return false;
        }
        return true;
    }
}
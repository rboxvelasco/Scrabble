package domini.diccionari;

import java.util.*;

import domini.excepcions.ExcepcioArxiuDiccionari;
import domini.excepcions.ExcepcioDomini;

import java.io.*;

/**
 * Representa un DAWG i implementa els mètodes per a generar-lo, modificar-lo i consultar-lo.
 *
 * Un DAWG està compost per un ArrayList on cada cel·la representa un Node amb un subvector que
 * conté les diferents aristes (Edges) que surten d'ell. Està pensat perquè la informació quedi
 * representada de la forma més compacta possible, evitant nodes repetits i nodes sense aristes
 * sortints. S'ha optat per una estructura bidimensional, en detriment d'una unidimensional, per
 * a poder fer l'estructura de dades encara més compacte eliminant el flag "isLastNode".
 */
public class DAWG {

    private List<Node> nodes;
    private static final Set<String> DIGRAFS = Set.of("RR", "LL", "NY", "CH", "L·L");
    private boolean useDigraphs; // True per CAT, ESP. False per ENG.

    /**
     * Representa una Arista del DAWG amb tota la informació necessària.
     *
     * Cada arista conté la següent informació: lletra que provoca la transició de node, Node
     * destí de la transició i un flag que indica si la transició duu a un Node terminal (que
     * accepta la paraula com a pertanyent al lèxic).
     */
    private static class Edge {
        String letter;
        int nextNode;
        boolean isTerminal;

        /**
         * Constructor de la classe Edge.
         *
         * Inicialitza una arista amb la informació provista pels paràmetres.
         *
         * @param letter     Indica la lletra associada a la transició representada per l'arista
         * @param nextNode   Indica l'índex del node destí al vector del DAWG al que pertany, en cas
         *                   de ser -1, indica que la transició no duu a cap node representat.
         * @param isTerminal Indica si l'arista duu a un Node acceptador (que accepta la paraula
         *                   com a pertanyent al lèxic)
         */
        Edge(String letter, int nextNode, boolean isTerminal) {
            this.letter = letter;
            this.nextNode = nextNode;
            this.isTerminal = isTerminal;
        }
    }

    /**
     * Representa un Node del DAWG.
     *
     * Cada node està compost per un array d'aristes, representant totes les aristes sortints.
     */
    private static class Node {
        private List<Edge> edges;

        /**
         * Constructor de la classe Node.
         *
         * Inicialitza un node buit.
         */
        public Node() {
            edges = new ArrayList<>();
        }

        /**
         * Consultora de la mida del Node.
         *
         * El mètode retorna quants Edges surten del node en qüestió.
         *
         * @return El nombre d'Edges que surten del node.
         */
        public int howManyEdges() {
            return edges.size();
        }
    }

    /**
     * Constructor de la classe DAWG.
     *
     * Inicialitza un DAWG buit.
     */
    public DAWG() {
        this.nodes = new ArrayList<>();
        this.nodes.add(new Node());
        this.useDigraphs = false;
    }

    /**
     * Genera un DAWG a partir d'un fitxer amb un format específic.
     *
     * Obre el fitxer "file_name" i en llegeix el contingut. A partir d'ell genera un DAWG que
     * reconeix el lèxic indicat pel fitxer. El fitxer ha de tenir el següent format:
     * 1. Una sola paraula per línia
     * 2. Tots els caràcters han d'estar en majúscules.
     *
     * @throws ExcepcioDomini Si hi ha un error en llegir el fitxer o en processar les dades.
     * @param file_name Nom del fitxer que conté el lèxic a identificar.
     */
    public void dictionary2DAWG(String file_name) throws ExcepcioDomini {
        if (file_name == "english.txt") useDigraphs = false;
        else useDigraphs = true;

        try (BufferedReader br = new BufferedReader(new FileReader("resources/" + file_name))) {
            String word;
            while ((word = br.readLine()) != null) {
                insertWord(word);
            }
        }
        catch (FileNotFoundException fnfe) {
            // Encapsulem la causa en la nostra ExcepcioArxiuDiccionari
            throw new ExcepcioArxiuDiccionari(file_name, fnfe);
        } catch (IOException ioe) {
            // Capturem altres I/O errors que puguin ocórrer
            throw new ExcepcioDomini("Error durant la lectura del diccionari: " + file_name, ioe);
        }
    }

    /**
     * Genera una llista amb les lletres i dígrafs de la paraula paràmetre.
     *
     * Recorre l'string d'entrada segmentant tots els seus elements identificant
     * correctament els dígrafs.
     *
     * @param word Paraula a segmentar.
     *
     * @return Una llista amb els elements ja segmentats.
     */
    private List<String> tokenize(String word) {
        word = word.trim().toUpperCase();
        List<String> tokens = new ArrayList<>();
        int i = 0;

        while (i < word.length()) {
            if (useDigraphs) {
                if (i + 3 <= word.length()) {
                    String trigraf = word.substring(i, i + 3);
                    if (DIGRAFS.contains(trigraf)) {
                        tokens.add(trigraf);
                        i += 3;
                        continue;
                    }
                }

                if (i + 2 <= word.length()) {
                    String digraf = word.substring(i, i + 2);
                    if (DIGRAFS.contains(digraf)) {
                        tokens.add(digraf);
                        i += 2;
                        continue;
                    }
                }
            }
            // Si no és dígraf, afegeix una sola lletra
            String letter = Character.toString(word.charAt(i));
            // Evitem afegir lletres buides o espais
            if (!letter.trim().isEmpty()) tokens.add(letter);
            i++;
        }
        return tokens;
    }

    /**
     * Modifica el DAWG perquè reconegui la paraula d'entrada com a pertanyent al
     * lèxic.
     *
     * Segmenta la paraula d'entrada en els seus elements i genera i modifica els
     * nodes i aristes que calguin per a què la paraula estigui reconegida pel DAWG
     *
     * @param word Indica la paraula que s'inserirà al DAWG
     */
    private void insertWord(String word) {

        int indexNode = 0;
        List<String> wordTokens = tokenize(word);
        int wordLength = wordTokens.size();

        for (int t = 0; t < wordLength; t++) {
            String token = wordTokens.get(t);
            boolean isLastToken = (t == wordTokens.size() - 1);

            // Comprovem si ja existeix un Edge amb aquesta transició no terminal al Node actual
            boolean edgeExists = false;
            boolean successor = false;
            int n_edges = nodes.get(indexNode).howManyEdges();
            int i = n_edges - 1;

            while (i != -1) {
                if (nodes.get(indexNode).edges.get(i).letter.equals(token)) {
                    edgeExists = true;
                    successor = nodes.get(indexNode).edges.get(i).nextNode != -1;
                    if (successor) indexNode = nodes.get(indexNode).edges.get(i).nextNode;
                    break;
                }
                i--;
            }

            // Cas edgeExists and !successor && !isLastToken: creem successor
            if (edgeExists && !successor && !isLastToken) {
                nodes.add(new Node());
                Edge e = nodes.get(indexNode).edges.get(i);
                e.nextNode = nodes.size() - 1;
                indexNode = nodes.size() - 1;
            }
            
            // Cas !edgeExists
            if (!edgeExists) {
                // Si no és última lletra, creem Edge i Node
                if (!isLastToken) {
                    nodes.add(new Node());
                    nodes.get(indexNode).edges.add(new Edge(token, nodes.size() - 1, isLastToken));
                    indexNode = nodes.size() - 1;
                }
                // Si és última lletra, creem Edge sense crear Node
                else nodes.get(indexNode).edges.add(new Edge(token, -1, isLastToken));
            }
        }
    }

    /**
     * Comprova si una paraula està reconeguda pel DAWG.
     *
     * Segmenta la paraula en els seus elements i recorre el DAWG en cerca d'una arista
     * terminal. En cas de no trobar cap camí que dugui a una arista terminal retorna fals,
     * d'altra manera retorna cert.
     *
     * @param word Paraula que volem comprovar si pertany o no al DAWG.
     *
     * @return true si la paraula és reconeguda pel DAWG, false en cas contrari.
     */
    public boolean contains(String word) {
        int indexNode = 0;
        boolean found = false;
        boolean terminal = false;
        List<String> wordTokens = tokenize(word);
        int wordLength = wordTokens.size();

        int t;
        for (t = 0; t < wordLength && indexNode != -1; t++) {
            found = false;
            String token = wordTokens.get(t);
            int i = nodes.get(indexNode).edges.size() - 1;

            // Comprovem si existeix un Edge amb el token que busquem
            while (i != -1) {
                Edge e = nodes.get(indexNode).edges.get(i);
                if (e.letter.equals(token)) {
                    found = true;
                    terminal = e.isTerminal;
                    indexNode = e.nextNode;
                    break;
                }
                i--;
            }

            if (!found) return false;
        }

        // Si no hem consumit la paraula sencera (hem sortit pq indexNode == -1)
        if (t < wordLength) return false;
        // Si hem consumit la paraula sencera:
        return terminal;
    }

    /**
     * Funció que, donada una lletra, retorna el conjunt de lletres que poden precedir-la.
     * Dit d'altra manera, es retornen les lletres que en alguna de les paraules del
     * diccionari venen succeïdes per la lletra donada.
     *
     * @param lletra Lletra de la que s'han de buscar predecessors.
     *
     * @return Un conjunt de lletres que poden precedir en una paraula a la lletra indicada.
     */
    public Set<String> searchPrefixes(String lletra) {

        // Registrem tots els nodes amb la lletra
        Set<Integer> tenenLletra = new HashSet<Integer>();

        // Recorrem tots els Edges de tots els Nodes
        for (int n = 0; n < nodes.size(); ++n) {
            for (int e = nodes.get(n).edges.size() - 1; e >= 0; --e) {
                Edge edge = nodes.get(n).edges.get(e);

                // Si trobem un Edge amb la lletra, guardem el node i saltem al següent
                if (edge.letter.equals(lletra)) {
                    tenenLletra.add(n);
                    break;
                }
            }
        }

        // Un cop registrats, tornem a recorrer buscant nodes que apuntin als del 1r set (prefixes)
        Set<String> ret = new HashSet<String>();

        for (int n = 0; n < nodes.size(); ++n) {
            for (int e = nodes.get(n).edges.size() - 1; e >= 0; --e) {
                Edge edge = nodes.get(n).edges.get(e);

                if (tenenLletra.contains(edge.nextNode))
                    ret.add(edge.letter);
            }
        }

        if (ret.size() != 0) ret.add("#");
        return ret;
    }

    /**
     * Funció que, donada una lletra, retorna el conjunt de lletres que poden succeir-la.
     * Dit d'altra manera, es retornen les lletres que en alguna de les paraules del
     * diccionari venen precedides per la lletra donada.
     *
     * @param lletra Lletra de la que s'han de buscar successors
     *
     * @return Un conjunt de lletres que poden succeir en una paraula a la lletra indicada.
     */
    public Set<String> searchSufixes(String lletra) {
        Set<String> ret = new HashSet<String>();

        // Recorrem tots els Edges de tots els Nodes
        for (int n = 0; n < nodes.size(); ++n) {
            for (int e = nodes.get(n).edges.size() - 1; e >= 0; --e) {
                Edge edge = nodes.get(n).edges.get(e);

                // Si trobem un Edge amb la lletra que busquem
                if (edge.letter.equals(lletra)) {
                    int sufixesNode = edge.nextNode;
                    // Resgistrem tots els edges del seu nextNode (si nextNode != -1)
                    if (sufixesNode != -1) {
                        for (int i = 0; i < nodes.get(sufixesNode).edges.size(); ++i) {
                            ret.add(nodes.get(sufixesNode).edges.get(i).letter);
                        }
                    }
                }
            }
        }

        if (!ret.isEmpty()) ret.add("#");
        return ret;
    }

    /**
     * Donat un Node del DAWG, retorna el conjunt de lletres amb arista sortint.
     *
     * @param nodeIndex Indica l'índex del Node a examinar.
     * 
     * @return Un conjunt de lletres sortints del node indicat.
     */
    public Set<String> getEdgesFromNode(int nodeIndex) {
        Set<String> ret = new HashSet<String>();
        if (nodeIndex == -1) return ret;

        for (Edge e : nodes.get(nodeIndex).edges) {
            ret.add(e.letter);
        }
        return ret;
    }

    /**
     * Donats un node i una lletra, retorna el node destí de la transició o -1 en
     * cas que no existeixi.
     *
     * @param nodeIndex Node orígen
     * @param letter    Lletra de la transició de node.
     * 
     * @return L'índex del node destí
     */
    public int getNodeFromTransition(int nodeIndex, String letter) {
        Node n = nodes.get(nodeIndex);
        
        for (Edge e : n.edges) {
            if (e.letter.equals(letter)) return e.nextNode;
        }
        return -1;
    }

    /**
     * Funció per a imprimir el DAWG.
     *
     * Imprimeix per terminal el DAWG de forma comprensible.
     */
    public void print_dawg() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.get(i).edges.size(); j++) {
                Edge e = nodes.get(i).edges.get(j);
            }
        }
    }
}

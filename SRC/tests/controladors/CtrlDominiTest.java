package controladors;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import domini.scrabble.Partida;
import domini.scrabble.Ranking;
import domini.jugadors.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

/**
 * Test exhaustiu de la classe CtrlDomini utilitzant JUnit 4 i Mockito.
 * Tots els missatges i proves estan en català.
 */
@RunWith(MockitoJUnitRunner.class)
public class CtrlDominiTest {
    @Mock
    private CtrlPartida mockCtrlPartida;

    @Mock
    private Ranking mockRanking;

    private CtrlDomini ctrl;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() throws Exception {
        // Restablir el singleton
        Field instField = CtrlDomini.class.getDeclaredField("instancia");
        instField.setAccessible(true);
        instField.set(null, null);
        ctrl = CtrlDomini.getInstancia();
        // Injectar mocks
        Field cpField = CtrlDomini.class.getDeclaredField("CtrlPartida");
        cpField.setAccessible(true);
        cpField.set(ctrl, mockCtrlPartida);
        Field rField = CtrlDomini.class.getDeclaredField("ranking");
        rField.setAccessible(true);
        rField.set(ctrl, mockRanking);
        // Redirigir sortides
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testCrearJugadorValida() {
        ctrl.crearJugador("usuari", "pwd");
        assertTrue(ctrl.existeixJugador("usuari"));
        assertTrue(ctrl.iniciarSessio("usuari", "pwd"));
        Integer id = ctrl.obtenirIdJugador("usuari", "pwd");
        assertNotNull(id);
    }

    @Test
    public void testCrearJugadorNomBuit() {
        ctrl.crearJugador("", "pwd");
        String sortida = outContent.toString();
        assertTrue(sortida.contains("Error: El nom d'usuari no pot estar buit."));
        assertFalse(ctrl.existeixJugador(""));
    }

    @Test
    public void testCrearJugadorContrasenyaBuida() {
        ctrl.crearJugador("usuari2", "");
        String sortida = outContent.toString();
        assertTrue(sortida.contains("Error: La contrasenya no pot estar buida."));
        assertFalse(ctrl.iniciarSessio("usuari2", ""));
    }

    @Test
    public void testExisteixJugadorSenseRegistrar() {
        assertFalse(ctrl.existeixJugador("George"));
    }
  
    @Test
    public void testEliminarJugador() {
        ctrl.crearJugador("elim", "123");
        assertTrue(ctrl.existeixJugador("elim"));
        boolean resultat = ctrl.eliminarJugador("elim", "123");
        assertTrue(resultat);
        assertFalse(ctrl.existeixJugador("elim"));
    }

    @Test
    public void testEliminarJugadorNoExisteix() {
        assertFalse(ctrl.eliminarJugador("Ivan", "pw"));
    }
  
    @Test
    public void testIniciarSessioCorreta() {
        ctrl.crearJugador("Dave", "secret");
        assertTrue(ctrl.iniciarSessio("Dave", "secret"));
    }

    @Test
    public void testIniciarSessioUsuariNoExisteix() {
        assertFalse(ctrl.iniciarSessio("Eve", "none"));
    }

    @Test
    public void testIniciarSessioContrasenyaIncorrecta() {
        ctrl.crearJugador("Frank", "mypwd");
        assertFalse(ctrl.iniciarSessio("Frank", "wrongpwd"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrearPartidaUsuariNoRegistrat() {
        ctrl.CrearPartida("NonReg", "x", "MAQUINA", "*", "1", 5);
    }
  
    @Test
    public void testCrearPartidaJugadorVsJugador() throws IOException {
        // Crear i registrar dos jugadors
        ctrl.crearJugador("j1", "p1");
        ctrl.crearJugador("j2", "p2");
        Partida stubPartida = mock(Partida.class);
        when(mockCtrlPartida.crearPartida(any(), any(), eq("1"), eq(5), eq(1))).thenReturn(stubPartida);

        ctrl.CrearPartida("j1", "p1", "j2", "p2", "1", 5);
        Partida partidaObtinguda = ctrl.getPartida(1);
        assertNotNull(partidaObtinguda);
        assertEquals(stubPartida, partidaObtinguda);
        verify(mockRanking).iniciarPartida(1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrearPartidaIdiomaInvalid() {
        ctrl.crearJugador("a", "1");
        ctrl.crearJugador("b", "2");
        ctrl.CrearPartida("a", "1", "b", "2", "x", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrearPartidaMidaInvalid() {
        ctrl.crearJugador("c", "1");
        ctrl.crearJugador("d", "2");
        ctrl.CrearPartida("c", "1", "d", "2", "1", 4);
    }

    @Test
    public void testGetIdsPartides() throws IOException {
        ctrl.crearJugador("p1", "1");
        ctrl.crearJugador("p2", "2");
        Partida stub = mock(Partida.class);
        when(mockCtrlPartida.crearPartida(any(), any(), anyString(), anyInt(), anyInt())).thenReturn(stub);
        ctrl.CrearPartida("p1", "1", "p2", "2", "1", 5);
        Set<Integer> ids = ctrl.getIdsPartides();
        assertTrue(ids.contains(1));
        try {
            ids.add(2);
            fail("getIdsPartides ha de retornar un Set immutable");
        } catch (UnsupportedOperationException e) {
            // correcte
        }
    }

    @Test
    public void testReprendrePartidaSensePartides() {
        ctrl.ReprendrePartida();
        String msg = outContent.toString();
        assertTrue(msg.contains("Error: No hi ha cap partida registrada."));
    }

    @Test
    public void testReprendrePartidaFinalitzada() throws Exception {
        // Injectem una partida finalitzada
        Partida mockFinal = mock(Partida.class);
        when(mockFinal.estaFinalitzada()).thenReturn(true);
        Field partsField = CtrlDomini.class.getDeclaredField("partides");
        partsField.setAccessible(true);
        HashMap<Integer, Partida> mapa = new HashMap<>();
        mapa.put(1, mockFinal);
        partsField.set(ctrl, mapa);
        Field idField = CtrlDomini.class.getDeclaredField("idPartidaRecientCreada");
        idField.setAccessible(true);
        idField.setInt(ctrl, 1);
        ctrl.ReprendrePartida();
        String msg = outContent.toString();
        assertTrue(msg.contains("Error: La partida amb ID 1 ja ha estat finalitzada."));
    }

    @Test
    public void testReprendrePartidaExitosa() throws Exception {
        Partida mockAct = mock(Partida.class);
        when(mockAct.estaFinalitzada()).thenReturn(false);
        // Assegurem que encurs no llenci excepció
        doNothing().when(mockAct).encurs();
        Field partsField = CtrlDomini.class.getDeclaredField("partides");
        partsField.setAccessible(true);
        HashMap<Integer, Partida> mapa = new HashMap<>();
        mapa.put(42, mockAct);
        partsField.set(ctrl, mapa);
        Field idField = CtrlDomini.class.getDeclaredField("idPartidaRecientCreada");
        idField.setAccessible(true);
        idField.setInt(ctrl, 42);
        ctrl.ReprendrePartida();
        verify(mockAct).encurs();
        String msg = outContent.toString();
        assertTrue(msg.contains("ha estat reanudada amb èxit."));
    }

    @Test
    public void testMostrarEstadistiquesJugadorInexistent() {
        ctrl.mostrarEstadistiques("noexisteix");
        // No ha de llençar res ni imprimir res significatiu
        assertTrue(outContent.toString().isEmpty());
    }

    @Test
    public void testMostrarRankingGlobalLimitat() {
        ctrl.mostrarRankingGlobalLimitat(3);
        verify(mockRanking).mostrarRankingGlobalLimitat(3);
    }

    @Test
    public void testActualitzarRankingPartidaNoExisteix() {
        ctrl.actualitzarRanking(99, mock(Jugador.class), 5, mock(Jugador.class), 3);
        String msg = outContent.toString();
        assertTrue(msg.contains("[ERROR] Partida no trobada amb ID: 99"));
    }

    @Test
    public void testActualitzarRankingInicialBuit() throws Exception {
        // Creem una partida dummy al Ctrl
        Partida stub = mock(Partida.class);
        Field partsField = CtrlDomini.class.getDeclaredField("partides");
        partsField.setAccessible(true);
        HashMap<Integer, Partida> mapa = new HashMap<>();
        mapa.put(7, stub);
        partsField.set(ctrl, mapa);
        // Ranking buit
        when(mockRanking.estaBuit()).thenReturn(true);
        Jugador j1 = mock(Jugador.class);
        when(j1.getIdJugador()).thenReturn(1);
        Jugador j2 = mock(Jugador.class);
        when(j2.getIdJugador()).thenReturn(2);
        ctrl.actualitzarRanking(7, j1, 10, j2, 8);
        verify(mockRanking).iniciarPartida(1, 2);
        verify(mockRanking).actualitzarPuntsPartidaLocal(1, 10);
        verify(mockRanking).actualitzarPuntsPartidaLocal(2, 8);
    }
}

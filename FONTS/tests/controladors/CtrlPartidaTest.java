package controladors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import domini.scrabble.Bossa;
import domini.scrabble.Fitxa;
import domini.scrabble.Taulell;
import domini.scrabble.Casella;
import domini.scrabble.Partida;
import domini.jugadors.Avatar;
import domini.diccionari.DAWG;

@RunWith(MockitoJUnitRunner.class)
public class CtrlPartidaTest {

    @Mock
    private CtrlDomini mockCtrlDomini;
    @Mock
    private Partida mockPartida;
    @Mock
    private DAWG mockDiccionari;
    @Mock
    private Avatar mockJugador1;
    @Mock
    private Avatar mockJugador2;
    @Mock
    private Bossa mockBossa;
    @Mock
    private Taulell mockTaulell;
    @Mock
    private Casella mockCasella;

    private CtrlPartida ctrl;

    @Before
    public void setUp() throws Exception {
        ctrl = new CtrlPartida(mockCtrlDomini);

        setField(ctrl, "partida", mockPartida);
        setField(ctrl, "diccionari", mockDiccionari);
        setField(ctrl, "jugador1", mockJugador1);
        setField(ctrl, "jugador2", mockJugador2);
        setField(ctrl, "torn", 1);

        when(mockPartida.getJugadorActual(1)).thenReturn(mockJugador1);
        when(mockPartida.getTaulell()).thenReturn(mockTaulell);
        when(mockPartida.getBossa()).thenReturn(mockBossa);
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field f = CtrlPartida.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    public void testCanviarFitxesSenseFitxes() {
        assertFalse(ctrl.canviarFitxes(Collections.emptyList(), 0, false, "", ""));
    }

    @Test
    public void testCanviarFitxesExitós() {
        List<String> fitxes = Arrays.asList("A","B");
        Fitxa fA = mock(Fitxa.class);
        Fitxa fB = mock(Fitxa.class);
        when(mockJugador1.eliminarFitxa("A")).thenReturn(fA);
        when(mockJugador1.eliminarFitxa("B")).thenReturn(fB);
        Fitxa nova1 = mock(Fitxa.class);
        Fitxa nova2 = mock(Fitxa.class);
        when(mockBossa.agafarFitxa()).thenReturn(nova1).thenReturn(nova2);

        assertTrue(ctrl.canviarFitxes(fitxes,2,false,"",""));
        verify(mockJugador1).reiniciarTornsPassatsConsecutius();
    }

    @Test
    public void testCanviarFitxesBossaBuida() {
        List<String> fitxes = Collections.singletonList("A");
        when(mockJugador1.eliminarFitxa("A")).thenReturn(mock(Fitxa.class));
        when(mockBossa.agafarFitxa()).thenReturn(null);

        assertFalse(ctrl.canviarFitxes(fitxes,1,false,"",""));
    }

    @Test
    public void testPassarTornIIncrementar() throws Exception {
        CtrlPartida spy = Mockito.spy(ctrl);
        // assegurem torn inicial a 1
        setField(spy, "torn", 1);
        doReturn(true).when(spy).finalitzarPerTornsConsecutius();

        boolean acabat = spy.passarTorn();
        assertTrue(acabat);
        assertEquals(2, spy.controladorTORN());
        verify(mockJugador1).incrementarTornsPassatsConsecutius();
    }

    @Test
    public void testIncrementarTorn() {
        ctrl = Mockito.spy(ctrl);
        doCallRealMethod().when(ctrl).incrementarTorn();
        // Usar setter via reflexió
        try {
            setField(ctrl, "torn", 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ctrl.incrementarTorn();
        assertEquals(6, ctrl.controladorTORN());
    }

    @Test
    public void testContraMaquina() {
        when(mockJugador1.getIdJugador()).thenReturn(-1);
        when(mockJugador2.getIdJugador()).thenReturn(2);
        assertTrue(ctrl.contraMaquina());
        when(mockJugador1.getIdJugador()).thenReturn(1);
        when(mockJugador2.getIdJugador()).thenReturn(2);
        assertFalse(ctrl.contraMaquina());
    }

    @Test
    public void testGetLletresJugadorTorn() {
        List<Fitxa> llista = Arrays.asList(mock(Fitxa.class));
        when(mockPartida.getFitxesActuals(1)).thenReturn(llista);
        assertSame(llista, ctrl.getLletresJugadorTorn());
    }

    @Test
    public void testImprimirTaulell() {
        ctrl.imprimirTaulell();
        verify(mockPartida).imprimirtaulell();
    }

    @Test
    public void testGetBossa1() {
        when(mockPartida.getBossa()).thenReturn(mockBossa);
        assertSame(mockBossa, ctrl.getBossa1());
    }

    @Test
    public void testFinalitzarPausarEncurs() {
        ctrl.finalitzarCt(); verify(mockPartida).finalitzar();
        ctrl.pausarCt(); verify(mockPartida).pausar();
        ctrl.encursCt(); verify(mockPartida).encurs();
    }

    @Test
    public void testGettersJugadorsIValors() throws Exception {
        when(mockPartida.getJugadorActual(2)).thenReturn(mockJugador2);
        when(mockJugador1.getNom()).thenReturn("J1");
        when(mockJugador2.getNom()).thenReturn("J2");
        when(mockJugador1.getIdJugador()).thenReturn(10);
        when(mockJugador1.getFitxes_actuals()).thenReturn(Arrays.asList(mock(Fitxa.class),mock(Fitxa.class)));
        assertEquals("J1",ctrl.jugador_controladorNOM());
        assertEquals("J2",ctrl.jugadorNoActual_controladorNOM());
        assertEquals(10,ctrl.jugador_controladorID());
        assertEquals(2,ctrl.jugador_controladorNUM_FITXES());
        assertEquals(1,ctrl.controladorTORN());

        when(mockJugador1.getNom()).thenReturn("J1");
        when(mockJugador2.getNom()).thenReturn("J2");
        when(mockJugador1.getIdJugador()).thenReturn(10);
        when(mockJugador1.getFitxes_actuals()).thenReturn(Arrays.asList(mock(Fitxa.class),mock(Fitxa.class)));
        assertEquals("J1",ctrl.jugador_controladorNOM());
        assertEquals("J2",ctrl.jugadorNoActual_controladorNOM());
        assertEquals(10,ctrl.jugador_controladorID());
        assertEquals(2,ctrl.jugador_controladorNUM_FITXES());
        assertEquals(1,ctrl.controladorTORN());
    }

    @Test
    public void testComprovaCrosschecks() {
        Fitxa fx = mock(Fitxa.class);
        when(fx.getLletra()).thenReturn("A");
        List<Fitxa> paraula = Collections.singletonList(fx);
        when(mockTaulell.getCasella(0,1)).thenReturn(mockCasella);
        when(mockCasella.getCrossChecks()).thenReturn(new boolean[]{true});
        assertTrue(ctrl.comprova_crosschecks(paraula,0,1,true,mockTaulell));
        when(mockCasella.getCrossChecks()).thenReturn(new boolean[]{false});
        assertFalse(ctrl.comprova_crosschecks(paraula,0,1,true,mockTaulell));
    }

    @Test
    public void testFerJugadaMaquinaSenseMaquina() {
        when(mockJugador1.getIdJugador()).thenReturn(5);
        ctrl.ferJugadaMaquina();
        verify(mockPartida, never()).ferJugadaMaquina(any(DAWG.class));
    }

    @Test
public void testFerJugadaMaquinaAmbMaquinaIPunts() throws Exception {
    when(mockJugador1.getIdJugador()).thenReturn(-1);
    when(mockPartida.ferJugadaMaquina(mockDiccionari)).thenReturn(10);

    ctrl.ferJugadaMaquina();

    // S'espera que es delegui en Partida per gestionar la puntuació
    verify(mockPartida).ferJugadaMaquina(mockDiccionari);
    // No cridem canviarFitxes ni passarTorn en aquest cas
    verify(mockPartida, never()).inicialitzarJugadors();
}


    @Test
    public void testFerJugadaMaquinaCanviarFitxes() throws Exception {
        when(mockJugador1.getIdJugador()).thenReturn(-1);
        when(mockPartida.ferJugadaMaquina(mockDiccionari)).thenReturn(0);
        CtrlPartida spy = Mockito.spy(ctrl);
        doReturn(false).when(spy).canviarFitxes(anyList(), anyInt(), anyBoolean(), anyString(), anyString());
        spy.ferJugadaMaquina();
        verify(spy).passarTorn();
    }

    @Test
    public void testFinalitzarPerLletresEsgotades() {
        when(mockPartida.getJugadorActual(1)).thenReturn(mockJugador1);
        when(mockJugador1.getFitxes_actuals()).thenReturn(Collections.emptyList());
        when(mockPartida.getBossa()).thenReturn(mockBossa);
        when(mockBossa.agafarFitxa()).thenReturn(null);
        when(mockJugador2.getIdJugador()).thenReturn(2);
        when(mockPartida.calcularPuntuacionsFitxes(mockJugador2)).thenReturn(5);
        when(mockPartida.getPuntuacioJugador1()).thenReturn(5);
        when(mockPartida.getPuntuacioJugador2()).thenReturn(0);

        boolean acabat = ctrl.finalitzarPerLletresEsgotades();
        assertTrue(acabat);
        verify(mockCtrlDomini).actualitzarRanking(eq(mockPartida.getIdPartida()), eq(mockJugador1), anyInt(), eq(mockJugador2), anyInt());
        verify(mockPartida).finalitzarPartida();
    }

    @Test
    public void testFinalitzarPerTornsConsecutius() {
        when(mockJugador1.getTornsPassatsConsecutius()).thenReturn(3);
        when(mockPartida.getPuntuacioJugador1()).thenReturn(0);
        when(mockPartida.getPuntuacioJugador2()).thenReturn(0);

        boolean acabat = ctrl.finalitzarPerTornsConsecutius();
        assertTrue(acabat);
        verify(mockCtrlDomini).actualitzarRanking(eq(mockPartida.getIdPartida()), eq(mockJugador1), anyInt(), eq(mockJugador2), anyInt());
        verify(mockPartida).finalitzarPartida();
    }

    @Test
    public void testAbandonarPartida() {
        when(mockPartida.getJugadorActual(1)).thenReturn(mockJugador1);
        when(mockJugador1.getNom()).thenReturn("X");
        when(mockJugador1.getIdJugador()).thenReturn(1);
        when(mockJugador2.getIdJugador()).thenReturn(2);
        when(mockPartida.getPuntuacioJugador1()).thenReturn(10);
        when(mockPartida.getPuntuacioJugador2()).thenReturn(5);

        ctrl.abandonarPartida();
        verify(mockCtrlDomini).actualitzarRanking(eq(mockPartida.getIdPartida()), eq(mockJugador1), anyInt(), eq(mockJugador2), anyInt());
        verify(mockPartida).finalitzarPartida();
    }
}

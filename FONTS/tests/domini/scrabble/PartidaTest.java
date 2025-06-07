package domini.scrabble;

import domini.jugadors.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartidaTest {
    private Partida partida;
    private Avatar jugador1;
    private Avatar jugador2;
    private static final int MIDA_TAULELL = 15;
    private static final String IDIOMA = "1"; // Català
    private static final int ID_PARTIDA = 1;
    
    private class JugadorMock extends Avatar {
        private List<Fitxa> fitxes;
        private int tornsPassats;
        
        public JugadorMock(int id, String nom, String contrasenya) {
            super(id, nom, contrasenya);
            this.fitxes = new ArrayList<>();
            this.tornsPassats = 0;
        }
        
        @Override
        public void reiniciarTornsPassatsConsecutius() {
            this.tornsPassats = 0;
        }
        
        @Override
        public List<Fitxa> getFitxes_actuals() {
            return fitxes;
        }
    }

    @Before
    public void setUp() throws IOException {
        jugador1 = new JugadorMock(1, "Jugador1", "contrasenya1");
        jugador2 = new JugadorMock(2, "Jugador2", "contrasenya2");
        partida = new Partida(jugador1, jugador2, MIDA_TAULELL, IDIOMA, ID_PARTIDA);
    }

    @Test
    public void testConstructor() {
        assertNotNull("La partida hauria de crear-se correctament", partida);
        assertEquals("L'ID de la partida hauria de ser l'especificat", ID_PARTIDA, partida.getIdPartida());
        assertEquals("La puntuació inicial del jugador 1 hauria de ser 0", 0, partida.getPuntuacioJugador1());
        assertEquals("La puntuació inicial del jugador 2 hauria de ser 0", 0, partida.getPuntuacioJugador2());
    }

    @Test
    public void testIncrementarPuntuacio() {
        int puntuacioInicial1 = partida.getPuntuacioJugador1();
        int puntuacioInicial2 = partida.getPuntuacioJugador2();
        int incremento = 10;
        
        partida.incrementar_puntuacio_partida(incremento, jugador1);
        assertEquals("La puntuació del jugador 1 hauria d'incrementar-se correctament", puntuacioInicial1 + incremento, partida.getPuntuacioJugador1());
        
        partida.incrementar_puntuacio_partida(incremento, jugador2);
        assertEquals("La puntuació del jugador 2 hauria d'incrementar-se correctament", puntuacioInicial2 + incremento, partida.getPuntuacioJugador2());
    }

    @Test
    public void testReiniciarPuntuacio() {
        partida.incrementar_puntuacio_partida(10, jugador1);
        partida.incrementar_puntuacio_partida(10, jugador2);
        
        partida.reiniciar_puntuacio_partida(jugador1);
        partida.reiniciar_puntuacio_partida(jugador2);
        
        assertEquals("La puntuació del jugador 1 hauria de reiniciar-se a 0", 0, partida.getPuntuacioJugador1());
        assertEquals("La puntuació del jugador 2 hauria de reiniciar-se a 0", 0, partida.getPuntuacioJugador2());
    }

    @Test
    public void testGetJugadorActual() {
        assertEquals("En el primer torn  hauria de ser el jugador 1", jugador1, partida.getJugadorActual(1));
        assertEquals("En el segon torn hauria de ser el jugador 2", jugador2, partida.getJugadorActual(2));
    }

    @Test
    public void testTransicionesEstado() {
        assertTrue("La partida hauria de poder pausar-se des de estat en curs", partida.canviarEstat(new EstatPausada(partida)));
        
        assertTrue("La partida hauria de poder tornar a estat en curs desde pausa", partida.canviarEstat(new EstatEnCurs(partida)));
        
        assertTrue("La partida hauria de poder finalizar-se", partida.canviarEstat(new EstatFinalitzada(partida)));
        
        assertFalse("No s'haurien de permetre canvis d'estat un cop finalizada", partida.canviarEstat(new EstatEnCurs(partida)));
    }

    @Test
    public void testCalcularPuntuacionsFitxes() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(1, "A", 1));
        fitxes.add(new Fitxa(2, "B", 2));
        ((JugadorMock)jugador1).fitxes = fitxes;
        
        assertEquals("La suma de puntuaciones hauria de ser correcta", 3, partida.calcularPuntuacionsFitxes(jugador1));
    }

    @Test
    public void testFinalitzarPartida() {
        assertTrue("La partida hauria de poder finalizar-se", partida.finalitzarPartida());
        assertTrue("La partida hauria d'estar en estat finalizada", partida.estaFinalitzada());
    }

    @Test
    public void testGetters() {
        assertNotNull("Hauria de retornar el taulell", partida.getTaulell());
        assertNotNull("Hauria de retornar la bossa", partida.getBossa());
        assertEquals("Hauria de retornar el jugador 1", jugador1, partida.getJugador1());
        assertEquals("Hauria de retornar el jugador 2", jugador2, partida.getJugador2());
        assertNotNull("Hauria de retornar l'estat", partida.getEstat());
    }

    @Test
    public void testEstadoInicial() {
        assertTrue("L'estat inicial hauria de ser EstatEnCurs", partida.getEstat() instanceof EstatEnCurs);
    }
}

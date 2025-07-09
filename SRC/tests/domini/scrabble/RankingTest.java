package domini.scrabble;

import domini.jugadors.Jugador;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class RankingTest {
    private Ranking ranking;
    private Map<Integer, Jugador> jugadors;
    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private class JugadorMock extends Jugador {
        private int puntuacioActual;
        private final String nom;

        public JugadorMock(int id, String nom, int puntuacioInicial) {
            super(id, "j1", "j1");
            this.nom = nom;
            this.puntuacioActual = puntuacioInicial;
        }

        @Override
        public int getPuntuacio_actual() {
            return puntuacioActual;
        }

        @Override
        public String getNom() {
            return nom;
        }
    }

    @Before
    public void setUp() {
        jugadors = new HashMap<>();
        // Crear alguns jugadors de pruova
        jugadors.put(1, new JugadorMock(1, "Jugador1", 100));
        jugadors.put(2, new JugadorMock(2, "Jugador2", 200));
        jugadors.put(3, new JugadorMock(3, "Jugador3", 300));
        
        ranking = new Ranking(jugadors);
        System.setOut(new PrintStream(outputContent));
    }

    @Test
    public void testIniciarPartida() {
        ranking.iniciarPartida(1, 2);
        assertFalse("El ranking local no hauria d'estar buit", ranking.estaBuit());
        
        ranking.mostrarRankingPartidaActual();
        String output = outputContent.toString();
        assertTrue("Han d'apareixer dos jugadors", output.contains("Jugador1") && output.contains("Jugador2"));
    }

    @Test
    public void testIniciarPartidaConMaquina() {
        ranking.iniciarPartida(-1, 1);
        assertFalse("El ranking local no hauria d'estar buit", ranking.estaBuit());
        
        ranking.mostrarRankingPartidaActual();
        String output = outputContent.toString();
        assertTrue("Només hauria d'apareixer el jugador humà",output.contains("Jugador1"));
        assertFalse("No hauria d'apareixer la màquina (-1)", output.contains("-1"));
    }

    @Test
    public void testActualizarPuntuacionLocal() {
        ranking.iniciarPartida(1, 2);
        ranking.actualitzarPuntsPartidaLocal(1, 50);
        ranking.actualitzarPuntsPartidaLocal(2, 30);
        
        ranking.mostrarRankingPartidaActual();
        String output = outputContent.toString();
        assertTrue("Hauria de mostrar la puntuació actualizada del Jugador1", output.contains("Jugador1") && output.contains("50"));
        assertTrue("Hauria de mostrar la puntuació actualizada del Jugador2", output.contains("Jugador2") && output.contains("30"));
    }

    @Test
    public void testActualizarPuntuacionGlobal() {
        int puntuacionInicial = ((JugadorMock)jugadors.get(1)).puntuacioActual;
        ranking.actualitzarPuntsPartidaGlobal(1, 50);
        
        ranking.mostrarRankingGlobal();
        String output = outputContent.toString();
        assertTrue("Hauria de mostrar la puntuació actualizada en el ranking global", output.contains("Jugador1") && output.contains(String.valueOf(puntuacionInicial + 50)));
    }

    @Test
    public void testRankingGlobalLimitado() {
        ranking.mostrarRankingGlobalLimitat(2);
        String output = outputContent.toString();
        assertTrue("Hauria de mostrar només els dos primers jugadores", output.contains("Jugador3") && output.contains("Jugador2"));
        assertFalse("No hauria de mostrar el tercer jugador", output.contains("Jugador1"));
    }

    @Test
    public void testEliminarJugador() {
        ranking.eliminarJugador(1);
        ranking.mostrarRankingGlobal();
        String output = outputContent.toString();
        assertFalse("El jugador eliminat no hauria d'aparéixer al ranquing", output.contains("Jugador1"));
        assertTrue("Els demés jugadors haurien de seguir al ranquing", output.contains("Jugador2") && output.contains("Jugador3"));
    }

    @Test
    public void testRankingVacio() {
        ranking = new Ranking(new HashMap<>());
        assertTrue("Un ranquing sense jugadors hauria de ser buit", ranking.estaBuit());
    }

    @Test
    public void testOrdenPuntuaciones() {
        ranking.iniciarPartida(1, 2);
        ranking.actualitzarPuntsPartidaLocal(1, 100);
        ranking.actualitzarPuntsPartidaLocal(2, 200);
        
        ranking.mostrarRankingPartidaActual();
        String output = outputContent.toString();
        int pos1 = output.indexOf("Jugador1");
        int pos2 = output.indexOf("Jugador2");
        assertTrue("El jugador amb més puntuació hauria d'apareixer primer", pos2 < pos1);
    }

    @Test
    public void testActualizacionMultiple() {
        ranking.iniciarPartida(1, 2);
        ranking.actualitzarPuntsPartidaLocal(1, 50);
        ranking.actualitzarPuntsPartidaLocal(1, 100);
        
        ranking.mostrarRankingPartidaActual();
        String output = outputContent.toString();
        assertTrue("Hauria de mostrar la última puntuació actualizada", output.contains("100") && !output.contains("50"));
    }

    @Test
    public void testJugadorInexistente() {
        ranking.actualitzarPuntsPartidaGlobal(999, 100);
        ranking.mostrarRankingGlobal();
        String output = outputContent.toString();
        assertFalse("No hauria de mostrar un jugador que no existeix", output.contains("999"));
    }
}

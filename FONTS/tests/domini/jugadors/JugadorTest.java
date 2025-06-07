package domini.jugadors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import domini.scrabble.Fitxa;
import java.util.ArrayList;
import java.util.List;

public class JugadorTest {
    private Jugador jugador;
    
    // Clase concreta para testing
    private class JugadorTestImpl extends Jugador {
        public JugadorTestImpl(int idJugador, String nom, String contrasenya) {
            super(idJugador, nom, contrasenya);
        }
    }
    
    @Before
    public void setUp() {
        jugador = new JugadorTestImpl(1, "TestUser", "password123");
    }

    @Test
    public void testConstructor() {
        assertEquals(1, jugador.getIdJugador());
        assertEquals("TestUser", jugador.getNom());
        assertEquals("password123", jugador.getContrasenya());
        assertEquals(0, jugador.getPuntuacio_actual());
        assertTrue(jugador.getFitxes_actuals().isEmpty());
        assertEquals(0, jugador.getTornsPassatsConsecutius());
    }

    @Test
    public void testSetAndGetFitxesActuals() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(0, "A", 1));
        fitxes.add(new Fitxa(1, "B", 3));
        
        jugador.setFitxesActuals(fitxes);
        
        List<Fitxa> fitxesRetornades = jugador.getFitxes_actuals();
        assertEquals(2, fitxesRetornades.size());
        assertEquals('A', fitxesRetornades.get(0).getLletra().charAt(0));
        assertEquals('B', fitxesRetornades.get(1).getLletra().charAt(0));
    }

    @Test
    public void testBuidarFitxesJugador() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(2, "A", 1));
        jugador.setFitxesActuals(fitxes);
        
        jugador.buidarFitxesJugador();
        assertTrue(jugador.getFitxes_actuals().isEmpty());
    }

    @Test
    public void testIncrementarPuntuacioActual() {
        assertEquals(0, jugador.getPuntuacio_actual());
        
        jugador.incrementar_puntuacio_actual(10);
        assertEquals(10, jugador.getPuntuacio_actual());
        
        jugador.incrementar_puntuacio_actual(5);
        assertEquals(15, jugador.getPuntuacio_actual());
    }

    @Test
    public void testAfegirFitxa() {
        Fitxa fitxa = new Fitxa(3, "A", 1);
        
        // Provar d'afegir el màxim permès (7 fitxes)
        for (int i = 0; i < 7; i++) {
            assertTrue(jugador.afegirFitxa(new Fitxa(4, "A", 1)));
        }
        
        // Intentar afegir una fitxa més (hauria de fallar)
        assertFalse(jugador.afegirFitxa(fitxa));
    }

    @Test
    public void testEliminarFitxa() {
        Fitxa fitxaA = new Fitxa(5, "A", 1);
        Fitxa fitxaB = new Fitxa(6, "B", 3);
        
        jugador.afegirFitxa(fitxaA);
        jugador.afegirFitxa(fitxaB);
        
        // Eliminar fitxa existent
        Fitxa fitxaEliminada = jugador.eliminarFitxa("A");
        assertNotNull(fitxaEliminada);
        assertEquals('A', fitxaEliminada.getLletra().charAt(0));
        assertEquals(1, jugador.getFitxes_actuals().size());
        
        // Intentar eliminar fitxa que no existeix
        fitxaEliminada = jugador.eliminarFitxa("Ñ");
        assertNull(fitxaEliminada);
    }

    @Test
    public void testTornsPassatsConsecutius() {
        assertEquals(0, jugador.getTornsPassatsConsecutius());
        
        jugador.incrementarTornsPassatsConsecutius();
        assertEquals(1, jugador.getTornsPassatsConsecutius());
        
        jugador.incrementarTornsPassatsConsecutius();
        assertEquals(2, jugador.getTornsPassatsConsecutius());
        
        jugador.reiniciarTornsPassatsConsecutius();
        assertEquals(0, jugador.getTornsPassatsConsecutius());
    }

    @Test
    public void testCompararContrasenya() {
        assertTrue(jugador.compararContrasenya("password123"));
        assertFalse(jugador.compararContrasenya("wrongpassword"));
        assertFalse(jugador.compararContrasenya(""));
        assertFalse(jugador.compararContrasenya(null));
    }

    @Test
    public void testInmutabilidadFitxesActuals() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(7,"A", 1));
        jugador.setFitxesActuals(fitxes);
        
        // Modificar la llista original no hauria d'afectar al Jugador
        fitxes.add(new Fitxa(8, "B", 3));
        assertEquals(1, jugador.getFitxes_actuals().size());
        
        // Modificar la llista retornada no hauria d'afectar al Jugador
        List<Fitxa> fitxesRetornades = jugador.getFitxes_actuals();
        fitxesRetornades.add(new Fitxa(9, "C", 4));
        assertEquals(1, jugador.getFitxes_actuals().size());
    }
}
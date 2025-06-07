package domini.jugadors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AvatarTest {
    private Avatar avatar;
    
    @Before
    public void setUp() {
        avatar = new Avatar(1, "TestUser", "password123");
    }

    @Test
    public void testConstructor() {
        assertEquals(1, avatar.getIdJugador());
        assertEquals("TestUser", avatar.getNom());
        assertEquals("password123", avatar.getContrasenya());
        assertEquals(0, avatar.getPartidesJugades());
        assertEquals(0, avatar.getPartides_guanyades());
        assertEquals(0, avatar.getPartides_guanyades_maquina());
        assertEquals(0, avatar.getPartides_guanyades_jugador());
        assertEquals(0, avatar.getPartides_jugades_maquina());
        assertEquals(0, avatar.getPartides_jugades_jugador());
        assertEquals(0, avatar.getPuntuacio_max_paraula());
        assertEquals(0, avatar.getPartides_abandonades());
        assertNull(avatar.getParaula_llarga());
    }

    @Test
    public void testIncrementarPartidesJugades() {
        avatar.incrementarPartidesJugades();
        assertEquals(1, avatar.getPartidesJugades());
        
        avatar.incrementarPartidesJugades();
        assertEquals(2, avatar.getPartidesJugades());
    }

    @Test
    public void testIncrementarPartidesGuanyades() {
        avatar.incrementarPartidesGuanyades();
        assertEquals(1, avatar.getPartides_guanyades());
        
        avatar.incrementarPartidesGuanyades();
        assertEquals(2, avatar.getPartides_guanyades());
    }

    @Test
    public void testIncrementarPartidesGuanyadesMaquina() {
        avatar.incrementarPartidesGuanyadesMaquina();
        assertEquals(1, avatar.getPartides_guanyades_maquina());
        
        avatar.incrementarPartidesGuanyadesMaquina();
        assertEquals(2, avatar.getPartides_guanyades_maquina());
    }

    @Test
    public void testIncrementarPartidesGuanyadesJugador() {
        avatar.incrementarPartidesGuanyadesJugador();
        assertEquals(1, avatar.getPartides_guanyades_jugador());
        
        avatar.incrementarPartidesGuanyadesJugador();
        assertEquals(2, avatar.getPartides_guanyades_jugador());
    }

    @Test
    public void testIncrementarPartidesJugadesMaquina() {
        avatar.incrementarPartidesJugadesMaquina();
        assertEquals(1, avatar.getPartides_jugades_maquina());
        
        avatar.incrementarPartidesJugadesMaquina();
        assertEquals(2, avatar.getPartides_jugades_maquina());
    }

    @Test
    public void testIncrementarPartidesJugadesJugador() {
        avatar.incrementarPartidesJugadesJugador();
        assertEquals(1, avatar.getPartides_jugades_jugador());
        
        avatar.incrementarPartidesJugadesJugador();
        assertEquals(2, avatar.getPartides_jugades_jugador());
    }

    @Test
    public void testIncrementarPartidesAbandonades() {
        avatar.incrementarPartidesAbandonades();
        assertEquals(1, avatar.getPartides_abandonades());
        
        avatar.incrementarPartidesAbandonades();
        assertEquals(2, avatar.getPartides_abandonades());
    }

    @Test
    public void testActualitzarPuntuacioMaxParaula() {
        avatar.actualitzarPuntuacioMaxParaula(10);
        assertEquals(10, avatar.getPuntuacio_max_paraula());
        
        // No hauria d'actualitzar-se amb un valor menor
        avatar.actualitzarPuntuacioMaxParaula(5);
        assertEquals(10, avatar.getPuntuacio_max_paraula());
        
        // Hauria d'actualitzar-se amb un valor major
        avatar.actualitzarPuntuacioMaxParaula(15);
        assertEquals(15, avatar.getPuntuacio_max_paraula());
    }

    @Test
    public void testSetParaulaMesLlarga() {
        // Cas inicial amb null
        assertNull(avatar.getParaula_llarga());
        
        // Primera paraula
        assertEquals("HOLA", avatar.setParaulaMesLlarga("HOLA"));
        assertEquals("HOLA", avatar.getParaula_llarga());
        
        // Paraula més curta (no hauria d'actualizar)
        assertEquals("HOLA", avatar.setParaulaMesLlarga("SOL"));
        assertEquals("HOLA", avatar.getParaula_llarga());
        
        // Paraula més llarga (hauria d'actualizar)
        assertEquals("PALABRA", avatar.setParaulaMesLlarga("PALABRA"));
        assertEquals("PALABRA", avatar.getParaula_llarga());
        
        // Prova amb null (no hauria d'actualizar)
        assertEquals("PALABRA", avatar.setParaulaMesLlarga(null));
        assertEquals("PALABRA", avatar.getParaula_llarga());
    }

    @Test
    public void testHerenciaJugador() {
        // Provar que hereda correctament de Jugador
        Jugador jugador = avatar;
        assertEquals(1, jugador.getIdJugador());
        assertEquals("TestUser", jugador.getNom());
        assertEquals("password123", jugador.getContrasenya());
    }
}

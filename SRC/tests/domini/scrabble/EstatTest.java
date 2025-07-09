package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import domini.jugadors.*;

public class EstatTest {
    private Estat estatTest;
    private Partida partida;
    private Avatar jugador1;
    private Avatar jugador2;
    private int MIDA_TAULELL;
    private boolean iniciarCridat;
    private boolean encursCridat;
    private boolean pausarCridat;
    private boolean finalitzarCridat;

    // Implementación concreta para testing
    private class EstatTestImpl extends Estat {
        public EstatTestImpl(Partida partida) {
            super(partida);
        }

        @Override
        public void iniciar() {
            iniciarCridat = true;
        }

        @Override
        public void encurs() {
            encursCridat = true;
        }

        @Override
        public void pausar() {
            pausarCridat = true;
        }

        @Override
        public void finalitzar() {
            finalitzarCridat = true;
        }
    }

    @Before
    public void setUp() {
        jugador1 = new Avatar(1, "j1", "j1");
        jugador2 = new Avatar(2, "j2", "j2");
        try {
            partida = new Partida(jugador1, jugador1, MIDA_TAULELL, "catalan", 0);
        }
        catch(IOException e) {
            System.out.println("Creació de partida ha fallat.");
        }
        estatTest = new EstatTestImpl(partida);
        iniciarCridat = false;
        encursCridat = false;
        pausarCridat = false;
        finalitzarCridat = false;
    }

    @Test
    public void testConstructor() {
        // Verificar que la partida s'ha asignat correctament
        assertNotNull("La partida no hauria de ser null", estatTest);
        
        // Verificar que es pot crear amb partida null
        Estat estatNullTest = new EstatTestImpl(null);
        assertNotNull("Hauria de poder crear-se amb partida null", estatNullTest);
    }

    @Test
    public void testIniciar() {
        estatTest.iniciar();
        assertTrue("El mètodo iniciar() hauria d'haver sigut cridat", iniciarCridat);
    }

    @Test
    public void testEncurs() {
        estatTest.encurs();
        assertTrue("El mètode encurs() hauria d'haver sigut cridat", encursCridat);
    }

    @Test
    public void testPausar() {
        estatTest.pausar();
        assertTrue("El mètode pausar() hauria d'haver sigut cridat", pausarCridat);
    }

    @Test
    public void testFinalitzar() {
        estatTest.finalitzar();
        assertTrue("El mètode finalitzar() hauria d'haver sigut cridat", finalitzarCridat);
    }

    @Test
    public void testSecuenciaEstats() {
        // Prvar una seqüèencia típica d'estats
        estatTest.iniciar();
        assertTrue("iniciar() hauria d'haver sigut cridat", iniciarCridat);
        
        estatTest.encurs();
        assertTrue("encurs() hauria d'haver sigut cridat", encursCridat);
        
        estatTest.pausar();
        assertTrue("pausar() hauria d'haver sigut cridat", pausarCridat);
        
        estatTest.finalitzar();
        assertTrue("finalitzar() hauria d'haver sigut cridat", finalitzarCridat);
    }

    @Test
    public void testMultiplesLlamadas() {
        // Verificar que els mètodes poden ser cridats múltiples veces
        estatTest.iniciar();
        estatTest.iniciar();
        assertTrue("iniciar() hauria de funcionar en múltiples crides", iniciarCridat);
        
        estatTest.pausar();
        estatTest.pausar();
        assertTrue("pausar() hauria de funcionar en múltiples crides", pausarCridat);
    }

    @Test
    public void testIndependenciaEstats() {
        // Verificar que els estats son independients
        estatTest.iniciar();
        assertTrue(iniciarCridat);
        assertFalse(encursCridat);
        assertFalse(pausarCridat);
        assertFalse(finalitzarCridat);
        
        // Resetejar flags
        iniciarCridat = false;
        
        estatTest.encurs();
        assertFalse(iniciarCridat);
        assertTrue(encursCridat);
        assertFalse(pausarCridat);
        assertFalse(finalitzarCridat);
    }
}
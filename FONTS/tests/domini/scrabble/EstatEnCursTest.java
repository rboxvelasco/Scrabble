package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EstatEnCursTest {
    private EstatEnCurs estatEnCurs;
    private Partida partida;
    
    private class PartidaTestImpl extends Partida {
        private Estat estatActual;
        
        @Override
        public boolean canviarEstat(Estat nouEstat) {
            this.estatActual = nouEstat;
            return true;
        }
        
        public Estat getEstatActual() {
            return estatActual;
        }
    }

    @Before
    public void setUp() {
        partida = new PartidaTestImpl();
        estatEnCurs = new EstatEnCurs(partida);
    }

    @Test
    public void testConstructor() {
        assertNotNull("EstatEnCurs hauria de crear-se correctament", estatEnCurs);
    }

    @Test
    public void testIniciar() {
        // El mètode iniciar() no hauria de fer res en EstatEnCurs
        Estat estatAntes = ((PartidaTestImpl)partida).getEstatActual();
        estatEnCurs.iniciar();
        Estat estatDespres = ((PartidaTestImpl)partida).getEstatActual();
        
        assertEquals("L'estat no hauria de canviar en cridar a iniciar()", estatAntes, estatDespres);
    }

    @Test
    public void testPausar() {
        estatEnCurs.pausar();
        Estat nouEstat = ((PartidaTestImpl)partida).getEstatActual();
        
        assertNotNull("El nou estat no hauria de ser null", nouEstat);
        assertTrue("El nou estat hauria de ser EstatPausada", nouEstat instanceof EstatPausada);
    }

    @Test
    public void testEncurs() {
        // El mètode encurs() no hauria de fer res en EstatEnCurs
        Estat estatAntes = ((PartidaTestImpl)partida).getEstatActual();
        estatEnCurs.encurs();
        Estat estatDespres = ((PartidaTestImpl)partida).getEstatActual();
        
        assertEquals("L'estat hauria de  canviar al cridar a encurs()", estatAntes, estatDespres);
    }

    @Test
    public void testFinalitzar() {
        estatEnCurs.finalitzar();
        Estat nouEstat = ((PartidaTestImpl)partida).getEstatActual();
        
        assertNotNull("El nou estat no debería ser null", nouEstat);
        assertTrue("El nou estat hauria de ser EstatFinalitzada", 
                  nouEstat instanceof EstatFinalitzada);
    }

    @Test
    public void testTransicionesMultiples() {
        // Provar seqüència de transicions
        estatEnCurs.pausar();
        assertTrue("Hauria de canviar a EstatPausada", ((PartidaTestImpl)partida).getEstatActual() instanceof EstatPausada);
        
        // Crear nou EstatEnCurs i finalizar
        EstatEnCurs nouEstatEnCurs = new EstatEnCurs(partida);
        nouEstatEnCurs.finalitzar();
        assertTrue("Hauria de canviar a EstatFinalitzada", ((PartidaTestImpl)partida).getEstatActual() instanceof EstatFinalitzada);
    }

    @Test
    public void testTransicionInvalida() {
        // Intentar iniciar quan ja està en curs
        Estat estatAntes = ((PartidaTestImpl)partida).getEstatActual();
        estatEnCurs.iniciar();
        assertEquals("No hauria de permetre transició a estat inicial", estatAntes, ((PartidaTestImpl)partida).getEstatActual());
    }

    @Test
    public void testPartidaNull() {
        // Verificar comportament amb partida null
        EstatEnCurs estatNull = new EstatEnCurs(null);
        assertNotNull("Hauria de crear-se inclús amb partida null", estatNull);
        
        // Les següents operacions no haurien de causar NullPointerException
        try {
            estatNull.iniciar();
            estatNull.encurs();
            estatNull.pausar();
            estatNull.finalitzar();
        } catch (NullPointerException e) {
            fail("No hauria de llençar NullPointerException amb partida null");
        }
    }

    @Test
    public void testNoModificacionPartida() {
        // Verificar que els mètodes que no han de modificar l'estat realment no ho fan
        PartidaTestImpl partidaTest = new PartidaTestImpl();
        EstatEnCurs estatTest = new EstatEnCurs(partidaTest);
        
        Estat estatInicial = partidaTest.getEstatActual();
        estatTest.iniciar();
        estatTest.encurs();
        
        assertEquals("Els mètodes iniciar() i encurs() no haurien de modificar l'estat", estatInicial, partidaTest.getEstatActual());
    }
}

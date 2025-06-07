package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EstatPausadaTest {
    private EstatPausada estatPausada;
    private Partida partida;
    
    private class PartidaTestImpl extends Partida {
        private Estat estatActual;
        private boolean estatModificat;
        
        @Override
        public boolean canviarEstat(Estat nouEstat) {
            this.estatActual = nouEstat;
            this.estatModificat = true;
            return true;
        }
        
        public Estat getEstatActual() {
            return estatActual;
        }
        
        public boolean isEstatModificat() {
            return estatModificat;
        }
        
        public void resetEstatModificat() {
            estatModificat = false;
        }
    }

    @Before
    public void setUp() {
        partida = new PartidaTestImpl();
        estatPausada = new EstatPausada(partida);
    }

    @Test
    public void testConstructor() {
        assertNotNull("EstatPausada hauria de crear-se correctament", estatPausada);
        
        // Provar constructor amb partida null
        EstatPausada estatNull = new EstatPausada(null);
        assertNotNull("Hauria de poder crear-se amb partida null", estatNull);
    }

    @Test
    public void testIniciar() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatPausada.iniciar();
        
        assertTrue("Hauria de modificar l'estat en cridar iniciar()", partidaTest.isEstatModificat());
        assertTrue("Hauria de canviar a EstatEnCurs", partidaTest.getEstatActual() instanceof EstatEnCurs);
    }

    @Test
    public void testPausar() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        Estat estatAntes = partidaTest.getEstatActual();
        estatPausada.pausar();
        
        assertFalse("No hauria de modificar l'estat en cridar pausar()", partidaTest.isEstatModificat());
        assertEquals("L'estat no hauria de canviar", estatAntes, partidaTest.getEstatActual());
    }

    @Test
    public void testEncurs() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatPausada.encurs();
        
        assertTrue("Hauria de modificar l'estat en cridar encurs()", partidaTest.isEstatModificat());
        assertTrue("Hauria de canviar a EstatEnCurs", partidaTest.getEstatActual() instanceof EstatEnCurs);
    }

    @Test
    public void testFinalitzar() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatPausada.finalitzar();
        
        assertTrue("Hauria de modificar l'estat en cridar finalitzar()", partidaTest.isEstatModificat());
        assertTrue("Hauria de canviar a EstatFinalitzada", partidaTest.getEstatActual() instanceof EstatFinalitzada);
    }

    @Test
    public void testTransicionsSequencials() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        
        // Provar transició a EnCurs
        estatPausada.iniciar();
        assertTrue("Hauria de canviar a EstatEnCurs", partidaTest.getEstatActual() instanceof EstatEnCurs);
        
        // Tornar a EstatPausada
        estatPausada = new EstatPausada(partidaTest);
        
        // Provar transició a Finalitzada
        estatPausada.finalitzar();
        assertTrue("Hauria de canviar a EstatFinalitzada", partidaTest.getEstatActual() instanceof EstatFinalitzada);
    }

    @Test
    public void testComportamentAmbPartidaNull() {
        EstatPausada estatNull = new EstatPausada(null);
        
        try {
            estatNull.iniciar();
            estatNull.pausar();
            estatNull.encurs();
            estatNull.finalitzar();
        } catch (NullPointerException e) {
            fail("No hauria de llençar NullPointerException amb partida null");
        }
    }

    @Test
    public void testEquivalenciaIniciarIEncurs() {
        PartidaTestImpl partidaTest1 = new PartidaTestImpl();
        PartidaTestImpl partidaTest2 = new PartidaTestImpl();
        
        EstatPausada estat1 = new EstatPausada(partidaTest1);
        EstatPausada estat2 = new EstatPausada(partidaTest2);
        
        // Provar tots dos mètodes
        estat1.iniciar();
        estat2.encurs();
        
        // Verificar que el resultado es el mismo
        assertTrue("Tant iniciar() com encurs() haurien de canviar a EstatEnCurs",
                  partidaTest1.getEstatActual() instanceof EstatEnCurs &&
                  partidaTest2.getEstatActual() instanceof EstatEnCurs);
    }

    @Test
    public void testIndependenciaInstancies() {
        PartidaTestImpl partidaTest = new PartidaTestImpl();
        EstatPausada estat1 = new EstatPausada(partidaTest);
        EstatPausada estat2 = new EstatPausada(partidaTest);
        
        assertNotSame("Les instancias hauria den ser diferentes", estat1, estat2);
        
        // Verificar que ambdues instàncies funcionen correctament
        estat1.iniciar();
        assertTrue("Primera instància hauria de canviar a EstatEnCurs", partidaTest.getEstatActual() instanceof EstatEnCurs);
        
        estat2.finalitzar();
        assertTrue("Segunda instància hauria de canviar a EstatFinalitzada", partidaTest.getEstatActual() instanceof EstatFinalitzada);
    }
}

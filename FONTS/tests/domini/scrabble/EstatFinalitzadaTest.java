package domini.scrabble;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EstatFinalitzadaTest {
    private EstatFinalitzada estatFinalitzada;
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
        estatFinalitzada = new EstatFinalitzada(partida);
    }

    @Test
    public void testConstructor() {
        assertNotNull("EstatFinalitzada hauria de crear-se correctamente", estatFinalitzada);
        
        // Provar constructor amb partida null
        EstatFinalitzada estatNull = new EstatFinalitzada(null);
        assertNotNull("Hauria de poder crear-se amb partida null", estatNull);
    }

    @Test
    public void testIniciar() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatFinalitzada.iniciar();
        
        assertFalse("No hauria de modificar l'estat en cridar iniciar()", partidaTest.isEstatModificat());
    }

    @Test
    public void testPausar() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatFinalitzada.pausar();
        
        assertFalse("No hauria de modificar l'estat en cridar pausar()", partidaTest.isEstatModificat());
    }

    @Test
    public void testEncurs() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatFinalitzada.encurs();
        
        assertFalse("No hauria de modificar l'estat en cridar encurs()", partidaTest.isEstatModificat());
    }

    @Test
    public void testFinalitzar() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatFinalitzada.finalitzar();
        
        assertFalse("No hauria de modificar l'estat en cridar finalitzar()", partidaTest.isEstatModificat());
    }

    @Test
    public void testInmutabilidadEstado() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        Estat estatInicial = partidaTest.getEstatActual();
        
        // Intentar totes les transicions possibles
        estatFinalitzada.iniciar();
        estatFinalitzada.pausar();
        estatFinalitzada.encurs();
        estatFinalitzada.finalitzar();
        
        assertEquals("L'estat no hauria de canviar després d'intentar totes les transicions", estatInicial, partidaTest.getEstatActual());
    }

    @Test
    public void testComportamientoConPartidaNull() {
        EstatFinalitzada estatNull = new EstatFinalitzada(null);
        
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
    public void testSecuenciaOperaciones() {
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        // Intentar una seqüència d'operacions
        estatFinalitzada.iniciar();
        assertFalse("iniciar() no hauria de modificar l'estat", partidaTest.isEstatModificat());
        
        estatFinalitzada.pausar();
        assertFalse("pausar() no hauria de modificar l'estat", partidaTest.isEstatModificat());
        
        estatFinalitzada.encurs();
        assertFalse("encurs() no hauria de modificar l'estat", partidaTest.isEstatModificat());
        
        estatFinalitzada.finalitzar();
        assertFalse("finalitzar() no hauria de modificar l'estat", partidaTest.isEstatModificat());
    }

    @Test
    public void testIndependenciaInstancias() {
        EstatFinalitzada otraInstancia = new EstatFinalitzada(partida);
        
        // Verificar que les instancies són independents
        assertNotSame("Les instàncies haurien de ser diferents", estatFinalitzada, otraInstancia);
        
        // Verificar que ambdues instàncies manetenen el mateix comportament
        PartidaTestImpl partidaTest = (PartidaTestImpl) partida;
        partidaTest.resetEstatModificat();
        
        estatFinalitzada.iniciar();
        assertFalse("Primera instància no hauria de modificar l'estat", partidaTest.isEstatModificat());
        
        otraInstancia.iniciar();
        assertFalse("Segunda instància no hauria de modificar l'estat", partidaTest.isEstatModificat());
    }
}
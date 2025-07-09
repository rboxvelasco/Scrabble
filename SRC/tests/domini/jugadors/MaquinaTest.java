package domini.jugadors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import domini.auxiliars.MaxWord;
import domini.scrabble.*;
import domini.diccionari.DAWG;
import domini.excepcions.ExcepcioArxiuDiccionari;
import domini.excepcions.ExcepcioDomini;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaquinaTest {
    private Maquina maquina;
    private Taulell taulell;
    private DAWG diccionari;
  
    @Before
    public void setUp() {
        maquina = new Maquina();
        taulell = new Taulell(15);
        diccionari = new DAWG();
    }

    @Test
    public void testConstructor() {
        assertEquals(-1, maquina.getIdJugador());
        assertEquals("Maquina", maquina.getNom());
        assertEquals("*", maquina.getContrasenya());
        assertEquals(0, maquina.getPuntuacio_actual());
        assertTrue(maquina.getFitxes_actuals().isEmpty());
    }

    @Test
    public void testAfegirFitxes() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(0, "A", 1));
        fitxes.add(new Fitxa(1, "E", 1));
        fitxes.add(new Fitxa(2, "I", 1));
        
        maquina.setFitxesActuals(fitxes);
        
        assertEquals(3, maquina.getFitxes_actuals().size());
        assertEquals("A", maquina.getFitxes_actuals().get(0).getLletra());
    }

    @Test
    public void testFerJugadaTaulellBuit() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(3, "C", 3));
        fitxes.add(new Fitxa(4, "A", 1));
        fitxes.add(new Fitxa(5, "T", 1));
        
        maquina.setFitxesActuals(fitxes);
        
        MaxWord resultat = maquina.ferJugada(taulell, diccionari);
        
        // En un taulell buit sense diccionari, no hauria de trobar cap paraula
        assertEquals(0, resultat.getPoints());
        assertEquals(new ArrayList<Fitxa>(), resultat.getWord());
    }

    @Test
    public void testFerJugadaAmbFitxesComodin() {
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(6, "#", 0));
        fitxes.add(new Fitxa(7, "A", 1));
        fitxes.add(new Fitxa(8, "T", 1));
        
        maquina.setFitxesActuals(fitxes);

        try {
            diccionari.dictionary2DAWG("catalan.txt");
        }
        catch (ExcepcioArxiuDiccionari e) {
            System.err.println("Error carregant el diccionari: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        catch (ExcepcioDomini e) {
            System.err.println("Error de domini intern: " + e.getMessage());
            e.printStackTrace();
        }

        MaxWord resultat = maquina.ferJugada(taulell, diccionari);
        
        // Verifica que el comodí s'utilitza correctament
        if (resultat.getWord() != null) {
            boolean tieneComodin = false;
            for (Fitxa f : resultat.getWord()) {
                if (f.getPuntuacio() == 0) {
                    tieneComodin = true;
                    break;
                }
            }
            assertTrue("La palabra debería usar el comodín", tieneComodin);
        }
    }

    @Test
    public void testFerJugadaAmbMultiplicadors() {
        // Colocar algunes fitxes al taulell amb multiplicadors
        Casella casella = taulell.getCasella(7, 7);
        casella.setMultiplicador(7, 7, 15);
        
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(9, "A", 1));
        fitxes.add(new Fitxa(10, "T", 1));
        
        maquina.setFitxesActuals(fitxes);
        
        MaxWord resultat = maquina.ferJugada(taulell, diccionari);
        
        // La puntuació hauriad e tenir en copte el/s multiplicador/s
        if (resultat.getPoints() > 0) {
            assertTrue(resultat.getPoints() >= 2);
        }
    }

    @Test
    public void testHerenciaJugador() {
        Jugador jugador = maquina; // Verificar que Maquina es un Jugador
        
        assertEquals(-1, jugador.getIdJugador());
        assertEquals("Maquina", jugador.getNom());
        assertEquals("*", jugador.getContrasenya());
    }

    @Test
    public void testFerJugadaSenseEspai() {
        // OMplim el taulell per a què no hi hagi espai per jugar
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                taulell.getCasella(i, j).afegirFitxa(new Fitxa(11, "X", 8));
            }
        }
        
        List<Fitxa> fitxes = new ArrayList<>();
        fitxes.add(new Fitxa(12, "A", 1));
        maquina.setFitxesActuals(fitxes);
        
        MaxWord resultat = maquina.ferJugada(taulell, diccionari);
        
        assertEquals(0, resultat.getPoints());
        assertEquals(new ArrayList<Fitxa>(), resultat.getWord());
    }

    @Test
    public void testFerJugadaSenseFitxes() {
        maquina.buidarFitxesJugador();
        MaxWord resultat = maquina.ferJugada(taulell, diccionari);
        
        assertEquals(0, resultat.getPoints());
        assertEquals(new ArrayList<Fitxa>(), resultat.getWord());
    }

    @Test
    public void testConsistenciaEstadoMaquina() {
        List<Fitxa> fitxesOriginales = new ArrayList<>();
        fitxesOriginales.add(new Fitxa(13, "A", 1));
        fitxesOriginales.add(new Fitxa(14, "B", 3));
        
        maquina.setFitxesActuals(fitxesOriginales);
        maquina.ferJugada(taulell, diccionari);
        
        // Verificar que l'estat de la màquina no ha canviat
        List<Fitxa> fitxesFinales = maquina.getFitxes_actuals();
        assertEquals(fitxesOriginales.size(), fitxesFinales.size());
        for (int i = 0; i < fitxesOriginales.size(); i++) {
            assertEquals(fitxesOriginales.get(i).getLletra(), fitxesFinales.get(i).getLletra());
        }
    }
}

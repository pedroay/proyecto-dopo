package Test;
import java.util.Stack;
import tower.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas para los tipos especiales de tazas y tapas:
 * Opener, Hierarchical, Box (tazas) y Crazy, Fearful (tapas).
 */
public class SubElementsTest {

    private Tower tower;
    private Tower tower2;

    @BeforeEach
    public void prepararEscenario() {
        tower = new Tower(300, 600);
        tower2 = new Tower(3);
    }

    // =========================================================================
    // OPENER (Abridor)
    // =========================================================================

    @Test
    public void pruebaAbridorInsertadoEnTorreVacia() {
        tower.pushCup("opener", 3);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(3, "cup"));
    }

    @Test
    public void pruebaAbridorEliminaTapasConNumeroMenor() {
        tower.pushCup(5);
        tower.pushLid(2);       
        tower.pushCup("opener", 3);
        assertTrue(tower.isOK());
        assertFalse(tower.isInElements(2, "lid"));
    }

    @Test
    public void pruebaAbridorDuplicadoRechazado() {
        tower.pushCup("opener", 3);
        tower.pushCup("opener", 3);
        assertFalse(tower.isOK());
    }

    @Test
    public void pruebaAbridorSoloDanaTapasQuitables() {
        Opener opener = new Opener(4, tower);
        Lid quitableLid = new Lid(2, tower);
        quitableLid.setQuitable(true);
        Lid nonQuitableLid = new Lid(1, tower);
        nonQuitableLid.setQuitable(false);

        assertTrue(opener.canDamage(quitableLid));
        assertFalse(opener.canDamage(nonQuitableLid));
    }

    @Test
    public void pruebaAbridorNoPuedeDesplazar() {
        Opener opener = new Opener(3, tower);
        Cup cup = new Cup(1, tower);
        assertFalse(opener.canDesplace(cup));
    }

    // =========================================================================
    // HIERARCHICAL (Jerárquico)
    // =========================================================================

    @Test
    public void pruebaJerarquicoInsertado() {
        tower.pushCup("hierarchical", 3);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(3, "cup"));
    }

    @Test
    public void pruebaJerarquicoDesplazaMenores() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup("hierarchical", 4);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(4, "cup"));
    }

    @Test
    public void pruebaJerarquicoNoDesplazaMayores() {
        tower.pushCup(5);
        tower.pushCup("hierarchical", 3);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(5, "cup"));
    }

    @Test
    public void pruebaLogicaDeDesplazamientoJerarquico() {
        Hierarchical h = new Hierarchical(5, tower);
        Cup smallQuitable = new Cup(3, tower);
        smallQuitable.setQuitable(true);
        Cup smallNonQuitable = new Cup(2, tower);
        smallNonQuitable.setQuitable(false);
        Cup largerCup = new Cup(6, tower);
        largerCup.setQuitable(true);

        assertTrue(h.canDesplace(smallQuitable));
        assertFalse(h.canDesplace(smallNonQuitable));
        assertFalse(h.canDesplace(largerCup));
    }

    @Test
    public void pruebaJerarquicoNoPuedeDañar() {
        Hierarchical h = new Hierarchical(3, tower);
        Lid lid = new Lid(1, tower);
        assertFalse(h.canDamage(lid));
    }

    @Test
    public void pruebaJerarquicoNoQuitableEnPosicion0() {
        Hierarchical h = new Hierarchical(3, new Lid(3, tower));
        assertTrue(h.isNotQuitablePosition(0));
    }

    // =========================================================================
    // BOX (Caja)
    // =========================================================================

    @Test
    public void pruebaCajaInsertada() {
        tower.pushCup("box", 3);
        for(Elements element :tower.getObjects()) {
        	assertTrue(element.thisIsInABox());
        }
        assertTrue(tower.isOK());

    }

    @Test
    public void pruebaBanderaCajaEsVerdadera() {
        Box box = new Box(3, tower);
        assertTrue(box.getIsBox());
    }

    @Test
    public void pruebaTazaNormalNoEsCaja() {
        Cup cup = new Cup(3, tower);
        assertFalse(cup.getIsBox());
    }

    @Test
    public void pruebaCajaDuplicadaRechazada() {
        tower.pushCup("box", 3);
        tower.pushCup("box", 3);
        Stack<Elements> objects = tower.getObjects();
        for (int i= objects.size();i <objects.size();i++) {
        	assertFalse(objects.get(i).getNumber() == objects.get(i).getNumber());
        }
        }
    
    @Test
    public void todosLosElementosNoSePuedenQuiatr() {
        tower2.pushCup("box", 4);
        for(Elements e:tower2.getObjects()) {
        	if (!e.getIsBox()) {
        	assertTrue(e.thisIsInABox() );
        	assertFalse(e.thisIsQuitable());
        	}
        }
    }
    

    @Test
    public void pruebaCajaCreaTapaAlSerInsertada() {
        tower.pushCup("box", 3);
        tower.pushLid(3);       
        assertTrue(tower.isInElements(3, "lid"));
    }

    // =========================================================================
    // CRAZY (Loca)
    // =========================================================================

    @Test
    public void pruebaTapaLocaInsertada() {
        tower.pushCup(3);
        tower.pushLid("crazy", 3);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(3, "lid"));
    }

    @Test
    public void pruebaBanderaLocaEsVerdadera() {
        Crazy crazy = new Crazy(3, tower);
        assertTrue(crazy.thisIsCrazy());
    }

    @Test
    public void pruebaTapaNormalNoEsLoca() {
        Lid lid = new Lid(3, tower);
        assertFalse(lid.thisIsCrazy());
    }

    @Test
    public void pruebaLocaVaALaBase() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid("crazy", 2);
        assertTrue(tower.isOK());
        String[][] staking = tower.stakingItems();
        assertEquals("lid", staking[0][0]);
        assertEquals("2", staking[0][1]);
    }

    @Test
    public void pruebaLocaDuplicadaRechazada() {
        tower.pushCup(3);
        tower.pushLid("crazy", 3);
        tower.pushLid("crazy", 3);
        assertFalse(tower.isOK());
    }

    @Test
    public void pruebaLocaNoDañaNiDesplaza() {
        Crazy crazy = new Crazy(3, tower);
        Cup cup = new Cup(2, tower);
        assertFalse(crazy.canDamage(cup));
        assertFalse(crazy.canDesplace(cup));
    }

    // =========================================================================
    // FEARFUL (Miedosa)
    // =========================================================================

    @Test
    public void pruebaMiedosaInsertadaConTazaCompañera() {
        tower.pushCup(3);
        tower.pushLid("fearful", 3);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(3, "lid"));
    }

    @Test
    public void pruebaMiedosaRechazadaSinTazaCompañera() {
        tower.pushLid("fearful", 3);
        assertFalse(tower.isInElements(3, "lid"));
    }

    @Test
    public void pruebaBanderaMiedosaEsVerdadera() {
        Fearful fearful = new Fearful(3, tower);
        assertTrue(fearful.thisIsFearful());
    }

    @Test
    public void pruebaTapaNormalNoEsMiedosa() {
        Lid lid = new Lid(3, tower);
        assertFalse(lid.thisIsFearful());
    }

    @Test
    public void pruebaMiedosaNoQuitableSiTazaYaEstaCubierta() {
        tower.pushCup(3);
        tower.pushLid(3);           
        tower.pushLid("fearful", 3);
        assertFalse(tower.isOK());  
    }

    @Test
    public void pruebaMiedosaNoDañaNiDesplaza() {
        Fearful fearful = new Fearful(3, tower);
        Cup cup = new Cup(2, tower);
        assertFalse(fearful.canDamage(cup));
        assertFalse(fearful.canDesplace(cup));
    }

    // =========================================================================
    // INTERACCIONES
    // =========================================================================

    @Test
    public void pruebaAbridorEliminaMiedosaSiEsQuitable() {
        tower.pushCup(5);
        tower.pushCup(2);
        tower.pushLid("fearful", 2); 
        tower.pushCup("opener", 4);
        assertTrue(tower.isOK());
        assertTrue(tower.isInElements(2, "lid"));
    }

    @Test
    public void pruebaAbridorNoEliminaMiedosaNoQuitable() {
        tower.pushCup(3);
        Fearful f = new Fearful(3, tower);
        f.setQuitable(false);
        tower.pushLid("fearful", 3);
        tower.pushCup("opener", 5);
        assertTrue(tower.isInElements(3, "lid"));
    }

    @Test
    public void pruebaJerarquicoConstruidoConTapaTienePosicion0NoQuitable() {
        Lid lid = new Lid(3, tower);
        Hierarchical h = new Hierarchical(3, lid);
        assertTrue(h.isNotQuitablePosition(0));
        assertFalse(h.isNotQuitablePosition(1));
    }

    @Test
    public void pruebaTorreMixtaPermaneceCorrecta() {
        tower.pushCup(5);
        tower.pushCup("hierarchical", 3);
        tower.pushCup("opener", 2);
        tower.pushLid("fearful", 5);
        assertTrue(tower.isInElements(5, "cup"));
        assertTrue(tower.isInElements(3, "cup"));
    }

    @Test
    public void pruebaLocaSiempreEnLaBaseEnTorreMixta() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid("crazy", 4);
        String[][] staking = tower.stakingItems();
        assertEquals("lid", staking[0][0]);
        assertEquals("4", staking[0][1]);
    }
}
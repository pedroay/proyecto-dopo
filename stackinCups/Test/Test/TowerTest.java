package Test;
import tower.*;  
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class TowerTest {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(100, 300);
    }
    
    @Test
    public void debeAgregarmeUnaCopaDentroDeOtra(){
        tower.pushCup(5);
        tower.pushCup(4);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushCup(1);
        Elements a = tower.getTop();
        Elements b = a.getInside();
        Elements c = b.getInside();
        Elements d = c.getInside();
        assertEquals(5,tower.getTop().getNumber());
        assertEquals(4,b.getNumber());
        assertEquals(3,c.getNumber());
        assertEquals(2,d.getNumber());                
        
    }
    
    @Test
    public void debeHacermeUnaTorreInvertida(){
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup(4);
        
        Elements a = tower.getTop();
        
        assertEquals(4,a.getNumber());
        assertEquals(140,a.getPosy());
    }
    
    @Test 
    public void debeDarmeElTopCorrectoConUnaMiniTorre(){
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(4);
        
        assertEquals(4,tower.getTop().getNumber());
        assertEquals(170,tower.getTop().getPosy());
    }
    
    @Test
    public void debeAgregarUnaTapaDentroDeUnaCopa(){
        tower.pushCup(5);
        tower.pushLid(3);
        
        Elements top = tower.getTop();
        Elements inside = top.getInside();
        
        assertEquals(5, top.getNumber());    
        assertEquals(3, inside.getNumber()); 
        assertEquals("lid", inside.getType());
    }
    
    @Test
    public void debeAgregarUnaTapaQueCubraUnaCopa(){
        tower.pushCup(5);
        tower.pushLid(5);
        
        Elements top = tower.getTop();
        int l_number=top.getNumber();
        Cup cup=tower.findCupByNumberOfLid(l_number);
        
        assertEquals(5, top.getNumber()); 
        assertEquals("lid", top.getType());  
        assertEquals("Covered", cup.getState()); 
        tower.makeVisible();
    }
    
    @Test
    public void debeDarmeElTopCorrectoConTapasYCopas(){
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid(4);
        
        Elements top = tower.getTop();
        
        assertEquals(5, top.getNumber());
        assertEquals("cup", top.getType());
    }
    
    @Test
    public void noDebeAgregarUnaTapaDuplicada(){
        tower.pushCup(5);
        tower.pushLid(3);
        tower.pushLid(3);
        
        assertFalse(tower.isOK());
    }
    
    @Test
    public void debeAgregarVariasTapasCorrectamente(){
        tower.pushCup(5);  
        tower.pushLid(3);
        tower.pushLid(4); 
        
        Elements top = tower.getTop();
        
        assertEquals(5, top.getNumber());     
        assertEquals("cup", top.getType());
        assertTrue(tower.isInElements(3,"lid"));
        assertTrue(tower.isInElements(4,"lid"));
    }
    
    @Test
    public void debeCrearmeUnaTorreSoloDeTapas(){
        tower.pushLid(3);
        tower.pushLid(4); 
        Elements top = tower.getTop();
        assertEquals(4,top.getNumber());
    }
    
    @Test
    public void debeCrearmeUnaTorreDeTapasConCopas(){
        tower.pushCup(4);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.pushCup(5);
        tower.pushLid(2);
        Elements top = tower.getTop();
        assertEquals(5,top.getNumber());
    }
    
    @Test
    public void debeEliminarmeElTopeDeUnaTorreSoloDeCopas(){
        tower.pushCup(3);
        tower.pushCup(4);
        tower.pushCup(2);
        tower.popCup();
        Elements top = tower.getTop();
        Stack<Elements> a = tower.getObjects();
        assertEquals(4,a.get(a.size()-1).getNumber());
        assertEquals(4,top.getNumber());
        tower.makeVisible();
    }
    
    @Test
    public void deveEliminarmeElTopeDeUnaTorreQueEsUnaLId(){
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(5);
        tower.popLid();
        Elements top = tower.getTop();
        Stack<Elements> a = tower.getObjects();
        assertEquals(3,a.get(a.size()-1).getNumber());
        assertEquals(3,top.getNumber());
    
    }
    
    @Test
    public void debeEliminarUnaTapa(){
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushLid(3);
        tower.popLid();
        Elements top = tower.getTop();
        Stack<Elements> a = tower.getObjects();
        assertEquals(5,a.get(a.size()-1).getNumber());
        assertEquals(5,top.getNumber());
    }
    
    @Test
    public void debeRemoverUnaCopaSinElementos() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.removeCup(3);
        assertTrue(tower.isOK());
        assertEquals(5, tower.getTop().getNumber());
        assertEquals("cup", tower.getTop().getType());
    }
    
    @Test
    public void debeRemoverLaUnicaCopa() {
        tower.pushCup(5);
        tower.removeCup(5);
        assertTrue(tower.isOK());
        assertNull(tower.getTop());
    }
    
    @Test
    public void debeRemoverCopayReubicarCopasInternas() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.removeCup(3);
        assertTrue(tower.isOK());
        assertEquals(5, tower.getTop().getNumber());
        assertEquals(1, tower.getTop().getInside().getNumber());
    }
    
    @Test
    public void noDebeRemoverCopaQueNoExiste() {
        tower.pushCup(5);
        tower.removeCup(9);
        assertFalse(tower.isOK());
        assertEquals(5, tower.getTop().getNumber());
    }
    
    @Test
    public void debeRemoverCopaDelMedio() {
        tower.pushCup(7);
        tower.pushCup(5);
        tower.pushCup(3);
        tower.removeCup(5);
        assertTrue(tower.isOK());
        assertEquals(7, tower.getTop().getNumber());
        assertEquals(3, tower.getTop().getInside().getNumber());
    }
    
    @Test
    public void debeRemoverUnaTapaSinElementos() {
        tower.pushCup(5);
        tower.pushLid(3);
        tower.removeLid(3);
        assertTrue(tower.isOK());
        assertFalse(tower.isInElements(3,"lid"));
    }
    
    @Test
    public void debeRemoverTapaQueCubreYDescubrirCopa() {
        tower.pushCup(5);
        tower.pushLid(5);
        Cup cup = tower.findCupByNumberOfLid(5);
        assertEquals("Covered", cup.getState());
        tower.removeLid(5);
        assertTrue(tower.isOK());
        cup = tower.findCupByNumber(5);
        assertEquals("noCovered", cup.getState());
    }
    
    @Test
    public void debeRemoverTapaEntreDosElementos() {
        tower.pushCup(5);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.removeLid(3);
        assertTrue(tower.isOK());
        Elements inside = tower.getTop().getInside();
        assertNotNull(inside);
        assertEquals(4, inside.getNumber());
    }
    
    //swap, swap to reduce y cover
    @Test
    public void debeIntercambiarPrimeraCopaPorUltima() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushCup(7);

        tower.swap(new String[]{"cup","7"}, new String[]{"cup","1"});

        String[][] staking = tower.stakingItems();
        assertEquals("cup", staking[0][0]); assertEquals("7", staking[0][1]);
        assertEquals("cup", staking[1][0]); assertEquals("3", staking[1][1]);
        assertEquals("cup", staking[2][0]); assertEquals("1", staking[2][1]);
        assertTrue(tower.isOK());
        tower.makeVisible();
    }
    
    @Test
    public void debeIntercambiarCopayTapa() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid(3);

        tower.swap(new String[]{"cup","3"}, new String[]{"lid","3"});

        String[][] staking = tower.stakingItems();
        assertEquals("cup", staking[0][0]); assertEquals("5", staking[0][1]);
        assertEquals("lid", staking[1][0]); assertEquals("3", staking[1][1]);
        assertEquals("cup", staking[2][0]); assertEquals("3", staking[2][1]);
        assertTrue(tower.isOK());
    }
    
    @Test
    public void debeIntercambiarDosTapas() {
        tower.pushCup(5);
        tower.pushLid(3);
        tower.pushLid(4);
        tower.swap(new String[]{"lid","3"}, new String[]{"lid","4"});
        String[][] staking = tower.stakingItems();
        assertEquals("cup", staking[0][0]); assertEquals("5", staking[0][1]);
        assertEquals("lid", staking[1][0]); assertEquals("4", staking[1][1]);
        assertEquals("lid", staking[2][0]); assertEquals("3", staking[2][1]);
        assertTrue(tower.isOK());
    }
    
    @Test
    public void swapMismoElementoNoAlteraElOrden() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.swap(new String[]{"cup","3"}, new String[]{"cup","3"});
        String[][] staking = tower.stakingItems();
        assertEquals("5", staking[0][1]);
        assertEquals("3", staking[1][1]);
        assertEquals("1", staking[2][1]);
        assertFalse(tower.isOK());
    }
    
    @Test
    public void noDebeIntercambiarSiPrimerElementoNoExiste() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.swap(new String[]{"cup","9"}, new String[]{"cup","3"});
        assertFalse(tower.isOK());
        String[][] staking = tower.stakingItems();
        assertEquals("5", staking[0][1]);
        assertEquals("3", staking[1][1]);
    }
    
    //swap to reduce
    @Test
    public void debeRetornarVacioConTorreVacia() {
        String[][] result = tower.swapToReduce();
        assertEquals(0, result.length);
    }
    
    @Test
    public void debeRetornarVacioConUnSoloElemento() {
        tower.pushCup(5);
        String[][] result = tower.swapToReduce();
        assertEquals(0, result.length);
    }
    
    @Test
    public void debeRetornarVacioSiObjectsOrdenadoDescendente() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        String[][] result = tower.swapToReduce();
        assertEquals(0, result.length);
    }
    
    //Cover
    @Test
    public void coverConTorreVaciaNoFalla() {
        tower.cover();
        assertNull(tower.getTop());
        assertEquals(0, tower.stakingItems().length);
    }
    
    @Test
    public void coverSinTapasNoAlteraElOrden() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.cover();
        String[][] staking = tower.stakingItems();
        assertEquals(3, staking.length);
        assertEquals("5", staking[0][1]);
        assertEquals("3", staking[1][1]);
        assertEquals("1", staking[2][1]);
    }
    
    @Test
    public void coverCubreUnaCopaCuandoHaySuTapa() {
        tower.pushLid(5);
        tower.pushCup(5);
        tower.cover();
        String[][] staking = tower.stakingItems();
        assertEquals(2, staking.length);
        assertEquals("cup", staking[0][0]); assertEquals("5", staking[0][1]);
        assertEquals("lid", staking[1][0]); assertEquals("5", staking[1][1]);
        Cup cup5 = tower.findCupByNumberOfLid(5);
        assertEquals("Covered", cup5.getState());
    }
    
    @Test
    public void coverCubreSoloCopasConSuTapaEnLaTorre() {
        tower.pushCup(5);
        tower.pushLid(3);
        tower.pushCup(3);
        tower.cover();
        String[][] staking = tower.stakingItems();
        assertEquals(3, staking.length);
        assertEquals("cup", staking[0][0]); assertEquals("5", staking[0][1]);
        assertEquals("cup", staking[1][0]); assertEquals("3", staking[1][1]);
        assertEquals("lid", staking[2][0]); assertEquals("3", staking[2][1]);
        Cup cup3 = tower.findCupByNumber(3);
        Cup cup5 = tower.findCupByNumber(5);
        assertEquals("Covered", cup3.getState());
        assertNotEquals("Covered", cup5.getState());
    }
    
    @Test
    public void coverMantieneTamanoDeLaTorre() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushLid(3);
        int tamanoAntes = tower.stakingItems().length;
        tower.cover();
        assertEquals(tamanoAntes, tower.stakingItems().length);
    }

    // ---------------------------------------------------------------
    // Box tests
    // ---------------------------------------------------------------

    @Test
    public void boxDebeCrearseEnLaTorre() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        Lid 
        assertTrue(tower.isInElements(4, "cup"));
    }

    @Test
    public void boxDebeDejarLaTorreEnEstadoValido() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        assertTrue(tower.isOK());
    }

    @Test
    public void boxDebeMarcartodosLosElementosInternosComoIsInABox() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        for (Elements e : tower.getObjects()) {
            if (!e.getIsBox()) {
                assertTrue("Element " + e.getNumber() + " should be marked isInABox",
                    e.thisIsInABox());
            }
        }
    }

    @Test
    public void boxDebeMarcartodosLosElementosInternosComoNoQuitables() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        for (Elements e : tower.getObjects()) {
            if (!e.getIsBox()) {
                assertFalse("Element " + e.getNumber() + " should not be quitable",
                    e.thisIsQuitable());
            }
        }
    }

    @Test
    public void boxDebeCrearSuTapaAutomaticamente() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        Cup box = tower.findCupByNumber(4);
        assertTrue(tower.isInElements(box.getNumber(), "lid"));
    }

    @Test
    public void noSeDebePoderEliminarUnElementoDentroDelBox() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        tower.removeCup(1);
        assertFalse(tower.isOK());
    }

    @Test
    public void boxDebeSerSuficientementeGrandeParaEncerrarTodaLaTorre() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        Cup box = tower.findCupByNumber(4);
        int boxWidth = box.getWidth();
        for (Elements e : tower.getObjects()) {
            if (!e.getIsBox()) {
                assertTrue("Element " + e.getNumber() + " width=" + e.getWidth()
                    + " exceeds box width=" + boxWidth, e.getWidth() <= boxWidth);
            }
        }
    }

    @Test
    public void boxDebeTenerElFlagGetIsBoxEnTrue() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 4);
        Cup box = tower.findCupByNumber(4);
        assertTrue(box.getIsBox());
    }
}

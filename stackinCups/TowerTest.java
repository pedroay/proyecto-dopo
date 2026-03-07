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

    }

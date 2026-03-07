import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    }

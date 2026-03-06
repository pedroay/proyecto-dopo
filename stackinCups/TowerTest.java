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
    }

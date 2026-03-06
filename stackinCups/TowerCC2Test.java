 import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TowerCC2Test {

    // ===============================
    // PRUEBAS DEL NUEVO CONSTRUCTOR
    // ===============================

    @Test
    public void accordingACShouldCreateTowerWithFourCups() {
        Tower tower = new Tower(4);
        assertEquals(280, tower.height());

        assertTrue(tower.isOk());

        String[][] items = tower.stackingItems();

        assertEquals("cup", items[0][0]);
        assertEquals("1", items[0][1]);
        assertEquals("cup", items[1][0]);
        assertEquals("3", items[1][1]);
        assertEquals("cup", items[2][0]);
        assertEquals("5", items[2][1]);
        assertEquals("cup", items[3][0]);
        assertEquals("7", items[3 ][1]);
    }

    @Test
    public void accordingACShouldCreateCorrectNumberOfElementsInStack() {
        Tower tower = new Tower(5);

        String[][] items = tower.stackingItems();
        assertEquals(5, items.length);
    }

    // ===============================
    // PRUEBAS SWAP
    // ===============================

    @Test
    public void accordingACShouldSwapTwoItemsCorrectly() {
        Tower tower = new Tower(2);
        tower.swap(new String[]{"cup","1"}, new String[]{"cup","3"});

        String[][] items = tower.stackingItems();

        assertEquals("3", items[0][1]);
        assertEquals("1", items[1][1]);
        assertEquals(3,tower.getTop().getNumber());
        assertTrue(tower.isOk());
    }
    
    @Test
    public void accordingACShouldSwapThreeItemsCorrectly() {
        Tower tower = new Tower(3);
        tower.swap(new String[]{"cup","1"}, new String[]{"cup","5"});

        String[][] items = tower.stackingItems();

        assertEquals("5", items[0][1]);
        assertEquals("1", items[2][1]);
        assertEquals(5,tower.getTop().getNumber());
        assertEquals(90,tower.height());
        assertTrue(tower.isOk());
    }
    

    // ===============================
    // PRUEBAS COVER
    // ===============================

    @Test
    public void accordingACShouldCoverAllCupsWithCorrectLids() {
        Tower tower = new Tower(0,100);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.cover();
        tower.pushCup(1);
        assertEquals(true,tower.getCups().get(0).isCovered());
        assertEquals(1,tower.getTop().getNumber());
    }
    
    @Test
    public void shouldNotCover() {
        Tower tower = new Tower(0,100);
        tower.pushCup(7);
        tower.pushLid(7);
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.pushCup(4);
        tower.pushLid(4);
        tower.cover();
        tower.makeVisible();
    }
    
    
    
    //================================
    // PRUEBAS SWAP TO REDUCE
    //================================
    
    @Test
    public void shouldReturnCupsToSwapToReduce(){
        Tower tower = new Tower(3);
        
        String [][] reduce = tower.swapToReduce();
        
        assertEquals("1",reduce[0][1]);
        assertEquals("3",reduce[1][1]);
    }
    
    @Test
    public void shouldNotReturnCupsToSwapToReduce() {
        Tower tower = new Tower(0, 100);
    
        for (int i = 3; i <=1; i--) {
            tower.pushCup(i);
        }
    
        String[][] reduce = tower.swapToReduce();
    
        assertEquals(0, reduce.length);
    }
    
    // ===============================
    // PRUEBAS MAKE VISIBLE
    // ===============================

    @Test
    public void accordingACShouldMakeTowerVisibleWithoutBreakingStructure() {
        Tower tower = new Tower(3);

        tower.makeVisible();

        assertTrue(tower.isOk());
        assertEquals(3, tower.height());
    }
    
    
}
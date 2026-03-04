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
        Tower tower = new Tower(3);

        tower.cover();

        String[][] items = tower.stackingItems();

        for (int i = 0; i < items.length; i += 2) {
            assertEquals("cup", items[i][0]);
            assertEquals("lid", items[i + 1][0]);
        }

        assertTrue(tower.isOk());
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
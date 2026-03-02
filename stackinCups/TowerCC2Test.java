import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TowerCC2Test {

    // ===============================
    // PRUEBAS DEL NUEVO CONSTRUCTOR
    // ===============================

    @Test
    public void accordingACShouldCreateTowerWithFiveCupsAndCorrectLids() {
        Tower tower = new Tower(5);

        // Verificar altura
        assertEquals(5, tower.height());

        // Verificar que la torre esté consistente
        assertTrue(tower.isOk());

        // Verificar contenido exacto
        String[][] items = tower.stackingItems();

        assertEquals("cup-1", items[0]);
        assertEquals("lid-1", items[1]);
        assertEquals("cup-2", items[2]);
        assertEquals("lid-2", items[3]);
        assertEquals("cup-3", items[4]);
        assertEquals("lid-3", items[5]);
        assertEquals("cup-4", items[6]);
        assertEquals("lid-4", items[7]);
        assertEquals("cup-5", items[8]);
        assertEquals("lid-5", items[9]);
    }

    @Test
    public void accordingACShouldCreateCorrectNumberOfElementsInStack() {
        Tower tower = new Tower(5);

        String[][] items = tower.stackingItems();

        // 5 copas + 5 tapas
        assertEquals(10, items.length);
    }

    @Test
    public void accordingACShouldMaintainCorrectOrderAfterCreation() {
        Tower tower = new Tower(5);

        String[][] items = tower.stackingItems();

        for (int i = 1; i <= 5; i++) {
            assertEquals("cup-" + i, items[(i - 1) * 2]);
            assertEquals("lid-" + i, items[(i - 1) * 2 + 1]);
        }
    }

    // ===============================
    // PRUEBAS REMOVE CUP
    // ===============================

    @Test
    public void accordingACShouldRemoveSpecificCupAndItsLid() {
        Tower tower = new Tower(3);

        tower.removeCup(2);

        String[][] items = tower.stackingItems();

        // Ya no debe existir cup-2 ni lid-2
        for (String[] item : items) {
            assertNotEquals("cup-2", item);
            assertNotEquals("lid-2", item);
        }

        assertEquals(2, tower.height());
        assertTrue(tower.isOk());
    }

    @Test
    public void accordingACShouldNotAlterOtherCupsWhenRemovingOne() {
        Tower tower = new Tower(3);

        tower.removeCup(2);

        String[][] items = tower.stackingItems();

        assertEquals("cup-1", items[0]);
        assertEquals("lid-1", items[1]);
        assertEquals("cup-3", items[2]);
        assertEquals("lid-3", items[3]);
    }

    // ===============================
    // PRUEBAS SWAP
    // ===============================

    @Test
    public void accordingACShouldSwapTwoItemsCorrectly() {
        Tower tower = new Tower(2);
        tower.swap(1,2);

        String[][] items = tower.stackingItems();

        assertEquals("cup-2", items[0]);
        assertEquals("lid-1", items[1]);
        assertEquals("cup-1", items[2]);
        assertEquals("lid-2", items[3]);

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
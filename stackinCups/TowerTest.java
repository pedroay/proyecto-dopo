import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TowerTest {

    private Tower tower;

    @BeforeEach
    public void setUp() {
        tower = new Tower(100, 300);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
    }
    @test
    private boolean containsCup(int number) {
        String[][] items = tower.stackingItems();
        for (String[] item : items) {
            if (item[0].equals("cup") && Integer.parseInt(item[1]) == number) {
                return true;
            }
        }
        return false;
    }
    @Test
    public void removeExistingCupShouldDisappearFromTower() {
        tower.removeCup(2);

        assertFalse(containsCup(2)); // Ya no debe estar
        assertTrue(containsCup(1));  // Las otras sí
        assertTrue(containsCup(3));
        assertEquals(2, tower.getCupsSize());
        assertTrue(tower.isOk());
    }
    
    public void removeNonExistingCupShouldNotModifyTower() {
        tower.removeCup(99);

        assertTrue(containsCup(1));
        assertTrue(containsCup(2));
        assertTrue(containsCup(3));
        assertEquals(3, tower.getCupsSize());
        assertFalse(tower.isOk());
    }
    @Test
    public void removeFromEmptyTowerShouldFail() {
        Tower empty = new Tower(100, 300);
        empty.removeCup(1);
        assertEquals(0, empty.getCupsSize());
        assertFalse(empty.isOk());
    }
    @Test
    public void removeTopCupShouldUpdateStructure() {
        tower.removeCup(3);
        assertFalse(containsCup(3));
        assertTrue(containsCup(1));
        assertTrue(containsCup(2));
        assertEquals(2, tower.getCupsSize());
        assertTrue(tower.isOk());
    }
}

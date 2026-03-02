import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TowerTest {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(100, 300);
    }

    // =========================
    // MÉTODO AUXILIAR
    // =========================
    private boolean containsCup(int number) {
        String[][] items = tower.stackingItems();

        for (String[] item : items) {
            if (item[0].equals("cup") &&
                Integer.parseInt(item[1]) == number) {
                return true;
            }
        }
        return false;
    }

    // =========================
    // CONSTRUCTOR
    // =========================
    @Test
    public void constructorShouldInitializeCorrectly() {
        assertEquals(0, tower.getCupsSize());
        assertEquals(0, tower.getLidsSize());
        assertFalse(tower.isVisible());
        assertTrue(tower.isOk());
        assertEquals(0, tower.height());
        assertEquals(0, tower.stackingItems().length);
    }

    // =========================
    // pushCup
    // =========================
    @Test
    public void pushCupShouldInsertCorrectly() {
        tower.pushCup(5);

        assertEquals(1, tower.getCupsSize());
        assertTrue(containsCup(5));
        assertTrue(tower.isOk());
    }

    @Test
    public void pushCupDuplicateShouldFail() {
        tower.pushCup(5);
        tower.pushCup(5);

        assertEquals(1, tower.getCupsSize());
        assertFalse(tower.isOk());
    }

    // =========================
    // popCup
    // =========================
    @Test
    public void popCupShouldRemoveTop() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.popCup();
        assertFalse(containsCup(3));
        assertEquals(2, tower.getCupsSize());
        assertTrue(tower.isOk());
    }

    @Test
    public void popCupEmptyShouldFail() {
        tower.popCup();
        assertFalse(tower.isOk());
    }

    // =========================
    // removeCup
    // =========================
    @Test
    public void removeSpecificCupShouldWork() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);

        tower.removeCup(2);

        assertFalse(containsCup(2));
        assertTrue(containsCup(1));
        assertTrue(containsCup(3));
        assertEquals(2, tower.getCupsSize());
        assertTrue(tower.isOk());
    }
    
    @Test
    public void removeShouldKeepCorrectOrder() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
    
        tower.removeCup(2);
    
        String[][] items = tower.stackingItems();
    
        assertEquals("3", items[0][1]);
        assertEquals("1", items[1][1]);
    }

    @Test
    public void removeNonExistingCupShouldFail() {
        tower.pushCup(1);

        tower.removeCup(99);

        assertEquals(1, tower.getCupsSize());
        assertFalse(tower.isOk());
    }
    
    @Test
    public void popShouldRemoveExactlyTop() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
    
        tower.popCup();
        assertFalse(containsCup(3));
        assertTrue(containsCup(2));
    }
    
    @Test
    public void removeTopCupShouldBehaveLikePop() {
        tower.pushCup(1);
        tower.pushCup(2);
    
        tower.removeCup(2);
    
        assertEquals(1, tower.getCupsSize());
        assertFalse(containsCup(2));
    }
    
    // =========================
    // pushLid / popLid
    // =========================
    @Test
    public void multipleLidsStackCorrectly() {
        tower.pushCup(1);
        tower.pushCup(2);
    
        tower.pushLid(2);
        tower.pushLid(2);
    
        assertEquals(2, tower.getLidsSize());
    }
    
    @Test
    public void pushLidShouldCoverTopCup() {
        tower.pushCup(10);
        tower.pushLid(10);

        int[] covered = tower.lidedCups();

        assertEquals(1, covered.length);
        assertEquals(10, covered[0]);
        assertTrue(tower.isOk());
    }

    @Test
    public void pushLidWrongCupShouldFail() {
        tower.pushCup(1);
        tower.pushCup(2);

        tower.pushLid(1); // no es el top

        assertEquals(0, tower.lidedCups().length);
        assertFalse(tower.isOk());
    }

    @Test
    public void popLidShouldUncoverCup() {
        tower.pushCup(7);
        tower.pushLid(7);

        tower.popLid();

        assertEquals(0, tower.lidedCups().length);
        assertTrue(tower.isOk());
    }

    // =========================
    // orderTower
    // =========================
    @Test
    public void orderTowerShouldSortDescending() {
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushCup(3);

        tower.orderTower();

        String[][] items = tower.stackingItems();

        assertEquals("5", items[0][1]);
        assertEquals("3", items[1][1]);
        assertEquals("1", items[2][1]);
        assertTrue(tower.isOk());
    }

    // =========================
    // reverseTower
    // =========================
    @Test
    public void reverseTowerShouldInvertCompletely() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
    
        String[][] before = tower.stackingItems();
        tower.reverseTower();
        String[][] after = tower.stackingItems();
    
        for(int i = 0; i < before.length; i++){
            assertEquals(
                before[i][1],
                after[before.length - 1 - i][1]
            );
        }
    }

    // =========================
    // height
    // =========================
    @Test
    public void heightShouldBeZeroWhenEmpty() {
        assertEquals(0, tower.height());
    }

    @Test
    public void heightShouldIncreaseAfterPush() {
        tower.pushCup(1);
        int h1 = tower.height();

        tower.pushCup(2);
        int h2 = tower.height();

        assertTrue(h2 > h1);
    }

    // =========================
    // visibility
    // =========================
    @Test
    public void visibilityShouldChangeState() {
        tower.makeVisible();
        assertTrue(tower.isVisible());

        tower.makeInvisible();
        assertFalse(tower.isVisible());
    }

    // =========================
    // exit
    // =========================
    @Test
    public void exitShouldClearEverything() {
        tower.pushCup(1);
        tower.makeVisible();

        tower.exit();

        assertEquals(0, tower.getCupsSize());
        assertFalse(tower.isVisible());
    }
}
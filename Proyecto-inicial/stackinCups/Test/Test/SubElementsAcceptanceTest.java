package Test;
import tower.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JOptionPane;

/**
 * Pruebas de aceptación gráficas y de ensamblado para los subelementos
 * (Box, Hierarchical, Opener, Crazy, Fearful).
 */
public class SubElementsAcceptanceTest {

    private Tower tower;

    @BeforeEach
    public void setUp() {
        tower = new Tower(0);
    }
    
    // --- 1. Box ---
    @Test
    public void acceptanceTestBoxArmadoBasico() {
        tower.pushCup(1);
        tower.pushCup("box", 4);
        tower.makeVisible();
        JOptionPane.showMessageDialog(null, "No se pudo hacer la acción");
        assertTrue(tower.isOK());
    }
    
    @Test
    public void acceptanceTestBoxEncerrandoMultiples() {
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup("box", 5);
        tower.makeVisible();
        JOptionPane.showMessageDialog(null, "No se pudo hacer la acción");
        assertTrue(tower.isOK());
        assertTrue(tower.isOK());
    }

    // --- 2. Hierarchical ---
    @Test
    public void acceptanceTestHierarchicalArmadoBasico() {
        tower.pushCup(6);
        tower.pushCup("hierarchical", 4);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }
    
    @Test
    public void acceptanceTestHierarchicalValidacion() {
        tower.pushCup(5);
        tower.pushCup("hierarchical", 3);
        tower.pushCup(2);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }

    // --- 3. Opener ---
    @Test
    public void acceptanceTestOpenerArmadoBasico() {
        tower.pushCup(3);
        tower.pushCup("opener", 1);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }
    
    @Test
    public void acceptanceTestOpenerDestruyendoTapa() {
        tower.pushCup(4);
        tower.pushLid(4);
        tower.pushCup("opener", 2);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }

    // --- 4. Crazy ---
    @Test
    public void acceptanceTestCrazyArmadoBasico() {
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushLid("crazy", 5);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }
    
    @Test
    public void acceptanceTestCrazyConTorreMasAlta() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushCup(5);
        tower.pushLid("crazy", 4);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }

    // --- 5. Fearful ---
    @Test
    public void acceptanceTestFearfulArmadoBasico() {
        tower.pushCup(2);
        tower.pushLid("fearful", 2);
        tower.makeVisible();
        assertTrue(tower.isOK());
    }
    
    @Test
    public void acceptanceTestFearfulRechazo() {
        tower.pushCup(5);

        tower.pushLid("fearful", 6);
        tower.makeVisible();
        assertFalse(tower.isOK()); 
    }
}

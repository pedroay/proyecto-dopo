package Test;
import tower.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * The test class TowerContestTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerContestTest
{
    
    
    @Test
    public void deberiaDar513(){
        TowerContest torre = new TowerContest();
        String s=torre.solve(3,5);
        assertEquals("5  1  3" ,s);
    }
    
    @Test
    public void noDeberiaDar531(){
        TowerContest torre = new TowerContest();
        String s=torre.solve(3,5);
        assertFalse(s.equals("5  3  1"));
    }
    
    @Test
    public void deberiaDarImposible(){
        TowerContest torre = new TowerContest();
        String s=torre.solve(50,500);
        assertEquals("Impossible" ,s);
    }
    
    @Test
    public void deberiaDar7315(){
        TowerContest torre = new TowerContest();
        String s=torre.solve(4,9);  
        assertEquals("7  3  1  5" ,s);  
    }
    
    @Test
    public void deberiaDarAltura90(){
        TowerContest torre = new TowerContest();
        Tower t = torre.auxiliar2(4,9);
        assertEquals(90 ,t.height());
    }
    
    @Test
    public void deberiaDar4920(){
        TowerContest torre = new TowerContest();
        String s=torre.solve(50,500);
        assertEquals("Impossible" ,s);
    }
    }

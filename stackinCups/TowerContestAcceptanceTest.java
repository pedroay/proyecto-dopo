import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 * The test class TowerContestAcceptanceTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerContestAcceptanceTest
{   @Test
    public void deberiaCrearCorrectamenteLaImagen(){
        TowerContest torre = new TowerContest();
        torre.simulate(3,5);
    }
    
    @Test 
    public void crearCorrectamenteImagen21(){
        Tower torre2 = new Tower(0);
        torre2.pushCup(3);
        torre2.pushCup(1);
        torre2.pushCup(2);
        torre2.makeVisible();
    }
    
    @Test
    public void deberiamostrarImposible(){
        TowerContest torre = new TowerContest();
        torre.simulate(3,1);
    }
    
    @Test
    public void deberiaCrearCorrectamenteLaImagen2(){
        TowerContest torre = new TowerContest();
        torre.simulate(4,9);
    }
    
    @Test 
    public void crearCorrectamenteImagen22(){
        Tower torre2 = new Tower(0);
        torre2.pushCup(4);
        torre2.pushCup(2);
        torre2.pushCup(1);
         torre2.pushCup(3);
        torre2.makeVisible();
    }
    }

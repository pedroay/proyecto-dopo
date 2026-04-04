package Test;
import tower.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class LidTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class LidTest
{
   @Test
   public void deberiaCrearUnaTapaDeUnAnchoDe50(){
       Lid tapa = new Lid(3);
       assertEquals(50,tapa.getWidth());
   }
   
   @Test
   public void deberiaFallarSiNOMeDaunAnchoDe50(){
       Lid tapa = new Lid(3);
       assertFalse(50 != tapa.getWidth());
   }
   
   @Test
   public void deberiaTenerUnaCopaConElMismoAncho(){
       Lid tapa = new Lid(3);
       Cup copa = tapa.getHisCup();
       
       assertEquals(tapa.getWidth(),copa.getHeight());
       assertEquals(tapa.getNumber(),copa.getNumber());
   }
   
   @Test
   public void debenTenerElMismoColor(){
       Lid tapa = new Lid(3);
       Cup copa = tapa.getHisCup();
    
       assertEquals(tapa.getColor(),copa.getColor());
   }
}
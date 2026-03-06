import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * La clase de prueba CupTest.
 *
 * @author  (tu nombre)
 * @version (una versión o fecha)
 */
public class CupTest {

    @Test
    public void deberiaCrearUnaCopaConAlturaCorrecta() {
        // Asumiendo que para el número 3, la altura calculada es 50 (similar a Lid)
        Cup copa = new Cup(3);
        assertEquals(50, copa.getHeight());
    }

    @Test
    public void deberiaIniciarEnEstadoNoCubierto() {
        Cup copa = new Cup(3);
        assertEquals("noCovered", copa.getState());
    }

    @Test
    public void deberiaTenerUnaTapaConElMismoNumero() {
        Cup copa = new Cup(3);
        Lid tapa = copa.getHisLid();
        
        assertNotNull(tapa); // Verifica que la tapa no sea nula
        assertEquals(copa.getNumber(), tapa.getNumber());
    }

    @Test
    public void deberiaTenerElMismoColorQueSuTapa() {
        Cup copa = new Cup(3);
        Lid tapa = copa.getHisLid();
        
        assertEquals(copa.getColor(), tapa.getColor());
    }

    @Test
    public void deberiaFallarSiElEstadoEsDiferenteDeNoCovered() {
        Cup copa = new Cup(3);
        // Comprobamos que NO sea un estado distinto al inicial
        assertFalse(!"noCovered".equals(copa.getState()));
    }
    
    @Test
    public void deberiaPosicionarseEnElEjeXCorrecto() {
        // Según tu código: posx = 150 - (height/2)
        // Si height es 50 -> 150 - 25 = 125
        Cup copa = new Cup(3);
        int expectedX = 150 - (copa.getHeight() / 2);
        assertEquals(expectedX, copa.getPosx());
    }
    
    @Test
    public void deberiaDejarmeSetUnaCopaAdentro(){
        Cup a = new Cup(5);
        Cup b = new Cup(4);
        a.setInside(b);
        assertEquals(b,a.getInside());
    }
    
    @Test
    public void deberiaDejarmeSetUnaTapaAdentro(){
        Cup a = new Cup(5);
        Cup b = new Cup(4);
        a.setInside(b.getHisLid());
        assertEquals(b.getHisLid(),a.getInside());
    }
    
    @Test 
    public void noDeberiatenerUnaCopaAdentroDeSiMismaMayorASi(){
        Cup a = new Cup(5);
        Cup b = new Cup(4);
        b.setInside(a);
        assertEquals(a,b.getAbove());
    }
}
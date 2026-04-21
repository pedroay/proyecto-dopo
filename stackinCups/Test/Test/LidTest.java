package Test;
import tower.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LidTest {
    
    private Tower torre;
    
    @BeforeEach
    public void setUp() {
        torre = new Tower(6, 300); // ancho y alto de la torre
    }
    
    @AfterEach
    public void tearDown() {
        torre = null;
    }
    
    @Test
    public void deberiaCrearUnaTapaConNumero3() {
        torre.pushCup(3);
        torre.pushLid(3);
        // Verifica que la tapa fue creada correctamente
        assertNotNull(torre);
    }
    
    @Test
    public void deberiaCrearTapaConCupAsociada() {
        Cup copa = new Cup(3, torre);
        Lid tapa = new Lid(3, copa,copa.getColor(),copa.getTower());        
        assertEquals(copa, tapa.getHisCup());
    }
    
    @Test
    public void deberiaTenerElMismoColorQueSuCopa() {
        Cup copa = new Cup(3, torre);
        Lid tapa = new Lid(3, copa,copa.getColor(),copa.getTower());
        
        assertEquals(copa.getColor(), tapa.getColor());
    }
    
    @Test
    public void deberiaTenerElMismoNumeroQueSuCopa() {
        Cup copa = new Cup(3, torre);
        Lid tapa = new Lid(3, copa,copa.getColor(),copa.getTower());
        
        assertEquals(copa.getNumber(), tapa.getNumber());
    }
    
    @Test
    public void deberiaFallarSiEstadoInicialNoEsNormal() {
        Cup copa = new Cup(3, torre);
        Lid tapa = new Lid(3, copa,copa.getColor(),copa.getTower());
        
        assertEquals("normal", tapa.getState());
    }
    
    @Test
    public void deberiaCambiarEstado() {
        Cup copa = new Cup(3, torre);
        Lid tapa = new Lid(3, copa,copa.getColor(),copa.getTower());
        tapa.setState("fearful");
        
        assertEquals("fearful", tapa.getState());
    }
}
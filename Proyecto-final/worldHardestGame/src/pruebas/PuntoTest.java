package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import dominio.*;

public class PuntoTest {

    @Test
    public void testPuntoInitialization() {
        Punto punto = new Punto(2, 3);
        assertEquals(2.0, punto.getPosx(), 0.001);
        assertEquals(3.0, punto.getPosy(), 0.001);
        assertFalse("Initially, punto should not be collected", punto.isCollected());
        assertTrue("Punto should be collectible", punto.isCollectible());
        assertTrue("Initially, punto should be visible", punto.isVisible());
        assertEquals("Draw size ratio should be 0.5f", 0.5f, punto.getDrawSizeRatio(), 0.001f);
        assertEquals("Stroke width should be 1.5f", 1.5f, punto.getStrokeWidth(), 0.001f);
        assertEquals("Primary color should be Yellow-ish", new Color(255, 210, 0), punto.getPrimaryColor());
        assertEquals("Border color should be darker Yellow-ish", new Color(200, 160, 0), punto.getBorderColor());
    }

    @Test
    public void testPuntoCollection() {
        Punto punto = new Punto(0, 0);
        
        punto.collect();
        assertTrue("Punto should be collected after collect()", punto.isCollected());
        assertFalse("Punto should not be visible after being collected", punto.isVisible());
        
        punto.setCollected(false);
        assertFalse("Punto should not be collected after setCollected(false)", punto.isCollected());
        assertTrue("Punto should be visible again", punto.isVisible());
    }

    @Test
    public void testOnCollectDefaultDoesNotCrash() {
        Punto punto = new Punto(0, 0);
        Player player = new Player("TestHero", 0, 0);
        
        // This should run without changing any state or throwing exception
        punto.onCollect(player);
        assertFalse("Default onCollect should not mark it collected automatically", punto.isCollected());
    }
}

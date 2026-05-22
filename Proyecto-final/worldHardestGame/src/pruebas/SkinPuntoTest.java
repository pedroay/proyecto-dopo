package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import dominio.*;

public class SkinPuntoTest {

    @Test
    public void testSkinPuntoInitialization() {
        SkinPunto bluePunto = new SkinPunto(1, 2, "blue");
        assertEquals(1.0, bluePunto.getPosx(), 0.001);
        assertEquals(2.0, bluePunto.getPosy(), 0.001);
        assertEquals("blue", bluePunto.getColor());
        
        SkinPunto greenPunto = new SkinPunto(3, 4, "green");
        assertEquals("green", greenPunto.getColor());
        
        SkinPunto redPunto = new SkinPunto(5, 6, "red");
        assertEquals("red", redPunto.getColor());
        
        SkinPunto defaultPunto = new SkinPunto(0, 0, "unknown");
        assertEquals("unknown", defaultPunto.getColor());
    }

    @Test
    public void testSkinPuntoColors() {
        // Blue Skin
        SkinPunto blue = new SkinPunto(0, 0, "blue");
        assertEquals(new Color(100, 150, 255), blue.getPrimaryColor());
        assertEquals(new Color(20,  80,  200), blue.getBorderColor());

        // Green Skin
        SkinPunto green = new SkinPunto(0, 0, "GREEN");
        assertEquals(new Color(100, 255, 100), green.getPrimaryColor());
        assertEquals(new Color(20,  200, 20), green.getBorderColor());

        // Red Skin
        SkinPunto red = new SkinPunto(0, 0, "Red");
        assertEquals(new Color(255, 100, 100), red.getPrimaryColor());
        assertEquals(new Color(200, 20,  20), red.getBorderColor());

        // Unknown Skin
        SkinPunto unknown = new SkinPunto(0, 0, "yellow");
        assertEquals(Color.WHITE, unknown.getPrimaryColor());
        assertEquals(Color.GRAY, unknown.getBorderColor());
    }

    @Test
    public void testSkinPuntoOnCollectEffects() {
        Player player = new Player("Hero", 0, 0);
        // Default state should be RedState
        assertTrue(player.getState() instanceof RedState);

        // Pick up Blue skin
        SkinPunto blue = new SkinPunto(0, 0, "blue");
        blue.onCollect(player);
        assertTrue("Player state should change to BlueState", player.getState() instanceof BlueState);

        // Pick up Green skin
        SkinPunto green = new SkinPunto(0, 0, "green");
        green.onCollect(player);
        assertTrue("Player state should change to GreenState", player.getState() instanceof GreenState);

        // Pick up Red skin
        SkinPunto red = new SkinPunto(0, 0, "red");
        red.onCollect(player);
        assertTrue("Player state should change to RedState", player.getState() instanceof RedState);
    }
}

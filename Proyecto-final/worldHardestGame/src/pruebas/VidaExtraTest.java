package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import dominio.*;

public class VidaExtraTest {

    @Test
    public void testVidaExtraInitialization() {
        VidaExtra extraLife = new VidaExtra(3, 4);
        assertEquals(3.0, extraLife.getPosx(), 0.001);
        assertEquals(4.0, extraLife.getPosy(), 0.001);
        assertTrue("VidaExtra should be collectible", extraLife.isCollectible());
        assertEquals("Primary color should be Hot Pink", new Color(255, 105, 180), extraLife.getPrimaryColor());
        assertEquals("Border color should be Medium Violet Red", new Color(199, 21, 133), extraLife.getBorderColor());
    }

    @Test
    public void testVidaExtraOnCollectEffect() {
        Player player = new Player("Hero", 0, 0);
        // Default RedState starts with 1 life
        assertEquals(1, player.getState().getVidas());

        VidaExtra extraLife = new VidaExtra(0, 0);
        extraLife.onCollect(player);

        // Life count should have increased to 2
        assertEquals("Player life count should increase by 1", 2, player.getState().getVidas());
    }
}

package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;

public class PlayerTest {

    @Test
    public void testPlayerInitialization() {
        // En un mapa de 40x40, la celda (2,3) debería estar en (80, 120)
        Player player = new Player("TestHero", 2, 3);
        assertEquals("Nombre incorrecto", "TestHero", player.getNombre());
        assertEquals("Posición X en píxeles incorrecta", 80.0, player.getX(), 0.001);
        assertEquals("Posición Y en píxeles incorrecta", 120.0, player.getY(), 0.001);
    }

    @Test
    public void testPlayerVelocity() {
        Player player = new Player("Speedy", 0, 0);
        player.setVelX(5.5);
        player.setVelY(-2.0);
        
        assertEquals(5.5, player.getVelX(), 0.001);
        assertEquals(-2.0, player.getVelY(), 0.001);
    }
}

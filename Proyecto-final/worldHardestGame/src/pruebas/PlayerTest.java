package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;

public class PlayerTest {

    @Test
    public void testPlayerInitialization() {
        // En un mapa de 40x40, la celda (2,3) debería estar en (80, 120)
        Player player = new Player("TestHero", 2, 3);
        assertEquals("Nombre incorrecto", "TestHero", player.getName());
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

    @Test
    public void testPlayerRespawnPoint() {
        Player player = new Player("RespawnTester", 5, 5);
        player.setRespawnPoint(100.0, 150.0);
        
        assertEquals(100.0, player.getRespawnX(), 0.001);
        assertEquals(150.0, player.getRespawnY(), 0.001);
    }
    
    @Test
    public void testPlayerStates() {
        Player player = new Player("StateTester", 0, 0);
        assertTrue("El estado por defecto debería ser RedState", player.getState() instanceof RedState);
        
        player.setState(new GreenState());
        assertTrue("El estado debería ser GreenState", player.getState() instanceof GreenState);
        
        player.setState(new BlueState());
        assertTrue("El estado debería ser BlueState", player.getState() instanceof BlueState);
    }

    @Test
    public void testBlueState(){
        BlueState blueState = new BlueState();
        assertEquals(java.awt.Color.BLUE, blueState.getColor());
        assertEquals(9.0, blueState.getSpeed(), 0.001);
        assertEquals(25.0, blueState.getSize(), 0.001);
    }

    @Test
    public void testGreenStateFirstContactNoDeath() {
        Player player = new Player("GreenHero", 0, 0);
        player.setState(new GreenState());
        
        // GreenState starts with 2 lives
        assertEquals(2, player.getState().getVidas());
        assertEquals(6.0, player.getState().getSpeed(), 0.001);
        assertFalse(player.getState().isImmune());

        // First contact: should lose 1 life, gain immunity, and slow down to 4.0 (2/3 of 6.0)
        player.handleEnemyContact();
        
        assertEquals(1, player.getVidas());
        assertTrue(player.isImmune());
        assertEquals(4.0, player.getState().getSpeed(), 0.001);
    }

    @Test
    public void testGreenStateSecondContactDeath() {
        Player player = new Player("GreenHero", 0, 0);
        player.setState(new GreenState());
        
        // First contact
        player.handleEnemyContact();
        assertEquals(1, player.getState().getVidas());
        assertTrue(player.getState().isImmune());

        // Second contact while immune should be ignored
        player.getState().handleEnemyContact();
        assertEquals(1, player.getState().getVidas()); // Still 1 life

        // Simulate 120 frames passing to expire immunity
        for (int i = 0; i < 120; i++) {
            player.onTick();
        }
        assertFalse(player.getState().isImmune());

        // Second contact after immunity expired: should lose the last life and die
        player.handleEnemyContact();
        assertEquals(0, player.getState().getVidas());
    }
}

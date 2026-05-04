package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.*;

/**
 * Pruebas de unidad para la clase WorldHG usando JUnit 4.
 */
public class WorldHGTest {

    private WorldHG game;

    @Before
    public void setUp() {
        // Inicializar el juego en cada prueba
        game = new WorldHG("player");
    }

    @Test
    public void shouldLoadLevelCorrectly() {
        String[] rows = {
            "W W W W W",
            "W S . . W",
            "W W W W W"
        };
        Level level = new Level(1, rows);
        game.loadLevel(level);

        assertNotNull("El jugador no debería ser nulo tras cargar el nivel", game.getPlayer1());
        assertNotNull("El tablero no debería ser nulo tras cargar el nivel", game.getBoard());
        assertEquals("El número de muertes debería empezar en 0", 0, game.getDeaths());
    }

    @Test
    public void shouldMovePlayerWhenKeyIsPressed() {
        String[] rows = {
            "W W W W W",
            "W S . . W",
            "W W W W W"
        };
        game.loadLevel(new Level(1, rows));
        
        Player p = game.getPlayer1();
        double initialX = p.getX();
        
        // Simular tecla derecha (Right) presionada
        game.setPlayerVelocity(false, false, false, true);
        game.tick(); 
        
        assertTrue("La posición X debería aumentar al moverse a la derecha", p.getX() > initialX);
    }

    @Test
    public void shouldNotPassThroughWalls() {
        String[] rows = {
            "W W W",
            "W S W",
            "W W W"
        };
        game.loadLevel(new Level(1, rows));
        
        Player p = game.getPlayer1();
        double xBefore = p.getX();
        double yBefore = p.getY();
        
        // Intentar moverse hacia arriba (contra una pared)
        game.setPlayerVelocity(true, false, false, false);
        for(int i = 0; i < 10; i++) game.tick();
        
        assertEquals("La posición X no debería cambiar al chocar con pared", xBefore, p.getX(), 0.001);
        assertEquals("La posición Y no debería cambiar al chocar con pared", yBefore, p.getY(), 0.001);
    }

    @Test
    public void shouldCollectCoinOnContact() {
        String[] rows = {
            "W W W W W",
            "W S P . W",
            "W W W W W"
        };
        game.loadLevel(new Level(1, rows));
        
        // Mover al jugador a la derecha hasta alcanzar la moneda en (1,2)
        game.setPlayerVelocity(false, false, false, true);
        for(int i = 0; i < 20; i++) game.tick();
        
        // Verificar que el objeto Punto en la celda (1,2) ya no existe o está marcado
        Board coinCell = game.getBoard()[1][2];
        boolean coinStillThere = false;
        for(Object obj : coinCell.getContents()) {
            if (obj instanceof Punto) coinStillThere = true;
        }
        
        assertFalse("La moneda debería haber sido recolectada", coinStillThere);
    }

    @Test
    public void shouldDieOnEnemyContact() {
        String[] rows = {
            "W W W W W",
            "W S . BH W",
            "W W W W W"
        };
        game.loadLevel(new Level(1, rows));
        
        int initialDeaths = game.getDeaths();
        
        // Mover al jugador a la derecha hacia el enemigo en (1,3)
        game.setPlayerVelocity(false, false, false, true);
        for(int i = 0; i < 40; i++) game.tick();
        
        assertTrue("El contador de muertes debería haber aumentado", game.getDeaths() > initialDeaths);
    }
}

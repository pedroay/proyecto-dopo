package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.*;
import dominio.Object;

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
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0", "W 3 0", "W 4 0",
            "W 0 1", "S 1 1", "W 4 1",
            "W 0 2", "W 1 2", "W 2 2", "W 3 2", "W 4 2"
        };
        Level level = new Level(1, 5, 3, entities);
        game.loadLevel(level);

        assertNotNull("El jugador no debería ser nulo tras cargar el nivel", game.getPlayer1());
        assertNotNull("El tablero no debería ser nulo tras cargar el nivel", game.getBoard());
        assertEquals("El número de muertes debería empezar en 0", 0, game.getDeaths());
    }

    @Test
    public void shouldMovePlayerWhenKeyIsPressed() {
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0", "W 3 0", "W 4 0",
            "W 0 1", "S 1 1", "W 4 1",
            "W 0 2", "W 1 2", "W 2 2", "W 3 2", "W 4 2"
        };
        game.loadLevel(new Level(1, 5, 3, entities));
        
        Player p = game.getPlayer1();
        double initialX = p.getX();
        
        // Simular tecla derecha (Right) presionada
        game.movePlayerContinuous(p, "RIGHT");
        game.tick(); 
        
        assertTrue("La posición X debería aumentar al moverse a la derecha", p.getX() > initialX);
    }

    @Test
    public void shouldNotPassThroughWalls() {
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0",
            "W 0 1", "S 1 1", "W 2 1",
            "W 0 2", "W 1 2", "W 2 2"
        };
        game.loadLevel(new Level(1, 3, 3, entities));
        
        Player p = game.getPlayer1();
        double xBefore = p.getX();
        double yBefore = p.getY();
        
        // Intentar moverse hacia arriba (contra una pared)
        for(int i = 0; i < 10; i++) {
            game.movePlayerContinuous(p, "UP");
            game.tick();
        }
        
        assertEquals("La posición X no debería cambiar al chocar con pared", xBefore, p.getX(), 0.001);
        assertEquals("La posición Y no debería cambiar al chocar con pared", yBefore, p.getY(), 0.001);
    }

    @Test
    public void shouldCollectCoinOnContact() {
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0", "W 3 0", "W 4 0",
            "W 0 1", "S 1 1", "P 2 1", "W 4 1",
            "W 0 2", "W 1 2", "W 2 2", "W 3 2", "W 4 2"
        };
        game.loadLevel(new Level(1, 5, 3, entities));
        
        Player p = game.getPlayer1();
        // Mover al jugador a la derecha hasta alcanzar la moneda en (2,1)
        for(int i = 0; i < 20; i++) {
            game.movePlayerContinuous(p, "RIGHT");
            game.tick();
        }
        
        // Verificar que el objeto Punto en la celda (1,2) (row 1, col 2) ya no existe o está marcado
        Board coinCell = game.getBoard()[1][2];
        boolean coinStillThere = false;
        for(Object obj : coinCell.getContents()) {
            if (obj.isCollectible()) coinStillThere = true;
        }
        
        assertFalse("La moneda debería haber sido recolectada", coinStillThere);
    }

    @Test
    public void shouldDieOnEnemyContact() {
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0", "W 3 0", "W 4 0",
            "W 0 1", "S 1 1", "BH 3 1", "W 4 1",
            "W 0 2", "W 1 2", "W 2 2", "W 3 2", "W 4 2"
        };
        game.loadLevel(new Level(1, 5, 3, entities));
        
        int initialDeaths = game.getDeaths();
        Player p = game.getPlayer1();
        
        // Mover al jugador a la derecha hacia el enemigo en (3,1)
        for(int i = 0; i < 40; i++) {
            game.movePlayerContinuous(p, "RIGHT");
            game.tick();
        }
        
        assertTrue("El contador de muertes debería haber aumentado", game.getDeaths() > initialDeaths);
    }

    @Test
    public void shouldDecreaseTimeAfter60Frames() {
        String[] entities = { "S 0 0" };
        game.loadLevel(new Level(1, 1, 1, entities));
        int initialTime = game.getTimeRemaining();

        for (int i = 0; i < 60; i++) {
            game.tick();
        }

        assertEquals("El tiempo debería disminuir en 1 tras 60 ticks", initialTime - 1, game.getTimeRemaining());
    }

    @Test
    public void shouldCompleteLevelWhenOnGoalAndNoCoinsLeft() {
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0", "W 3 0",
            "W 0 1", "S 1 1", "G 2 1", "W 3 1",
            "W 0 2", "W 1 2", "W 2 2", "W 3 2"
        };
        game.loadLevel(new Level(1, 4, 3, entities));
        
        Player p = game.getPlayer1();
        // Mover al jugador a la meta
        for(int i = 0; i < 30; i++) {
            game.movePlayerContinuous(p, "RIGHT");
            game.tick();
        }
        
        assertTrue("El nivel debería estar completado", game.isLevelComplete());
    }

    @Test
    public void shouldNotCompleteLevelIfCoinsRemaining() {
        String[] entities = {
            "W 0 0", "W 1 0", "W 2 0",
            "W 0 1", "S 1 1", "W 2 1",
            "W 0 2", "P 1 2", "W 2 2",
            "W 0 3", "G 1 3", "W 2 3",
            "W 0 4", "W 1 4", "W 2 4"
        };
        game.loadLevel(new Level(1, 3, 5, entities));
        
        Player p = game.getPlayer1();
        // Mover al jugador directamente a la meta "G" (1,3) saltándose la moneda "P" (1,2)
        p.setX(1 * WorldHG.CELL_SIZE);
        p.setY(3 * WorldHG.CELL_SIZE);
        
        game.tick(); // procesar interacciones
        
        assertFalse("El nivel NO debería completarse si quedan monedas", game.isLevelComplete());
    }

    @Test
    public void shouldNormalizeDiagonalVelocity() {
        String[] entities = { "S 0 0" };
        game.loadLevel(new Level(1, 1, 1, entities));
        
        game.setPlayerVelocity(false, true, false, true); // Abajo y Derecha
        Player p = game.getPlayer1();
        
        double speed = p.getState().getSpeed();
        double expectedVel = speed * (1.0 / Math.sqrt(2));
        
        assertEquals("La velocidad diagonal X debería estar normalizada", expectedVel, p.getVelX(), 0.001);
        assertEquals("La velocidad diagonal Y debería estar normalizada", expectedVel, p.getVelY(), 0.001);
    }

    @Test
    public void shouldAddEnemyCorrectly() {
        String[] entities = { "S 0 0" };
        game.loadLevel(new Level(1, 1, 1, entities));
        int initialEnemies = game.getEnemies().size();
        
        Enemy enemy = new Mina(0, 0);
        game.addEnemy(enemy);
        
        assertEquals("La lista de enemigos debería aumentar", initialEnemies + 1, game.getEnemies().size());
        assertTrue("La lista de enemigos debería contener el enemigo añadido", game.getEnemies().contains(enemy));
    }

    @Test
    public void shouldReturnTrueWhenTimeIsUp() {
        String[] entities = { "S 0 0" };
        game.loadLevel(new Level(1, 1, 1, entities));
        
        // Simular que el tiempo se acaba (180 segundos * 60 ticks)
        for (int i = 0; i < 180 * 60; i++) {
            game.tick();
        }
        
        assertTrue("isTimeUp debería retornar true cuando el tiempo llega a 0", game.isTimeUp());
    }

    @Test
    public void shouldFormatInfoCorrectly() {
        String[] entities = { "S 0 0" };
        game.loadLevel(new Level(1, 1, 1, entities));
        
        String info = game.getInfo();
        assertTrue("La información debería contener el tiempo", info.contains("Tiempo: 3:00"));
        assertTrue("La información debería contener las muertes", info.contains("Muertes: 0"));
        assertTrue("La información debería contener el estado de monedas", info.contains("Monedas: Todas"));
    }
}

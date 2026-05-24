package pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.*;

public class AcceptanceTest {

    private WorldHG game;

    @Before
    public void setUp() {
        game = new WorldHG("player");
    }

    /**
     * 1. Una prueba de aceptación donde el jugador gana al recolectar todas las monedas y llegar a la meta.
     */
    @Test
    public void testPlayerWins() {
        // Nivel con zona de inicio (S) y meta (G), sin monedas
        String[] entities = {
            "S 0 0", "G 1 0"
        };
        game.loadLevel(new Level(1, 2, 1, entities));
        Player p = game.getPlayer(1);
        
        // Inicialmente el nivel no está completado
        assertFalse(game.isLevelComplete());
        
        // Movemos al jugador a la celda de la meta (Columna 1, x = 40 px)
        p.setX(1 * WorldHG.CELL_SIZE);
        p.setY(0);
        
        // Ejecutamos tick para procesar interacciones
        game.tick();
        
        assertTrue("El nivel debería completarse porque no hay monedas pendientes y el jugador llegó a la meta", game.isLevelComplete());
        assertEquals("Jugador 1", game.getWinner());
    }

    /**
     * 2. Una prueba de aceptación donde pasan los 3 minutos (180 segundos) y el jugador pierde.
     */
    @Test
    public void testPlayerLosesByTime() {
        String[] entities = { "S 0 0" };
        game.loadLevel(new Level(1, 1, 1, entities));
        
        assertFalse("Al iniciar el tiempo no debería haberse agotado", game.isTimeUp());
        assertEquals(180, game.getTimeRemaining());
        
        // Simulamos el paso de 3 minutos completos (180 segundos * 60 frames/segundo)
        for (int i = 0; i < 180 * 60; i++) {
            game.tick();
        }
        
        assertTrue("El tiempo de juego debería haberse agotado", game.isTimeUp());
        assertEquals(0, game.getTimeRemaining());
    }

    /**
     * 3. Una prueba de aceptación donde el jugador va al final sin las monedas y pierde por pasar el tiempo.
     */
    @Test
    public void testPlayerReachesGoalWithoutCoinsAndLosesByTime() {
        // Nivel con inicio (S), moneda (P) y meta (G)
        String[] entities = {
            "S 0 0", "P 1 0", "G 2 0"
        };
        game.loadLevel(new Level(1, 3, 1, entities));
        Player p = game.getPlayer(1);
        
        // Movemos al jugador directamente a la meta (Columna 2, x = 80 px) sin pasar por la moneda en (1,0)
        p.setX(2 * WorldHG.CELL_SIZE);
        p.setY(0);
        
        game.tick();
        
        // El nivel NO debe completarse porque hay monedas pendientes
        assertFalse("El nivel no debería estar completado porque falta recolectar la moneda", game.isLevelComplete());
        
        // Simulamos que el tiempo límite se agota estando en la meta
        for (int i = 0; i < 180 * 60; i++) {
            game.tick();
        }
        
        assertTrue("El tiempo de juego debería haberse agotado", game.isTimeUp());
        assertFalse("El nivel debería seguir incompleto al haberse acabado el tiempo", game.isLevelComplete());
    }

    /**
     * 4. Una prueba de aceptación donde el jugador pierde todas las vidas (GreenState con 2 vidas).
     */
    @Test
    public void testPlayerLosesAllLives() {
        // Nivel con inicio (S) y un enemigo tipo bola (B) en la siguiente celda
        String[] entities = {
            "S 0 0", "BH 1 0"
        };
        game.loadLevel(new Level(1, 2, 1, entities));
        Player p = game.getPlayer(1);
        
        // Cambiamos el estado a GreenState (que otorga 2 vidas iniciales)
        p.setState(new GreenState());
        assertEquals(2, p.getState().getVidas());
        assertEquals(0, game.getDeaths());
        
        // Forzamos colisión moviéndolo al pixel del enemigo (x = 40 px)
        p.setX(1 * WorldHG.CELL_SIZE);
        p.setY(0);
        
        // Primer impacto: pierde 1 vida, queda inmune, no muere del todo
        game.tick();
        assertEquals(1, p.getState().getVidas());
        assertTrue(p.getState().isImmune());
        assertEquals(0, game.getDeaths());
        
        // Esperamos a que la inmunidad termine (120 frames)
        for (int i = 0; i < 120; i++) {
            game.tick();
        }
        assertFalse(p.getState().isImmune());
        
        // Forzamos la segunda colisión en la misma celda
        p.setX(1 * WorldHG.CELL_SIZE);
        p.setY(0);
        
        // Segundo impacto: pierde la segunda vida y se registra muerte
        game.tick();
        assertEquals("El contador de muertes del juego debería haber aumentado a 1", 1, game.getDeaths());
    }

    /**
     * 5. Una prueba de aceptación donde el jugador recoge una skin (PB) y gana con su nuevo estado (BlueState).
     */
    @Test
    public void testPlayerSkinChangeAndWinning() {
        // Nivel con inicio (S), skin azul (PB) y meta (G)
        String[] entities = {
            "S 0 0", "PB 1 0", "G 2 0"
        };
        game.loadLevel(new Level(1, 3, 1, entities));
        Player p = game.getPlayer(1);
        
        // Al inicio tiene el RedState (por defecto)
        assertTrue(p.getState() instanceof RedState);
        
        // Recogemos la skin en la celda (1,0)
        p.setX(1 * WorldHG.CELL_SIZE);
        p.setY(0);
        game.tick();
        
        // El estado debe haber cambiado a BlueState
        assertTrue("La skin debería haber cambiado a BlueState", p.getState() instanceof BlueState);
        
        // Movemos a la meta (2,0)
        p.setX(2 * WorldHG.CELL_SIZE);
        p.setY(0);
        game.tick();
        
        // El nivel debe estar completado exitosamente
        assertTrue("El nivel debería haberse completado tras obtener la skin y llegar a la meta", game.isLevelComplete());
    }

    /**
     * 6. Una prueba de aceptación donde el jugador reaparece en la última zona segura (SafeZone Z) tras morir.
     */
    @Test
    public void testPlayerRespawnOnDeath() {
        // Nivel con inicio (S), zona segura intermedia (Z) y un enemigo (B)
        String[] entities = {
            "S 0 0", "Z 1 0", "BH 2 0"
        };
        game.loadLevel(new Level(1, 3, 1, entities));
        Player p = game.getPlayer(1);
        
        // Nos posicionamos primero en la zona segura (1,0) para establecer el nuevo punto de control (respawn)
        p.setX(1 * WorldHG.CELL_SIZE);
        p.setY(0);
        game.tick();
        
        // Validamos que el punto de respawn se haya actualizado a los píxeles de la zona segura (x = 40 px)
        assertEquals(40.0, p.getRespawnX(), 0.001);
        assertEquals(0.0, p.getRespawnY(), 0.001);
        
        // Nos movemos al enemigo (2,0)
        p.setX(2 * WorldHG.CELL_SIZE);
        p.setY(0);
        game.tick();
        
        // Tras el choque el jugador muere y debe reaparecer en la SafeZone (x = 40 px) en lugar del Start (x = 0 px)
        assertEquals(1, game.getDeaths());
        assertEquals("El jugador debería reaparecer en la SafeZone (x = 40)", 40.0, p.getX(), 0.001);
        assertEquals(0.0, p.getY(), 0.001);
    }
}

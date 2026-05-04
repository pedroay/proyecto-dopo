package dominio;

/**
 * Represents the user-controlled player.
 *
 * New features compared to the previous version:
 * - Respawn points are stored in pixels (double) to maintain consistency 
 *   with the continuous movement system.
 * - velX / velY variables reside in the Object base class and are activated 
 *   from GamePanel upon detecting key presses.
 */
public class Player extends Hero {

    private String name;

    /** Respawn point in pixels (column × CELL_SIZE). */
    private double respawnX;

    /** Respawn point in pixels (row × CELL_SIZE). */
    private double respawnY;

    /**
     * @param name    player's name (e.g., "Player1")
     * @param posx    initial column in the grid
     * @param posy    initial row in the grid
     */
    public Player(String name, int posx, int posy) {
        super(posx, posy);
        this.name = name;
        // The initial respawn matches the starting position in pixels
        this.respawnX = posx * 40.0;
        this.respawnY = posy * 40.0;
    }

    /**
     * Updates the respawn point using pixel coordinates.
     *
     * @param px position in pixels on the X axis
     * @param py position in pixels on the Y axis
     */
    public void setRespawnPoint(double px, double py) {
        this.respawnX = px;
        this.respawnY = py;
    }

    public double getRespawnX() {
        return respawnX;
    }

    public double getRespawnY() {
        return respawnY;
    }

    public String getName() {
        return name;
    }
}

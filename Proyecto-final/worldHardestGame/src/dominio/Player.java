package dominio;
import java.util.ArrayList;

/**
 * Represents the user-controlled player.
 *
 * New features compared to the previous version:
 * - Respawn points are stored in pixels (double) to maintain consistency 
 *   with the continuous movement system.
 * - velX / velY variables reside in the Object base class and are activated 
 *   from GamePanel upon detecting key presses.
 */
public class Player extends Hero implements interactWEnemy {

    private String name;
    private PlayerState state;
    private PlayerState originalState;
    private int deaths;

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
        this.state = new RedState();
        this.originalState = this.state;
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

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }
    
    public PlayerState getOriginalState() {
    	return originalState;
    }
    
    public void setOriginalState(PlayerState state) {
    	originalState = state;
    }
    
    /**
     * Checks if any enemy (Ball or Mine) overlaps with the player
     * using AABB detection matching their visual representations.
     */
    public void checkEnemyPlayerCollisions(double cellSize,ArrayList<Enemy> enemies) {

        // Player's visual bounding box
        double pSize = this.getSize();
        double pOffset = (cellSize - pSize) / 2.0;
        double pLeft = this.getX() + pOffset;
        double pTop = this.getY() + pOffset;

        for (Enemy enemy : enemies) {
        	if( shouldInteract(enemy) ) {
	            double eSize = cellSize - 10;
	            double eOffset = (cellSize - eSize) / 2.0;
	            double eLeft = enemy.getX() + eOffset;
	            double eTop = enemy.getY() + eOffset;
	
	            if (aabbOverlap(pLeft, pTop, pSize, eLeft, eTop, eSize)) {
	                if (this.isImmune()) {
	                    continue;
	                }
	                
	                this.handleEnemyContact();
	                if (this.getVidas() == 0)
	                	playerDies(cellSize);
	                    return; // Si muere, dejamos de revisar colisiones en este frame
	                }
	            }
	        }
        }
    
    
    /**
    * The player dies: increments the death counter and teleports them to the
    * respawn point in pixels.
    * Velocity is reset to zero to prevent the player from moving immediately upon
    * respawning.
    */
   private void playerDies(double CELL_SIZE) {
       deaths++;
       setX(getRespawnX());
       setY(getRespawnY());
       setPosx((int) (getRespawnX() / CELL_SIZE));
       setPosy((int) (getRespawnY() / CELL_SIZE));
       setVelX(0);
       setVelY(0);
       setState(getOriginalState());
       onPlayerDeath();
   }

    // ─── Behaviour hooks ──────────────────────────────────────────────────────

    /** The GUI uses this to skip drawing the player inside drawObject(). */
    @Override
    public boolean isPlayer() {
        return true;
    }

    // ─── Renderable — Player knows its own rendering category ─────────────────
    // (Player is drawn by drawPlayer(), not drawObject().
    //  Because Object defaults isVisible() to false, drawObject() skips it.)

    @Override
    public java.awt.Color getPrimaryColor() {
        // Actual color comes from getState().getColor() in drawPlayer()
        return java.awt.Color.RED;
    }

    @Override
    public java.awt.Color getBorderColor() {
        return java.awt.Color.RED.darker();
    }
    
    @Override
    public double getSpeed() {
    	return state.getSpeed();
    }

    public void onTick(){
        state.onTick();
    }
    
    public double getSize() {
    	return state.getSize();
    }
    
    public boolean isImmune() {
    	return state.isImmune();
    }
    
    public int getVidas() {
    	return state.getVidas();
    }
    	
    public void handleEnemyContact() {
    	 state.handleEnemyContact();
    }
    
    public void onPlayerDeath() {
    	state.onPlayerDeath();
    }
    
    public boolean shouldInteract(Object enemy) {
    	return canColideWith(enemy);
    	
    }
    
    public boolean isWithinExplosionRadius(Object other) {
    	return true;
    }
    
    public void setDeaths(int newDeath) {
    	deaths = newDeath;
    }
    
    public int getDeaths() {
    	return deaths;
    }
    
    public  void interact(Enemy other) {}
}

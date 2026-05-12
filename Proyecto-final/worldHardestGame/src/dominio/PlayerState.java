package dominio;

import java.awt.Color;

/**
 * Base abstract class for player states (skins).
 * Defines color, speed, size, and interaction with enemies.
 */
public abstract class PlayerState implements java.io.Serializable {
    protected Color color;
    protected double speed;
    protected double size;

    public PlayerState(Color color, double speed, double size) {
        this.color = color;
        this.speed = speed;
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSize() {
        return size;
    }

    /**
     * @return true if the player should die upon contacting an enemy.
     */
    public abstract boolean diesOnContact();

    /**
     * Called when the player touches an enemy.
     * Can be used to change state properties (like reducing speed for Green).
     * 
     * @param player the player object
     */
    public void handleEnemyContact(Player player) {
        // Default behavior: do nothing. Override in subclasses if needed.
    }

    /**
     * Called by WorldHG when the player dies (any cause).
     * Override in subclasses that need to reset internal state on death.
     * Replaces the former (instanceof GreenState) check in playerDies().
     * 
     * @param player the player that just died
     */
    public void onPlayerDeath(Player player) {
        // Default: no action
    }

    /**
     * Called every frame (~60fps) to update time-based state (e.g. immunity
     * counters).
     * 
     * @param player the player that owns this state
     */
    public void onTick(Player player) {
        // Default: no action
    }

    /**
     * @return true if the player is currently immune to enemy damage.
     */
    public boolean isImmune() {
        return false;
    }
}

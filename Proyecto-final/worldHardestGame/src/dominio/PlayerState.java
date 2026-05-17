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

    protected int vidasIniciales;
    protected int vidas;
    protected int immunityFrames = 0;
    protected int blinkCounter = 0;
    protected static final int IMMUNITY_DURATION = 120; // 2 seconds default

    public PlayerState(Color color, double speed, double size, int vidasIniciales) {
        this.color = color;
        this.speed = speed;
        this.size = size;
        this.vidasIniciales = vidasIniciales;
        this.vidas = vidasIniciales;
    }

    public Color getColor() {
        if (isImmune() && (blinkCounter / 8) % 2 == 0) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), 120);
        }
        return color;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSize() {
        return size;
    }

    public int getVidas() {
        return vidas;
    }

    public void addVida() {
        this.vidas++;
    }

    public void restarVida() {
        if (this.vidas > 0) this.vidas--;
    }

    public void resetVidas() {
        this.vidas = this.vidasIniciales;
    }

    /**
     * Called when the player touches an enemy.
     */
    public void handleEnemyContact(Player player) {
        if (isImmune()) return;
        
        restarVida();
        if (vidas > 0) {
            immunityFrames = IMMUNITY_DURATION;
            blinkCounter = 0;
        }
    }

    /**
     * Called by WorldHG when the player dies (vidas == 0).
     */
    public void onPlayerDeath(Player player) {
        resetVidas();
        this.immunityFrames = 0;
        this.blinkCounter = 0;
    }

    /**
     * Called every frame (~60fps)
     */
    public void onTick(Player player) {
        if (immunityFrames > 0) {
            immunityFrames--;
            blinkCounter++;
        }
    }

    public boolean isImmune() {
        return immunityFrames > 0;
    }
}


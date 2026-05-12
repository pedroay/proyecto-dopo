package dominio;

import java.awt.Color;

/**
 * Green Skin — Resistant.
 * Each time the player touches an enemy:
 *   - Gains 5 seconds of immunity (cannot be damaged).
 *   - Speed is reduced by 1/3 of its current value.
 * During immunity the player blinks visually (handled by the GUI).
 * The player never dies from enemy contact, but becomes progressively slower.
 */
public class GreenState extends PlayerState {

    /** 5 seconds × 60 frames/second = 300 frames of immunity. */
    private static final int IMMUNITY_DURATION = 300;

    /** Base speed before any reductions. */
    private static final double BASE_SPEED = 6.0;

    /** Whether the player has already survived one hit. */
    private boolean hasContacted = false;

    /** Remaining immunity frames (0 = not immune). */
    private int immunityFrames = 0;

    /** Frame counter used for the blink animation. */
    private int blinkCounter = 0;

    public GreenState() {
        super(Color.GREEN, BASE_SPEED, 40.0);
    }

    // ─── Core overrides ──────────────────────────────────────────────────────

    @Override
    public boolean diesOnContact() {
        // First hit: survives. Second hit (after immunity wears off): dies.
        return hasContacted;
    }

    @Override
    public boolean isImmune() {
        return immunityFrames > 0;
    }

    @Override
    public void handleEnemyContact(Player player) {
        if (isImmune()) return; // ignore hits while already immune

        // Mark that the player has used its free hit
        hasContacted = true;

        // Activate 5-second immunity
        immunityFrames = IMMUNITY_DURATION;
        blinkCounter = 0;

        // Reduce speed by 1/3 of the current speed
        this.speed = this.speed * 2.0 / 3.0;
    }

    @Override
    public void onTick(Player player) {
        if (immunityFrames > 0) {
            immunityFrames--;
            blinkCounter++;
        }
    }

    @Override
    public void onPlayerDeath(Player player) {
        resetState();
    }

    // ─── Visual helpers ──────────────────────────────────────────────────────

    /**
     * During immunity the color flickers between green and a transparent-ish
     * light green every 8 frames, producing a blinking effect.
     */
    @Override
    public Color getColor() {
        if (isImmune() && (blinkCounter / 8) % 2 == 0) {
            return new Color(144, 238, 144, 120); // semi-transparent light green
        }
        return color;
    }

    // ─── Internal ────────────────────────────────────────────────────────────

    /**
     * Resets all transient state (immunity, speed) back to initial values.
     * Called when the player dies and respawns.
     */
    private void resetState() {
        this.hasContacted = false;
        this.immunityFrames = 0;
        this.blinkCounter = 0;
        this.speed = BASE_SPEED;
    }
}

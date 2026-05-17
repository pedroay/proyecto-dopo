package dominio;

import java.awt.Color;

/**
 * Green Skin — Resistant.
 * Starts with 2 lives.
 * Each time the player touches an enemy:
 *   - Loses 1 life and gains temporary immunity.
 *   - Speed is reduced by 1/3 of its current value.
 * During immunity the player blinks visually (handled by the superclass).
 */
public class GreenState extends PlayerState {

    /** Base speed before any reductions. */
    private static final double BASE_SPEED = 6.0;

    public GreenState() {
        super(Color.GREEN, BASE_SPEED, 40.0, 2);
    }

    // ─── Core overrides ──────────────────────────────────────────────────────

    @Override
    public void handleEnemyContact(Player player) {
        if (isImmune()) return; // ignore hits while already immune

        super.handleEnemyContact(player);

        // Reduce speed by 1/3 of the current speed if it survives
        if (getVidas() > 0) {
            this.speed = this.speed * 2.0 / 3.0;
        }
    }

    @Override
    public void onPlayerDeath(Player player) {
        super.onPlayerDeath(player);
        this.speed = BASE_SPEED;
    }
}

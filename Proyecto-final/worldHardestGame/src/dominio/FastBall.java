package dominio;

import java.awt.Color;

/**
 * Fast-moving enemy but travels at
 * double the base speed.
 *
 * It leverages the speed defined in Ball —
 * no movement logic is duplicated here.
 *
 * Available movement patterns (same as Ball):
 *   "H" = horizontal bounce
 *   "V" = vertical bounce
 *   "P" = perimeter follow
 *
 * Representation in level*.txt:
 *   FH, FV, FP
 */
public class FastBall extends Ball {

    private static final double speedMultiplier = 2.0;

    private static final Color FAST_PRIMARY = new Color(220, 60, 0);   // Fiery orange-red
    private static final Color FAST_BORDER  = new Color(150, 20, 0);   // Dark red border

    /**
     * @param posx   initial column in the grid
     * @param posy   initial row in the grid
     * @param state  movement pattern: "H", "V", or "P"
     */
    public FastBall(int posx, int posy, String state) {
        super(posx, posy, state);
        setSpeed(super.getSpeed() * speedMultiplier);
    }

    /**
     * Returns double the base Ball speed.
     * The private movement methods in Ball call this.getSpeed() at runtime,
     * so they automatically use this faster value without any code duplication.
     */

    // ─── Renderable — FastBall uses a distinct red/orange color ──────────────

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getStrokeWidth() {
        return 2.5f;
    }

    @Override
    public Color getPrimaryColor() {
        return FAST_PRIMARY;
    }

    @Override
    public Color getBorderColor() {
        return FAST_BORDER;
    }
}

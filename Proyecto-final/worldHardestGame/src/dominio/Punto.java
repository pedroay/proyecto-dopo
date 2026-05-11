package dominio;

import java.awt.Color;

public class Punto extends Object implements interactWPlayer {
    private boolean collected;

    private static final Color PRIMARY_COLOR = new Color(255, 210, 0);
    private static final Color BORDER_COLOR  = new Color(200, 160, 0);

    public Punto(int posx, int posy) {
        super(posx, posy);
        this.collected = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }

    /**
     * Called by WorldHG when the player steps on this collectible.
     * Override in subclasses to apply extra effects (e.g. SkinPunto changes player state).
     * @param player the player that collected this object
     */
    public void onCollect(Player player) {
        // Default: no side-effect beyond marking as collected
    }

    // ─── Behaviour hooks ──────────────────────────────────────────────────────

    @Override
    public boolean isCollectible() {
        return true;
    }

    // ─── Renderable — Punto knows its own appearance ──────────────────────────

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getDrawSizeRatio() {
        return 0.5f; // Draws smaller and centered
    }

    @Override
    public float getStrokeWidth() {
        return 1.5f;
    }

    @Override
    public Color getPrimaryColor() {
        return PRIMARY_COLOR;
    }

    @Override
    public Color getBorderColor() {
        return BORDER_COLOR;
    }
}

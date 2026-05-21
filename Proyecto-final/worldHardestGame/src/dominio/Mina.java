package dominio;

import java.awt.Color;

/**
 * Static enemy. Does not move, but damages the player upon contact.
 * In the .txt file, it is represented as: M
 */
public class Mina extends Enemy implements interactWEnemy {

    private static final Color MINE_PRIMARY = new Color(20, 20, 120);
    private static final Color MINE_BORDER  = Color.BLACK;

    private int radio = 1;

    public Mina(int posx, int posy) {
        this(posx, posy, 1);
        this.setSpeed(0);
    }

    public Mina(int posx, int posy, int radio) {
        super(posx, posy);
        super.setMove(false);
        this.radio = radio;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public void move(Board[][] board) {}

    // ─── interactWEnemy ──────────────────────────────────────────────────────

    @Override
    public boolean shouldInteract(Enemy other) {
        // Triggered only if they occupy the exact same cell (direct collision)
        return this.getPosx() == other.getPosx() && this.getPosy() == other.getPosy();
    }

    @Override
    public boolean isWithinExplosionRadius(Enemy other) {
        // Chebyshev distance on grid coordinates: max(|dx|, |dy|) <= radio - 1
        int dx = (int) Math.abs(this.getPosx() - other.getPosx());
        int dy = (int) Math.abs(this.getPosy() - other.getPosy());
        int dist = Math.max(dx, dy);
        return dist <= (this.radio - 1);
    }

    @Override
    public void interact(Enemy other) {
        // Domain level interaction: can trigger extra custom effects or events between them.
        // Currently, it is a marker for WorldHG to process the deletion.
    }

    // ─── Renderable — Mina knows its own appearance ───────────────────────────

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getStrokeWidth() {
        return 1.5f;
    }

    @Override
    public Color getPrimaryColor() {
        return MINE_PRIMARY;
    }

    @Override
    public Color getBorderColor() {
        return MINE_BORDER;
    }
}
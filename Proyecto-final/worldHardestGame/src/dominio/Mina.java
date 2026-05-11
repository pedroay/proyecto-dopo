package dominio;

import java.awt.Color;

/**
 * Static enemy. Does not move, but damages the player upon contact.
 * In the .txt file, it is represented as: M
 */
public class Mina extends Enemy {

    private static final Color MINE_PRIMARY = new Color(20, 20, 120);
    private static final Color MINE_BORDER  = Color.BLACK;

    public Mina(int posx, int posy) {
        super(posx, posy);
        super.setMove(false);
    }

    public void move(Board[][] board) {}

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
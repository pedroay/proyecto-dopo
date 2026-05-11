package dominio;

import java.awt.Color;

/**
 * A special collectible that, when picked up, changes the player's state (skin).
 * The color field determines which PlayerState is applied AND what color
 * this coin is rendered with — no external logic needed.
 *
 * Extending Punto and overriding onCollect() means WorldHG never needs to
 * check (instanceof SkinPunto) — it simply calls coin.onCollect(player).
 */
public class SkinPunto extends Punto {
    private final String color;

    // Each skin type owns its own color palette
    private static final Color BLUE_PRIMARY  = new Color(100, 150, 255);
    private static final Color BLUE_BORDER   = new Color(20,  80,  200);
    private static final Color GREEN_PRIMARY = new Color(100, 255, 100);
    private static final Color GREEN_BORDER  = new Color(20,  200, 20);
    private static final Color RED_PRIMARY   = new Color(255, 100, 100);
    private static final Color RED_BORDER    = new Color(200, 20,  20);

    public SkinPunto(int posx, int posy, String color) {
        super(posx, posy);
        this.color = color;
    }

    /**
     * Applies the corresponding PlayerState when collected.
     * No caller needs to know the concrete type — they just call onCollect().
     */
    @Override
    public void onCollect(Player player) {
        switch (color.toLowerCase()) {
            case "blue":  player.setState(new BlueState());  break;
            case "green": player.setState(new GreenState()); break;
            case "red":   player.setState(new RedState());   break;
        }
    }

    // ─── Renderable — SkinPunto knows its own skin-coloured appearance ────────

    @Override
    public Color getPrimaryColor() {
        switch (color.toLowerCase()) {
            case "blue":  return BLUE_PRIMARY;
            case "green": return GREEN_PRIMARY;
            case "red":   return RED_PRIMARY;
            default:      return Color.WHITE;
        }
    }

    @Override
    public Color getBorderColor() {
        switch (color.toLowerCase()) {
            case "blue":  return BLUE_BORDER;
            case "green": return GREEN_BORDER;
            case "red":   return RED_BORDER;
            default:      return Color.GRAY;
        }
    }

    public String getColor() {
        return color;
    }
}

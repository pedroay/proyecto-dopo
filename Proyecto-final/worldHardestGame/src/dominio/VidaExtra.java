package dominio;

import java.awt.Color;

public class VidaExtra extends Punto {
    private static final Color PRIMARY_COLOR = new Color(255, 105, 180); // Hot Pink
    private static final Color BORDER_COLOR = new Color(199, 21, 133); // Medium Violet Red

    public VidaExtra(int posx, int posy) {
        super(posx, posy);
    }

    @Override
    public void onCollect(Player player) {
        player.getState().addVida();
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

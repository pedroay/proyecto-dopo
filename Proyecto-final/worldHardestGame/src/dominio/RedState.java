package dominio;

import java.awt.Color;

/**
 * Red Skin (classic).
 * Standard square with normal velocity and maneuverability.
 */
public class RedState extends PlayerState {

    public RedState() {
        // Assuming normal speed is 6.0 and normal size is 40.0 (CELL_SIZE)
        super(Color.RED, 6.0, 30.0, 1);
    }
}

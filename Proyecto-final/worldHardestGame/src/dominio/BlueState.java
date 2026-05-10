package dominio;

import java.awt.Color;

/**
 * Blue Skin.
 * Fast square that moves faster than red, but of smaller size.
 */
public class BlueState extends PlayerState {

    public BlueState() {
        // Faster speed, smaller size
        super(Color.BLUE, 9.0, 25.0); 
    }

    @Override
    public boolean diesOnContact() {
        return true; // Dies instantly on contact
    }
}

package dominio;

import java.awt.Color;

/**
 * Contract for any game object that can be drawn by the GUI.
 *
 * The GUI never identifies the object's type — it only asks for attributes
 * and uses them directly. Each class is responsible for knowing its own
 * rendering behaviour.
 *
 * Default implementations (provided by the Object base class) make objects
 * invisible, so subclasses only need to override what they want to show.
 */
public interface Renderable {

    /**
     * @return false if this object should be skipped by drawObject()
     *         (e.g. the player, which is drawn separately, or invisible objects).
     */
    boolean isVisible();

    /**
     * @return ratio of the available cell area this object occupies when drawn.
     *         1.0 = full cell (enemies), 0.5 = half-size centred (collectibles).
     */
    float getDrawSizeRatio();

    /**
     * @return the stroke width for the outline of this object.
     */
    float getStrokeWidth();

    /**
     * @return the main fill colour of this object.
     */
    Color getPrimaryColor();

    /**
     * @return the border / outline colour of this object.
     */
    Color getBorderColor();
}

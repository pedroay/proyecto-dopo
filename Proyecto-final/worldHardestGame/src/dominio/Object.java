package dominio;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Base class for all game objects.
 *
 * Maintains BOTH integer coordinates (posx, posy) for compatibility with the
 * static board (Board[][]) and floating-point coordinates (x, y) to represent
 * the exact pixel position during continuous movement.
 *
 * The double coordinates (x, y) are the "source of truth" during gameplay;
 * the integers (posx, posy) indicate which grid cell the object is in
 * and are used for wall collision detection.
 *
 * Implements {@link Renderable} so the GUI can query any object for its own
 * rendering data without instanceof checks or wrapper objects.
 */
public abstract class Object implements Renderable, java.io.Serializable {

    // Grid position (column/row indices) 
    private double posx;
    private double posy;

    // Continuous position in pixels
    private double x;
    private double y;

    //Velocity in pixels/frame 
    private double velX;
    private double velY;

    private ArrayList<String> colideWith;

    public Object(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
        // Initialize continuous coordinates at the top-left of the grid cell.
        // CELL_SIZE = 40 px (defined in WorldHG as a reference constant).
        this.x = posx * 40.0;
        this.y = posy * 40.0;
        this.velX = 0;
        this.velY = 0;
        this.colideWith = new ArrayList<>();
    }

 // Getters / Setters for integer position (grid)

    public double getPosx() { 
    	return posx;
    	}
    public double getPosy() { 
    	return posy;
    	}

    public void setPosx(double newPosx) { 
        this.posx = newPosx; 
    }
    
    public void setPosy(double newPosy) { 
        this.posy = newPosy; 
    }

    // Getters / Setters for continuous position (pixels) 

    public double getX() { 
    	return x; 
    	}
    public double getY() { 
    	return y; 
    	}

    public void setX(double x) { 
    	this.x = x; 
    	}
    public void setY(double y) { 
    	this.y = y; }

    
    //  Getters / Setters for velocity 

    public double getVelX() { 
    	return velX; 
    	}
    
    public double getVelY() {
    	return velY;
    	}

    public void setVelX(double velX) { 
    	this.velX = velX;
    	}
    public void setVelY(double velY) {
    	this.velY = velY; 
    	}

    /**
     * Multiplies the current X velocity by the given factor.
     * Useful for subclasses that want to run at a multiple of the base speed.
     */
    public void multiplyVelX(double factor) {
        this.velX *= factor;
    }

    /**
     * Multiplies the current Y velocity by the given factor.
     * Useful for subclasses that want to run at a multiple of the base speed.
     */
    public void multiplyVelY(double factor) {
        this.velY *= factor;
    }
    public void multVelX(double mult) {
    	double actVelx=getVelX();
    	double newVelx=actVelx*mult;
    	setVelX(newVelx);
    }
    
    public void multVelY(double mult) {
    	double actVely=getVelY();
    	double newVely=actVely*mult;
    	setVelY(newVely);
    }

    // ─── Behaviour hooks ──────────────────────────────────────────────────────

    /**
     * @return true if this object can be collected by the player (coins, skins…).
     *         Override in collectible subclasses.
     */
    public boolean isCollectible() {
        return false;
    }

    /**
     * @return true only for the Player object.
     *         Lightweight sentinel used by the GUI to skip the player
     *         inside drawObject() (player is drawn separately).
     */
    public boolean isPlayer() {
        return false;
    }

    // ─── Renderable defaults ──────────────────────────────────────────────────

    /** Default: not drawn by drawObject(). Visible subclasses override this to true. */
    @Override
    public boolean isVisible() {
        return false;
    }

    /**
     * Default: full-cell size. Collectibles override this to 0.5 to draw
     * a smaller, centred oval.
     */
    @Override
    public float getDrawSizeRatio() {
        return 1.0f;
    }

    /** Default stroke width. Override to customise the outline thickness. */
    @Override
    public float getStrokeWidth() {
        return 1.5f;
    }

    /** Default neutral fill colour. Subclasses override with their own colour. */
    @Override
    public Color getPrimaryColor() {
        return Color.GRAY;
    }

    /** Default neutral border colour. Subclasses override with their own colour. */
    @Override
    public Color getBorderColor() {
        return Color.DARK_GRAY;
    }

    //  Collisions (original inheritance)

    public boolean canColideW(Object obj) {
        return colideWith.contains(obj.getClass().getName());
    }

    public void addColideWith(Object newObject) {
        colideWith.add(newObject.getClass().getName());
    }
}

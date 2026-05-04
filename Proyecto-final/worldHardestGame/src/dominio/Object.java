package dominio;

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
 */
public abstract class Object {

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

    //  Collisions (original inheritance)

    public boolean canColideW(Object obj) {
        return colideWith.contains(obj);
    }

    public void addColideWith(Object newObject) {
    }
}

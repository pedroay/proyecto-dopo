package dominio;

/**
 * Abstract class for all moving entities (player and enemies).
 *
 * Implements canMove using the velocities (velX, velY) stored in Object.
 * The actual movement logic (including wall collisions) is executed by WorldHG;
 * these methods are convenience methods for direct displacements.
 */
public abstract class Personaje extends Object implements CanMove {
	
	private boolean move;
	private double speed;
    public Personaje(int posx, int posy) {
        super(posx, posy);
    }

    // ── Implementation of canMove (no parameters) ─────────────────────────────
    // These use the current velocity stored in Object (velX, velY).

    @Override
    public void moveY (double stepY)    { 
    	setY(getY() + stepY); 
    }

  

    @Override
    public void moveX(double stepX)  { 
        setX(getX() + stepX); 
    }
    
   public boolean canMoveInMap() {
	   return move;
   }
   
   public void setMove(boolean value) {
	   move = value;
   }
   
   public double getSpeed() {
	   return speed;
   }
   
   public void setSpeed(double speed) {
	   this.speed = speed;
   }
}
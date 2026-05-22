package dominio;

/**
 * Interface for all objects capable of moving across the map.
 *
 * Methods do not receive a delta because each class implementing the interface
 * manages its own internal speed (velX, velY in Object).
 * The actual movement in pixels is handled by WorldHG via the object's
 * velX/velY fields.
 */
public interface CanMove {
	
	 
    public void moveY(double StepY);
    public void moveX(double StepX);

    public boolean canMoveInMap();
    public void setMove(boolean value);
    
    public double getSpeed();
    public void setSpeed(double speed);
    }
	



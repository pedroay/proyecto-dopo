package dominio;

/**
 * Interface for all objects capable of moving across the map.
 *
 * Methods do not receive a delta because each class implementing the interface
 * manages its own internal speed (velX, velY in Object).
 * The actual movement in pixels is handled by WorldHG via the object's
 * velX/velY fields.
 */
public interface canMove {
    public void moveUp();
    public void moveDown();
    public void moveRight();
    public void moveLeft();
    }

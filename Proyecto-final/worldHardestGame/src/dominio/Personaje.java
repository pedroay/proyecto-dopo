package dominio;

/**
 * Abstract class for all moving entities (player and enemies).
 *
 * Implements canMove using the velocities (velX, velY) stored in Object.
 * The actual movement logic (including wall collisions) is executed by WorldHG;
 * these methods are convenience methods for direct displacements.
 */
public abstract class Personaje extends Object implements canMove {

    public Personaje(int posx, int posy) {
        super(posx, posy);
    }

    // ── Implementation of canMove (no parameters) ─────────────────────────────
    // These use the current velocity stored in Object (velX, velY).

    @Override
    public void moveUp()    { 
        setY(getY() - Math.abs(getVelY() > 0 ? getVelY() : 1)); 
    }

    @Override
    public void moveDown()  { 
        setY(getY() + Math.abs(getVelY() > 0 ? getVelY() : 1)); 
    }

    @Override
    public void moveRight() { 
        setX(getX() + Math.abs(getVelX() > 0 ? getVelX() : 1));
    }

    @Override
    public void moveLeft()  { 
        setX(getX() - Math.abs(getVelX() > 0 ? getVelX() : 1)); 
    }
}
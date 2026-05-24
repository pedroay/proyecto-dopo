package dominio;

/**
 * Defines the capability of an enemy to interact with another enemy in the game.
 * It provides methods to determine if a direct collision/interaction is triggered,
 * if another enemy is within an explosion/blast radius, and to execute the interaction itself.
 */
public interface interactWEnemy {

    /**
     * Checks whether this enemy should initiate a direct interaction or collision with another enemy.
     *
     * @param other the other enemy to evaluate
     * @return true if the conditions for direct interaction are met, false otherwise
     */
    boolean shouldInteract(Object other);

    /**
     * Determines if another enemy is located within the explosion or effect radius of this enemy.
     *
     * @param other the other enemy to evaluate
     * @return true if the other enemy is within the explosion radius, false otherwise
     */
    boolean isWithinExplosionRadius(Object other);

    /**
     * Performs the interaction behavior between this enemy and another enemy.
     *
     * @param other the other enemy that is being interacted with
     */
    void interact(Enemy other);
    
    /**
     * Returns true if two visual boxes overlap.
     * Uses a slight inner margin to avoid pixel-perfect unfair deaths. 
     */
    default boolean aabbOverlap(double aLeft, double aTop, double aSize, double bLeft, double bTop, double bSize) {
        double margin = 1.0;
        return aLeft + margin < bLeft + bSize - margin &&
                aLeft + aSize - margin > bLeft + margin &&
                aTop + margin < bTop + bSize - margin &&
                aTop + aSize - margin > bTop + margin;
    }
}

package dominio;

public interface interactWEnemy {
    boolean shouldInteract(Enemy other);
    boolean isWithinExplosionRadius(Enemy other);
    void interact(Enemy other);
}

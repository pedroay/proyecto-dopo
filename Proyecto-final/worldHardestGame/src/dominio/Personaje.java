package dominio;

/**
 * Clase abstracta para todas las entidades móviles (jugador y enemigos).
 *
 * Implementa canMove usando las velocidades (velX, velY) almacenadas en Object.
 * La lógica de movimiento real (con colisión de paredes) la ejecuta WorldHG;
 * estos métodos son de conveniencia para desplazamientos directos.
 */
public abstract class Personaje extends Object implements canMove {

    public Personaje(int posx, int posy) {
        super(posx, posy);
    }

    // ── Implementación de canMove (sin parámetro) ─────────────────────────────
    // Usan la velocidad actual almacenada en Object (velX, velY).

    @Override
    public void moveUp()    { setY(getY() - Math.abs(getVelY() > 0 ? getVelY() : 1)); }

    @Override
    public void moveDown()  { setY(getY() + Math.abs(getVelY() > 0 ? getVelY() : 1)); }

    @Override
    public void moveRight() { setX(getX() + Math.abs(getVelX() > 0 ? getVelX() : 1)); }

    @Override
    public void moveLeft()  { setX(getX() - Math.abs(getVelX() > 0 ? getVelX() : 1)); }
}

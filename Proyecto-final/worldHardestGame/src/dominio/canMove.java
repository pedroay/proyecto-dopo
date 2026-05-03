package dominio;

/**
 * Interfaz para todos los objetos capaces de desplazarse en el mapa.
 *
 * Los métodos no reciben delta porque cada clase que implementa la interfaz
 * conoce su propia velocidad internamente (velX, velY en Object).
 * El movimiento real en píxeles lo gestiona WorldHG a través de los campos
 * velX/velY del objeto.
 */
public interface canMove {
    public void moveUp();
    public void moveDown();
    public void moveRight();
    public void moveLeft();
}

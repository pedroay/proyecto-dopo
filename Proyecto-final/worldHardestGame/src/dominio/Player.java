package dominio;

/**
 * Representa al jugador controlado por el usuario.
 *
 * Novedades respecto a la versión anterior:
 * - Los puntos de reaparición (respawn) se almacenan en píxeles (double) para
 *   mantener consistencia con el sistema de movimiento continuo.
 * - Las variables velX / velY viven en la clase base Object y se activan desde
 *   el GamePanel al detectar teclas presionadas.
 */
public class Player extends Hero {

    private String nombre;

    /** Punto de reaparición en píxeles (columna × CELL_SIZE). */
    private double respawnX;

    /** Punto de reaparición en píxeles (fila × CELL_SIZE). */
    private double respawnY;

    /**
     * @param nombre nombre del jugador (ej. "Player1")
     * @param posx   columna inicial en la grilla
     * @param posy   fila inicial en la grilla
     */
    public Player(String nombre, int posx, int posy) {
        super(posx, posy);
        this.nombre = nombre;
        // El respawn inicial coincide con la posición de inicio en píxeles
        this.respawnX = posx * 40.0;
        this.respawnY = posy * 40.0;
    }

    /**
     * Actualiza el punto de reaparición usando coordenadas de píxeles.
     *
     * @param px posición en píxeles sobre el eje X
     * @param py posición en píxeles sobre el eje Y
     */
    public void setRespawnPoint(double px, double py) {
        this.respawnX = px;
        this.respawnY = py;
    }

    public double getRespawnX() {
        return respawnX;
    }

    public double getRespawnY() {
        return respawnY;
    }

    public String getNombre() {
        return nombre;
    }
}

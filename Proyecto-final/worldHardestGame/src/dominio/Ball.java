package dominio;

/**
 * Enemigo móvil cuyo comportamiento está definido por un String de estado.
 *
 * Con el nuevo sistema de movimiento continuo, Ball ya NO salta de celda en
 * celda: avanza BALL_SPEED píxeles por frame y rebota cuando su caja de
 * colisión (AABB) choca con una pared del mapa estático.
 *
 * Estados disponibles:
 *   "H" = horizontal — rebota entre paredes izquierda y derecha.
 *   "V" = vertical   — rebota entre paredes superior e inferior.
 *   "P" = perímetro  — sigue el contorno de las paredes en sentido horario.
 *
 * Representación en level*.txt:
 *   BH, BV, BP
 */
public class Ball extends Enemy {

    /** Velocidad de desplazamiento en píxeles por frame (~60 FPS). */
    private static final double BALL_SPEED = 2.5;

    /** Tamaño de la celda en píxeles (debe coincidir con GamePanel.CELL_SIZE). */
    public static final int CELL_SIZE = 40;

    private String estado;  // patrón de movimiento
    private double dirX;    // dirección en X: -1.0, 0 o 1.0
    private double dirY;    // dirección en Y: -1.0, 0 o 1.0

    /**
     * @param posx   columna inicial en la grilla
     * @param posy   fila inicial en la grilla
     * @param estado patrón de movimiento: "H", "V" o "P"
     */
    public Ball(int posx, int posy, String estado) {
        super(posx, posy);
        this.estado = estado;
        initDirection();
    }

    /** Inicializa la dirección según el estado. */
    private void initDirection() {
        switch (estado) {
            case "H":
                dirX = 1;
                dirY = 0;
                break; // inicia yendo a la derecha
            case "V":
                dirX = 0;
                dirY = 1;
                break; // inicia yendo hacia abajo
            case "P":
                dirX = 1;
                dirY = 0;
                break; // inicia en sentido horario (→)
            default:
                dirX = 1;
                dirY = 0;
                break;
        }
    }

    /**
     * Avanza la Ball un frame usando coordenadas continuas.
     * Delega al método correspondiente según el estado.
     *
     * @param board tablero estático (solo paredes, metas, etc.)
     */
    public void move(Board[][] board) {
        switch (estado) {
            case "H":
            case "V":
                moveStraight(board);
                break;
            case "P":
                movePerimeter(board);
                break;
        }
        // Actualizar la celda de grilla (posx/posy) para que WorldHG pueda
        // usar AABB entre jugador y Ball sin depender del Board[][].
        setPosx((int) (getX() / CELL_SIZE));
        setPosy((int) (getY() / CELL_SIZE));
    }

    // ─── Movimiento recto con rebote ──────────────────────────────────────────

    /**
     * Mueve la Ball en línea recta (H o V).
     * Si la próxima posición invade una pared, invierte la dirección y rebota.
     */
    private void moveStraight(Board[][] board) {
        double nextX = getX() + dirX * BALL_SPEED;
        double nextY = getY() + dirY * BALL_SPEED;

        if (isPixelBlocked(nextX, nextY, board)) {
            dirX = -dirX;
            dirY = -dirY;
            nextX = getX() + dirX * BALL_SPEED;
            nextY = getY() + dirY * BALL_SPEED;
        }

        setX(nextX);
        setY(nextY);
    }

    // ─── Movimiento en perímetro ──────────────────────────────────────────────

    /**
     * Movimiento en perímetro: sigue las paredes en sentido horario.
     * Prioridad: girar derecha → seguir recto → girar izquierda → reversa.
     */
    private void movePerimeter(Board[][] board) {
        double[][] attempts = {
            turnRight(dirX, dirY),
            { dirX, dirY },
            turnLeft(dirX, dirY),
            { -dirX, -dirY }
        };

        for (double[] dir : attempts) {
            double nextX = getX() + dir[0] * BALL_SPEED;
            double nextY = getY() + dir[1] * BALL_SPEED;
            if (!isPixelBlocked(nextX, nextY, board)) {
                dirX = dir[0];
                dirY = dir[1];
                setX(nextX);
                setY(nextY);
                return;
            }
        }
        // Todos los caminos bloqueados → se queda quieta este frame
    }

    // ─── Utilidades de dirección ──────────────────────────────────────────────

    /** Giro a la derecha (sentido horario): (1,0)→(0,1)→(-1,0)→(0,-1)→(1,0). */
    private double[] turnRight(double dx, double dy) {
        return new double[]{ -dy, dx };
    }

    /** Giro a la izquierda (sentido anti-horario). */
    private double[] turnLeft(double dx, double dy) {
        return new double[]{ dy, -dx };
    }

    // ─── Detección de colisión con paredes (AABB) ─────────────────────────────

    /**
     * Verifica si la esquina delantera de la Ball (en píxeles) cae dentro de
     * una celda no transitable.
     *
     * Se comprueban las cuatro esquinas del bounding box de la Ball (tamaño
     * CELL_SIZE × CELL_SIZE) para detectar correctamente la colisión al
     * moverse en diagonal durante el modo perímetro.
     *
     * @param px posición X candidata (en píxeles)
     * @param py posición Y candidata (en píxeles)
     */
    private boolean isPixelBlocked(double px, double py, Board[][] board) {
        int size = CELL_SIZE;
        // Probamos las cuatro esquinas de la caja de colisión
        int[][] corners = {
            { (int) px,           (int) py },
            { (int)(px + size-1), (int) py },
            { (int) px,           (int)(py + size-1) },
            { (int)(px + size-1), (int)(py + size-1) }
        };
        for (int[] c : corners) {
            int col = c[0] / size;
            int row = c[1] / size;
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return true;
            if (!board[row][col].isCanHaveObjectOnTop() || board[row][col].isSafeZone()) return true;
        }
        return false;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public String getEstado() {
        return estado;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }
}

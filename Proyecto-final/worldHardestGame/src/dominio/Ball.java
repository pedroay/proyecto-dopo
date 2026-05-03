package dominio;

/**
 * Enemigo móvil cuyo comportamiento está definido por un String de estado.
 * Esto permite agregar nuevos patrones de movimiento fácilmente.
 *
 * Estados disponibles:
 *   "H" = horizontal: se mueve de izquierda a derecha, rebota en paredes.
 *   "V" = vertical:   se mueve de arriba a abajo, rebota en paredes.
 *   "P" = perímetro:  recorre el contorno de las paredes en sentido horario.
 *
 * En el archivo .txt se representa como:
 *   BH = Ball estado horizontal
 *   BV = Ball estado vertical
 *   BP = Ball estado perímetro
 */
public class Ball extends Enemy implements canMove {
    private String estado;  // describe el patrón de movimiento
    private int dirX;       // dirección actual en X (-1, 0, 1)
    private int dirY;       // dirección actual en Y (-1, 0, 1)

    /**
     * @param posx   posición inicial columna
     * @param posy   posición inicial fila
     * @param estado patrón de movimiento: "H", "V" o "P"
     */
    public Ball(int posx, int posy, String estado) {
        super(posx, posy);
        this.estado = estado;
        initDirection();
    }

    /**
     * Inicializa la dirección de acuerdo al estado.
     */
    private void initDirection() {
        switch (estado) {
            case "H":
                dirX = 1;  dirY = 0;  break; // inicia yendo a la derecha
            case "V":
                dirX = 0;  dirY = 1;  break; // inicia yendo hacia abajo
            case "P":
                dirX = 1;  dirY = 0;  break; // inicia en sentido horario (→)
            default:
                dirX = 1;  dirY = 0;  break;
        }
    }

    /**
     * Delega el movimiento al método correspondiente según el estado.
     *
     * @param board la grilla dinámica del juego
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
            // Aquí se pueden agregar nuevos estados en el futuro
        }
    }

    /**
     * Movimiento en línea recta con rebote.
     * Si la próxima celda está bloqueada, invierte la dirección.
     */
    private void moveStraight(Board[][] board) {
        int currentX = getPosx();
        int currentY = getPosy();
        int newX = currentX + dirX;
        int newY = currentY + dirY;

        if (isBlocked(newX, newY, board)) {
            dirX = -dirX;
            dirY = -dirY;
            newX = currentX + dirX;
            newY = currentY + dirY;
        }

        updatePosition(currentX, currentY, newX, newY, board);
    }

    /**
     * Movimiento en perímetro: sigue las paredes en sentido horario.
     * Lógica: intenta girar a la derecha primero, luego ir recto,
     * luego girar a la izquierda, y finalmente retroceder.
     * Esto produce un recorrido que abraza el contorno de los obstáculos.
     */
    private void movePerimeter(Board[][] board) {
        int currentX = getPosx();
        int currentY = getPosy();

        // Prioridad: girar derecha → recto → girar izquierda → reversa
        int[][] attempts = {
            turnRight(dirX, dirY),
            {dirX, dirY},
            turnLeft(dirX, dirY),
            {-dirX, -dirY}
        };

        for (int[] dir : attempts) {
            int newX = currentX + dir[0];
            int newY = currentY + dir[1];
            if (!isBlocked(newX, newY, board)) {
                dirX = dir[0];
                dirY = dir[1];
                updatePosition(currentX, currentY, newX, newY, board);
                return;
            }
        }
        // Si todos están bloqueados, se queda quieto este tick
    }

    /**
     * Giro a la derecha (sentido horario):
     *   → (1,0)  vira a  ↓ (0,1)
     *   ↓ (0,1)  vira a  ← (-1,0)
     *   ← (-1,0) vira a  ↑ (0,-1)
     *   ↑ (0,-1) vira a  → (1,0)
     */
    private int[] turnRight(int dx, int dy) {
        return new int[]{-dy, dx};
    }

    /**
     * Giro a la izquierda (sentido anti-horario):
     *   → (1,0)  vira a  ↑ (0,-1)
     *   ↑ (0,-1) vira a  ← (-1,0)
     *   ← (-1,0) vira a  ↓ (0,1)
     *   ↓ (0,1)  vira a  → (1,0)
     */
    private int[] turnLeft(int dx, int dy) {
        return new int[]{dy, -dx};
    }

    /**
     * Actualiza la posición en el tablero: remueve de la celda actual y agrega en la nueva.
     */
    private void updatePosition(int fromX, int fromY, int toX, int toY, Board[][] board) {
        board[fromY][fromX].removeObject(this);
        setPosx(toX);
        setPosy(toY);
        board[toY][toX].addObject(this);
    }

    /**
     * Verifica si una celda está bloqueada (fuera de límites o no transitable).
     */
    private boolean isBlocked(int x, int y, Board[][] board) {
        if (x < 0 || x >= board[0].length || y < 0 || y >= board.length) {
            return true;
        }
        return !board[y][x].isCanHaveObjectOnTop();
    }

    /**
     * Implementación de canMove: delegan a Personaje (mueven posx/posy en 1).
     * La validación de colisiones con paredes es responsabilidad de WorldHG,
     * que verifica la celda destino antes de autorizar el movimiento.
     */
    @Override
    public void moveUp()    { super.moveUp();    }

    @Override
    public void moveDown()  { super.moveDown();  }

    @Override
    public void moveLeft()  { super.moveLeft();  }

    @Override
    public void moveRight() { super.moveRight(); }

    public String getEstado() { return estado; }
    public int getDirX() { return dirX; }
    public int getDirY() { return dirY; }
}

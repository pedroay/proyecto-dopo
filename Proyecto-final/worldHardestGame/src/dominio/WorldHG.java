package dominio;

import java.util.ArrayList;

/**
 * Núcleo del juego. Gestiona el estado lógico completo:
 * tablero estático, jugador y enemigos.
 *
 * ─── Arquitectura de movimiento continuo ────────────────────────────────────
 * El tablero Board[][] ahora contiene SOLO elementos estáticos:
 *   Borde (W), Start (S), Goal (G), SafeZone (Z), Punto (P).
 * El jugador y las bolas enemigas viven FUERA del tablero y usan
 * coordenadas en píxeles (double x, y).
 *
 * Las colisiones se detectan mediante AABB (Axis-Aligned Bounding Box):
 * dos rectángulos se solapan si sus intervalos en X e Y se intersectan.
 *
 * tick() debe llamarse ~60 veces por segundo desde GamePanel.
 * ────────────────────────────────────────────────────────────────────────────
 */
public class WorldHG {

    /** Tamaño de celda en píxeles — debe coincidir con GamePanel.CELL_SIZE. */
    public static final int CELL_SIZE = 40;

    /** Velocidad de movimiento del jugador en píxeles/frame. */
    public static final double PLAYER_SPEED = 6.0;

    /** Duración inicial del nivel en segundos. */
    private static final int INITIAL_TIME = 180;

    /** Cuántos frames acumular antes de descontar un segundo (a 60fps → 60). */
    private static final int FRAMES_PER_SECOND = 60;

    private Level level;

    /** Tablero estático: solo paredes, metas, monedas, zonas seguras. */
    private Board[][] board;

    private Player player1;
    private String modalidad;
    private int deaths;
    private int timeRemaining;   // en segundos
    private int frameCounter;    // frames acumulados para contar segundos
    private boolean levelComplete;

    /** Lista de todos los enemigos activos (Ball, Mina…). */
    private ArrayList<Enemy> enemies;

    public WorldHG(String modalidad) {
        this.modalidad  = modalidad;
        this.deaths     = 0;
        this.timeRemaining = INITIAL_TIME;
        this.enemies    = new ArrayList<>();
    }

    // ─── Carga de nivel ───────────────────────────────────────────────────────

    /**
     * Carga un nivel: construye el tablero estático y ubica al jugador en S.
     * Los enemigos (Ball, Mina) se registran en la lista interna pero NO en
     * el Board[][].
     */
    public void loadLevel(Level level) {
        this.level         = level;
        this.enemies       = new ArrayList<>();
        this.timeRemaining = INITIAL_TIME;
        this.frameCounter  = 0;
        this.deaths        = 0;
        this.levelComplete = false;
        this.board = buildBoard(level);

        if ("player".equals(modalidad)) {
            int[] start = findStart();
            this.player1 = new Player("Player1", start[0], start[1]);
        }
    }

    /**
     * Construye la grilla estática a partir del archivo de nivel.
     * Tokens:
     *   W  = pared (Borde)
     *   S  = inicio (Start)
     *   G  = meta (Goal)
     *   Z  = zona segura (SafeZone)
     *   P  = moneda (Punto)
     *   B* = Ball (enemigo móvil)  → NO se coloca en Board[][], solo en enemies[]
     *   M  = Mina (enemigo estático) → idem
     *   .  = celda vacía
     */
    private Board[][] buildBoard(Level level) {
        String[] rows = level.getRows();
        int height = level.getHeight();
        int width = level.getWidth();
        Board[][] newBoard = new Board[height][width];

        for (int y = 0; y < height; y++) {
            String[] tokens = rows[y].split(" ");
            for (int x = 0; x < tokens.length; x++) {
                String token = tokens[x];

                // Ball: cualquier token "B" + estado
                if (token.startsWith("B") && token.length() > 1) {
                    String estado = token.substring(1);
                    Ball ball = new Ball(x, y, estado);
                    enemies.add(ball);
                    newBoard[y][x] = new Board(x, y, true); // celda vacía debajo
                    continue;
                }

                switch (token) {
                    case "W":
                        newBoard[y][x] = new Board(x, y, false);
                        newBoard[y][x].addObject(new Borde(x, y));
                        break;
                    case "S":
                        newBoard[y][x] = new Board(x, y, true);
                        newBoard[y][x].addObject(new Start(x, y));
                        break;
                    case "G":
                        newBoard[y][x] = new Board(x, y, true);
                        newBoard[y][x].addObject(new Goal(x, y));
                        break;
                    case "Z":
                        newBoard[y][x] = new Board(x, y, true);
                        newBoard[y][x].addObject(new SafeZone(x, y));
                        break;
                    case "P":
                        newBoard[y][x] = new Board(x, y, true);
                        newBoard[y][x].addObject(new Punto(x, y));
                        break;
                    case "M": {
                        Mina mina = new Mina(x, y);
                        enemies.add(mina);
                        newBoard[y][x] = new Board(x, y, true);
                        break;
                    }
                    default: // "."
                        newBoard[y][x] = new Board(x, y, true);
                        break;
                }
            }
        }
        return newBoard;
    }

    // ─── Bucle principal ──────────────────────────────────────────────────────

    /**
     * Avanza el juego un frame (~16ms a 60fps).
     *
     * Orden de operaciones:
     *   1. Contar tiempo (cada 60 frames = 1 segundo).
     *   2. Mover las bolas enemigas.
     *   3. Mover al jugador según su velocidad actual.
     *   4. Detectar colisiones jugador↔enemigo y jugador↔objetos especiales.
     */
    public void tick() {
        // 1. Tiempo
        frameCounter++;
        if (frameCounter >= FRAMES_PER_SECOND) {
            frameCounter = 0;
            if (timeRemaining > 0) timeRemaining--;
        }

        // 2. Mover enemigos
        for (Enemy enemy : enemies) {
            if (enemy instanceof Ball) {
                ((Ball) enemy).move(board);
            }
        }

        // 3. Mover jugador
        // El movimiento continuo ahora lo dispara el GUI al presionar teclas,
        // llamando a movePlayerContinuous(player, direction).

        // 4. Interacciones
        checkEnemyPlayerCollisions();
        if (player1 != null) {
            checkPlayerBoardInteractions(player1);
        }
    }

    // ─── Movimiento del jugador ───────────────────────────────────────────────

    /**
     * Mueve al jugador en la dirección indicada ("UP", "DOWN", "LEFT", "RIGHT")
     * usando el sistema de coordenadas continuas con detección AABB de paredes.
     *
     * Es el punto de entrada que usa el GUI: traduce la dirección a velocidad,
     * aplica el movimiento con colisión y actualiza la celda de grilla.
     *
     * @param player    el jugador a mover
     * @param direction "UP" | "DOWN" | "LEFT" | "RIGHT"
     */
    public void movePlayerContinuous(Player player, String direction) {
        double vx = 0, vy = 0;
        switch (direction) {
            case "UP":    vy = -PLAYER_SPEED; break;
            case "DOWN":  vy =  PLAYER_SPEED; break;
            case "LEFT":  vx = -PLAYER_SPEED; break;
            case "RIGHT": vx =  PLAYER_SPEED; break;
            default: return; // dirección desconocida, no mover
        }

        // Movimiento paso a paso para tocar paredes (elimina "paredes fantasma")
        int steps = (int) Math.ceil(Math.abs(vx) > Math.abs(vy) ? Math.abs(vx) : Math.abs(vy));
        double stepX = vx / (steps > 0 ? steps : 1);
        double stepY = vy / (steps > 0 ? steps : 1);

        for (int i = 0; i < steps; i++) {
            // Intentar mover en X
            if (!isPlayerBlocked(player.getX() + stepX, player.getY())) {
                player.setX(player.getX() + stepX);
            }
            // Intentar mover en Y
            if (!isPlayerBlocked(player.getX(), player.getY() + stepY)) {
                player.setY(player.getY() + stepY);
            }
        }

        player.setPosx(player.getX() / CELL_SIZE);
        player.setPosy(player.getY() / CELL_SIZE);
    }

    /**
     * Verifica si la caja de colisión del jugador (CELL_SIZE × CELL_SIZE)
     * en la posición (px, py) intersecta con alguna pared del tablero.
     */
    private boolean isPlayerBlocked(double px, double py) {
        int size = CELL_SIZE;
        // Márgenes internos para no quedar atascado en esquinas (4 px de margen)
        int margin = 4;
        int[][] corners = {
            { (int)(px + margin),        (int)(py + margin) },
            { (int)(px + size - margin), (int)(py + margin) },
            { (int)(px + margin),        (int)(py + size - margin) },
            { (int)(px + size - margin), (int)(py + size - margin) }
        };
        for (int[] c : corners) {
            int col = c[0] / size;
            int row = c[1] / size;
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return true;
            if (!board[row][col].isCanHaveObjectOnTop()) return true;
        }
        return false;
    }

    /**
     * Método público para que GamePanel actualice la velocidad del jugador.
     * La dirección viene del estado de las teclas.
     *
     * @param direction "UP" | "DOWN" | "LEFT" | "RIGHT" | "" (parar eje)
     * @param axis      "H" (horizontal) o "V" (vertical)
     */
    public void setPlayerVelocity(boolean up, boolean down, boolean left, boolean right) {
        if (player1 == null) return;
        double vx = 0, vy = 0;
        if (left)  vx -= PLAYER_SPEED;
        if (right) vx += PLAYER_SPEED;
        if (up)    vy -= PLAYER_SPEED;
        if (down)  vy += PLAYER_SPEED;

        // Normalizar diagonal para evitar que sea más rápida
        if (vx != 0 && vy != 0) {
            double factor = 1.0 / Math.sqrt(2);
            vx *= factor;
            vy *= factor;
        }
        player1.setVelX(vx);
        player1.setVelY(vy);
    }

    // ─── Colisiones ───────────────────────────────────────────────────────────

    /**
     * Comprueba si algún enemigo (Ball o Mina) se solapa con el jugador
     * usando detección AABB.
     */
    private void checkEnemyPlayerCollisions() {
        if (player1 == null) return;
        for (Enemy enemy : enemies) {
            if (aabbOverlap(player1.getX(), player1.getY(),
                            enemy.getX(),   enemy.getY())) {
                playerDies(player1);
                return;
            }
        }
    }

    /**
     * Retorna true si dos cajas de CELL_SIZE×CELL_SIZE se solapan.
     * Usa un margen interior para tolerar pequeños roces sin matar al jugador.
     */
    private boolean aabbOverlap(double ax, double ay, double bx, double by) {
        int s = CELL_SIZE;
        int margin = 6; // margen de tolerancia en píxeles
        return ax + margin < bx + s - margin &&
               ax + s - margin > bx + margin &&
               ay + margin < by + s - margin &&
               ay + s - margin > by + margin;
    }

    /**
     * Revisa qué objetos estáticos hay en la celda donde se encuentra el
     * jugador y aplica los efectos correspondientes.
     */
    private void checkPlayerBoardInteractions(Player player) {
        // Usar el centro del jugador para detectar la celda actual (más preciso)
        double centerX = player.getX() + CELL_SIZE / 2.0;
        double centerY = player.getY() + CELL_SIZE / 2.0;
        int col = (int) (centerX / CELL_SIZE);
        int row = (int) (centerY / CELL_SIZE);

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return;

        Board cell = board[row][col];
        for (Object obj : new ArrayList<>(cell.getContents())) {
            if (obj instanceof Punto) {
                Punto coin = (Punto) obj;
                if (!coin.isCollected()) {
                    coin.collect();
                    cell.removeObject(coin);
                }
            } else if (obj instanceof SafeZone) {
                player.setRespawnPoint(player.getX(), player.getY());
            } else if (obj instanceof Goal) {
                if (allCoinsCollected()) {
                    levelComplete = true;
                }
            }
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private int[] findStart() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                for (Object obj : board[y][x].getContents()) {
                    if (obj instanceof Start) return new int[]{ x, y };
                }
            }
        }
        return new int[]{ 0, 0 }; // fallback
    }

    private boolean allCoinsCollected() {
        for (Board[] row : board) {
            for (Board cell : row) {
                for (Object obj : cell.getContents()) {
                    if (obj instanceof Punto && !((Punto) obj).isCollected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * El jugador muere: suma una muerte y lo teleporta al respawn en píxeles.
     * La velocidad se anula para que no siga moviéndose al renacer.
     */
    private void playerDies(Player player) {
        deaths++;
        player.setX(player.getRespawnX());
        player.setY(player.getRespawnY());
        player.setPosx((int)(player.getRespawnX() / CELL_SIZE));
        player.setPosy((int)(player.getRespawnY() / CELL_SIZE));
        player.setVelX(0);
        player.setVelY(0);
    }

    // --- Getters de estado del juego ---

    public int getDeaths() { return deaths; }

    public int getTimeRemaining() { return timeRemaining; }

    public boolean isTimeUp() { return timeRemaining <= 0; }

    public boolean isLevelComplete() { return levelComplete; }

    public Player getPlayer1() { return player1; }

    public Board[][] getBoard() { return board; }

    public Level getLevel() { return level; }

    public ArrayList<Enemy> getEnemies() { return enemies; }

    public String getInfo() {
        int mins = timeRemaining / 60;
        int secs = timeRemaining % 60;
        return String.format("Tiempo: %d:%02d | Muertes: %d | Monedas: %s",
                mins, secs, deaths, allCoinsCollected() ? "Todas" : "Faltan");
    }

    public String saveGame() {
        // TODO: Implementar serialización
        return null;
    }

    public void importGame(String file) {
        // TODO: Implementar carga de partida
    }
}

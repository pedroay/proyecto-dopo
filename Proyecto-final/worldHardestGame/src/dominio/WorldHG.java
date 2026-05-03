package dominio;

import java.util.ArrayList;

public class WorldHG {
    private Level level;
    private Board[][] board;
    private Player player1;
    private String modalidad;
    private int deaths;
    private int timeRemaining; // en segundos (3 minutos = 180 seg)
    private ArrayList<Enemy> enemies; // referencia rápida a todos los enemigos activos

    public WorldHG(String modalidad) {
        this.modalidad = modalidad;
        this.deaths = 0;
        this.timeRemaining = 180;
        this.enemies = new ArrayList<>();
    }

    /**
     * Carga un nivel: parsea el Level estático y construye el Board[][] dinámico.
     * Ubica al jugador en la celda de inicio (S).
     */
    public void loadLevel(Level level) {
        this.level = level;
        this.enemies = new ArrayList<>();
        this.timeRemaining = 180;
        this.deaths = 0;
        this.board = buildBoard(level);

        if ("player".equals(modalidad)) {
            int[] start = findStart();
            this.player1 = new Player("Player1", start[0], start[1]);
            board[start[1]][start[0]].addObject(player1);
        }
    }

    /**
     * Construye la grilla dinámica Board[][] a partir del Level estático.
     * Tokens soportados:
     *   W   = pared (Borde)
     *   S   = zona de inicio (Start)
     *   G   = meta final (Goal)
     *   Z   = zona segura intermedia (SafeZone)
     *   P   = moneda (Punto)
     *   B{estado} = Ball con el estado dado (BH, BV, BP, etc.)
     *   M   = Mina (enemigo estático)
     *   .   = celda vacía
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

                // Ball: cualquier token que empiece con "B" y tenga estado (BH, BV, BP...)
                if (token.startsWith("B") && token.length() > 1) {
                    String estado = token.substring(1); // "H", "V", "P", etc.
                    Ball ball = new Ball(x, y, estado);
                    newBoard[y][x] = new Board(x, y, true);
                    newBoard[y][x].addObject(ball);
                    enemies.add(ball);
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
                        newBoard[y][x] = new Board(x, y, true);
                        newBoard[y][x].addObject(mina);
                        enemies.add(mina);
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

    /**
     * Avanza el juego un paso:
     * 1. Descuenta un segundo del tiempo
     * 2. Mueve todos los enemigos móviles
     * 3. Verifica si algún enemigo colisionó con el jugador
     * Debe llamarse una vez por segundo desde la capa de presentación.
     */
    public void tick() {
        if (timeRemaining > 0) {
            timeRemaining--;
        }
        moveEnemies();
        checkEnemyPlayerCollisions();
    }

    /**
     * Mueve todos los enemigos que tienen lógica de movimiento.
     * La Mina no se mueve — solo las Ball.
     */
    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy instanceof Ball) {
                ((Ball) enemy).move(board);
            }
            // Mina: estática, no se mueve
        }
    }

    /**
     * Verifica si algún enemigo está en la misma celda que el jugador
     * después de que los enemigos se movieron.
     */
    private void checkEnemyPlayerCollisions() {
        if (player1 == null) return;
        int px = player1.getPosx();
        int py = player1.getPosy();
        for (Object obj : new ArrayList<>(board[py][px].getContents())) {
            if (obj instanceof Enemy) {
                playerDies(player1);
                return;
            }
        }
    }

    /**
     * Encuentra la posición de inicio (celda con Start) en el board.
     * Retorna {x, y}.
     */
    private int[] findStart() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                for (Object obj : board[y][x].getContents()) {
                    if (obj instanceof Start) return new int[]{x, y};
                }
            }
        }
        return new int[]{0, 0}; // fallback
    }

    /**
     * Mueve un jugador en la dirección indicada.
     * Verifica límites y si la celda destino es transitable.
     * Luego resuelve interacciones (enemigo, moneda, meta, zona segura).
     */
    public void movePlayer(Player player, String direction) {
        int currentX = player.getPosx();
        int currentY = player.getPosy();
        int newX = currentX;
        int newY = currentY;

        switch (direction) {
            case "UP":    newY = currentY - 1; break;
            case "DOWN":  newY = currentY + 1; break;
            case "LEFT":  newX = currentX - 1; break;
            case "RIGHT": newX = currentX + 1; break;
        }

        // Verificar límites del tablero
        if (newX < 0 || newY < 0 || newY >= board.length || newX >= board[0].length) return;

        Board targetCell = board[newY][newX];

        // Verificar si la celda es transitable
        if (!targetCell.isCanHaveObjectOnTop()) return;

        // Mover el jugador en el board
        board[currentY][currentX].removeObject(player);
        player.setPosx(newX);
        player.setPosy(newY);
        board[newY][newX].addObject(player);

        // Resolver interacciones en la celda destino
        checkInteractions(player, targetCell);
    }

    /**
     * Revisa los objetos presentes en la celda y resuelve interacciones con el jugador.
     */
    private void checkInteractions(Player player, Board cell) {
        for (Object obj : new ArrayList<>(cell.getContents())) {
            if (obj instanceof Enemy) {
                playerDies(player);
                return;
            } else if (obj instanceof Punto) {
                Punto coin = (Punto) obj;
                if (!coin.isCollected()) {
                    coin.collect();
                    cell.removeObject(coin);
                }
            } else if (obj instanceof SafeZone) {
                player.setRespawnPoint(player.getPosx(), player.getPosy());
            } else if (obj instanceof Goal) {
                if (allCoinsCollected()) {
                    // TODO: notificar victoria / cargar siguiente nivel
                }
            }
        }
    }

    /**
     * Verifica si todas las monedas del nivel fueron recolectadas.
     */
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
     * El jugador muere: suma una muerte y lo reaparece en su último spawn point.
     */
    private void playerDies(Player player) {
        deaths++;
        board[player.getPosy()][player.getPosx()].removeObject(player);
        player.setPosx(player.getRespawnX());
        player.setPosy(player.getRespawnY());
        board[player.getRespawnY()][player.getRespawnX()].addObject(player);
    }

    // --- Getters de estado del juego ---

    public int getDeaths() { return deaths; }

    public int getTimeRemaining() { return timeRemaining; }

    public boolean isTimeUp() { return timeRemaining <= 0; }

    public boolean isLevelComplete() { return allCoinsCollected(); }

    public Player getPlayer1() { return player1; }

    public Board[][] getBoard() { return board; }

    public Level getLevel() { return level; }

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

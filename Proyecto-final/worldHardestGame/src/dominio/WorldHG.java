package dominio;

import java.util.ArrayList;

public class WorldHG {
    private Level level;
    private Board[][] board;
    private Player player1;
    private Player player2;
    private String modalidad;
    private int deaths;

    public WorldHG(String modalidad) {
        this.modalidad = modalidad;
        this.deaths = 0;
    }

    /**
     * Carga un nivel: parsea el Level estático y construye el Board[][] dinámico.
     * Ubica al jugador en la celda de inicio (S).
     */
    public void loadLevel(Level level) {
        this.level = level;
        this.board = buildBoard(level);

        if ("player".equals(modalidad)) {
            int[] start = findStart();
            this.player1 = new Player("Player1", start[0], start[1]);
            board[start[1]][start[0]].addObject(player1);
        }
    }

    /**
     * Construye la grilla dinámica Board[][] a partir de la representación estática del Level.
     * Cada token del .txt se traduce en una celda Board con su contenido inicial.
     */
    private Board[][] buildBoard(Level level) {
        String[] rows = level.getRows();
        int height = level.getHeight();
        int width = level.getWidth();
        Board[][] newBoard = new Board[height][width];

        for (int y = 0; y < height; y++) {
            String[] tokens = rows[y].split(" ");
            for (int x = 0; x < tokens.length; x++) {
                switch (tokens[x]) {
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
                    case "B":
                        newBoard[y][x] = new Board(x, y, true);
                        newBoard[y][x].addObject(new Ball(x, y));
                        break;
                    default: // "." celda vacía
                        newBoard[y][x] = new Board(x, y, true);
                        break;
                }
            }
        }
        return newBoard;
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
        ArrayList<Object> snapshot = new ArrayList<>(cell.getContents());
        for (Object obj : snapshot) {
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

    public int getDeaths() {
        return deaths;
    }

    public String getInfo() {
        return "Muertes: " + deaths + " | Monedas recolectadas: " + (allCoinsCollected() ? "Todas" : "Faltan");
    }

    public String saveGame() {
        // TODO: Implementar serialización
        return null;
    }

    public void importGame(String file) {
        // TODO: Implementar carga de partida
    }
}

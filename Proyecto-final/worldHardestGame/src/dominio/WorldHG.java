package dominio;

import java.util.ArrayList;

/**
 * Game core. Manages the complete logical state:
 * static board, player, and enemies.
 *
 */
public class WorldHG {

    /** Cell size in pixels*/
    public static final int CELL_SIZE = 40;

    /** Player movement speed in pixels/frame. */
    public static final double PLAYER_SPEED = 6.0;

    /** Initial level duration in seconds. */
    private static final int INITIAL_TIME = 180;

    /** How many frames to accumulate before deducting a second (at 60fps → 60). */
    private static final int FRAMES_PER_SECOND = 60;

    private Level level;

    /** Static board: only walls, goals, coins, and safe zones. */
    private Board[][] board;

    private Player player1;
    private String modality;
    private int deaths;
    private int timeRemaining;   // in seconds
    private int frameCounter;    // accumulated frames to count seconds
    private boolean levelComplete;

    /** List of all active enemies (Ball, Mine...). */
    private ArrayList<Enemy> enemies;

    public WorldHG(String modality) {
        this.modality  = modality;
        this.deaths     = 0;
        this.timeRemaining = INITIAL_TIME;
        this.enemies    = new ArrayList<>();
    }

    
    /**
     * Loads a level: builds the static board and places the player at S.
     * Enemies (Ball, Mine) are registered in the internal list but NOT in
     * the Board[][].
     */
    public void loadLevel(Level level) {
        this.level         = level;
        this.enemies       = new ArrayList<>();
        this.timeRemaining = INITIAL_TIME;
        this.frameCounter  = 0;
        this.deaths        = 0;
        this.levelComplete = false;
        this.board = buildBoard(level);

        if ("player".equals(modality)) {
            int[] start = findStart();
            this.player1 = new Player("Player1", start[0], start[1]);
        }
    }
    
    /**
     * Builds the static grid from the level file.
     * Tokens:
     *   W  = wall (Border)
     *   S  = start (Start)
     *   G  = goal (Goal)
     *   Z  = safe zone (SafeZone)
     *   P  = coin (Point/Coin)
     *   B* = Ball (moving enemy) → NOT placed in Board[][], only in enemies[]
     *   M  = Mine (static enemy) → ditto
     *   .  = empty cell
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

                // Ball: any token "B" + state
                if (token.startsWith("B") && token.length() > 1) {
                    String state = token.substring(1);
                    Ball ball = new Ball(x, y, state);
                    enemies.add(ball);
                    newBoard[y][x] = new Board(x, y, true); // empty cell underneath
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
                        Mina mine = new Mina(x, y);
                        enemies.add(mine);
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

 // Main Loop 

    /**
     * Advances the game by one frame (~16ms at 60fps).
     *
     * Order of operations:
     *   1. Update timer (every 60 frames = 1 second).
     *   2. Move enemy balls.
     *   3. Move the player based on current velocity.
     *   4. Detect player↔enemy and player↔special object collisions.
     */
    public void tick() {
        // 1. Timer
        frameCounter++;
        if (frameCounter >= FRAMES_PER_SECOND) {
            frameCounter = 0;
            if (timeRemaining > 0) timeRemaining--;
        }

        // 2. Move enemies
        for (Enemy enemy : enemies) {
            if (enemy instanceof Ball) {
                ((Ball) enemy).move(board);
            }
        }

        //nota para el pedro del futuro: aca debe estar el error de porque se queda queito al tocar la tecla al inicio
        // despues revisa este como move continues

        // 4. Interactions
        checkEnemyPlayerCollisions();
        if (player1 != null) {
            checkPlayerBoardInteractions(player1);
        }
    }

 //Player Movement 

    /**
     * Moves the player in the specified direction ("UP", "DOWN", "LEFT", "RIGHT")
     * using the continuous coordinate system with AABB wall detection.
     * @param player    the player to move
     * @param direction "UP" | "DOWN" | "LEFT" | "RIGHT"
     */
    public void movePlayerContinuous(Player player, String direction) {
        double vx = 0, vy = 0;
        switch (direction) {
            case "UP":    vy = -PLAYER_SPEED; break;
            case "DOWN":  vy =  PLAYER_SPEED; break;
            case "LEFT":  vx = -PLAYER_SPEED; break;
            case "RIGHT": vx =  PLAYER_SPEED; break;
            default: return; // Unknown direction, do not move
        }

        int steps = (int) Math.ceil(Math.abs(vx) > Math.abs(vy) ? Math.abs(vx) : Math.abs(vy));
        double stepX = vx / (steps > 0 ? steps : 1);
        double stepY = vy / (steps > 0 ? steps : 1);

        for (int i = 0; i < steps; i++) {
            // Attempt to move in X
            if (!isPlayerBlocked(player.getX() + stepX, player.getY())) {
                player.setX(player.getX() + stepX);
            }
            // Attempt to move in Y
            if (!isPlayerBlocked(player.getX(), player.getY() + stepY)) {
                player.setY(player.getY() + stepY);
            }
        }

        player.setPosx(player.getX() / CELL_SIZE);
        player.setPosy(player.getY() / CELL_SIZE);
    }

    /**
     * Checks if the player's collision box (CELL_SIZE × CELL_SIZE)
     * at position (px, py) intersects with any wall on the board.
     */
    private boolean isPlayerBlocked(double px, double py) {
        int size = CELL_SIZE;
        // Internal margins to prevent getting stuck in corners (4 px margin)
        int margin = 4;
        int[][] corners = {
            { (int)(px + margin), (int)(py + margin) },
            { (int)(px + size - margin), (int)(py + margin) },
            { (int)(px + margin), (int)(py + size - margin) },
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
     * Public method for GamePanel to update the player's velocity.
     * The direction is derived from the state of the keys.
     *
     * @param up    true if the "Up" key is pressed
     * @param down  true if the "Down" key is pressed
     * @param left  true if the "Left" key is pressed
     * @param right true if the "Right" key is pressed
     */
    public void setPlayerVelocity(boolean up, boolean down, boolean left, boolean right) {
        if (player1 == null) return;
        double vx = 0, vy = 0;
        if (left)  vx -= PLAYER_SPEED;
        if (right) vx += PLAYER_SPEED;
        if (up)    vy -= PLAYER_SPEED;
        if (down)  vy += PLAYER_SPEED;

        // Normalize diagonal movement to prevent it from being faster
        if (vx != 0 && vy != 0) {
            double factor = 1.0 / Math.sqrt(2);
            vx *= factor;
            vy *= factor;
        }
        player1.setVelX(vx);
        player1.setVelY(vy);
    }

 // ─── Collisions ───────────────────────────────────────────────────────────

    /**
     * Checks if any enemy (Ball or Mine) overlaps with the player
     * using AABB detection.
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
     * Returns true if two CELL_SIZE×CELL_SIZE boxes overlap.
     * Uses an internal margin to allow for minor grazing without killing the player.
     */
    private boolean aabbOverlap(double ax, double ay, double bx, double by) {
        int s = CELL_SIZE;
        int margin = 6; // tolerance margin in pixels
        return ax + margin < bx + s - margin &&
               ax + s - margin > bx + margin &&
               ay + margin < by + s - margin &&
               ay + s - margin > by + margin;
    }

    /**
     * Checks which static objects are present in the cell currently occupied 
     * by the player and applies the corresponding effects.
     */
    private void checkPlayerBoardInteractions(Player player) {
        // Use the player's center point to detect the current cell (more precise)
        double centerX = player.getX() + CELL_SIZE / 2.0;
        double centerY = player.getY() + CELL_SIZE / 2.0;
        int col = (int) (centerX / CELL_SIZE);
        int row = (int) (centerY / CELL_SIZE);

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return;

        Board cell = board[row][col];
        // Create a copy of the list to avoid ConcurrentModificationException during removal
        for (Object obj : new ArrayList<>(cell.getContents())) {
            if (obj instanceof Punto) {
                Punto coin = (Punto) obj;
                if (!coin.isCollected()) {
                    coin.collect();
                    cell.removeObject(coin);
                }
            } else if (obj instanceof SafeZone) {
                // Update the checkpoint/respawn point
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
     * The player dies: increments the death counter and teleports them to the respawn point in pixels.
     * Velocity is reset to zero to prevent the player from moving immediately upon respawning.
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

    public int getDeaths() { 
    	return deaths; 
    	}

    public int getTimeRemaining() { 
    	return timeRemaining; 
    }

    public boolean isTimeUp() {
return timeRemaining <= 0; 
}

    public boolean isLevelComplete() { 
    	return levelComplete;
    	}

    public Player getPlayer1() { 
    	return player1;
    	}

    public Board[][] getBoard() { 
    	return board; 
    	}

    public Level getLevel() {
    	return level; 
    	}

    public ArrayList<Enemy> getEnemies() { 
    	return enemies;
}

    public String getInfo() {
        int mins = timeRemaining / 60;
        int secs = timeRemaining % 60;
        return String.format("Tiempo: %d:%02d | Muertes: %d | Monedas: %s",
                mins, secs, deaths, allCoinsCollected() ? "Todas" : "Faltan");
    }

    public String saveGame() {
        return null;
    }

    public void importGame(String file) {
    }
}

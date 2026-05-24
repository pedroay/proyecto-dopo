package dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

/**
 * Game core. Manages the complete logical state:
 * static board, player, and enemies.
 *
 */
public class WorldHG implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Cell size in pixels */
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

    private ArrayList<Player> players;
    private String modality;
    private String winner;
    private int timeRemaining; // in seconds
    private int frameCounter; // accumulated frames to count seconds
    private boolean levelComplete;

    /** List of all active enemies (Ball, Mine...). */
    private ArrayList<Enemy> enemies;

    /** Handlers for parsing board tokens */
    private transient Map<String, TokenHandler> tokenHandlers;

    public WorldHG(String modality) {
        this.modality = modality;
        this.timeRemaining = INITIAL_TIME;
        this.enemies = new ArrayList<>();
        this.players = new ArrayList<>();
        this.tokenHandlers = new HashMap<>();
        initTokenHandlers();
    }

    private void initTokenHandlers() {
        tokenHandlers.put("W", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.setState(new Borde());
            map[row][col] = cell;
        });
        tokenHandlers.put("S", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.setState(new Start());
            map[row][col] = cell;
        });
        tokenHandlers.put("G", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.setState(new Goal());
            map[row][col] = cell;
        });
        tokenHandlers.put("Z", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.setState(new SafeZone());
            map[row][col] = cell;
        });
        tokenHandlers.put("P", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.addObject(new Punto(col, row));
            map[row][col] = cell;
        });
        tokenHandlers.put("PB", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.addObject(new SkinPunto(col, row, "blue"));
            map[row][col] = cell;
        });
        tokenHandlers.put("PG", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.addObject(new SkinPunto(col, row, "green"));
            map[row][col] = cell;
        });
        tokenHandlers.put("PR", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.addObject(new SkinPunto(col, row, "red"));
            map[row][col] = cell;
        });
        tokenHandlers.put("M", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            Mina mine = new Mina(col, row);
            ctx.addEnemy(mine);
            cell.addObject(mine);
            map[row][col] = cell;
        });
        tokenHandlers.put("V", (map, row, col, token, ctx) -> {
            Board cell = new Board(col, row);
            cell.addObject(new VidaExtra(col, row));
            map[row][col] = cell;
        });
        tokenHandlers.put(".", (map, row, col, token, ctx) -> map[row][col] = new Board(col, row));
    }

    public void addEnemy(Enemy enemy) {
        if (this.enemies != null) {
            this.enemies.add(enemy);
        }
    }

    /**
     * Loads a level: builds the static board and places the player at S.
     * Enemies (Ball, Mine) are registered in the internal list but NOT in
     * the Board[][].
     */
    public void loadLevel(Level level) {
        this.level = level;
        this.enemies = new ArrayList<>();
        this.players = new ArrayList<>();
        this.timeRemaining = INITIAL_TIME;
        this.frameCounter = 0;
        for(Player player:players) {
        	player.setDeaths(0);
        }
        this.levelComplete = false;
        this.board = buildBoard(level);

        if ("player".equals(modality) || "solo".equals(modality) || "pvp".equals(modality) || "pve".equals(modality)) {
            int[] start = findStart();
            Player player1 = new Player("Player1", start[0], start[1]);	
            players.add(player1);
            
            if ("pvp".equals(modality) || "pve".equals(modality)) {
                int[] goal = findGoal();
                Player player2 = new Player("Player2", goal[0], goal[1]);
                player2.setState(new BlueState());
                player2.setOriginalState(player2.getState());
                players.add(player2);
            }
        }
    }

    /**
     * Builds the static grid from the level's entity-list format.
     * Each entity line has the form: TOKEN col row
     * Cells not mentioned default to empty.
     */
    private Board[][] buildBoard(Level level) {
        int height = level.getHeight();
        int width = level.getWidth();
        Board[][] newBoard = new Board[height][width];

        // 1. Fill with empty cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newBoard[y][x] = new Board(x, y);
            }
        }

        // 2. Place each declared entity at its position
        for (String entityLine : level.getEntityLines()) {
            String[] parts = entityLine.split("\\s+");
            if (parts.length < 3)
                continue; // skip malformed lines
            int col = Integer.parseInt(parts[1]);
            int row = Integer.parseInt(parts[2]);
            if (row >= 0 && row < height && col >= 0 && col < width) {
                logicBoard(newBoard, row, col, parts);
            }
        }
        return newBoard;
    }

    /**
     * Processes a single token from the level definition and updates the board and
     * enemy list.
     * It places static objects (walls, goals, etc.) on the grid and instantiates
     * dynamic
     * entities like balls and mines.
     *
     * @param map   the 2D array representing the static board state
     * @param row   the vertical index (row) in the board array
     * @param col   the horizontal index (column) in the board array
     * @param token the string token representing the element to be processed
     */
    public void logicBoard(Board[][] map, int row, int col, String[] parts) {
    	String token = parts[0];
        if (token.startsWith("B") && token.length() > 1) {
            String state = token.substring(1);
            Ball ball = new Ball(col, row, state);
            enemies.add(ball);
            for(Player p:players) {
            	p.addColideWith(ball.getClass());
            }
            map[row][col] = new Board(col, row); // empty cell underneath
        } else if (token.startsWith("F") && token.length() > 1) {
            String state = token.substring(1);
            FastBall fastBall = new FastBall(col, row, state);
            enemies.add(fastBall);
            for(Player p:players) {
            	p.addColideWith(fastBall.getClass());
            }
            map[row][col] = new Board(col, row); // empty cell underneath
        } else if(token.startsWith("PO")){
        	ArrayList<Map.Entry<String, Integer>> instruccionesPolicia = new ArrayList<>();
        	for (int i = 3; i < parts.length; i += 2) {
                if (i + 1 < parts.length) { 
                    String direccion = parts[i];
                    int pasos = Integer.parseInt(parts[i + 1]);
                    instruccionesPolicia.add(Map.entry(direccion, pasos));
                }
        	}
        	Police policia = new Police(col, row, instruccionesPolicia);
            enemies.add(policia);
            for(Player p:players) {
            	p.addColideWith(policia.getClass());
            }
            map[row][col] = new Board(col, row);
        }
        else if (token.startsWith("M")) {
            Board cell = new Board(col, row);
            int radius = 1;
            if (token.length() > 1) {
                try {
                    radius = Integer.parseInt(token.substring(1));
                } catch (NumberFormatException e) {
                    // Fallback to 1
                }
            }
            Mina mine = new Mina(col, row, radius);
            this.addEnemy(mine);
            for(Player p:players) {
            	p.addColideWith(mine.getClass());
            }
            cell.addObject(mine);
            map[row][col] = cell;
        } else {
            TokenHandler handler = tokenHandlers.get(token);
            if (handler != null) {
                handler.handle(map, row, col, token, this);
            } else {
                // Fallback for empty or unknown tokens
                map[row][col] = new Board(col, row);
            }
        }
    }
    // Main Loop

    /**
     * Advances the game by one frame (~16ms at 60fps).
     *
     * Order of operations:
     * 1. Update timer (every 60 frames = 1 second).
     * 2. Move enemy balls.
     * 3. Move the player based on current velocity.
     * 4. Detect player↔enemy and player↔special object collisions.
     */
    public void tick() {
        // 1. Timer
        frameCounter++;
        if (frameCounter >= FRAMES_PER_SECOND) {
            frameCounter = 0;
            if (timeRemaining > 0)
                timeRemaining--;
        }

        // 2. Move enemies
        for (Enemy enemy : enemies) {
            if (enemy.canMoveInMap()) {
                enemy.move(board);
            }
        }

        // 3. Update player state (immunity timers, etc.)
        for(Player player:players) {
        	 if (player != null) {
                 player.onTick();
             }
        	 player.checkEnemyPlayerCollisions(CELL_SIZE,enemies);
        	 checkPlayerBoardInteractions(player);
        }

        checkEnemyEnemyInteractions();
        addColideEnemis();
    }

    // Player Movement

    /**
     * Moves the player in the specified direction ("UP", "DOWN", "LEFT", "RIGHT")
     * using the continuous coordinate system with AABB wall detection.
     * 
     * @param player    the player to move
     * @param direction "UP" | "DOWN" | "LEFT" | "RIGHT"
     */
    public void movePlayerContinuous(Player player, String direction) {
        double vx = 0, vy = 0;
        double speed = player.getSpeed();
        switch (direction) {
            case "UP":
                vy = -speed;
                break;
            case "DOWN":
                vy = speed;
                break;
            case "LEFT":
                vx = -speed;
                break;
            case "RIGHT":
                vx = speed;
                break;
            default:
                return; // Unknown direction, do not move
        }

        int steps = (int) Math.ceil(Math.abs(vx) > Math.abs(vy) ? Math.abs(vx) : Math.abs(vy));
        double stepX = vx / (steps > 0 ? steps : 1);
        double stepY = vy / (steps > 0 ? steps : 1);

        for (int i = 0; i < steps; i++) {
            // Attempt to move in X
            if (!isPlayerBlocked(player.getX() + stepX, player.getY(), player)) {
                player.moveX(stepX);
            }
            // Attempt to move in Y
            if (!isPlayerBlocked(player.getX(), player.getY() + stepY, player)) {
                player.moveY(stepY);
            }
        }

        player.setPosx(player.getX() / CELL_SIZE);
        player.setPosy(player.getY() / CELL_SIZE);
    }

    /**
     * Checks if the player's visual bounding box at the candidate position (px, py)
     * intersects with any wall on the board.
     */
    private boolean isPlayerBlocked(double px, double py, Player p) {
        if (p == null)
            return false;
        double stateSize = p.getState().getSize();
        double offset = (CELL_SIZE - stateSize) / 2.0;

        // Visual corners of the player (we shrink the box by 1 pixel to allow smooth
        // sliding)
        double margin = 1.0;
        double left = px + offset + margin;
        double right = px + offset + stateSize - margin;
        double top = py + offset + margin;
        double bottom = py + offset + stateSize - margin;

        double[][] corners = {
                { left, top },
                { right, top },
                { left, bottom },
                { right, bottom }
        };

        for (double[] c : corners) {
            int col = (int) (c[0] / CELL_SIZE);
            int row = (int) (c[1] / CELL_SIZE);
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length)
                return true;
            if (!board[row][col].isCanHaveObjectOnTop())
                return true;
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
    public void setPlayerVelocity(Player player1,boolean up, boolean down, boolean left, boolean right) {
        if (player1 == null)
            return;
        double speed = player1.getState().getSpeed();
        double vx = 0, vy = 0;
        if (left)
            vx -= speed;
        if (right)
            vx += speed;
        if (up)
            vy -= speed;
        if (down)
            vy += speed;

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
     * Checks which static objects are present in the cell currently occupied
     * by the player and applies the corresponding effects.
     */
    private void checkPlayerBoardInteractions(Player player) {
        // Use the player's center point to detect the current cell (more precise)
        double centerX = player.getX() + CELL_SIZE / 2.0;
        double centerY = player.getY() + CELL_SIZE / 2.0;
        int col = (int) (centerX / CELL_SIZE);
        int row = (int) (centerY / CELL_SIZE);

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length)
            return;

        Board cell = board[row][col];

        // 1. Ya NO usamos un for(Player p:players) aquí. 
        // Solo afectamos al 'player' que pasó por parámetro.
        
        if (cell.isARespawn() || cell.isAFinish()) {
            player.setRespawnPoint(col * CELL_SIZE, row * CELL_SIZE);
        }

     // Obtenemos qué jugador es (0 para el Jugador 1, 1 para el Jugador 2)
        int playerIndex = players.indexOf(player);

        // Definimos qué es "la meta" para cada quien
        // Jugador 1 gana si llega al final (G)
        boolean p1Wins = (playerIndex == 0 && cell.isAFinish());
        
        // Jugador 2 gana si llega a su propia meta 
        // (Cámbialo a cell.isAFinish() si ambos deben llegar a la misma G, 
        // o déjalo como cell.isARespawn() si el P2 debe llegar a la S)
        boolean p2Wins = (playerIndex == 1 && cell.isARespawn()); 

        if (p1Wins || p2Wins) {
            if (allCoinsCollected()) {
                levelComplete = true;
                // Lo guardamos sin espacio para evitar errores de lectura en tu GamePanel
                winner = "Jugador" + (playerIndex + 1); 
            }
        }

        // 3. Lógica de recolección de monedas intacta
        for (Object obj : new ArrayList<>(cell.getContents())) {
            if (obj.isCollectible()) {
                Punto coin = (Punto) obj;
                if (!coin.isCollected()) {
                    coin.collect();
                    cell.removeObject(coin);
                    coin.onCollect(player); // SkinPunto overrides this to apply a state
                }
            }
        }
    }
        

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private int[] findStart() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x].isARespawn())
                    return new int[] { x, y };
            }
        }
        return new int[] { 0, 0 }; // fallback
    }

    private int[] findGoal() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x].isAFinish())
                    return new int[] { x, y };
            }
        }
        return new int[] { 0, 0 }; // fallback
    }

    private boolean allCoinsCollected() {
        for (Board[] row : board) {
            for (Board cell : row) {
                for (Object obj : cell.getContents()) {
                    if (obj.isCollectible() && !((Punto) obj).isCollected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    // --- Getters de estado del juego ---

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public boolean isTimeUp() {
        return timeRemaining <= 0;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public Player getPlayer(int numberPlayer) {
        return players.get(numberPlayer-1);
    }

    public String getWinner() {
        return winner;
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
        int deaths=0;
        String livesText = "";
        for(Player player:players) {
	        if (player != null) {
	        	livesText += " | Vidas J"+ (players.indexOf(player) +1) +
	        			":" + player.getVidas();
	        	deaths += player.getDeaths();
	        	
	        }
	        	
        }
	        
	
	        return String.format("Tiempo: %d:%02d | Muertes: %d | Monedas: %s%s",
	                mins, secs, deaths, allCoinsCollected() ? "Todas" : "Faltan", livesText);
        
    }

    public void saveAs(File file) throws WorldHGException {
        if (!file.getName().toLowerCase().endsWith(".dat")) {
            file = new File(file.getParentFile(), file.getName() + ".dat");
        }
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
             java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (java.io.IOException e) {
        	throw new WorldHGException(WorldHGException.IO_ERROR);
        }
    }
    
    public static WorldHG open(File file) throws WorldHGException {
        try (java.io.FileInputStream fis = new java.io.FileInputStream(file);
             java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis)) {
            java.lang.Object obj = ois.readObject();
            if (!(obj instanceof WorldHG)) {
                throw new WorldHGException(WorldHGException.IO_ERROR);
            }
            return (WorldHG) obj;
        } catch (java.io.IOException | ClassNotFoundException e) {
            throw new WorldHGException(WorldHGException.IO_ERROR);
        }
    }

    public void removeEnemy(Enemy enemy) {
        if (this.enemies != null) {
            this.enemies.remove(enemy);
        }
        if (board != null) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c].getContents().contains(enemy)) {
                        board[r][c].removeObject(enemy);
                    }
                }
            }
        }
    }
    
   

    private void checkEnemyEnemyInteractions() {
        ArrayList<Enemy> currentEnemies = new ArrayList<>(enemies);
        ArrayList<Enemy> toRemove = new ArrayList<>();
        
        for (Enemy enemy : currentEnemies) {
            if (enemy instanceof interactWEnemy && !toRemove.contains(enemy)) {
                interactWEnemy interactor = (interactWEnemy) enemy;
                
                // 1. Check if the mine is triggered by a direct collision (shouldInteract)
                boolean triggered = false;
                for (Enemy other : currentEnemies) {
                    if (enemy != other && !toRemove.contains(other)) {
                        if (interactor.shouldInteract(other)) {
                            triggered = true;
                            break;
                        }
                    }
                }
                
                // 2. If triggered, explode and affect all enemies in the explosion radius (plus the mine itself)
                if (triggered) {
                    for (Enemy other : currentEnemies) {
                        if (!toRemove.contains(other)) {
                            if (other == enemy || interactor.isWithinExplosionRadius(other)) {
                                interactor.interact(other);
                                toRemove.add(other);
                            }
                        }
                    }
                    toRemove.add(enemy);
                }
            }
        }
        
        for (Enemy e : toRemove) {
            removeEnemy(e);
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.tokenHandlers = new HashMap<>();
        initTokenHandlers();
    }
    
    public int getDeaths() {
    	int deaths=0;
        for(Player player:players) {
        	deaths += player.getDeaths();
        }
        return deaths;
    }
    
    private void addColideEnemis() {
    	for(Player player:players) {
    		for(Enemy enemy:enemies) {
    			player.addColideWith(enemy.getClass());
    		}
    	}
    }
}

package dominio;

/**
 * Moving enemy whose behavior is defined by a state String.
 *
 * With the new continuous movement system, Ball NO LONGER jumps from cell
 * to cell: it advances BALL_SPEED pixels per frame and bounces when its
 * collision box (AABB) hits a wall in the static map.
 *
 * Available states:
 *   "H" = horizontal — bounces between left and right walls.
 *   "V" = vertical   — bounces between top and bottom walls.
 *   "P" = perimeter  — follows the contour of the walls clockwise.
 *
 * Representation in level*.txt:
 *   BH, BV, BP
 */
public class Ball extends Enemy {

	/** Movement speed in pixels per frame (~60 FPS). */
    private static final double BALL_SPEED = 2.5;

    /** Cell size in pixels (must match GamePanel.CELL_SIZE). */
    public static final int CELL_SIZE = 40;

    private String state;   // movement pattern
    private double dirX;    // X direction: -1.0, 0, or 1.0
    private double dirY;    // Y direction: -1.0, 0, or 1.0

    /**
     * @param posx   initial column in the grid
     * @param posy   initial row in the grid
     * @param state  movement pattern: "H", "V", or "P"
     */
    public Ball(int posx, int posy, String state) {
        super(posx, posy);
        this.state = state;
        initDirection();
    }
    
    /** Initializes the direction based on the state. */
    private void initDirection() {
        switch (state) {
            case "H":
                dirX = 1;
                dirY = 0;
                break; // starts by moving right
            case "V":
                dirX = 0;
                dirY = 1;
                break; // starts by moving down
            case "P":
                dirX = 1;
                dirY = 0;
                break; // starts clockwise (→)
            default:
                dirX = 1;
                dirY = 0;
                break;
        }
    }

    /**
     * Advances the Ball by one frame using continuous coordinates.
     * Delegates to the corresponding method based on the state.
     *
     * @param board static board (walls, goals, etc.)
     */
    public void move(Board[][] board) {
        switch (state) {
            case "H":	
            case "V":
                moveStraight(board);
                break;
            case "P":
                movePerimeter(board);
                break;
        }
        // Update the grid cell (posx/posy) so WorldHG can use AABB 
        // between player and Ball without relying on Board[][].
        setPosx((int) (getX() / CELL_SIZE));
        setPosy((int) (getY() / CELL_SIZE));
    }

   //MOVING METHOD
    

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


 // ─── Perimeter Movement ───────────────────────────────────────────────────

    /**
     * Perimeter movement: follows the walls in a clockwise direction.
     * Priority: turn right → continue straight → turn left → reverse.
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
        // All paths blocked → remains still this frame
    }

 // ─── Direction Utilities ───────────────────────────────────────────────────

    /** Right turn (clockwise): (1,0)→(0,1)→(-1,0)→(0,-1)→(1,0). */
    private double[] turnRight(double dx, double dy) {
        return new double[]{ -dy, dx };
    }

    /** Left turn (counter-clockwise). */
    private double[] turnLeft(double dx, double dy) {
        return new double[]{ dy, -dx };
    }

 //Wall Collision Detection (AABB) 

    /**
     * Checks if the leading edges of the Ball (in pixels) fall within a
     * non-traversable cell.
     *
     * The four corners of the Ball's bounding box (size CELL_SIZE × CELL_SIZE)
     * are checked to correctly detect collisions when moving diagonally 
     * during perimeter mode.
     *
     * @param px candidate X position (in pixels)
     * @param py candidate Y position (in pixels)
     */
    private boolean isPixelBlocked(double px, double py, Board[][] board) {
        int size = CELL_SIZE;
        // Test the four corners of the collision box
        int[][] corners = {
            { (int) px, (int) py },
            { (int)(px + size-1), (int) py },
            { (int) px, (int)(py + size-1) },
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

    public String getState() {
        return state;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }
}

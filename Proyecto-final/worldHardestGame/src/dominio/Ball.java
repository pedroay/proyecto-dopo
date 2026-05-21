package dominio;

import java.awt.Color;

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


    /** Cell size in pixels (must match GamePanel.CELL_SIZE). */
    public static final int CELL_SIZE = 40;

    private static final Color BALL_PRIMARY = new Color(30, 100, 200);
    private static final Color BALL_BORDER  = new Color(10,  60, 160);

    private String state;   // estado de movimiento horizontal o vertical
    private double dirX;    // X direction: -1.0, 0, or 1.0 esto nos dice donde se va a mover tipo en el plano cartesiano
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
        super.setMove(true);
        super.setSpeed(2.5);
    }
    
    /** Initializes the direction based on the state. */
    private void initDirection() {
        switch (state) {
            case "H":
                dirX = 1;
                dirY = 0;
                break;
            case "V":
                dirX = 0;
                dirY = 1;
                break; 
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
        }
        //esto es para que como tenemos 2 indicadroes posicion uno en la grid y otro en pixeles para manejar interaciones
        //Pos es para gri x o y para interaciones
        setPosx((int) (getX() / CELL_SIZE));
        setPosy((int) (getY() / CELL_SIZE));
    }

   //MOVING METHOD
    

    /**
     * Mueve la Ball en línea recta (H o V).
     * Si la próxima posición invade una pared, invierte la dirección y rebota.
     */
    private void moveStraight(Board[][] board) {
        double speed = getSpeed();
        double nextX = getX() + dirX * speed;
        double nextY = getY() + dirY * speed;

        if (isPixelBlocked(nextX, nextY, board)) {
            dirX = -dirX;
            dirY = -dirY;
            nextX = getX() + dirX * speed;
            nextY = getY() + dirY * speed;
        }

        setX(nextX);
        setY(nextY);
    }






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
        // aqui lo que hacemos es crear las cuatro esquina
        //(px, py) ─────────── (px+39, py)
        //│                      │
        //│       BALL           │
        //│                      │
        //(px, py+39) ──────── (px+39, py+39)
        // le restamos uno para no invadir ninguna  celda 
        int[][] corners = {
            { (int) px, (int) py },
            { (int)(px + size-1), (int) py },
            { (int) px, (int)(py + size-1) },
            { (int)(px + size-1), (int)(py + size-1) }
        };
        // t aca se divide para saver en cual de la board esta
        for (int[] c : corners) {
            int col = c[0] / size;
            int row = c[1] / size;
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return true;
            if (!board[row][col].isCanHaveObjectOnTop() || board[row][col].isSafe()) return true;
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

    // ─── Renderable — Ball knows its own appearance ───────────────────────────

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getStrokeWidth() {
        return 2.0f;
    }

    @Override
    public Color getPrimaryColor() {
        return BALL_PRIMARY;
    }

    @Override
    public Color getBorderColor() {
        return BALL_BORDER;
    }
}

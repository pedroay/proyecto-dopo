package dominio;

import java.awt.Color;

/**
 * Police enemy. Extends Enemy and moves horizontally 
 * with a flashing red/blue strobe effect.
 */
public class Police extends Enemy {

    private static final double POLICE_SPEED = 4.0;
    private static final int CELL_SIZE = 40;

    private double dirX = 1;
    private double dirY = 0;
    private int frameCount = 0;

    /**
     * @param posx initial column in the grid
     * @param posy initial row in the grid
     */
    public Police(int posx, int posy) {
        super(posx, posy);
        super.setMove(true);
    }

    @Override
    public void move(Board[][] board) {
        double nextX = getX() + dirX * POLICE_SPEED;
        double nextY = getY() + dirY * POLICE_SPEED;

        if (isPixelBlocked(nextX, nextY, board)) {
            dirX = -dirX;
            dirY = -dirY;
            nextX = getX() + dirX * POLICE_SPEED;
            nextY = getY() + dirY * POLICE_SPEED;
        }

        setX(nextX);
        setY(nextY);
        
        // Update the grid position
        setPosx((int) (getX() / CELL_SIZE));
        setPosy((int) (getY() / CELL_SIZE));
        
        frameCount++;
    }

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
            if (!board[row][col].isCanHaveObjectOnTop() || board[row][col].isSafe()) return true;
        }
        return false;
    }

    // ─── Renderable — Police knows its own appearance ───────────────────────────

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getStrokeWidth() {
        return 2.5f;
    }

    @Override
    public Color getPrimaryColor() {
        // Strobe light effect: alternates between Blue and Red every 15 frames
        return (frameCount / 15) % 2 == 0 ? new Color(0, 0, 200) : new Color(200, 0, 0);
    }

    @Override
    public Color getBorderColor() {
        return Color.WHITE; // White border to make it pop like a patrol car
    }
}

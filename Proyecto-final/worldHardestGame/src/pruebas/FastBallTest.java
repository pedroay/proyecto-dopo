package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;

public class FastBallTest {
    @Test
    public void testFastBallSpeed() {
        FastBall fastBall = new FastBall(0, 0, "H");
        assertEquals(5.0, fastBall.getSpeed(), 0.001);
        assertTrue( fastBall.isVisible());
    }
    

    @Test
    public void testFastBallPositionAfterTicks() {
        // Setup a 5x5 board of transitable cells
        Board[][] board = new Board[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Board(j, i);
                board[i][j].setState(new Empty());
            }
        }

        // FastBall starts at col=1, row=1 => x=40.0, y=40.0 in pixels
        FastBall fastBall = new FastBall(1, 1, "H");
        assertEquals(40.0, fastBall.getX(), 0.001);
        assertEquals(40.0, fastBall.getY(), 0.001);

        // Move 8 ticks. Speed is 5.0, dirX is 1.0.
        // Each tick moves 5.0 pixels to the right.
        // After 8 ticks, x should be 40.0 + 8 * 5.0 = 80.0
        for (int tick = 0; tick < 8; tick++) {
            fastBall.move(board);
        }

        assertEquals(80.0, fastBall.getX(), 0.001);
        assertEquals(40.0, fastBall.getY(), 0.001);
        
        // Grid positions should be updated (80 / 40 = 2, 40 / 40 = 1)
        assertEquals(2, (int) fastBall.getPosx());
        assertEquals(1, (int) fastBall.getPosy());
    }
}

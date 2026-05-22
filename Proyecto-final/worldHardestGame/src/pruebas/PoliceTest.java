package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import dominio.*;

public class PoliceTest {

    @Test
    public void testPoliceInitialization() {
        Police police = new Police(3, 4);
        assertEquals(3.0, police.getPosx(), 0.001);
        assertEquals(4.0, police.getPosy(), 0.001);
        assertEquals(120.0, police.getX(), 0.001);
        assertEquals(160.0, police.getY(), 0.001);
        
        assertTrue("Police should have move set to true", police.canMoveInMap());
        assertEquals("Police speed should be 4.0", 4.0, police.getSpeed(), 0.001);
        assertTrue("Police should be visible", police.isVisible());
        assertEquals("Police stroke width should be 2.5f", 2.5f, police.getStrokeWidth(), 0.001f);
        assertEquals("Police border color should be White", Color.WHITE, police.getBorderColor());
    }

    @Test
    public void testPoliceColorStrobeEffect() {
        Police police = new Police(0, 0,);
        
        Board[][] board = new Board[2][2];
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                board[r][c] = new Board(c, r);
                board[r][c].setState(new Empty()); // can have objects on top, not safe
            }
        }

        // Initially frameCount = 0 (frame/15 = 0, % 2 = 0) -> Blue
        assertEquals(new Color(0, 0, 200), police.getPrimaryColor());

        // Move 14 times
        for (int i = 0; i < 14; i++) {
            police.move(board);
        }
        // Framecount is 14 -> still Blue
        assertEquals(new Color(0, 0, 200), police.getPrimaryColor());

        // Move 15th time
        police.move(board);
        // Framecount is 15 -> (15/15) % 2 == 1 -> Red
        assertEquals(new Color(200, 0, 0), police.getPrimaryColor());

        // Move 15 more times to framecount 30
        for (int i = 0; i < 15; i++) {
            police.move(board);
        }
        // Framecount is 30 -> (30/15) % 2 == 0 -> Blue
        assertEquals(new Color(0, 0, 200), police.getPrimaryColor());
    }

    @Test
    public void testPoliceMovementAndBounce() {
        // Create a 1x3 board
        // row 0 has: col 0 (Empty), col 1 (Empty), col 2 (Borde - blocks)
        Board[][] board = new Board[1][3];
        board[0][0] = new Board(0, 0);
        board[0][0].setState(new Empty());
        
        board[0][1] = new Board(1, 0);
        board[0][1].setState(new Empty());
        
        board[0][2] = new Board(2, 0);
        board[0][2].setState(new Borde()); // Borde will block (isCanHaveObjectOnTop() is false)

        Police police = new Police(0, 0); // Speed 4.0, dirX = 1
        
        // Initial pos: x = 0.0
        assertEquals(0.0, police.getX(), 0.001);
        
        // Move towards right
        police.move(board);
        assertEquals(4.0, police.getX(), 0.001);

        // Position it closer to the block (col 2 is at pixel 80)
        // With size = 40, when x >= 41, the right corner (x + 39) will touch or cross 80.
        // Let's set the position to 40.0 (in col 1)
        police.setX(40.0);
        police.setY(0.0);
        police.setPosx(1);
        police.setPosy(0);
        
        // From x = 40.0, moving right with speed 4.0 puts nextX = 44.0.
        // Corner x + size - 1 is 44 + 39 = 83, which is in column 83/40 = 2 (the blocked Borde).
        // It should bounce (invert dirX to -1, nextX becomes 40 - 4 = 36).
        police.move(board);
        assertEquals(36.0, police.getX(), 0.001);
    }
}

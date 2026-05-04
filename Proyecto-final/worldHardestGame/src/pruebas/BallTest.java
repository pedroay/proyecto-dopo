package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;

public class BallTest {

    @Test
    public void testBallDirections() {
        // Bola Horizontal
        Ball hBall = new Ball(0, 0, "H");
        assertEquals("Dirección X inicial para H debería ser 1", 1.0, hBall.getDirX(), 0.001);
        assertEquals("Dirección Y inicial para H debería ser 0", 0.0, hBall.getDirY(), 0.001);
        
        // Bola Vertical
        Ball vBall = new Ball(0, 0, "V");
        assertEquals("Dirección X inicial para V debería ser 0", 0.0, vBall.getDirX(), 0.001);
        assertEquals("Dirección Y inicial para V debería ser 1", 1.0, vBall.getDirY(), 0.001);
    }

    @Test
    public void testBallBounceLogic() {
        // Mock de tablero con paredes
        Board[][] board = new Board[3][3];
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                board[i][j] = new Board(j, i, false); // Todas paredes inicialmente
            }
        }
        // Celda central transitable
        board[1][1] = new Board(1, 1, true);
        
        Ball ball = new Ball(1, 1, "H"); // Está en (40, 40)
        ball.setX(40);
        ball.setY(40);
        
        // Si intenta moverse a la derecha (hacia una pared en 2,1), debería rebotar
        ball.move(board);
        
        // La dirección debería haberse invertido
        assertEquals("La dirección X debería haberse invertido tras chocar", -1.0, ball.getDirX(), 0.001);
    }
}

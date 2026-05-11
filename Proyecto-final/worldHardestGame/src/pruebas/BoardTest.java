package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;
import dominio.Object;

public class BoardTest {

    @Test
    public void testBoardContents() {
        Board board = new Board(0, 0, true);
        Punto coin = new Punto(0, 0);
        
        board.addObject(coin);
        assertEquals("La lista contents debería tener 1 objeto", 1, board.getContents().size());
        assertTrue("El objeto debería ser coleccionable (isCollectible)", board.getContents().get(0).isCollectible());
        
        board.removeObject(coin);
        assertTrue("La lista contents debería estar vacía tras remover", board.isEmpty());
    }

    @Test
    public void testBoardSafeZone() {
        Board safeBoard = new Board(0, 0, true);
        safeBoard.addObject(new Start(0, 0));
        assertTrue("Debería ser zona segura si tiene un Start", safeBoard.isASafeZone());
        
        Board normalBoard = new Board(1, 1, true);
        assertFalse("No debería ser zona segura si está vacía", normalBoard.isASafeZone());
    }
}

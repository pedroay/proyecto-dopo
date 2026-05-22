package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import dominio.Object;

public class ObjectTest {

    // Concrete subclass of Object for testing purposes
    private static class TestObject extends Object {
        public TestObject(int posx, int posy) {
            super(posx, posy);
        }
    }

    @Test
    public void testConstructorAndInitialState() {
        TestObject obj = new TestObject(2, 3);
        assertEquals("posx should match constructor parameter", 2.0, obj.getPosx(), 0.001);
        assertEquals("posy should match constructor parameter", 3.0, obj.getPosy(), 0.001);
        assertEquals("x should be posx * 40.0", 80.0, obj.getX(), 0.001);
        assertEquals("y should be posy * 40.0", 120.0, obj.getY(), 0.001);
        assertEquals("velX should be initialized to 0", 0.0, obj.getVelX(), 0.001);
        assertEquals("velY should be initialized to 0", 0.0, obj.getVelY(), 0.001);
    }

    @Test
    public void testSettersAndGetters() {
        TestObject obj = new TestObject(0, 0);
        
        obj.setPosx(5.5);
        assertEquals(5.5, obj.getPosx(), 0.001);
        
        obj.setPosy(6.5);
        assertEquals(6.5, obj.getPosy(), 0.001);
        
        obj.setX(150.0);
        assertEquals(150.0, obj.getX(), 0.001);
        
        obj.setY(250.0);
        assertEquals(250.0, obj.getY(), 0.001);
        
        obj.setVelX(3.5);
        assertEquals(3.5, obj.getVelX(), 0.001);
        
        obj.setVelY(-1.5);
        assertEquals(-1.5, obj.getVelY(), 0.001);
    }

    @Test
    public void testVelocityMultiplication() {
        TestObject obj = new TestObject(0, 0);
        obj.setVelX(2.0);
        obj.setVelY(3.0);
        
        obj.multiplyVelX(2.5);
        assertEquals(5.0, obj.getVelX(), 0.001);
        
        obj.multiplyVelY(3.0);
        assertEquals(9.0, obj.getVelY(), 0.001);
        
        obj.multVelX(0.5);
        assertEquals(2.5, obj.getVelX(), 0.001);
        
        obj.multVelY(2.0);
        assertEquals(18.0, obj.getVelY(), 0.001);
    }

    @Test
    public void testDefaultBehaviorHooks() {
        TestObject obj = new TestObject(0, 0);
        assertFalse("Default isCollectible should be false", obj.isCollectible());
        assertFalse("Default isPlayer should be false", obj.isPlayer());
    }

    @Test
    public void testDefaultRenderableMethods() {
        TestObject obj = new TestObject(0, 0);
        assertFalse("Default isVisible should be false", obj.isVisible());
        assertEquals("Default getDrawSizeRatio should be 1.0f", 1.0f, obj.getDrawSizeRatio(), 0.001f);
        assertEquals("Default getStrokeWidth should be 1.5f", 1.5f, obj.getStrokeWidth(), 0.001f);
        assertEquals("Default getPrimaryColor should be Color.GRAY", Color.GRAY, obj.getPrimaryColor());
        assertEquals("Default getBorderColor should be Color.DARK_GRAY", Color.DARK_GRAY, obj.getBorderColor());
    }

    @Test
    public void testCollisionDetection() {
        TestObject obj1 = new TestObject(0, 0);
        TestObject obj2 = new TestObject(1, 1);
        
        // Initially should not collide
        assertFalse(obj1.canColideW(obj2));
        
        obj1.addColideWith(obj2);
        
        // Now it should collide since we fixed the bug
        assertTrue("Collision should be detected after adding class", obj1.canColideW(obj2));
    }
}

package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;

public class SafeZoneTest {

    @Test
    public void testSafeZoneProperties() {
        SafeZone safeZone = new SafeZone();
        
        assertTrue("SafeZone should allow objects on top", safeZone.canHaveObjectOnTop());
        assertTrue("SafeZone should be safe", safeZone.isSafe());
        assertTrue("SafeZone should act as a respawn zone", safeZone.isARespawn());
        assertFalse("SafeZone is not a finish zone by default", safeZone.isAFinish());
    }
}

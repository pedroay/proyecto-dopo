package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;
import java.io.File;
import java.io.IOException;

public class LevelTest {

    private File getLevelFile(String filename) {
        // Try relative to worldHardestGame project root
        File f1 = new File("src/dominio/levels/" + filename);
        if (f1.exists()) return f1;

        // Try relative to Proyecto-final workspace root
        File f2 = new File("worldHardestGame/src/dominio/levels/" + filename);
        if (f2.exists()) return f2;

        // Fallback to f1 so it fails normally with file not found if neither exists
        return f1;
    }

    @Test
    public void testLoadFirstLevel() throws IOException {
        File file = getLevelFile("level1.txt");
        Level level = Level.loadFromFile(file.getAbsolutePath());
        
        // Revisa que el número del nivel sea 1
        assertEquals(1, level.getLevelNumber());
        
        // Revisa que el ancho sea 20
        assertEquals(20, level.getWidth());
        
        // Revisa que el alto sea 7
        assertEquals(7, level.getHeight());
    }

    @Test
    public void testImportAndExportLevel() throws IOException, WorldHGException {
        // Cargar el nivel original
        File originalFile = getLevelFile("level1.txt");
        Level level = Level.importFrom(originalFile);
        
        assertEquals(1, level.getLevelNumber());
        assertEquals(20, level.getWidth());
        assertEquals(7, level.getHeight());
        
        // Exportar a un archivo temporal
        
        File tempFile = File.createTempFile("temp_level", ".txt");
        tempFile.deleteOnExit();
        level.exportAs(tempFile);
        
        // Importar del archivo exportado y comparar
        Level importedLevel = Level.importFrom(tempFile);
        assertEquals(level.getLevelNumber(), importedLevel.getLevelNumber());
        assertEquals(level.getWidth(), importedLevel.getWidth());
        assertEquals(level.getHeight(), importedLevel.getHeight());
        assertArrayEquals(level.getEntityLines(), importedLevel.getEntityLines());
    }

    @Test
    public void testExceptionOfImport() {
        File file = getLevelFile("prueba.png");
        try {
            Level.importFrom(file);
            fail("Debería haber lanzado WorldHGException al importar un archivo con formato incorrecto");
        } catch (WorldHGException e) {
            assertEquals(WorldHGException.IMPORT_ERROR, e.getMessage());
        }
    }   

    @Test
    public void testToString() {
        String[] entities = {"W 0 0", "S 1 1", "G 2 2"};
        Level level = new Level(2, 10, 8, entities);
        
        String expected = "Level 2 (10x8)\n" +
                          "W 0 0\n" +
                          "S 1 1\n" +
                          "G 2 2\n";
        
        assertEquals(expected, level.toString());
    }
}

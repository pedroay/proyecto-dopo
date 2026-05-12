package dominio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a game level loaded from a file.
 *
 * New file format (entity-list):
 *   Line 1: level number
 *   Line 2: width height (grid dimensions)
 *   Remaining lines: TOKEN col row (one entity per line)
 *
 * Example:
 *   1
 *   19 7
 *   W 0 0
 *   W 1 0
 *   S 1 1
 *   BH 5 2
 *   M 4 3
 *
 * Any cell not explicitly declared defaults to empty (".").
 */
public class Level implements java.io.Serializable {
    private int levelNumber;
    private int width;
    private int height;

    /** Each entry is a raw line like "W 0 0" or "BH 5 2". */
    private String[] entityLines;

    public Level(int levelNumber, int width, int height, String[] entityLines) {
        this.levelNumber = levelNumber;
        this.width = width;
        this.height = height;
        this.entityLines = entityLines;
    }

    /**
     * Loads a level from a .txt file using the entity-list format.
     */
    public static Level loadFromFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Line 1: level number
            int levelNumber = Integer.parseInt(br.readLine().trim());

            // Line 2: width height
            String[] dims = br.readLine().trim().split("\\s+");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            // Remaining lines: entity declarations
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }

            return new Level(levelNumber, width, height, lines.toArray(new String[0]));
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Returns the raw entity-declaration lines (e.g. "W 0 0").
     */
    public String[] getEntityLines() {
        return entityLines;
    }

    /**
     * @deprecated Kept temporarily for compatibility. Use getEntityLines().
     */
    @Deprecated
    public String[] getRows() {
        return entityLines;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level ").append(levelNumber);
        sb.append(" (").append(width).append("x").append(height).append(")\n");
        for (String eLine : entityLines) {
            sb.append(eLine).append("\n");
        }
        return sb.toString();
    }
}

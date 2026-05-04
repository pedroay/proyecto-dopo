package dominio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Level {
    private int levelNumber;
    private String[] rows;

    public Level(int levelNumber, String[] rows) {
        this.levelNumber = levelNumber;
        this.rows = rows;
    }

    /**
     * Loads a level from a .txt file.
     * Expected format:
     *   First line: level number (e.g., "1")
     *   Following lines: map with space-separated tokens
     *   Tokens: W=wall, S=start, G=goal, Z=safe zone, P=coin, B=basic enemy, .=empty
     *
     * Example:
     *   1
     *   W W W W W W W
     *   W S . P . G W
     *   W . B . B . W
     *   W W W W W W W
     */
    public static Level loadFromFile(String filePath) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int levelNumber = Integer.parseInt(br.readLine().trim());
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    lines.add(line.trim());
                }
            }
            return new Level(levelNumber, lines.toArray(new String[0]));
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String[] getRows() {
        return rows;
    }

    public int getHeight() {
        return rows.length;
    }

    public int getWidth() {
        return rows.length > 0 ? rows[0].split(" ").length : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level ").append(levelNumber).append("\n");
        for (String row : rows) {
            sb.append(row).append("\n");
        }
        return sb.toString();
    }
}

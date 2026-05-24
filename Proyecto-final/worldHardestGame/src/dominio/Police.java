 package dominio;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Police enemy. Extends Enemy and moves horizontally 
 * with a flashing red/blue strobe effect.
 */
public class Police extends Enemy {

    private static final int CELL_SIZE = 40;

    private double dirX = 1;
    private double dirY = 0;
    private int frameCount = 0;
    private ArrayList<Map.Entry<String, Integer>> instrucciones;
    private int indiceInstruccionActual = 0;
    private double pixelesRecorridosActual = 0;

    /**
     * @param posx initial column in the grid
     * @param posy initial row in the grid
     */
    public Police(int posx, int posy, ArrayList<Map.Entry<String, Integer>> instrucciones ) {
        super(posx, posy);
        super.setMove(true);
        super.setSpeed(4.0);
        this.instrucciones = instrucciones;
    }

    @Override
    public void move(Board[][] board) {
        if (instrucciones == null || instrucciones.isEmpty()) return;

        // 1. Leer la instrucción actual
        Map.Entry<String, Integer> instruccion = instrucciones.get(indiceInstruccionActual);
        String direccion = instruccion.getKey().toLowerCase();
        int casillasObjetivo = instruccion.getValue();
        
        // Convertir casillas a píxeles (ej: 10 casillas * 40 px = 400 px)
        double pixelesObjetivo = casillasObjetivo * CELL_SIZE;

        // 2. Determinar hacia dónde moverse basándonos en el String
        double dx = 0;
        double dy = 0;
        switch (direccion) {
            case "up":    dy = -1; break;
            case "down":  dy = 1;  break;
            case "left":  dx = -1; break;
            case "right": dx = 1;  break;
            default: break; // Ignorar instrucciones inválidas
        }

        // 3. Calcular el paso de este frame
        double paso = getSpeed();

        // Evitar pasarse del destino exacto si el último paso es más grande que lo que falta
        if (pixelesRecorridosActual + paso > pixelesObjetivo) {
            paso = pixelesObjetivo - pixelesRecorridosActual;
        }

        double nextX = getX() + (dx * paso);
        double nextY = getY() + (dy * paso);

        // 4. Sistema de seguridad por si choca contra una pared (opcional)
        // Si choca antes de terminar la instrucción, pasamos a la siguiente de inmediato
        // para que no se quede atascado intentando atravesar un muro.
        if (isPixelBlocked(nextX, nextY, board)) {
            siguienteInstruccion();
            return;
        }

        // 5. Aplicar el movimiento
        setX(nextX);
        setY(nextY);
        
        // Update the grid position
        setPosx((int) (getX() / CELL_SIZE));
        setPosy((int) (getY() / CELL_SIZE));
        
        // Sumar lo que recorrimos
        pixelesRecorridosActual += paso;

        // 6. Revisar si ya terminamos esta instrucción
        if (pixelesRecorridosActual >= pixelesObjetivo) {
            siguienteInstruccion();
        }

        frameCount++;
    }

    /**
     * Pasa a la siguiente instrucción y reinicia el contador de píxeles.
     * Si llega al final de la lista, vuelve a empezar (hace un ciclo de patrulla).
     */
    private void siguienteInstruccion() {
        pixelesRecorridosActual = 0;
        indiceInstruccionActual++;
        
        // Si terminamos todas las instrucciones, volvemos a la primera (loop)
        if (indiceInstruccionActual >= instrucciones.size()) {
            indiceInstruccionActual = 0;
        }
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

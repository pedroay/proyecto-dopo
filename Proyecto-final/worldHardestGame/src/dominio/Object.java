package dominio;

import java.util.ArrayList;

/**
 * Clase base para todos los objetos del juego.
 *
 * Mantiene TANTO coordenadas enteras (posx, posy) para compatibilidad con el
 * tablero estático (Board[][]) como coordenadas de punto flotante (x, y) para
 * representar la posición exacta en píxeles durante el movimiento continuo.
 *
 * Las coordenadas double (x, y) son la "fuente de verdad" durante el juego;
 * las enteras (posx, posy) indican en qué celda de la grilla se encuentra el
 * objeto y se usan para verificar colisiones con paredes.
 */
public abstract class Object {

    // ── Posición en grilla (índices de columna/fila) ─────────────────────────
    private int posx;
    private int posy;

    // ── Posición continua en píxeles ─────────────────────────────────────────
    private double x;
    private double y;

    // ── Velocidad en píxeles/frame ────────────────────────────────────────────
    private double velX;
    private double velY;

    private ArrayList<String> colideWith;

    public Object(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
        // Inicializamos las coordenadas continuas en el centro de la celda de la grilla.
        // CELL_SIZE = 40 px (definido en WorldHG como constante de referencia).
        this.x = posx * 40.0;
        this.y = posy * 40.0;
        this.velX = 0;
        this.velY = 0;
        this.colideWith = new ArrayList<>();
    }

    // ── Getters / Setters de posición entera (grilla) ─────────────────────────

    public int getPosx() { return posx; }
    public int getPosy() { return posy; }

    public void setPosx(int newPosx) { this.posx = newPosx; }
    public void setPosy(int newPosy) { this.posy = newPosy; }

    // ── Getters / Setters de posición continua (píxeles) ──────────────────────

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    // ── Getters / Setters de velocidad ────────────────────────────────────────

    public double getVelX() { return velX; }
    public double getVelY() { return velY; }

    public void setVelX(double velX) { this.velX = velX; }
    public void setVelY(double velY) { this.velY = velY; }

    // ── Colisiones (herencia original) ────────────────────────────────────────

    public boolean canColideW(Object obj) {
        return colideWith.contains(obj);
    }

    public void addColideWith(Object newObject) {
    }
}

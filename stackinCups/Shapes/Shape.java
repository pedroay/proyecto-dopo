package Shapes;

/**
 * Clase base abstracta para todas las figuras del paquete Shapes.
 * Centraliza los atributos comunes (posición, color, visibilidad) y
 * los métodos de movimiento, delegando el dibujado concreto a las subclases
 * mediante los métodos abstractos draw() y erase().
 *
 * Refactorización 1DOPO-I04 – Entrega 4: aprovechamiento de herencia.
 *
 * @author Michael Kolling and David J. Barnes (Modified)
 * @version 2.0
 */
public abstract class Shape {

    // Campos protected para que las subclases puedan acceder directamente
    // sin necesidad de llamar a getters/setters en cada operación interna.
    protected int    xPosition;
    protected int    yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Constructor base: establece posición y color por defecto.
     * Las posiciones (70, 15) y el color "magenta" sirven como punto de
     * partida; cada subclase puede sobreescribirlos en su propio constructor.
     */
    public Shape() {
        xPosition = 70;
        yPosition = 15;
        color     = "magenta";
        isVisible = false;
    }

    // -------------------------------------------------------------------------
    // Getters / Setters de posición (público para uso en tower y otras clases)
    // -------------------------------------------------------------------------

    /** Devuelve la coordenada X de la figura. */
    public int getXP() {
        return xPosition;
    }

    /** Devuelve la coordenada Y de la figura. */
    public int getYP() {
        return yPosition;
    }

    /** Establece la coordenada X de la figura. */
    public void setXP(int nPosx) {
        xPosition = nPosx;
    }

    /** Establece la coordenada Y de la figura. */
    public void setYP(int nPosy) {
        yPosition = nPosy;
    }

    /**
     * Mueve la figura a una nueva posición absoluta.
     * @param nPosy nueva coordenada Y
     * @param nPosx nueva coordenada X
     */
    public void setP(int nPosy, int nPosx) {
        xPosition = nPosx;
        yPosition = nPosy;
    }

    // -------------------------------------------------------------------------
    // Visibilidad
    // -------------------------------------------------------------------------

    /**
     * Hace visible la figura. Si ya era visible, no hace nada.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible la figura. Si ya era invisible, no hace nada.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    // -------------------------------------------------------------------------
    // Movimientos rápidos (delegan en moveHorizontal / moveVertical)
    // -------------------------------------------------------------------------

    /** Mueve la figura 20 px a la derecha. */
    public void moveRight() {
        moveHorizontal(20);
    }

    /** Mueve la figura 20 px a la izquierda. */
    public void moveLeft() {
        moveHorizontal(-20);
    }

    /** Mueve la figura 20 px hacia arriba. */
    public void moveUp() {
        moveVertical(-20);
    }

    /** Mueve la figura 20 px hacia abajo. */
    public void moveDown() {
        moveVertical(20);
    }

    // -------------------------------------------------------------------------
    // Movimientos con distancia explícita
    // -------------------------------------------------------------------------

    /**
     * Desplaza la figura horizontalmente la distancia indicada.
     * @param distance píxeles a desplazar (negativo = izquierda)
     */
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Desplaza la figura verticalmente la distancia indicada.
     * @param distance píxeles a desplazar (negativo = arriba)
     */
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    // -------------------------------------------------------------------------
    // Movimientos lentos (frame a frame para animaciones)
    // -------------------------------------------------------------------------

    /**
     * Desplaza la figura horizontalmente píxel a píxel (animación suave).
     * @param distance distancia total en píxeles
     */
    public void slowMoveHorizontal(int distance) {
        int delta;
        if (distance < 0) {
            delta    = -1;
            distance = -distance;
        } else {
            delta = 1;
        }
        for (int i = 0; i < distance; i++) {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Desplaza la figura verticalmente píxel a píxel (animación suave).
     * @param distance distancia total en píxeles
     */
    public void slowMoveVertical(int distance) {
        int delta;
        if (distance < 0) {
            delta    = -1;
            distance = -distance;
        } else {
            delta = 1;
        }
        for (int i = 0; i < distance; i++) {
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el color de la figura.
     * @param newColor nuevo color ("red", "yellow", "blue", "green",
     *                 "magenta", "black", "white")
     */
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    // -------------------------------------------------------------------------
    // Métodos abstractos: cada subclase define cómo se dibuja y borra
    // -------------------------------------------------------------------------

    /**
     * Dibuja la figura en el Canvas con sus especificaciones actuales.
     * Debe ser implementado por cada subclase concreta.
     */
    public abstract void draw();

    /**
     * Borra la figura del Canvas.
     * Debe ser implementado por cada subclase concreta.
     */
    protected abstract void erase();
}
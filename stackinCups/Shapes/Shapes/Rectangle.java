package Shapes;
import java.awt.*;

/**
 * Un rectángulo que puede ser manipulado y que se dibuja en el canvas.
 * Hereda de Shape los atributos de posición, color y visibilidad,
 * así como todos los métodos de movimiento comunes.
 *
 * Refactorización 1DOPO-I04 – Entrega 4: aprovechamiento de herencia.
 *
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 2.0
 */
public class Rectangle extends ShapeFigure {

    /** Número de lados de un rectángulo. */
    public static final int EDGES = 4;

    private int height;
    private int width;
    private int perimeter;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    /**
     * Crea un nuevo rectángulo en la posición por defecto con color magenta.
     */
    public Rectangle() {
        // xPosition=70, yPosition=15, color="magenta", isVisible=false
        // están establecidos por el constructor de Shape.
        height    = 30;
        width     = 40;
        perimeter = calculatePerimeter();
    }

    /**
     * Crea un nuevo rectángulo cuadrado a partir de su perímetro.
     * @param per perímetro deseado; cada lado mide per/4 píxeles
     */
    public Rectangle(int per) {
        height    = per / 4;
        width     = per / 4;
        perimeter = per;
    }

    // -------------------------------------------------------------------------
    // Operaciones propias de Rectangle
    // -------------------------------------------------------------------------

    /**
     * Crea un rectángulo "espejo" del mismo tamaño, pegado a la izquierda.
     */
    public void mirror() {
        Rectangle mirror = new Rectangle();
        mirror.changeSize(height, width);
        mirror.setXP(xPosition - width);
        mirror.setYP(yPosition);
        if (isVisible) {
            mirror.makeVisible();
        }
        draw();
    }

    /**
     * Cambia el tamaño del rectángulo.
     * @param newHeight nueva altura en píxeles (debe ser >= 0)
     * @param newWidth  nuevo ancho en píxeles  (debe ser >= 0)
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width  = newWidth;
        calculatePerimeter();
        draw();
    }

    /**
     * Escala el rectángulo un 100 % (duplica o reduce a la mitad).
     * @param z '+' para aumentar, '-' para reducir
     */
    public void zoom(char z) {
        if (z == '-') {
            changeSize(height / 2, width / 2);
        } else if (z == '+') {
            changeSize(height * 2, width * 2);
        }
    }

    /**
     * Desplaza el rectángulo horizontalmente en varios pasos uniformes.
     * @param times    número de pasos
     * @param position distancia total a recorrer en píxeles
     */
    public void walk(int times, int position) {
        if (times > 0) {
            int minimoves = position / times;
            for (int i = 0; i < times; i++) {
                slowMoveHorizontal(minimoves);
            }
        }
    }

    /** Devuelve la altura actual del rectángulo. */
    public int getHeight() {
        return height;
    }

    /** Devuelve la anchura actual del rectángulo. */
    public int getWidth() {
        return width;
    }

    /** Devuelve el perímetro actual del rectángulo. */
    public int getPerimeter() {
        return perimeter;
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    /** Recalcula y devuelve el perímetro. */
    private int calculatePerimeter() {
        perimeter = (height * 2) + (width * 2);
        return perimeter;
    }

    // -------------------------------------------------------------------------
    // Implementación de los métodos abstractos de Shape
    // -------------------------------------------------------------------------

    /**
     * Dibuja el rectángulo en el Canvas con las especificaciones actuales.
     */
    @Override
    public void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }

    /**
     * Borra el rectángulo del Canvas.
     */
    @Override
    protected void erase() {
        Canvas canvas = Canvas.getCanvas();
        canvas.erase(this);
    }
}


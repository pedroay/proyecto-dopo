package Shapes;
import java.awt.*;

/**
 * Un triángulo que puede ser manipulado y que se dibuja en el canvas.
 * Hereda de Shape los atributos de posición, color y visibilidad,
 * así como todos los métodos de movimiento comunes.
 *
 * Refactorización 1DOPO-I04 – Entrega 4: aprovechamiento de herencia.
 *
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 2.0
 */
public class Triangle extends Shape {

    /** Número de vértices de un triángulo. */
    public static final int VERTICES = 3;

    private int height;
    private int width;

    /**
     * Crea un nuevo triángulo en la posición por defecto con color verde.
     */
    public Triangle() {
        // Hereda campos de Shape y los sobreescribe según valores originales.
        xPosition = 140;
        yPosition = 15;
        color     = "green";
        height    = 30;
        width     = 40;
    }

    /**
     * Crea un nuevo triángulo con altura y ancho especificados.
     * La posición y el color se mantienen con los valores por defecto.
     * @param height altura del triángulo en píxeles (debe ser >= 0)
     * @param width  base del triángulo en píxeles  (debe ser >= 0)
     */
    public Triangle(int height, int width) {
        xPosition  = 140;
        yPosition  = 15;
        color      = "green";
        this.height = height;
        this.width  = width;
    }

    // -------------------------------------------------------------------------
    // Tamaño
    // -------------------------------------------------------------------------

    /**
     * Cambia el tamaño del triángulo.
     * @param newHeight nueva altura en píxeles (debe ser >= 0)
     * @param newWidth  nuevo ancho en píxeles  (debe ser >= 0)
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width  = newWidth;
        draw();
    }

    /** Devuelve la altura actual del triángulo. */
    public int getHeight() {
        return height;
    }

    /** Devuelve la anchura actual del triángulo. */
    public int getWidth() {
        return width;
    }

    // -------------------------------------------------------------------------
    // Implementación de los métodos abstractos de Shape
    // -------------------------------------------------------------------------

    /**
     * Dibuja el triángulo en el Canvas con las especificaciones actuales.
     */
    @Override
    public void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width / 2), xPosition - (width / 2) };
            int[] ypoints = { yPosition, yPosition + height,       yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }

    /**
     * Borra el triángulo del Canvas.
     */
    @Override
    protected void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
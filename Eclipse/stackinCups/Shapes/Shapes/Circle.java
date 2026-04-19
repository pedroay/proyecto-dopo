package Shapes;
import java.awt.geom.Ellipse2D;

/**
 * Un círculo que puede ser manipulado y que se dibuja en el canvas.
 * Hereda de Shape los atributos de posición, color y visibilidad,
 * así como todos los métodos de movimiento comunes.
 *
 * Refactorización 1DOPO-I04 – Entrega 4: aprovechamiento de herencia.
 *
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 2.0
 */
public class Circle extends ShapeFigure {

    /** Constante matemática PI usada para cálculos de área/perímetro. */
    public static final double PI = 3.1416;

    private int diameter;

    /**
     * Crea un nuevo círculo en la posición por defecto con color azul.
     */
    public Circle() {
        // Hereda xPosition=70, yPosition=15, isVisible=false de Shape;
        // sobreescribimos según los valores originales de Circle.
        xPosition = 20;
        yPosition = 15;
        color     = "blue";
        diameter  = 30;
    }

    // -------------------------------------------------------------------------
    // Tamaño
    // -------------------------------------------------------------------------

    /**
     * Cambia el diámetro del círculo.
     * @param newDiameter nuevo diámetro en píxeles (debe ser >= 0)
     */
    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }

    /** Devuelve el diámetro actual del círculo. */
    public int getDiameter() {
        return diameter;
    }

    // -------------------------------------------------------------------------
    // Implementación de los métodos abstractos de Shape
    // -------------------------------------------------------------------------

    /**
     * Dibuja el círculo en el Canvas con las especificaciones actuales.
     */
    @Override
    public void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
            canvas.wait(10);
        }
    }

    /**
     * Borra el círculo del Canvas.
     */
    @Override
    protected void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
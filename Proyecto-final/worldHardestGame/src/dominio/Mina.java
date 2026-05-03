package dominio;

/**
 * Enemigo estático. No se mueve, pero daña al jugador si lo toca.
 * En el archivo .txt se representa como: M
 */
public class Mina extends Enemy {
    public Mina(int posx, int posy) {
        super(posx, posy);
    }

    // No tiene lógica de movimiento — es un obstáculo fijo en el tablero.
    // La colisión con el jugador la maneja WorldHG en checkInteractions().
}

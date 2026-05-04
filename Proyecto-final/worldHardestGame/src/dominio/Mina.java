package dominio;

/**
 * Static enemy. Does not move, but damages the player upon contact.
 * In the .txt file, it is represented as: M
 */
public class Mina extends Enemy {
    public Mina(int posx, int posy) {
        super(posx, posy);
    }

    // Has no movement logic — it is a fixed obstacle on the board.
    // Collision with the player is handled by WorldHG in checkInteractions().
}
package dominio;

/**
 * Static enemy. Does not move, but damages the player upon contact.
 * In the .txt file, it is represented as: M
 */
public class Mina extends Enemy {
    public Mina(int posx, int posy) {
        super(posx, posy);
        super.setMove(false);
    }

    public void move(Board[][] board) {}
}
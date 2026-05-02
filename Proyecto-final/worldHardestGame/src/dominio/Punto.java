package dominio;

public class Punto extends Object implements interactWPlayer {
    private boolean collected;

    public Punto(int posx, int posy) {
        super(posx, posy);
        this.collected = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }
}

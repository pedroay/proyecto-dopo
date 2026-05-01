package dominio;

public abstract class Enemy extends Personaje implements interactWPlayer, canDamage {
    public Enemy(int posx, int posy) {
        super(posx, posy);
    }
}

package dominio;

public abstract class Personaje extends Objeto implements canMove {
    public Personaje(int posx, int posy) {
        super(posx, posy);
    }
}
